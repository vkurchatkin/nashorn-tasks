package com.github.vkurchatkin.tasks.runtime.internals.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * User: vk
 * Date: 26/05/14
 * Time: 12:15
 */
public class Executor {
    private CompletionService<String> completionService;
    private ExecutorService executorService;
    private Map<Future, Job> jobs;


    public Executor(int concurrency) {
        executorService = Executors.newFixedThreadPool(concurrency);
        completionService = new ExecutorCompletionService<String>(executorService);
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

    public void shutdown () {
       executorService.shutdownNow();
    }
}
