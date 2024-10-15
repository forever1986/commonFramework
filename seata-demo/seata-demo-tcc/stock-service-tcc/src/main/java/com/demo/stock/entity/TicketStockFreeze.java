package com.demo.stock.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ticket_stock_freeze")
public class TicketStockFreeze {

    @TableId(type = IdType.INPUT)
    private String xid;

    private String good;

    private Integer freezeStock;

    //1-try状态，0-cancel状态
    private Integer state;

}
