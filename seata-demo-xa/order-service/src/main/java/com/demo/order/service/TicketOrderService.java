package com.demo.order.service;

import com.demo.order.client.TicketStockClient;
import com.demo.order.entity.TicketOrder;
import com.demo.order.mapper.TicketOrderMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketOrderService {

    @Autowired
    private TicketOrderMapper ticketOrderMapper;
    @Autowired
    private TicketStockClient ticketStockClient;

    @GlobalTransactional
    public Integer createOrder(TicketOrder ticketOrder) {
        //创建订单
        ticketOrderMapper.insert(ticketOrder);
        //减库存
        ticketStockClient.reduceCount(ticketOrder.getGood(), ticketOrder.getCount());
        //返回订单号
        return ticketOrder.getId();
    }

}
