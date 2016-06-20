package com.alphabt.fx.control.clock;

import com.alphabt.fx.control.clock.util.AlarmBuilder;
import com.alphabt.fx.control.clock.util.ClockTheme;
import com.alphabt.fx.control.clock.worker.Worker;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * Created by danielbt on 11/05/16.
 */
public class ClockView extends Control {

    private ObjectProperty<Worker> workerProperty = new SimpleObjectProperty<>(this, "chrworker");
    private ObjectProperty<ClockTheme> themeProperty = new SimpleObjectProperty<>(this, "themeProperty", ClockTheme.DEFAULT_LIGHT);
    private DoubleProperty radiusProperty = new SimpleDoubleProperty(this, "radius", 60.0);
    private ObjectProperty<EventHandler<ActionEvent>> onActionProperty = new SimpleObjectProperty<>(this, "onAction");
    private StringProperty labelProperty = new SimpleStringProperty(this, "labelProperty", "--:--:--");
    private ObjectProperty<AlarmBuilder> attachAlarmBuilderProperty = new SimpleObjectProperty();

    public ClockView() {
    }

    public ClockView(double radius) {
        radiusProperty.set(radius);
    }

    public ClockView(Worker worker) {
        checkWorker(worker);
        workerProperty.set(worker);
    }

    public ClockView(double radius, Worker worker) {
        checkWorker(worker);
        radiusProperty.set(radius);
        workerProperty.set(worker);
    }

    public ClockView(double radius, Worker worker, ClockTheme theme) {
        this(radius, worker);
        themeProperty.set(theme);
    }

    public DoubleProperty radiusProperty() {
        return radiusProperty;
    }

    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onActionProperty;
    }

    public ObjectProperty<Worker> workerProperty() {
        return workerProperty;
    }

    public StringProperty labelProperty() {
        return labelProperty;
    }

    public ObjectProperty<ClockTheme> themeProperty() {
        return themeProperty;
    }

    public ObjectProperty<AlarmBuilder> attachAlarmBuilderProperty() {
        return attachAlarmBuilderProperty;
    }

    public void attachAlarmBuilder(AlarmBuilder alarmBuilder) {
        attachAlarmBuilderProperty.set(alarmBuilder);
    }

    public void setWorker(Worker worker) {
        checkWorker(worker);
        workerProperty.set(worker);
    }

    public Worker getWorker() {
        return workerProperty.get();
    }

    public void setTheme(ClockTheme value) {
        themeProperty.set(value);
    }

    public ClockTheme getTheme() {
        return themeProperty.get();
    }

    public void setRadius(double value) {
        radiusProperty.set(value);
    }

    public double getRadius() {
        return radiusProperty.get();
    }

    public String getText() {
        return labelProperty.get();
    }

    public void setOnAction(EventHandler<ActionEvent> event) {
        onActionProperty.set(event);
    }

    public EventHandler<ActionEvent> getOnAction() {
        return onActionProperty.get();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ClockViewSkin(this);
    }

    private void checkWorker(Object value) {
        if (value == null) {
            throw new NullPointerException("Worker is 'null' and can't be initialized.");
        }
    }
}
