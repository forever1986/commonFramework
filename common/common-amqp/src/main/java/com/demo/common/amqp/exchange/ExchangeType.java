package com.demo.common.amqp.exchange;

/**
 * 不同交换机绑定枚举
 */
public enum ExchangeType {

    HEADER("header", new HeaderBindExchange()),
    FANOUT("fanout", new FanoutBindExchange()),
    DIRECT("direct", new DirectBindExchange()),
    TOPIC("topic", new TopicBindExchange());


    private String type;

    private BindExchange exchange;

    ExchangeType() {
    }

    ExchangeType(String type, BindExchange exchange) {
        this.type = type;
        this.exchange = exchange;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BindExchange getExchange() {
        return exchange;
    }

    public void setExchange(BindExchange exchange) {
        this.exchange = exchange;
    }

}
