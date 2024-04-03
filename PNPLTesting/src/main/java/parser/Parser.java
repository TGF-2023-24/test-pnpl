package parser;

import objects.Arc;
import objects.Node;
import objects.PNPL;
import objects.Place;
import objects.Relation;
import objects.Transition;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.Literal;
import utils.Type;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Parser {

    public static PNPL parse(Object file) {
        Utils.LoggerSeguimiento().trace("Parseando...");
        PNPL.PNPLBuilder pnplBuild = new PNPL.PNPLBuilder();    
        Object PetriNet = Utils.getList(file, "pn_150");
        Object FM = Utils.getList(file, "featuremodel");
        Object presenceConditions = Utils.getListComplete(file, "presenceconditions");

        Object nodes = Utils.getList(FM, "nodes");
        Object relations =  Utils.getList(FM, "relations");

        Object elements = Utils.getList(PetriNet, "elements");

        return pnplBuild.nodes(parseNodes(nodes))
                .relations(parseRelations(relations))
                .presenceCondition(parsePresenceCondition(presenceConditions))
                .transitions(parseTransitions(elements))
                .arcs(parseArc(elements))
                .build();


    }

    private static List<Node> parseNodes(Object nodes) {
        List<Node> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando nodos...");
        try {
            for (int i = 0; i < Utils.getSize(nodes); i++) {
                Object node = Utils.getElement(nodes, i);
                Node n = new Node.NodeBuilder((String)Utils.getAttribute(node, "name"))
                .isAbstract(Utils.getBooleanAttribute(node, "abstract"))
                .isMandatory(Utils.getBooleanAttribute(node, "mandatory"))
                .requirments(Arrays.stream(Utils.getAttribute(node, "requires").toString().split("(?=[A-Z])")).toList())
                .excludes(Arrays.stream(Utils.getAttribute(node, "excludes").toString().split("(?=[A-Z])")).toList())
                .build();
                list.add(n);
            }
            for (int i = 0; i < Utils.getSize(nodes); i++)
            for (Node nodo : list) {
                nodo.setNodeRequirements(parseRequirements(nodo.getNodeRequirements(), list));
                nodo.setExcludes(parseRequirements(nodo.getExcludes(), list));
                Utils.LoggerSeguimiento().debug("Nodo " + list.indexOf(nodo)+": " + nodo.toString());
            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear los nodos: " + e.getMessage());
        }
            return list;
        }
        
    private static List<Relation> parseRelations(Object relations) {
        List<Relation> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando relaciones...");
        try {

            for (int i = 0;  i <  Utils.getSize(relations); i++) {
                Object relation = Utils.getElement(relations, i);
                Object aux_list = Utils.getAttribute(relation, "children");
                if (aux_list == "") aux_list = Utils.getAttribute(relation, "childs");
                Relation relacion = new Relation.RelationBuilder(Utils.getAttribute(relation,"parent").toString())
                .children(Arrays.stream(aux_list.toString().split("(?=[A-Z])")).toList())
                .type(parseType(Utils.getAttribute(relation,"type").toString()))
                .build();
                list.add(relacion);
                Utils.LoggerSeguimiento().debug("Relacion " + list.indexOf(relacion)+": " + relacion.toString());
            }
        } catch (Exception e) {
            Utils.LoggerError().error("Error al parsear las relaciones: " + e.getMessage());
        }
            return list;
        }
        
        private static List<String> parseChildren(JSONArray children) {
        List<String> list = new ArrayList<>();
        for (Object obj : children)
            list.add(obj.toString());
        return list;
    }

    private static List<String> parseRequirements(List<String> words, List<Node> existingNodes) {
        List<String> list = new ArrayList<>();
        HashSet<String> added_nodes = new HashSet<>();
        for (String word : words) {
            if (word.equals(Literal.VALOR_VACIO)) continue;
            for (Node node : existingNodes) {
                if (node.getName().contains(word) && !added_nodes.contains(node.getName())) {
                    list.add(node.getName());
                    added_nodes.add(node.getName());
                }

            }
        }
        return list;
    }

    private static Type parseType(String typeToSearch) {
        for (Type type : Type.values()) {
            if (type.toString().equals(typeToSearch))
                return type;
        }
        return null;
    }

    private static List<String> parsePresenceCondition(Object presenceConditions) {
        List<String> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando PCs...");
        try {
            for (int i = 0;  i < Utils.getSize(presenceConditions); i++) {
                Object pc = Utils.getElement(presenceConditions, i);
                String presenceCondition = Utils.getAttribute(pc, "id").toString();
                list.add(presenceCondition);
                Utils.LoggerSeguimiento().debug("Presence Condition " + list.indexOf(presenceCondition)+": " + presenceCondition);
            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear las PCs: " + e.getMessage());
        }
        return list;
    }

    private static List<Transition> parseTransitions(Object elements) {
        List<Transition> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando transiciones...");
        try {

            for (int i = 0; i < Utils.getSize(elements); i++) {
                Object element = Utils.getElement(elements, i);
                String type = Utils.getAttribute(element, "xsi:type").toString();

                if (type.contains("Transition")) {
                    Transition transition = new Transition.TransitionBuilder(Utils.getAttribute(element, "name").toString())
                    .presenceCondition(Utils.getAttribute(element, "presenceCondition").toString())
                    .build();
                    list.add(transition);
                    Utils.LoggerSeguimiento().debug("Transition " + list.indexOf(transition)+": " + transition.toString());
                }

                // Place p = new Place.PlaceBuilder(Utils.getAttribute(place, "name").toString())
                // .presenceCondition(Utils.getAttribute(place, "id").toString())
                // .build();
                // list.add(p);
                // Utils.LoggerSeguimiento().debug("Place " + list.indexOf(p)+": " + p.toString());

            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear las transiciones: " + e.getMessage());
        }
            return list;
    }

    private static List<Arc> parseArc(Object elements) {
        List<Arc> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando arcos...");
        try {

            for (int i = 0; i < Utils.getSize(elements); i++) {
                Object element = Utils.getElement(elements, i);
                String type = Utils.getAttribute(element, "xsi:type").toString();

                if (type.contains("Arc")) {
                    Arc arco = new Arc.ArcBuilder(Utils.getAttribute(element, "name").toString())
                    .presenceCondition(Utils.getAttribute(element, "presenceCondition").toString())
                    .type(Utils.getAttribute(element, "xsi:type").toString())
                    .source(Utils.getAttribute(element, "source").toString())
                    .target(Utils.getAttribute(element, "target").toString())
                    .build();
                    list.add(arco);
                    Utils.LoggerSeguimiento().debug("Arc " + list.indexOf(arco)+": " + arco.toString());
                }

            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear los arcos: " + e.getMessage());
        }
            return list;
    }
}
