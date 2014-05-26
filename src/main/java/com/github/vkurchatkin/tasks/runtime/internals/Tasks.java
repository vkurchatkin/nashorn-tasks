package com.github.vkurchatkin.tasks.runtime.internals;

import com.github.vkurchatkin.tasks.runtime.Internal;
import com.github.vkurchatkin.tasks.runtime.internals.tasks.Executor;
import com.github.vkurchatkin.tasks.runtime.internals.tasks.Job;

/**
 * User: vk
 * Date: 26/05/14
 * Time: 11:48
 */

@Internal(name="tasks")
public class Tasks {
    public Executor createExecutor(int concurrency) {
        return new Executor(concurrency);
    }

    public Job createJob(String src, Object data) {
      return new Job(src, data);
    }

}
