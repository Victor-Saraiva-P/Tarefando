package com.victor.tarefando.tarefando.mappers;

public interface Mapper<A, B> {

    B mapTo(A a);

    A mapFrom(B b);

}