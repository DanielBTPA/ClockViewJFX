package com.alphabt.fx.control.clock.util;

import com.alphabt.fx.control.clock.worker.Worker;
import com.alphabt.fx.control.clock.internal.work.ClockHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by danielbt on 03/06/16.
 */
public final class AlarmBuilder extends ClockHandler {

    public AlarmBuilder(Worker worker) {
        super();
        this.worker = worker;
        initialize();
    }

    public AlarmBuilder withValue(ClockValue value) {
        checkSubmit();
        this.value = value.getClonedValue();
        return this;
    }

    public AlarmBuilder withAlarmListener(AlarmListener alarmListener) {
        checkSubmit();
        listeners.add(alarmListener);
        return this;
    }

    public AlarmBuilder withRepeatNotification(boolean repeat) {
        checkSubmit();
        this.repeat = repeat;
        return this;
    }

    public AlarmBuilder withCustomNotifiable(Notifiable notifiable) {
        checkSubmit();
        this.notifiable = notifiable;
        return this;
    }

    public AlarmBuilder withPause(boolean pause) {
        checkSubmit();
        this.pause = pause;
        return this;
    }

    public AlarmBuilder withCleanAfterFinalizing(boolean clear) {
        checkSubmit();
        this.clearAfterFinish = clear;
        return this;
    }

    public void submit() {
        submitOperation();
    }

    public void cancel() {
        cancelOperation();
    }

    public void clear() {
        clearOperation();
    }

    public boolean isSubmit() {
        return submit;
    }

    @FunctionalInterface
    public interface AlarmListener {
        void alert();
    }

    private List<AlarmListener> listeners;
    private ClockValue value;
    private Worker worker;
    private Notifiable notifiable;
    private boolean submit, clearAfterFinish, repeat, pause;
    private TimerImpl timer;

    private void initialize() {
        worker.setAlarmBuilder(this);
        listeners = new ArrayList<>();
        timer = new TimerImpl();
        post(execOperation);
    }

    private void checkSubmit() {
        if (submit)
            throw new IllegalStateException("Operation already submitted. Use 'cancel()'.");
    }

    private void submitOperation() {
        if (worker instanceof Notifiable && notifiable == null) {
            notifiable = (Notifiable) worker;
        }

        if (notifiable == null) {
            throw new NullPointerException(worker.getClass().getSimpleName() + " not implements Notifiable, Use " +
                    "'withCustomNotifiable()' of class AlarmBuilder.");
        }

        if (value == null) {
            throw new NullPointerException("Put an value with 'withValue()' to compute.");
        }

        if (pause && !(worker instanceof Controllable))
            throw new IllegalStateException("The class " + worker.getClass().getSimpleName() +
                    " not implement Controllable.");

        checkSubmit();

        submit = true;
    }

    private void cancelOperation() {
        submit = false;
    }

    private void clearOperation() {
        if (!submit) {
            value = null;
            pause = false;
            remove(execOperation);
        } else {
            throw new IllegalStateException("Use 'cancel()' in order to use this method.");
        }
    }

    private Executable execOperation = new Executable() {
        boolean repeatNotification = true;

        @Override
        public void execute() {
            if (submit) {
                if (notifiable.notifyIf(value)) {
                    if (repeatNotification && timer.next()) {
                        this.repeatNotification = repeat;

                        Collections.shuffle(listeners);

                        for (Iterator<AlarmListener> iterator = listeners.iterator();
                             iterator.hasNext(); ) {
                            AlarmListener listener = iterator.next();

                            if (pause) ((Controllable) worker).pause();

                            listener.alert();

                            if (clearAfterFinish) {
                                clearAfterFinish = false;
                                iterator.remove();
                                cancel();
                                clearOperation();
                            }
                        }
                    }
                } else {
                    repeatNotification = true;
                    timer.reset();
                }
            }
        }
    };

    private class TimerImpl {
        private static final short DEFAULT_TIME_SEC = 1;

        private int sec = DEFAULT_TIME_SEC, msec;
        private int count;

        boolean next() {
            if ((count++) <= 0) {
                return true;
            }

            count = count > 10 ? 1 : count;

            if (msec > 0) {
                msec -= 1;
            } else {
                msec = 59;

                if (sec > 0)
                    sec -= 1;
                else
                    sec = 59;
            }

            if (sec == 0 && msec == 0) {
                sec = DEFAULT_TIME_SEC;
                msec = 0;
                return true;
            }

            return false;
        }

        void reset() {
            count = 0;
            sec = DEFAULT_TIME_SEC;
            msec = 0;
        }
    }
}