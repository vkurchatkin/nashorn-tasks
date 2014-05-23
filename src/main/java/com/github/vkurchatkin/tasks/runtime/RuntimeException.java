package com.github.vkurchatkin.tasks.runtime;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 16:14
 */
public class RuntimeException extends Exception {
    public RuntimeException(String message) {
        super(message);
    }

    public RuntimeException(Throwable cause) {
        super(cause);
    }
}
