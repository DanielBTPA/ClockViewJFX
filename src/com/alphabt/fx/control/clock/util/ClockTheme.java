package com.alphabt.fx.control.clock.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by danielbt on 17/06/16.
 */
public class ClockTheme {

    public static final ClockTheme DEFAULT_DARK = new ClockTheme(Color.web("#424242"), Color.WHITE, Color.web("#424242").darker());
    public static final ClockTheme DEFAULT_LIGHT = new ClockTheme(Color.web("#EEEEEE"), Color.BLACK, Color.web("#EEEEEE").darker());

    public static ClockTheme createTheme(Paint backgroundFill, Paint textFill, Paint rippleFill) {
        return new ClockTheme(backgroundFill, textFill, rippleFill);
    }

    private ObjectProperty<Paint> backgroundFillProperty = new SimpleObjectProperty<>();
    private ObjectProperty<Paint> textFillProperty = new SimpleObjectProperty<>();
    private ObjectProperty<Paint> rippleFillProperty = new SimpleObjectProperty<>();

    private ClockTheme(Paint backgroundFill, Paint textFill, Paint rippleFill) {
        backgroundFillProperty.set(backgroundFill);
        textFillProperty.set(textFill);
        rippleFillProperty.set(rippleFill);
    }

    public ReadOnlyObjectProperty<Paint> backgroundFillProperty() {
        return backgroundFillProperty;
    }

    public ReadOnlyObjectProperty<Paint> textFillProperty() {
        return textFillProperty;
    }

    public ReadOnlyObjectProperty<Paint> rippleFillProperty() {
        return rippleFillProperty;
    }

    public Paint getBackgroundFill() {
        return backgroundFillProperty().get();
    }

    public Paint getTextFill() {
        return textFillProperty().get();
    }

    public Paint getRippleFill() {
        return rippleFillProperty().get();
    }

    public static final class Colors {
        public static final ClockTheme RED_DARK = ClockTheme.createTheme(Color.web("#f44336"), Color.WHITE,
                Color.web("#b71c1c"));

        public static final ClockTheme RED_LIGHT = ClockTheme.createTheme(Color.web("#f44336"), Color.BLACK,
                Color.web("#b71c1c"));

        public static final ClockTheme PINK_DARK = ClockTheme.createTheme(Color.web("#E91E63"), Color.WHITE,
                Color.web("#880E4F"));

        public static final ClockTheme PINK_LIGHT = ClockTheme.createTheme(Color.web("#E91E63"), Color.BLACK,
                Color.web("#880E4F"));

        public static final ClockTheme PURPLE_DARK = ClockTheme.createTheme(Color.web("#9C27B0"), Color.WHITE,
                Color.web("#4A148C"));

        public static final ClockTheme PURPLE_LIGHT = ClockTheme.createTheme(Color.web("#9C27B0"), Color.BLACK,
                Color.web("#4A148C"));

        public static final ClockTheme DEEP_PURPLE_DARK = ClockTheme.createTheme(Color.web("#673AB7"), Color.WHITE,
                Color.web("#311B92"));

        public static final ClockTheme DEEP_PURPLE_LIGHT = ClockTheme.createTheme(Color.web("#673AB7"), Color.BLACK,
                Color.web("#311B92"));

        public static final ClockTheme INDIGO_DARK = ClockTheme.createTheme(Color.web("#3F51B5"), Color.WHITE,
                Color.web("#1A237E"));

        public static final ClockTheme INDIGO_LIGHT = ClockTheme.createTheme(Color.web("#3F51B5"), Color.BLACK,
                Color.web("#1A237E"));

        public static final ClockTheme BLUE_DARK = ClockTheme.createTheme(Color.web("#2196F3"), Color.WHITE,
                Color.web("#0D47A1"));

        public static final ClockTheme BLUE_LIGHT = ClockTheme.createTheme(Color.web("#2196F3"), Color.BLACK,
                Color.web("#0D47A1"));

        public static final ClockTheme CYAN_DARK = ClockTheme.createTheme(Color.web("#00BCD4"), Color.WHITE,
                Color.web("#006064"));

        public static final ClockTheme CYAN_LIGHT = ClockTheme.createTheme(Color.web("#00BCD4"), Color.BLACK,
                Color.web("#006064"));

        public static final ClockTheme TEAL_DARK = ClockTheme.createTheme(Color.web("#009688"), Color.WHITE,
                Color.web("#004D40"));

        public static final ClockTheme TEAL_LIGHT = ClockTheme.createTheme(Color.web("#009688"), Color.BLACK,
                Color.web("#004D40"));

        public static final ClockTheme GREEN_DARK = ClockTheme.createTheme(Color.web("#69F0AE"), Color.WHITE,
                Color.web("#1B5E20"));

        public static final ClockTheme GREEN_LIGHT = ClockTheme.createTheme(Color.web("#69F0AE"), Color.BLACK,
                Color.web("#1B5E20"));

        public static final ClockTheme YELLOW_DARK = ClockTheme.createTheme(Color.web("#FFEB3B"), Color.WHITE,
                Color.web("#F57F17"));

        public static final ClockTheme YELLOW_LIGHT = ClockTheme.createTheme(Color.web("#FFEB3B"), Color.BLACK,
                Color.web("#F57F17"));

        public static final ClockTheme ORANGE_DARK = ClockTheme.createTheme(Color.web("#FF9800"), Color.WHITE,
                Color.web("#E65100"));

        public static final ClockTheme ORANGE_LIGHT = ClockTheme.createTheme(Color.web("#FF9800"), Color.BLACK,
                Color.web("#E65100"));

        public static final ClockTheme DEEP_ORANGE_DARK = ClockTheme.createTheme(Color.web("#FF5722"), Color.WHITE,
                Color.web("#BF360C"));

        public static final ClockTheme DEEP_ORANGE_LIGHT = ClockTheme.createTheme(Color.web("#FF5722"), Color.BLACK,
                Color.web("#BF360C"));

        public static final ClockTheme BROWN_DARK = ClockTheme.createTheme(Color.web("#795548"), Color.WHITE,
                Color.web("#3E2723"));

        public static final ClockTheme BROWN_LIGHT = ClockTheme.createTheme(Color.web("#795548"), Color.BLACK,
                Color.web("#3E2723"));
    }
}
