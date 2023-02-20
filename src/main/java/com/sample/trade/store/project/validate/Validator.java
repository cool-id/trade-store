package com.sample.trade.store.project.validate;

public interface Validator <T, R> {

    public R validate(T data);

}
