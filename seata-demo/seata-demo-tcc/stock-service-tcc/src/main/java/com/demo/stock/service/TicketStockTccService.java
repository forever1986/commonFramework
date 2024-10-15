package com.demo.stock.service;

import com.demo.stock.constant.FreezeStatus;
import com.demo.stock.entity.TicketStockFreeze;
import com.demo.stock.mapper.TicketStockFreezeMapper;
import com.demo.stock.mapper.TicketStockMapper;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@LocalTCC  //TCC 是事务是 3 中操作的首字母缩写，即 try（执行操作），confirm（确认提交），cancel（数据回滚）
@Slf4j
@Service
public class TicketStockTccService {

    @Autowired
    private TicketStockMapper ticketStockMapper;


    @Autowired
    private TicketStockFreezeMapper ticketStockFreezeMapper;

    //该注解配置了 tcc 事务的 3 个方法：
    //name 配置 try 方法tryReduceStock
    //commitMethod 配置 confirm 方法
    //rollbackMethod 配置 cancel 方法
    @TwoPhaseBusinessAction(name = "tryReduceStock",
            commitMethod = "confirm", rollbackMethod = "cancel")
    @Transactional //因为同时操作库存和冻结记录，可能存在一方失败，因此使用本地事务绑定
    public void tryReduceStock(
            //使用该注解指定的参数，
            //参数值可以在 confirm 方法和 cancel 方法的 BusinessActionContext 参数中获取到
            @BusinessActionContextParameter(paramName = "good") String good,
            @BusinessActionContextParameter(paramName = "stock") Integer stock) {
        //获取事务id
        String xid = RootContext.getXID();
        //解决“悬挂”问题，需要判断是否有冻结记录，如果有的话，就不能再执行 try 操作了
        TicketStockFreeze oldTicketStockFreeze = ticketStockFreezeMapper.selectById(xid);
        if (oldTicketStockFreeze != null) {
            return;
        }
        //扣减库存
        log.info("try-->扣减库存:商品-{0},数量-{1}", good, stock);
        ticketStockMapper.reduceCount(good, stock);
        //记录冻结的库存和事务状态
        TicketStockFreeze ticketStockFreeze = new TicketStockFreeze();
        ticketStockFreeze.setGood(good);
        ticketStockFreeze.setFreezeStock(stock);
        // 1 表示 try 状态，0 表示 cancel 状态
        ticketStockFreeze.setState(FreezeStatus.FREEZE_STATUS_TRY);
        ticketStockFreeze.setXid(xid);
        log.info("try-->冻结库存:商品-{0},数量-{1}", good, stock);
        ticketStockFreezeMapper.insert(ticketStockFreeze);
        log.info("减库存成功");
    }

    //事务成功提交的方法，此时需要删除冻结记录即可
    public boolean confirm(BusinessActionContext bac) {
        //获取事务id
        String xid = bac.getXid();
        //根据id删除冻结记录
        log.info("confirm-->删除冻结库存:xid-{0}", xid);
        int count = ticketStockFreezeMapper.deleteById(xid);
        return true;
    }

    //数据回滚方法，此时需要恢复库存，更改冻结记录的状态
    @Transactional //因为同时操作库存和冻结记录，可能存在一方失败，因此使用本地事务绑定
    public boolean cancel(BusinessActionContext bac) {
        //通过事务id查询冻结记录中的库存
        String xid = bac.getXid();
        TicketStockFreeze ticketStockFreeze = ticketStockFreezeMapper.selectById(xid);

        //解决“空回滚”问题：如果 freeze 为 null，表示之前没有执行过 try，此时需要空回滚，向 ticket_stock_freeze 表示添加一条 cancel 状态的记录
        if (ticketStockFreeze == null) {
            ticketStockFreeze = new TicketStockFreeze();
            //由于在 try 方法（也就是 reduceStock 方法）的参数使用了 @BusinessActionContextParameter 注解，
            //因此这里使用 BusinessActionContext.getActionContext("good")获得参数
            String good = bac.getActionContext("good").toString();
            ticketStockFreeze.setGood(good);
            ticketStockFreeze.setFreezeStock(0);
            // 1 表示 try 状态，0 表示 cancel 状态
            ticketStockFreeze.setState(FreezeStatus.FREEZE_STATUS_CANCEL);
            ticketStockFreeze.setXid(xid);
            log.info("cancel-->空回滚:商品-{0},数量-0", good);
            ticketStockFreezeMapper.insert(ticketStockFreeze);
            return true;
        }

        //解决“幂等性”问题：为了防止 cancel 方法被调用了多次，这里需要幂等性判断.如果获取到的冻结记录，状态本身已经是 cancel 状态，则不再进行处理
        if (ticketStockFreeze.getState() == 0) {
            log.info("cancel-->幂等性处理:商品-{0}", ticketStockFreeze.getGood());
            return true;
        }

        //恢复库存
        log.info("cancel-->恢复库存:商品-{0},数量-0", ticketStockFreeze.getGood(), ticketStockFreeze.getFreezeStock());
        ticketStockMapper.addCount(ticketStockFreeze.getGood(), ticketStockFreeze.getFreezeStock());
        //将冻结库存清零，状态改为 cancel（1 表示 try 状态，0 表示 cancel 状态）
        ticketStockFreeze.setFreezeStock(0);
        ticketStockFreeze.setState(FreezeStatus.FREEZE_STATUS_CANCEL);
        log.info("cancel-->更新冻结状态:商品-{0},数量-0", ticketStockFreeze.getGood(), ticketStockFreeze.getFreezeStock());
        ticketStockFreezeMapper.updateById(ticketStockFreeze);
        return true;
    }

}
