package com.demo.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("stock-service")
public interface TicketStockClient {

    @GetMapping("/reduce/{good}/{stock}")
    public String reduceCount(@PathVariable("good") String good,
                                              @PathVariable("stock") Integer stock);

}
