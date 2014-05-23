package com.github.vkurchatkin.tasks.runtime;

import javax.script.ScriptException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * User: vk
 * Date: 23/05/14
 * Time: 15:44
 */
public class Bindings {
    private Runtime runtime;

    public Bindings(Runtime runtime) {
        this.runtime = runtime;
    }

    public void log (String str) {
        System.out.println(str);
    }

    public Object runScript (String src, String filename) throws ScriptException {
        return runtime.runScript(src, filename);
    }

    public String readFile (String path) throws IOException {

        FileReader reader = new FileReader(path);

        StringBuilder sb = new StringBuilder();

        int ch;
        while((ch = reader.read())!= -1)
            sb.append((char)ch);

        return sb.toString();
    }

    public List<String> getArgs () {
        return Arrays.asList(runtime.getArgs());
    }

    public void exit (int code) {
        System.exit(code);
    }

    public String getCwd () {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }
}
