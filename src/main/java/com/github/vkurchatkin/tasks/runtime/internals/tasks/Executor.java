package com.github.vkurchatkin.tasks.runtime.internals.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * User: vk
 * Date: 26/05/14
 * Time: 12:15
 */
public class Executor implements ThreadFactory {
    private CompletionService<String> completionService;
    private Map<Future, Job> jobs;


    public Executor(int concurrency) {
        completionService = new ExecutorCompletionService<String>(Executors.newFixedThreadPool(concurrency, this));
        jobs = new HashMap<>();
    }

    public void submitJob (Job job) {
        Future future =  completionService.submit(job);
        job.setFuture(future);
        jobs.put(future, job);
    }

    public Job nextJob () throws InterruptedException {
        Future  future = completionService.take();

        Job job = jobs.get(future);
        jobs.remove(future);

        return job;
    }


    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    }
}
