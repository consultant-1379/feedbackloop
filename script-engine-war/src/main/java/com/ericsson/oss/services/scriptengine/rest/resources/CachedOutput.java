package com.ericsson.oss.services.scriptengine.rest.resources;


import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class CachedOutput implements Serializable {
    private final List<AbstractDto> maintainedElements;
    private List<AbstractDto> readOnceElements;

    CachedOutput(final List<AbstractDto> maintainedElements) {
        this.maintainedElements = maintainedElements;
        this.readOnceElements = new ArrayList<>(0);
    }

    CachedOutput(final List<AbstractDto> maintainedElements, final List<AbstractDto> readOnceElements) {
        this.maintainedElements = maintainedElements;
        this.readOnceElements = readOnceElements;
    }

    List<AbstractDto> getMaintainedElements() {
        return maintainedElements;
    }

    List<AbstractDto> removeReadOnceElements() {
        if(readOnceElements.isEmpty()){
            return readOnceElements;
        }
        final List<AbstractDto> returnElements = readOnceElements;
        readOnceElements = new ArrayList<>(0);
        return returnElements;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        final CachedOutput that = (CachedOutput) o;

        if (!maintainedElements.equals(that.maintainedElements)){
            return false;
        }
        if (!readOnceElements.equals(that.readOnceElements)){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = maintainedElements.hashCode();
        result = 31 * result + readOnceElements.hashCode();
        return result;
    }
}