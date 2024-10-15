package com.demo.stock.controller;

import com.demo.stock.service.TicketStockTccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketStockController {

    @Autowired
    private TicketStockTccService ticketStockTccService;

    @GetMapping("/reduce/{good}/{stock}")
    public ResponseEntity<String> reduceCount(@PathVariable("good") String good,
                                              @PathVariable("stock") Integer stock) {
        ticketStockTccService.tryReduceStock(good, stock);
        return ResponseEntity.ok("减库存量成功");
    }


}
