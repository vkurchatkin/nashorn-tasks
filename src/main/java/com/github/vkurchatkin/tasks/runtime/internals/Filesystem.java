package com.github.vkurchatkin.tasks.runtime.internals;

import java.io.FileReader;
import java.io.IOException;

/**
 * User: vk
 * Date: 24/05/14
 * Time: 13:59
 */
public class Filesystem {
    public String readFile (String path) throws IOException {

        FileReader reader = new FileReader(path);

        StringBuilder sb = new StringBuilder();

        int ch;
        while((ch = reader.read())!= -1)
            sb.append((char)ch);

        return sb.toString();
    }
}
