package com.alphabt.fx.control.clock.worker;

import com.alphabt.fx.control.clock.util.*;

import java.util.Calendar;

/**
 * Created by danielbt on 25/05/16.
 */
public class ClockWorker extends Worker implements Alterable<ClockFormat> {

    private boolean hourOfDay;
    private Calendar calendar;
    private Selector<ClockFormat> clockFormatSelector;

    public ClockWorker() {
        this(ClockFormat.CLOCK_THREE);
    }

    public ClockWorker(boolean hourOfDay) {
        this();
        this.hourOfDay = hourOfDay;
    }

    public ClockWorker(ClockFormat clockFormat) {
        super(clockFormat);
    }

    public ClockWorker(boolean hourOfDay, ClockFormat clockFormat) {
        super(clockFormat);
        this.hourOfDay = hourOfDay;
    }

    public void setHourOfday(boolean hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public boolean isHourOfday() {
        return hourOfDay;
    }

    @Override
    protected void onInitialize() {
        clockFormatSelector = new Selector<ClockFormat>() {
            @Override
            protected ClockFormat[] getElements() {
                return new ClockFormat[]{
                        ClockFormat.CLOCK_FOUR,
                        ClockFormat.CLOCK_TWO,
                        ClockFormat.CLOCK_THREE
                };
            }

            @Override
            public void onChange(ClockFormat object) {
                setFormat(object);
            }
        };

        revalidate();
    }

    @Override
    protected void onExecute(ClockValue value) {

        int hour = calendar.get(hourOfDay ? Calendar.HOUR_OF_DAY : Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millsecond = calendar.get(Calendar.MILLISECOND) / 10;

        value.setAllValues(hour, minute, second, millsecond);

        revalidate();
    }

    @Override
    protected void onRevalidate() {
        calendar = Calendar.getInstance();
    }

    @Override
    public Selector<ClockFormat> selector() {
        return clockFormatSelector;
    }
}
