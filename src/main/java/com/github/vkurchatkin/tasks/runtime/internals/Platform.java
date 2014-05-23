package com.github.vkurchatkin.tasks.runtime.internals;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 20:35
 */
public class Platform {
    public String getOS () {
        return System.getProperty("os.name").toLowerCase();
    }
}
