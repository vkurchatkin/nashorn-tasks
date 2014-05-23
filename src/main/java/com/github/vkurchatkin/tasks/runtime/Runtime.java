package com.github.vkurchatkin.tasks.runtime;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 16:10
 */
public class Runtime {
    private final static String[] NASHORN_OPTS = { "--no-syntax-extensions" };

    private NashornScriptEngine engine;
    private ScriptContext context;

    private Bindings bindings;
    private String[] args;

    public Runtime(String[] args) {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        engine = (NashornScriptEngine) factory.getScriptEngine(NASHORN_OPTS);
        bindings = new Bindings(this);
        context = new SimpleScriptContext();
        context.setBindings(new SimpleBindings(), ScriptContext.GLOBAL_SCOPE);
        this.args = args;
    }

    synchronized public void run () throws RuntimeException {

        ScriptObjectMirror res;

        try {
            res = (ScriptObjectMirror) runInternal("bootstrap.js");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        res.call(null, bindings);
    }

    public Object runInternal (String filename) throws ScriptException, IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("bootstrap.js");

        StringBuilder sb = new StringBuilder();

        int ch;
        while((ch = stream.read())!= -1)
            sb.append((char)ch);

        return runScript(sb.toString(), filename);
    }

    public Object runScript (String src, String filename) throws ScriptException {
        context.getBindings(ScriptContext.GLOBAL_SCOPE).put(ScriptEngine.FILENAME, filename);

        return engine.eval(src, context);
    }

    public String[] getArgs() {
        return args;
    }
}
