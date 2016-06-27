package com.alphabt.fx.control.clock.worker;

import com.alphabt.fx.control.clock.internal.work.ClockState;
import com.alphabt.fx.control.clock.util.AlarmBuilder;
import com.alphabt.fx.control.clock.util.ClockFormat;
import com.alphabt.fx.control.clock.util.ClockValue;
import com.alphabt.fx.control.clock.util.Executable;
import javafx.beans.property.*;

/**
 * Created by danielbt on 24/05/16.
 */
public abstract class Worker extends ClockState {

    private StringProperty labelProperty = new SimpleStringProperty();
    private ObjectProperty<ClockFormat> clockFormatProperty = new SimpleObjectProperty<>();
    private ObjectProperty<ClockValue> valueProperty = new SimpleObjectProperty<>();
    private AlarmBuilder alarmBuilder;

    private boolean revalidate = false;

    public Worker(ClockFormat clockFormat) {
        this(clockFormat, null);
    }

    public Worker(ClockFormat clockFormat, ClockValue value) {
        super();
        clockFormatProperty.set(clockFormat);
        valueProperty.set(value);
        initialize();
    }

    public ReadOnlyObjectProperty<ClockFormat> formatProperty() {
        return clockFormatProperty;
    }

    public ReadOnlyObjectProperty<ClockValue> valueProperty() {
        return valueProperty;
    }

    public ReadOnlyStringProperty labelProperty() {
        return labelProperty;
    }

    protected abstract void onInitialize();

    protected abstract void onExecute(ClockValue value);

    protected void onRevalidate() {
    }

    protected void onDispose() {
    }

    public void setAlarmBuilder(AlarmBuilder alarmBuilder) {
        this.alarmBuilder = alarmBuilder;
    }

    public AlarmBuilder getAlarmBuilder() {
        return alarmBuilder;
    }

    protected void revalidate() {
        revalidate = true;
    }

    public void setValue(ClockValue value) {
        valueProperty.set(value);
    }

    public ClockValue getValue() {
        return valueProperty.get();
    }

    public void setFormat(ClockFormat value) {
        clockFormatProperty.set(value);
    }

    public ClockFormat getFormat() {
        return clockFormatProperty.get();
    }

    public void dispose() {
        onDispose();
        remove(executableOperation);
    }

    private boolean initialize = false;

    private void initialize() {
        if (clockFormatProperty.get() == null) {
            throw new NullPointerException("Initialize ClockFormat on this constructor.");
        }

        if (valueProperty.get() == null) {
            valueProperty.set(ClockValue.ZERO.getClonedValue());
        }

        post(executableOperation);
    }

    private Executable executableOperation = () -> {
        ClockValue value = valueProperty.get();
        if (!initialize) {
            onInitialize();
            initialize = true;
        }

        if (!revalidate) {
            onExecute(value);
            putValue(value);
        } else {
            onRevalidate();
            revalidate = false;
        }
    };


    private void putValue(ClockValue value) {
        String formatValue = null;

        switch (getFormat()) {
            case CLOCK_ONE:
                formatValue = "" + formatNumber(value.getFirstValue());
                break;
            case CLOCK_TWO:
                formatValue = formatNumber(value.getFirstValue()) + ":" + formatNumber(value.getSecondValue());
                break;
            case CLOCK_THREE:
                formatValue = formatNumber(value.getFirstValue()) + ":" + formatNumber(value.getSecondValue()) + ":" +
                        formatNumber(value.getThirdValue());
                break;
            case CLOCK_FOUR:
                formatValue = formatNumber(value.getFirstValue()) + ":" + formatNumber(value.getSecondValue()) + ":" +
                        formatNumber(value.getThirdValue()) + ":" + formatNumber(value.getFourthValue());
        }

        labelProperty.set(formatValue);
    }

    private String formatNumber(int number) {
        return (number < 10 ? "0" + number : "" + number);
    }

    @Override
    public String toString() {
        return labelProperty.get();
    }
}
