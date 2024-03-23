package main;

import controller.Controller;
import utils.Literal;
import utils.Utils;

public class Main {
    public static void main(String[] args) {
        Utils.LoggerSeguimiento().trace("-----------------------");
        Utils.LoggerSeguimiento().debug("Iniciando la aplicación");
        
        if (args.length == 0) Utils.LoggerError().error(Literal.PARAMETROS_INVALIDOS);
        else {
            String directorio_raiz = System.getProperty("user.dir") + Literal.NOMBRE_PROYECTO;
            Controller.getInstance().execute(directorio_raiz + args[0]);
            Utils.LoggerSeguimiento().debug("Finalizando la aplicación");
            Utils.LoggerSeguimiento().trace("-----------------------");
        }
    }
}