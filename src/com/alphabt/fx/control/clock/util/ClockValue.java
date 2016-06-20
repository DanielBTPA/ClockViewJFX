package com.alphabt.fx.control.clock.util;

/**
 * Created by danielbt on 31/05/16.
 */
public class ClockValue implements Cloneable {

    public static final ClockValue ZERO = ClockValue.values(0);

    public static ClockValue values(int firstValue, int secondValue, int thirdValue, int fourthValue) {
        return new ClockValue(firstValue, secondValue, thirdValue, fourthValue);
    }

    public static ClockValue values(int firstValue, int secondValue, int thirdValue) {
        return new ClockValue(firstValue, secondValue, thirdValue, 0);
    }

    public static ClockValue values(int firstValue, int secondValue) {
        return new ClockValue(firstValue, secondValue, 0, 0);
    }

    public static ClockValue values(int firstValue) {
        return new ClockValue(firstValue, 0, 0, 0);
    }

    public static ClockValue values(String parseValue) {
        if (!parseValue.contains(":")) {
            throw new IllegalArgumentException("Need be on this format: '00:00:00:00'.");
        }

        String[] parse = parseValue.split(":");

        if (parse.length > 4) {
            throw new IllegalArgumentException("Max 4 values.");
        }

        int[] values = new int[4];

        for (int i = 0; i < parse.length; i++) {
            values[i] = Integer.valueOf(parse[i]);
        }

        return new ClockValue(values);
    }

    private int firstValue, secondValue, thirdValue, fourthValue;

    private ClockValue(int... values) {
        setAllValues(values[0], values[1], values[2], values[3]);
    }

    public void setFirstValue(int value) {
        this.firstValue = value;
    }

    public void setSecondValue(int value) {
        this.secondValue = value;
    }

    public void setThirdValue(int value) {
        this.thirdValue = value;
    }

    public void setFourthValue(int value) {
        this.fourthValue = value;
    }

    public void setAllValues(int firstValue, int secondValue, int thirdValue, int fourthValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.thirdValue = thirdValue;
        this.fourthValue = fourthValue;
    }

    public void setAllValues(ClockValue newValue) {
        setAllValues(newValue.firstValue, newValue.secondValue, newValue.thirdValue, newValue.fourthValue);
    }

    public int getFirstValue() {
        return firstValue;
    }

    public int getSecondValue() {
        return secondValue;
    }

    public int getThirdValue() {
        return thirdValue;
    }

    public int getFourthValue() {
        return fourthValue;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ClockValue) {
            ClockValue clockValue = (ClockValue) obj;
            return (this.firstValue == clockValue.firstValue &&
                    this.secondValue == clockValue.secondValue &&
                    this.thirdValue == clockValue.thirdValue &&
                    this.fourthValue == clockValue.fourthValue);
        }

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "[firstValue: " + firstValue + ", " + "secondValue: " + secondValue +
                ", " + "thirdValue: " + thirdValue + ", " + "fourthValue: " + fourthValue +
                "]";
    }

    public ClockValue getClonedValue() {
        ClockValue value = null;
        try {
            value = (ClockValue) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return value;
    }
}