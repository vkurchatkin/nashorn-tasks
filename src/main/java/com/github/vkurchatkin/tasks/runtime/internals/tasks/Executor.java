package com.github.vkurchatkin.tasks.runtime.internals.tasks;

import java.util.HashMap;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * User: vk
 * Date: 26/05/14
 * Time: 12:15
 */
public class Executor {
    private CompletionService<String> completionService;
    private HashMap<Future, Job> jobs;


    public Executor(int concurrency) {
        completionService = new ExecutorCompletionService<String>(Executors.newFixedThreadPool(concurrency));
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
}
