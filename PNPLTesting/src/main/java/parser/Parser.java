package parser;

import objects.Node;
import objects.PNPL;
import objects.Place;
import objects.Relation;
import org.graphwalker.core.model.Edge;
import org.graphwalker.core.model.Vertex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Parser {

    public static PNPL parse(JSONObject json) {
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
        for (Object obj : nodes) {
            JSONObject node = (JSONObject) obj;

            Node n = new Node.NodeBuilder((String)node.get("name"))
                    .isAbstract((boolean) node.get("abstract"))
                    .isMandatory((boolean) node.get("mandatory"))
                    .requirments(Arrays.stream(node.get("requires").toString().split("(?=[A-Z][^A-Z])\n")).toList())
                    .excludes(Arrays.stream(node.get("excludes").toString().split("(?=[A-Z][^A-Z])\n")).toList())
                    .build();
            list.add(n);
        }
        for (Node nodo : list) {
            nodo.setNodeRequirements(parseRequirements(nodo.getNodeRequirements(), list));
            nodo.setExcludes(parseRequirements(nodo.getExcludes(), list));
        }
        return list;
    }

    private static List<Relation> parseRelations(JSONArray relations) {
        List<Relation> list = new ArrayList<>();
        for (Object obj : relations) {
            JSONObject relation = (JSONObject) obj;
            Relation r = new Relation.RelationBuilder(relation.get("parent").toString())
                    .children(parseChildren((JSONArray) relation.get("children")))
                    .type(parseType(relation.get("type").toString()))
                    .build();
            list.add(r);
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
        for (Object obj : presenceConditions) {
            JSONObject pc = (JSONObject) obj;
            list.add(pc.get("id").toString());
        }
        return list;
    }

    private static List<Place> parsePlaces(JSONArray places) {
        List<Place> list = new ArrayList<>();
        for (Object obj : places) {
            JSONObject place = (JSONObject) obj;

            Place p = new Place.PlaceBuilder(place.get("name").toString())
                    .presenceCondition(place.get("id_PC").toString())
                    .build();
            list.add(p);
        }
        return list;
    }
}
