package com.alphabt.fx.control.clock.util;

import com.alphabt.fx.control.clock.internal.work.ClockHandler;
import com.alphabt.fx.control.clock.internal.work.ClockState;
import com.alphabt.fx.control.clock.worker.Worker;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;

import javafx.scene.paint.Paint;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danielbt on 22/06/16.
 */
public class ClockTools {

    public static final String EMPTY_LABEL = "--:--:--:--";

    private ClockTools() {
    }

    public static ClockValue parseValue(String parse) {
        int[] values = new int[4];

        if (parse != null && !parse.equals("")) {
            Pattern pattern = Pattern.compile("[^\\d:]");
            Matcher matcher = pattern.matcher(parse);

            if (matcher.find())
                throw new IllegalArgumentException("Only numbers follow of two points.");


            String[] split = parse.split(":");

            if (split.length > 4)
                throw new NumberFormatException("Max 4 values each.");

            for (int i = 0; i < split.length; i++) {
                values[i] = Integer.valueOf(split[i]);
            }
        }

        return ClockValue.values(values[0], values[1], values[2], values[3]);
    }

    public static void maskText(StringProperty property) {
        maskText(property, null);
    }

    public static void maskText(StringProperty property, ChangeListener<ClockValue> changeListener) {
        SimpleObjectProperty<ClockValue> cvp = new SimpleObjectProperty<>();

        if (changeListener != null)
            cvp.addListener(changeListener);

        property.addListener((observable, oldValue, newValue) -> {
            newValue = newValue == null ? "" : newValue;
            oldValue = oldValue == null ? "" : oldValue;
            boolean cond = newValue.length() <= EMPTY_LABEL.length();

            if (oldValue.length() < newValue.length()) {
                if (cond) {
                    newValue = newValue.replaceFirst("[^0-9]", "");
                    newValue = newValue.replaceFirst("(\\d{2})(\\d)", "$1:$2");
                    newValue = newValue.replaceFirst("(\\d{2}):(\\d{2})(\\d)", "$1:$2:$3");
                    newValue = newValue.replaceFirst("(\\d{2}):(\\d{2}):(\\d{2})(\\d)", "$1:$2:$3:$4");
                    property.setValue(newValue);
                } else property.setValue(oldValue);
            }

            cvp.set(parseValue(cond ? newValue : oldValue));
        });
    }

    public static void postProcess(Executable executable) {
        ClockHandler.postProcess(executable);
    }

    public static <E> Alterable.Selector<E> createSelector(List<E> objects, Alterable.SelectorListener<E> selectorListener, boolean firstChange) {
        Alterable.Selector<E> selector = new Alterable.Selector<E>() {
            @Override
            public void onChange(E object) {
                selectorListener.onChange(object);
            }

            @Override
            protected E[] getElements() {
                return objects.toArray((E[]) new Object[objects.size()]);
            }
        };

        Platform.runLater(() -> {
            if (firstChange) selector.change();
        });

        return selector;
    }

    public static void createSimpleAlarm(Worker worker, ClockValue value, boolean repeatNotification) {
        createSimpleAlarm(worker, value, null, repeatNotification);
    }

    public static void createSimpleAlarm(Worker worker, ClockValue value, Notifiable notifiable, boolean repeatNotification) {
        AlarmBuilder alarmBuilder = new AlarmBuilder(worker);
        alarmBuilder.withValue(value);
        alarmBuilder.withCustomNotifiable(notifiable);
        alarmBuilder.withRepeatNotification(repeatNotification);
        alarmBuilder.submit();
    }

    public static ClockTheme createSimpleTheme(ClockTheme baseTheme, Paint background) {
        return ClockTheme.createTheme(background, baseTheme.getTextFill(), baseTheme.getRippleFill());
    }

    public static ClockTheme createSimpleTheme(ClockTheme baseTheme, Paint background, Paint ripple) {
        return ClockTheme.createTheme(background, baseTheme.getTextFill(), ripple);
    }

    public static void putLocalInstanceState(Object context, String key, Object value) {
        ClockState.putInstanceState(context, key, value);
    }

    public static Object getLocalInstanceState(Object context, String key) {
        return ClockState.getInstanceState(context, key);
    }

    public static void putGlobalInstanceState(String key, Object value) {
        ClockState.putGlobalInstanceState(key, value);
    }

    public static Object getGlobalInstanceState(String key) {
        return ClockState.getGlobalInstanceState(key);
    }
}
