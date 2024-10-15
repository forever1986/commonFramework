package com.demo.order.controller;

import com.demo.order.entity.TicketOrder;
import com.demo.order.service.TicketOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class TicketOrderController {

    @Autowired
    private TicketOrderService ticketOrderService;

    @PostMapping("/create")
    public ResponseEntity<Integer> createOrder(@RequestBody TicketOrder ticketOrder) {
        try {
            Integer orderId = ticketOrderService.createOrder(ticketOrder);
            log.info("controller下单成功");
            return ResponseEntity.ok(orderId);
        } catch (Exception ex) {
            log.error("controller下单失败：{}", ex.getMessage());
            return ResponseEntity.status(500).body(0);
        }
    }

}
