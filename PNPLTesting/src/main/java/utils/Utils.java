package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {
    private static final Logger seguimiento = LogManager.getLogger("seguimiento");
    private static final Logger errores = LogManager.getLogger("errores");

    public static Logger LoggerSeguimiento() { return seguimiento; }
    public static Logger LoggerError() { return errores; }
}
