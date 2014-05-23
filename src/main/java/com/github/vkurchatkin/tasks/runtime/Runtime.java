package com.github.vkurchatkin.tasks.runtime;

import com.github.vkurchatkin.tasks.runtime.internals.Platform;
import com.github.vkurchatkin.tasks.runtime.internals.Stdio;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 16:10
 */
public class Runtime {
    private final static String[] NASHORN_OPTS = { "--no-syntax-extensions" };
    private final static String[] BUILTINS = { "console", "util", "path" };

    private NashornScriptEngine engine;
    private ScriptContext context;

    private Bindings bindings;
    private String[] args;
    private Map<String, Module> moduleCache;
    private Map<String, Object> internals;

    public Runtime(String[] args) {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        engine = (NashornScriptEngine) factory.getScriptEngine(NASHORN_OPTS);
        bindings = new Bindings(this);
        context = new SimpleScriptContext();
        context.setBindings(new SimpleBindings(), ScriptContext.GLOBAL_SCOPE);
        this.args = args;
        moduleCache = new HashMap<>();

        internals = new HashMap<>();

        internals.put("stdio", new Stdio());
        internals.put("platform", new Platform());
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
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(filename);

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

    public Object require (String id) throws RuntimeException, ScriptException, IOException {
        if (moduleCache.containsKey(id)) {
            return moduleCache.get(id).getExports();
        }

        Module module;

        if (Arrays.asList(BUILTINS).contains(id)) {
            module = new Module(id, id + ".js");
            this.moduleCache.put(id, module);
            this.runInternal(id + ".js");
        } else {
            throw new RuntimeException("Not implemented yet");
        }

        if (module.getFactory() == null) {
            throw  new RuntimeException("Module was loaded, but not defined");
        }


        return module.getExports();
    }

    public void defineModule (String id, ScriptObjectMirror factory) throws RuntimeException {
        if (!moduleCache.containsKey(id)) {
            throw new RuntimeException("Unknown module");
        }

        moduleCache.get(id).setFactory(factory);
    }

    public Object getInternal (String id) {
        return internals.get(id);
    }

}
