package com.demo.stock.service;

import com.demo.stock.mapper.TicketStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketStockService {

    @Autowired
    private TicketStockMapper ticketStockMapper;

    public void reduceStock(String good, Integer stock) {
        try {
            ticketStockMapper.reduceCount(good, stock);
        } catch (Exception e) {
            throw new RuntimeException("减库存失败");
        }
        log.info("减库存成功");
    }

}
