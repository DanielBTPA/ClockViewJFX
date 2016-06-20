package com.alphabt.fx.control.clock.worker;

import com.alphabt.fx.control.clock.util.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Created by danielbt on 01/06/16.
 */
public class CountdownWorker extends Worker implements Controllable, Notifiable, Alterable<ClockFormat> {

    private Selector<ClockFormat> clockFormatSelector;
    private ClockValue oldValue;
    private AlarmBuilder alarmBuilder;
    private ObjectProperty<State> stateProperty = new SimpleObjectProperty<>(State.DONED);

    public CountdownWorker(ClockValue clockValue) {
        this(ClockFormat.CLOCK_FOUR, clockValue);
    }

    public CountdownWorker(ClockFormat clockFormat, ClockValue clockValue) {
        super(clockFormat, clockValue);
    }

    @Override
    protected void onInitialize() {
        clockFormatSelector = new Selector<ClockFormat>() {
            @Override
            protected ClockFormat[] getElements() {
                return new ClockFormat[]{
                        ClockFormat.CLOCK_TWO,
                        ClockFormat.CLOCK_THREE,
                        ClockFormat.CLOCK_FOUR
                };
            }

            @Override
            protected void onChange(ClockFormat object) {
                setFormat(object);
            }
        };

        oldValue = getValue().getClonedValue();
    }

    @Override
    protected void onExecute(ClockValue value) {
        if (isStarted()) {
            if (!value.equals(ClockValue.ZERO)) {
                if (value.getFourthValue() > 0) {
                    value.setFourthValue(value.getFourthValue() - 1);
                } else {
                    value.setFourthValue(59);

                    if (value.getThirdValue() > 0) {
                        value.setThirdValue(value.getThirdValue() - 1);
                    } else {
                        value.setThirdValue(59);

                        if (value.getSecondValue() > 0) {
                            value.setSecondValue(value.getSecondValue() - 1);
                        } else {
                            value.setSecondValue(59);

                            if (value.getFirstValue() > 0) {
                                value.setFirstValue(value.getFirstValue() - 1);
                            } else {
                                value.setFirstValue(59);
                            }
                        }
                    }
                }
            }
        } else if (isStoped()) {
            value.setAllValues(oldValue);
            stateProperty.set(State.DONED);
        }
    }

    public void setAlarmBuilder(AlarmBuilder notification) {
        this.alarmBuilder = notification;
    }

    public AlarmBuilder getAlarmBuilder() {
        return alarmBuilder;
    }

    @Override
    public Selector<ClockFormat> selector() {
        return clockFormatSelector;
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
