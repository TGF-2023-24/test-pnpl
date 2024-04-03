package logic;

import java.util.ArrayList;
import java.util.List;

import objects.PNPL;

public class Tester {

    private PNPL metamodelo, modelo;

    public Tester(PNPL metamodelo, PNPL modelo) {
        this.metamodelo = metamodelo;
        this.modelo = modelo;
    }

    public List<String> check() {
        List<String> errores = new ArrayList<>();

        errores.addAll(checkArcs());

        return errores;
    }

    private List<String> checkArcs() {
        List<String> errores = new ArrayList<>();


        return errores;
    }
    
}
