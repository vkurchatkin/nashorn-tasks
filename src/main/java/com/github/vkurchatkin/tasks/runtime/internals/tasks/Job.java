package com.github.vkurchatkin.tasks.runtime.internals.tasks;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * User: vk
 * Date: 26/05/14
 * Time: 11:57
 */
public class Job implements Callable<String> {

    private final NashornScriptEngine engine;
    private String source;
    private Object data;

    public Object getData() {
        return data;
    }

    public Future<String> getFuture() {
        return future;
    }

    public void setFuture(Future<String> future) {
        this.future = future;
    }

    public String getResult () throws ExecutionException, InterruptedException {
        return  future.get();
    }

    private Future<String> future;

    public Job(String source, Object data) {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        engine = (NashornScriptEngine) factory.getScriptEngine();
        this.source = source;
    }

    @Override
    public String call() throws Exception {

        return (String) engine.eval(source);
    }
}
