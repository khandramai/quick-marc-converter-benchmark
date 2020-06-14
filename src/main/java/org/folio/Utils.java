package org.folio;

import io.vertx.core.json.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class Utils {

    public static JsonObject getMockAsJson(String fullPath) {
        try {
            return new JsonObject(getMockData(fullPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }

    public static String getMockData(String path) throws IOException {
        try (InputStream resourceAsStream = ConverterLoop.class.getClassLoader().getResourceAsStream(path)) {
            if (resourceAsStream != null) {
                return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
            } else {
                StringBuilder sb = new StringBuilder();
                try (Stream<String> lines = Files.lines(Paths.get(path))) {
                    lines.forEach(sb::append);
                }
                return sb.toString();
            }
        }
    }

    public static String getCurrentDateTimeAsString() {
        return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
    }
}
