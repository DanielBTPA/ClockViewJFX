package com.alphabt.fx.control.clock.util;

/**
 * Created by danielbt on 13/06/16.
 */

@FunctionalInterface
public interface Notifiable {
    boolean notifyIf(ClockValue thisValue);
}
