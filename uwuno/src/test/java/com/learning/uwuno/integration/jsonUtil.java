package com.learning.uwuno.integration;

import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

public class jsonUtil {
    private jsonUtil() {
        // Should not run
    }
    public static String jsonToString(String jsonPath) throws FileNotFoundException {
        File file = new File(jsonPath);
        FileInputStream inputStream = new FileInputStream(file);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }
}
