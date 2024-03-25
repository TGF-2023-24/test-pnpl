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
            String ecore = directorio_raiz + args[0];
            String xmi = directorio_raiz + args[1];
            Controller.getInstance().execute(ecore, xmi);
            Utils.LoggerSeguimiento().debug(Literal.FIN);
            Utils.LoggerSeguimiento().trace(Literal.SEPARADOR);
        }
    }
}