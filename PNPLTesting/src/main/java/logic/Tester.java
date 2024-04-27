package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import objects.Arc;
import objects.Node;
import objects.PNPL;
import objects.Place;
import objects.Relation;
import objects.Transition;
import objects.metamodel.ArcType;
import objects.metamodel.Metamodel;

public class Tester {

    private Metamodel metamodelo;
    private PNPL modelo;

    public Tester(Metamodel metamodelo, PNPL modelo) {
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
            
            if (!metamodelo.getTypes().contains(relation.getType()))
                errores.add("Tipo " + relation.getType() + " en la relacion " + relation.getId() + " no encontrado");    

        }

        return errores;
    }


    private List<String> checkArcs() {
        List<String> errores = new ArrayList<>();

        HashMap<String, Place> placeHM = new HashMap<>();
        for (Place place : modelo.getPlaces()) {
            if (!placeHM.containsKey(place.getName())) placeHM.put(place.getName(), place);
        }

        HashMap<String, Transition> transitionHM = new HashMap<>();
        for (Transition transition : modelo.getTransitions()) {
            if (!transitionHM.containsKey(transition.getName())) transitionHM.put(transition.getName(), transition);
        }

        for (Arc arco : modelo.getArcs()) {
            if (!modelo.getPCs().contains(arco.getPresenceCondition()))
                errores.add("Presence Condition " + arco.getPresenceCondition() + " en arco " + arco.getName() + " no encontrada");
            if (!placeHM.containsKey(arco.getSource()) && !transitionHM.containsKey(arco.getSource())) 
                errores.add("Elemento no encontrado");
            if (!placeHM.containsKey(arco.getTarget()) && !transitionHM.containsKey(arco.getTarget())) 
                errores.add("Elemento no encontrado");
            for (ArcType type : metamodelo.getArcTypes()) {
                if (type.getName().equals(arco.getType())) {
                    if (type.getSource() == "Transition" && !transitionHM.containsKey(arco.getSource()) && placeHM.containsKey(arco.getSource())) 
                        errores.add("Tipo esperado: Transicion; Tipo recibido: Place " + arco.getSource() + " en arco " + arco.getName());
                    if (type.getSource() == "Place" && transitionHM.containsKey(arco.getSource()) && !placeHM.containsKey(arco.getSource())) 
                        errores.add("Tipo esperado: Place; Tipo recibido: Transicion " + arco.getSource() + " en arco " + arco.getName());
                    if (type.getTarget() == "Transition" && !transitionHM.containsKey(arco.getTarget()) && placeHM.containsKey(arco.getTarget())) 
                        errores.add("Tipo esperado: Transicion; Tipo recibido: Place " + arco.getTarget() + " en arco " + arco.getName());
                    if (type.getTarget() == "Place" && transitionHM.containsKey(arco.getTarget()) && !placeHM.containsKey(arco.getTarget())) 
                        errores.add("Tipo esperado: Place; Tipo recibido: Transicion " + arco.getTarget() + " en arco " + arco.getName());

                }
            }
        }

        return errores;
    }
    
}
