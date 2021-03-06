package com.github.vkurchatkin.tasks.runtime;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ECMAException;
import org.reflections.Reflections;

import javax.script.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 16:10
 */
public class Runtime {
    private final static String[] NASHORN_OPTS = { "--no-syntax-extensions", "--no-java" };

    private NashornScriptEngine engine;
    private ScriptContext context;

    private String[] args;
    private Map<String, Object> internals;
    private ScriptObjectMirror microtaskFunction;

    public Runtime(String[] args) {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        engine = (NashornScriptEngine) factory.getScriptEngine(NASHORN_OPTS);
        context = new SimpleScriptContext();
        context.setBindings(new SimpleBindings(), ScriptContext.GLOBAL_SCOPE);
        this.args = args;

        Reflections reflections = new Reflections("com.github.vkurchatkin.tasks.runtime.internals");

        Set<Class<?>> internalClasses = reflections.getTypesAnnotatedWith(Internal.class);

        internals = new HashMap<>();

        for (Class internal : internalClasses) {
            Internal annotation = (Internal) internal.getAnnotation(Internal.class);
            try {
                internals.put(annotation.name(), internal.newInstance());
            } catch (Exception e) {
            }
        }

    }

    synchronized public void run () throws RuntimeException {

        ScriptObjectMirror res;
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("bootstrap.js");

        StringBuilder sb = new StringBuilder();

        try {
            int ch;

            while((ch = stream.read())!= -1)
                sb.append((char)ch);

            res = (ScriptObjectMirror) runScript(sb.toString(), "bootstrap.js");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        res.call(null, this);

        if (microtaskFunction != null)
            microtaskFunction.call(null);

    }


    public Object runScript (String src, String filename) throws ScriptException {
        context.getBindings(ScriptContext.GLOBAL_SCOPE).put(ScriptEngine.FILENAME, filename);

        return engine.eval(src, context);
    }

    public String[] getArgs() {
        return args;
    }

    public Object getInternal (String id) {
        return internals.get(id);
    }

    public void debugLog (String str) {
        System.out.println(str);
    }

    public String getErrorStack (Throwable e) {
        return ECMAException.getScriptStackString(e); // HACK
    }

    public void registerMicrotaskFunction (ScriptObjectMirror fn) {
        microtaskFunction = fn;
    }

}
