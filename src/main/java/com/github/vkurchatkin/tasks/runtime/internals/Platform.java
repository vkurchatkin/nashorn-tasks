package com.github.vkurchatkin.tasks.runtime.internals;

import com.github.vkurchatkin.tasks.runtime.Internal;

import java.nio.file.Paths;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 20:35
 */

@Internal(name="platform")
public class Platform {
    public String getOS () {
        return System.getProperty("os.name").toLowerCase();
    }

    public String getCwd () {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    public void exit (int code) {
        System.exit(code);
    }

    public int getCpus () {
        return Runtime.getRuntime().availableProcessors();
    }

    public long getNanotime () {
        return System.nanoTime();
    }
}
