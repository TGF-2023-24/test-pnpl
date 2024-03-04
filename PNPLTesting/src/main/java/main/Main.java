package main;

import controller.Controller;
import utils.Literal;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) System.err.println(Literal.PARAMETROS_INVALIDOS);
        else Controller.getInstance().execute(args[0]);
    }
}