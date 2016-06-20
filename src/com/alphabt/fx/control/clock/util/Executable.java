package com.alphabt.fx.control.clock.util;

/**
 * Created by danielbt on 16/06/16.
 */

@FunctionalInterface
public interface Executable {

    default long executeAndReturnMills() {
        long start = System.currentTimeMillis();
        execute();
        return (System.currentTimeMillis() - start);
    }

    void execute();
}