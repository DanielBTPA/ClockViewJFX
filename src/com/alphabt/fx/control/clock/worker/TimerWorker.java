package com.alphabt.fx.control.clock.worker;

import com.alphabt.fx.control.clock.util.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Created by danielbt on 05/05/16.
 */

public class TimerWorker extends Worker implements Controllable, Notifiable, Alterable<ClockFormat> {

    private Selector<ClockFormat> selector;
    private AlarmBuilder alarmBuilder;
    private ObjectProperty<State> stateProperty = new SimpleObjectProperty<>(State.DONED);

    public TimerWorker() {
        this(ClockFormat.CLOCK_FOUR);
    }

    public TimerWorker(ClockFormat clockFormat) {
        super(clockFormat);
    }

    @Override
    protected void onInitialize() {
        selector = new Selector<ClockFormat>() {
            @Override
            protected ClockFormat[] getElements() {
                return new ClockFormat[] {
                        ClockFormat.CLOCK_TWO,
                        ClockFormat.CLOCK_THREE,
                        ClockFormat.CLOCK_FOUR
                };
            }

            @Override
            public void onChange(ClockFormat object) {
                setFormat(object);
            }
        };
    }

    @Override
    protected void onExecute(ClockValue value) {
        if (isStarted()) {
            if (value.getFourthValue() < 59) {
                value.setFourthValue(value.getFourthValue() + 1);
            } else {
                value.setFourthValue(0);

                if (value.getThirdValue() < 59) {
                    value.setThirdValue(value.getThirdValue() + 1);
                } else {
                    value.setThirdValue(0);

                    if (value.getSecondValue() < 59) {
                        value.setSecondValue(value.getSecondValue() + 1);
                    } else {
                        value.setSecondValue(0);

                        if (value.getFirstValue() < 59) {
                            value.setFirstValue(value.getFirstValue() + 1);
                        } else {
                            value.setFirstValue(0);
                        }
                    }
                }
            }
        } else if (isStoped()) {
            value.setAllValues(ClockValue.ZERO);
            stateProperty.set(State.DONED);
        }
    }

    @Override
    public Selector<ClockFormat> selector() {
        return selector;
    }

    public void setAlarmBuilder(AlarmBuilder notification) {
        this.alarmBuilder = notification;
    }

    public AlarmBuilder getAlarmBuilder() {
        return alarmBuilder;
    }

    @Override
    public boolean notifyIf(ClockValue thisValue) {
        return getValue().equals(thisValue);
    }

    @Override
    public void start() {
        stateProperty.set(State.STARTED);
    }

    @Override
    public void pause() {
        stateProperty.set(State.PAUSED);
    }

    @Override
    public void stop() {
        stateProperty.set(State.STOPED);
    }

    @Override
    public State getState() {
        return stateProperty.get();
    }

    @Override
    public boolean isDoned() {
        return stateProperty.get() == State.DONED;
    }

    @Override
    public boolean isStarted() {
        return stateProperty.get() == State.STARTED;
    }

    @Override
    public boolean isPaused() {
        return stateProperty.get() == State.PAUSED;
    }

    @Override
    public boolean isStoped() {
        return stateProperty.get() == State.STOPED;
    }
}
