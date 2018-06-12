package model.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple static logging class for debugging.
 */
public class PaintLogging {

    private static final Logger LOGGER = Logger.getLogger(PaintLogging.class.getName());

    public static void logInfo(String info) {
        LOGGER.info(info);
    }

    public static void stopLogging() {
        LOGGER.setLevel(Level.OFF);
    }
}
