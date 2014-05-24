package com.github.vkurchatkin.tasks.runtime.internals;

import com.github.vkurchatkin.tasks.runtime.Internal;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 20:13
 */

@Internal(name="stdio")
public class Stdio {
    public void writeToStdout (String str) {
        System.out.print(str);
    }

    public void writeToStderr (String str) {
        System.err.print(str);
    }
}
