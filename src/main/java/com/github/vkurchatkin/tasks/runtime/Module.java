package com.github.vkurchatkin.tasks.runtime;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 19:27
 */
public class Module {
    private String id;
    private ScriptObjectMirror factory;
    private ScriptObjectMirror exports;
    private String filename;
    private Boolean loading;

    public Module(String id, String filename) {
        this.id = id;
        this.loading = false;
        this.filename = filename;
    }

    public String getId() {
        return id;
    }

    public ScriptObjectMirror getFactory() {
        return factory;
    }

    public Boolean getLoading() {
        return loading;
    }

    public String getFilename() {
        return filename;
    }

    public ScriptObjectMirror getExports() throws RuntimeException {
        if (loading)
            throw new RuntimeException("Circular dependencies");

        if (exports == null) {
            loading = true;
            exports = (ScriptObjectMirror) factory.call(null);
            loading = false;
        }

        return exports;
    }

    public void setFactory(ScriptObjectMirror factory) {
        this.factory = factory;
    }
}
