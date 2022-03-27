package ru.finex.core;

import lombok.Cleanup;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * @author m0nster.mind
 */
public class Banner {

    private static final String BANNER_PATH = "banner";

    /**
     * Print banner.
     */
    public static void print() {
        loadBanner().forEach(System.out::println);
    }

    private static List<String> loadBanner() {
        @Cleanup InputStream is = Banner.class.getClassLoader().getResourceAsStream(BANNER_PATH);
        if (is == null) {
            return Collections.emptyList();
        }

        try {
            return IOUtils.readLines(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
