package com.siemens.devops.codingchallenge.converter;

/**
 * Specifies a functional interface that converts one type to another.
 */
@FunctionalInterface
public interface Converter<F, T> {

    public T convert(F f);

}
