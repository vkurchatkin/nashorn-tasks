package com.github.vkurchatkin.tasks.runtime.internals;

import com.github.vkurchatkin.tasks.runtime.Internal;

import java.io.*;

/**
 * User: vk
 * Date: 24/05/14
 * Time: 13:59
 */

@Internal(name="filesystem")
public class Filesystem {
    public String readFile (String path) throws IOException {
        FileReader reader = new FileReader(path);
        return concatReader(reader);
    }

    public String readResource (String path) throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path);
        return concatReader(new InputStreamReader(stream));
    }

    private String concatReader (Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        int ch;
        while((ch = reader.read())!= -1)
            sb.append((char)ch);

        return sb.toString();
    }
}
