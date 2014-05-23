package com.github.vkurchatkin.tasks;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 15:09
 */
public class Main {
    public static void main(String [] args) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        try {
            InputStream stream = Main.class.getClassLoader().getResourceAsStream("bootstrap.js");
            Reader reader = new InputStreamReader(stream);

            engine.eval(reader);
        } catch (Exception e) {
            System.err.println("Initialization failed");
            System.exit(-1);
        }
    }
}
