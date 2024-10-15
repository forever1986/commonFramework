package com.demo.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.stock.entity.TicketStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TicketStockMapper extends BaseMapper<TicketStock> {

    //减库存
    @Update("update ticket_stock set `stock` = `stock` - #{stock} where good = #{good}")
    int reduceCount(@Param("good") String good, @Param("stock") int stock);

    //加库存
    @Update("update ticket_stock set `stock` = `stock` + #{stock} where good = #{good}")
    int addCount(@Param("good") String good, @Param("stock") int stock);

}
