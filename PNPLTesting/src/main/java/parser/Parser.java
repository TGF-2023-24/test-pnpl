package parser;

import objects.Node;
import objects.PNPL;
import objects.Relation;
import org.graphwalker.core.model.Edge;
import org.graphwalker.core.model.Vertex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    public static PNPL parse(JSONObject json) {
        PNPL.PNPLBuilder pnplBuild = new PNPL.PNPLBuilder();
        JSONObject FM = (JSONObject) json.get("FeatureModel");
        JSONArray nodes = (JSONArray) FM.get("Nodes");
        JSONArray relations = (JSONArray) FM.get("Relations");
        return pnplBuild.nodes(parseNodes(nodes))
                .relations(parseRelations(relations))
                .build();


    }

    private static List<Vertex> parseNodes(JSONArray nodes) {
        List<Vertex> list = new ArrayList<>();
        for (Object obj : nodes) {
            JSONObject node = (JSONObject) obj;

            Vertex v = new Node.NodeBuilder((String)node.get("name"))
                    .isAbstract((boolean) node.get("abstract"))
                    .isMandatory((boolean) node.get("mandatory"))
                    .requirments(Arrays.stream(node.get("requires").toString().split(",")).toList())
                    .excludes(Arrays.stream(node.get("excludes").toString().split(",")).toList())
                    .build();
            list.add(v);
        }
        return list;
    }

    private static List<Edge> parseRelations(JSONArray relations) {
        List<Edge> list = new ArrayList<>();
        for (Object obj : relations) {
            JSONObject relation = (JSONObject) obj;

            Edge v = new Relation.RelationBuilder(relation.get("parent").toString())
                    //TODO
                    .build();
            list.add(v);
        }
        return list;
    }
}
