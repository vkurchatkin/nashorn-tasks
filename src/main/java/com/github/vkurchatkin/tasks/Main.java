package com.github.vkurchatkin.tasks;

import com.github.vkurchatkin.tasks.runtime.Runtime;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 15:09
 */
public class Main {
    public static void main(String [] args) {

        Runtime runtime = new Runtime();

        try {
            runtime.run();
        } catch (Exception e) {
            System.err.println("Initialization failed");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
