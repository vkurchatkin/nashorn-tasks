package com.github.vkurchatkin.tasks.runtime;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 16:10
 */
public class Runtime {
    private final static String[] NASHORN_OPTS = { "--no-java", "--no-syntax-extensions" };

    private NashornScriptEngine engine;

    private Bindings bindings;

    public Runtime() {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        engine = (NashornScriptEngine) factory.getScriptEngine(NASHORN_OPTS);
        bindings = new Bindings();
    }

    synchronized public void run () throws RuntimeException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("bootstrap.js");
        Reader reader = new InputStreamReader(stream);

        ScriptObjectMirror res = null;
        try {
            res = (ScriptObjectMirror) engine.eval(reader);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        res.call(null, bindings);

    }
}
