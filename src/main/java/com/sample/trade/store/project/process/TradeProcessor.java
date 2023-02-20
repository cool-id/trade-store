package com.sample.trade.store.project.process;

public interface TradeProcessor <T> {

    public String process(T data) throws Exception;

    public String update() throws Exception;
}
