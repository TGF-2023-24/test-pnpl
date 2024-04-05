package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import objects.Arc;
import objects.Node;
import objects.PNPL;
import objects.Relation;
import objects.Transition;

public class Tester {

    private PNPL metamodelo, modelo;

    public Tester(PNPL metamodelo, PNPL modelo) {
        this.metamodelo = metamodelo;
        this.modelo = modelo;
    }

    public List<String> check() {
        List<String> errores = new ArrayList<>();

        errores.addAll(checkTransitions());
        errores.addAll(checkNodes());
        errores.addAll(checkRelations());
        errores.addAll(checkArcs());

        return errores;
    }

    private List<String> checkTransitions() {
        List<String> errores = new ArrayList<>();

        for (Transition transition : modelo.getTransitions()) {
            if (!modelo.getPCs().contains(transition.getPresenceCondition()))
                errores.add("Presence Condition " + transition.getPresenceCondition() + " en transición " + transition.getName() + " no encontrada");
        }

        return errores;
    }

    private List<String> checkNodes() {
        List<String> errores = new ArrayList<>();

        HashMap<String, Node> nodeHM = new HashMap<>();
        for (Node nodo : modelo.getNodes()) {
            if (!nodeHM.containsKey(nodo.getName())) nodeHM.put(nodo.getName(), nodo);
        }

        for (Node nodo : modelo.getNodes()) {
            for (String requisito : nodo.getNodeRequirements()) {
                if (!nodeHM.containsKey(requisito))    
                    errores.add("Nodo " + requisito + " requerido por  " + nodo.getName() + " no encontrado");        
            }
            for (String exclusion : nodo.getExcludes()) {
                if (!nodeHM.containsKey(exclusion))    
                    errores.add("Nodo " + exclusion + " excluido por  " + nodo.getName() + " no encontrado");        
            }
        }

        return errores;
    }

    private List<String> checkRelations() {
        List<String> errores = new ArrayList<>();

        HashMap<String, Node> nodeHM = new HashMap<>();
        for (Node nodo : modelo.getNodes()) {
            if (!nodeHM.containsKey(nodo.getName())) nodeHM.put(nodo.getName(), nodo);
        }

        for (Relation relation : modelo.getRelations()) {
           if (!nodeHM.containsKey(relation.getParent()))
                errores.add("Relacion con nodo antecesor " + relation.getParent() + " no encontrado");    
            for (String child : relation.getChildren()) {
                if (!nodeHM.containsKey(child))
                    errores.add("Relacion con nodo sucesor " + child + " no encontrado");    
            }    

        }

        return errores;
    }


    private List<String> checkArcs() {
        List<String> errores = new ArrayList<>();

        for (Arc arco : modelo.getArcs()) {
            if (!modelo.getPCs().contains(arco.getPresenceCondition()))
                errores.add("Presence Condition " + arco.getPresenceCondition() + " en arco " + arco.getName() + " no encontrada");
        }
        //TODO más validaciones

        return errores;
    }
    
}
