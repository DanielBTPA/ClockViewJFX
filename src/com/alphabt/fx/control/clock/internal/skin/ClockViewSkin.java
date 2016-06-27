package com.alphabt.fx.control.clock.internal.skin;

import com.alphabt.fx.control.clock.ClockView;
import com.alphabt.fx.control.clock.util.AlarmBuilder;
import com.alphabt.fx.control.clock.util.ClockTheme;
import com.alphabt.fx.control.clock.worker.Worker;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;

public class ClockViewSkin implements Skin<ClockView> {

    private StackPane root = new StackPane();
    private ClockView clockView;
    private Ripple ripple;

    public ClockViewSkin(ClockView clockView) {
        this.clockView = clockView;
        initialize();
    }

    @Override
    public ClockView getSkinnable() {
        return clockView;
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public void dispose() {
        if (clockView.workerProperty().get() != null)
            clockView.workerProperty().get().dispose();
    }

    public void ripple() {
        Platform.runLater(() -> ripple.animateAll());
    }

    private void initialize() {
        Circle circle = new Circle();
        circle.setStrokeWidth(3);
        circle.setStrokeType(StrokeType.OUTSIDE);
        circle.radiusProperty().bind(clockView.radiusProperty());
        circle.scaleXProperty().bind(clockView.scaleXProperty());
        circle.scaleYProperty().bind(clockView.scaleYProperty());
        circle.scaleZProperty().bind(clockView.scaleZProperty());

        Text text = new Text();
        text.scaleXProperty().bind(clockView.scaleXProperty());
        text.scaleYProperty().bind(clockView.scaleYProperty());
        text.scaleZProperty().bind(clockView.scaleZProperty());
        text.textProperty().bindBidirectional(clockView.labelProperty());

        ObjectProperty<AlarmBuilder> alarmBuilderProperty = new SimpleObjectProperty<>();
        alarmBuilderProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.cancel();
                oldValue.clear();
            }

            newValue.withAlarmListener(() -> runLater(() -> ripple.animateAll()));

            try {
                newValue.submit();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
        alarmBuilderProperty.bind(clockView.attachAlarmBuilderProperty());

        ChangeListener<String> changeListenerLabel = (observable, oldValue, newValue) ->
                runLater(() -> text.setText(newValue));

        ObjectProperty<Worker> workerProperty = new SimpleObjectProperty<>();
        workerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.labelProperty().removeListener(changeListenerLabel);
                if (alarmBuilderProperty.get() != null) {
                    alarmBuilderProperty.get().cancel();
                }
            }

            newValue.labelProperty().addListener(changeListenerLabel);

            runLater(() -> text.setText(newValue.toString()));
        });

        workerProperty.bind(clockView.workerProperty());

        circle.radiusProperty().addListener((observable, oldValue, newValue) -> {
            text.setFont(Font.font((double) newValue / 3.5));
        });

        text.setFont(Font.font(circle.getRadius() / 3.5));

        ripple = new Ripple(circle);


        clockView.themeProperty().addListener((observable, oldValue, newValue) -> {
            circle.setStroke(((Color) newValue.getBackgroundFill()).darker());
            circle.setFill(newValue.getBackgroundFill());
            text.setFill(newValue.getTextFill());
            ripple.setFill(newValue.getRippleFill());
        });

        circle.setStroke(((Color) clockView.getTheme().getBackgroundFill()).darker());
        circle.setFill(clockView.getTheme().getBackgroundFill());
        text.setFill(clockView.getTheme().getTextFill());
        ripple.setFill(clockView.getTheme().getRippleFill());

        root.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                onAction();
            } else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                ripple.animateScale();
            } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                ripple.animateOpacity();
            }
        });

        root.getChildren().addAll(circle, ripple, text);
    }

    private void onAction() {
        if (clockView.getOnAction() != null) {
            EventHandler<ActionEvent> eventHandler = clockView.getOnAction();
            eventHandler.handle(new ActionEvent(clockView, ActionEvent.NULL_SOURCE_TARGET));
        }
    }

    private void runLater(Runnable runnable) {
        Platform.runLater(runnable);
    }

    private class Ripple extends Circle {
        private ScaleTransition scaleTransition;
        private FadeTransition fadeTransition;
        private double oldOpacity;

        private Circle node;

        Ripple(Circle node) {
            super(1);
            this.node = node;
            initialize();
        }

        void initialize() {
            oldOpacity = getOpacity();
            setOpacity(0.0);

            scaleTransition = new ScaleTransition(Duration.millis(490), this);
            scaleTransition.setFromX(0.0);
            scaleTransition.setFromY(0.0);

            DoubleProperty radiusProperty = new SimpleDoubleProperty();
            radiusProperty.addListener((observable, oldValue, newValue) -> {
                scaleTransition.setToX((Double) newValue);
                scaleTransition.setToY((Double) newValue);
            });
            radiusProperty.bind(node.radiusProperty());

            scaleTransition.setOnFinished(event -> {
                node.setStroke(((Color) getFill()).darker());
            });

            fadeTransition = new FadeTransition(Duration.millis(530), this);
            fadeTransition.setFromValue(5.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setInterpolator(Interpolator.EASE_OUT);
            fadeTransition.setOnFinished(event -> {
                node.setStroke(((Color) node.getFill()).darker());
                setScaleX(0.0);
                setScaleY(0.0);
                setOpacity(oldOpacity);
            });
        }

        void animateAll() {
            if (isStoppedOpacity() && isStoppedScale()) {
                animateScale();
                animateOpacity();
            }
        }

        void animateScale() {
            if (isStoppedScale()) {
                setOpacity(oldOpacity);
                scaleTransition.playFromStart();
            }
        }

        void animateOpacity() {
            if (isStoppedOpacity())
                fadeTransition.playFromStart();
        }

        private boolean isStoppedScale() {
            return scaleTransition.getStatus() == Animation.Status.STOPPED;
        }

        private boolean isStoppedOpacity() {
            return fadeTransition.getStatus() == Animation.Status.STOPPED;
        }
    }
}