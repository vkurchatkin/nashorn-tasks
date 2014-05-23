package com.github.vkurchatkin.tasks.runtime;

import javax.script.ScriptException;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 15:44
 */
public class Bindings {
    private Runtime runtime;

    public Bindings(Runtime runtime) {
        this.runtime = runtime;
    }

    public void log (String str) {
        System.out.println(str);
    }

    public Object runScript (String src, String filename) throws ScriptException {
        return runtime.runScript(src, filename);
    }
}
