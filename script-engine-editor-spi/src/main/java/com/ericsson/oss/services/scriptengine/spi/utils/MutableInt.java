package com.ericsson.oss.services.scriptengine.spi.utils;

class MutableInt extends Number {
    private static final long serialVersionUID = 1L;
    int value;
    MutableInt(final int value){
        this.value = value;
    }

    int getValue() {
        return value;
    }

    void setValue(final int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other){
            return true;
        }

        if (other == null) {
            return false;
        }

        if (other instanceof Number) {
            return ((Number) other).longValue() == longValue();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return value;
    }
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}