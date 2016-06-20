package com.alphabt.fx.control.clock.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Created by danielbt on 01/06/16.
 */
public interface Controllable {

    void start();

    void pause();

    void stop();

    State getState();

    boolean isStarted();

    boolean isPaused();

    boolean isStoped();

    boolean isDoned();

    enum State {
        DONED, STARTED, PAUSED, STOPED
    }
}
