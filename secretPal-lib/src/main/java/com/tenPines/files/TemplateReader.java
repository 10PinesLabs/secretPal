package com.tenPines.files;

import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.Charset;


public class TemplateReader {

    public String getContent(String templateName) {
        try {
            return Resources.toString(Resources.getResource("mail-templates/" + templateName), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
