package parser;

import objects.Node;
import objects.PNPL;
import objects.Place;
import objects.Relation;
import org.graphwalker.core.model.Edge;
import org.graphwalker.core.model.Vertex;
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

    public static PNPL parse(JSONObject json) {
        Utils.LoggerSeguimiento().trace("Parseando");
        PNPL.PNPLBuilder pnplBuild = new PNPL.PNPLBuilder();
        JSONObject PetriNet = (JSONObject) json.get("PetriNet");
        JSONObject FM = (JSONObject) json.get("FeatureModel");
        JSONArray presenceConditions = (JSONArray) json.get("PresenceConditions");

        JSONArray nodes = (JSONArray) FM.get("Nodes");
        JSONArray relations = (JSONArray) FM.get("Relations");

        JSONArray places = (JSONArray) PetriNet.get("places");

        return pnplBuild.nodes(parseNodes(nodes))
                .relations(parseRelations(relations))
                .presenceCondition(parsePresenceCondition(presenceConditions))
                .places(parsePlaces(places))
                .build();


    }

    private static List<Node> parseNodes(JSONArray nodes) {
        List<Node> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando nodos...");
        try {
            for (Object obj : nodes) {
                JSONObject node = (JSONObject) obj;
                Node n = new Node.NodeBuilder((String)node.get("name"))
                .isAbstract((boolean) node.get("abstract"))
                .isMandatory((boolean) node.get("mandatory"))
                .requirments(Arrays.stream(node.get("requires").toString().split("(?=[A-Z])")).toList())
                .excludes(Arrays.stream(node.get("excludes").toString().split("(?=[A-Z])")).toList())
                .build();
                list.add(n);
            }
            for (Node nodo : list) {
                nodo.setNodeRequirements(parseRequirements(nodo.getNodeRequirements(), list));
                nodo.setExcludes(parseRequirements(nodo.getExcludes(), list));
                Utils.LoggerSeguimiento().debug("Nodo " + list.indexOf(nodo)+": ", nodo.toString());
            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear los nodos: " + e.getMessage());
        }
            return list;
        }
        
    private static List<Relation> parseRelations(JSONArray relations) {
        List<Relation> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando relaciones...");
        try {

            for (Object obj : relations) {
                JSONObject relation = (JSONObject) obj;
                Relation relacion = new Relation.RelationBuilder(relation.get("parent").toString())
                .children(parseChildren((JSONArray) relation.get("children")))
                .type(parseType(relation.get("type").toString()))
                .build();
                list.add(relacion);
                Utils.LoggerSeguimiento().debug("Relacion " + list.indexOf(relacion)+": ", relacion.toString());
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

    private static List<String> parsePresenceCondition(JSONArray presenceConditions) {
        List<String> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando PCs...");
        try {
            for (Object obj : presenceConditions) {
                JSONObject pc = (JSONObject) obj;
                String presenceCondition = pc.get("id").toString();
                list.add(presenceCondition);
                Utils.LoggerSeguimiento().debug("Presence Condition " + list.indexOf(presenceCondition)+": ", presenceCondition);
            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear las PCs: " + e.getMessage());
        }
        return list;
    }

    private static List<Place> parsePlaces(JSONArray places) {
        List<Place> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando places...");
        try {

            for (Object obj : places) {
                JSONObject place = (JSONObject) obj;
                
                Place p = new Place.PlaceBuilder(place.get("name").toString())
                .presenceCondition(place.get("id_PC").toString())
                .build();
                list.add(p);
                Utils.LoggerSeguimiento().debug("Place " + list.indexOf(p)+": ", p.toString());

            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear los places: " + e.getMessage());
        }
            return list;
    }
}
