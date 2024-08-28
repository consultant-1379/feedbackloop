package com.ericsson.oss.services.scriptengine.rest.resources.scriptengine.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.ericsson.oss.services.scriptengine.rest.resources.CommandRestResourceBean;
import com.ericsson.oss.services.scriptengine.rest.resources.FileRestResourceBean;

@ApplicationPath("/services")
public class ScriptEngineResourceMapper extends Application {
    private final Set<Object> resourceObjects = new HashSet<>();
    private final Set<Class<?>> resourceClasses = new HashSet<>();

    public ScriptEngineResourceMapper() {
        resourceClasses.add(CommandRestResourceBean.class);
        resourceClasses.add(FileRestResourceBean.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return resourceClasses;

    }

    @Override
    public Set<Object> getSingletons() {
        return resourceObjects;
    }

}
