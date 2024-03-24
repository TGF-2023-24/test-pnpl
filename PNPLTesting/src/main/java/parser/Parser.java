package parser;

import objects.Node;
import objects.PNPL;
import objects.Place;
import objects.Relation;
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
        JSONObject json = (JSONObject) file;
        //JSONObject PetriNet = (JSONObject) json.get("PetriNet");
        Object PetriNet = Utils.getElement(file, "PetriNet");
        Object FM = Utils.getElement(file, "FeatureModel");
        Object presenceConditions = Utils.getElement(file, "PresenceConditions");

        Object nodes = Utils.getElement(FM, "Nodes");
        Object relations =  Utils.getElement(FM, "Relations");

        Object places = Utils.getElement(PetriNet, "places");

        return pnplBuild.nodes(parseNodes(nodes))
                .relations(parseRelations(relations))
                .presenceCondition(parsePresenceCondition(presenceConditions))
                .places(parsePlaces(places))
                .build();


    }

    private static List<Node> parseNodes(Object nodes) {
        List<Node> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando nodos...");
        try {
            for (int i = 0; i < Utils.getSize(nodes); i++) {
                JSONObject node = (JSONObject) Utils.getElement(nodes, i);
                Node n = new Node.NodeBuilder((String)node.get("name"))
                .isAbstract((boolean) node.get("abstract"))
                .isMandatory((boolean) node.get("mandatory"))
                .requirments(Arrays.stream(node.get("requires").toString().split("(?=[A-Z])")).toList())
                .excludes(Arrays.stream(node.get("excludes").toString().split("(?=[A-Z])")).toList())
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
                JSONObject relation = (JSONObject) Utils.getElement(relations, i);
                Relation relacion = new Relation.RelationBuilder(relation.get("parent").toString())
                .children(parseChildren((JSONArray) relation.get("children")))
                .type(parseType(relation.get("type").toString()))
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
                JSONObject pc = (JSONObject) Utils.getElement(presenceConditions, i);
                String presenceCondition = pc.get("id").toString();
                list.add(presenceCondition);
                Utils.LoggerSeguimiento().debug("Presence Condition " + list.indexOf(presenceCondition)+": " + presenceCondition);
            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear las PCs: " + e.getMessage());
        }
        return list;
    }

    private static List<Place> parsePlaces(Object places) {
        List<Place> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando places...");
        try {

            for (int i = 0; i < Utils.getSize(places); i++) {
                JSONObject place = (JSONObject) Utils.getElement(places, i);
                
                Place p = new Place.PlaceBuilder(place.get("name").toString())
                .presenceCondition(place.get("id_PC").toString())
                .build();
                list.add(p);
                Utils.LoggerSeguimiento().debug("Place " + list.indexOf(p)+": " + p.toString());

            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear los places: " + e.getMessage());
        }
            return list;
    }
}
