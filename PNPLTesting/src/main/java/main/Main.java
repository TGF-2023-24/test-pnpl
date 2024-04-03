package main;

import controller.Controller;
import utils.Literal;
import utils.Utils;

public class Main {
    public static void main(String[] args) {
        Utils.LoggerSeguimiento().trace(Literal.SEPARADOR);
        Utils.LoggerSeguimiento().debug(Literal.INICIO);
        
        if (args.length == 0) Utils.LoggerError().error(Literal.PARAMETROS_INVALIDOS);
        else {
            String directorio_raiz = System.getProperty("user.dir") + Literal.NOMBRE_PROYECTO;
            for (int i = 0; i < args.length; i++) {
                args[i] = directorio_raiz + args[i];
            }

            Controller.getInstance().execute(args);
            Utils.LoggerSeguimiento().debug(Literal.FIN);
            Utils.LoggerSeguimiento().trace(Literal.SEPARADOR);
        }
    }
}