package parser;

import objects.Arc;
import objects.Node;
import objects.PNPL;
import objects.Place;
import objects.Relation;
import objects.Transition;
import objects.metamodel.ArcType;
import objects.metamodel.Metamodel;
import objects.metamodel.ArcType.ArcTypeBuilder;
import objects.metamodel.Metamodel.MetamodelBuilder;
import utils.Literal;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Parser {

    public static Metamodel parseMetamodel(Document file) {
        MetamodelBuilder metamodelBuild = new MetamodelBuilder();    

        return metamodelBuild
            .arcTypes(parseArcTypes(file))
            .types(parseMetamodelTypes(file))
            .build();
    }   

    private static List<ArcType> parseArcTypes(Document file) {
        List<ArcType> list = new ArrayList<>();

        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            XPathExpression xPathExpr = xPath.compile("//eClassifiers[@name='ArcPT' or @name='ArcTP']");
            NodeList nodeList = (NodeList) xPathExpr.evaluate(file, XPathConstants.NODESET);           
            for (int i = 0; i < nodeList.getLength(); i++) {
                org.w3c.dom.Node node = nodeList.item(i);
                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    NodeList childNodes = element.getChildNodes();
                    ArcTypeBuilder arcBuild = new ArcTypeBuilder(element.getAttribute("name"));
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        org.w3c.dom.Node childNode = childNodes.item(j);
                        if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                            Element childElement = (Element) childNode;
                            if (childElement.getAttribute("name").equals("source"))
                                arcBuild = arcBuild.src(childElement.getAttribute("eType").substring(3));
                            else if (childElement.getAttribute("name").equals("target"))
                                arcBuild = arcBuild.target(childElement.getAttribute("eType").substring(3));
                        }
                    }
                    list.add(arcBuild.build());
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static List<String> parseMetamodelTypes(Document file) {
        List<String> types = new ArrayList<>();

        Object eClassifierNodes = (NodeList) Utils.getListComplete(file, "eClassifiers");
        for (int i = 0; i < Utils.getSize(eClassifierNodes); i++) {
            Object eClassifierElement = Utils.getElement(eClassifierNodes, i);
            String name = Utils.getAttribute(eClassifierElement, "xsi:type").toString().toLowerCase();
            if (name.contains("eenum"))
            {
                for (int j = 0; j < Utils.getSize(eClassifierElement); j++) {
                    Object ele = Utils.getElement(eClassifierElement, j);
                    Object attribute = Utils.getAttribute(ele, "name");
                    if (attribute != null) types.add(attribute.toString());
                }
            }
            
        }
        return types;
    }

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
                .places(parsePlaces(elements))
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
                .type(Utils.getAttribute(relation,"type").toString())
                .build();
                list.add(relacion);
                Utils.LoggerSeguimiento().debug("Relacion " + list.indexOf(relacion)+": " + relacion.toString());
            }
        } catch (Exception e) {
            Utils.LoggerError().error("Error al parsear las relaciones: " + e.getMessage());
        }
            return list;
        }
        
    //     private static List<String> parseChildren(JSONArray children) {
    //     List<String> list = new ArrayList<>();
    //     for (Object obj : children)
    //         list.add(obj.toString());
    //     return list;
    // }

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

                

            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear las transiciones: " + e.getMessage());
        }
            return list;
    }

    private static List<Place> parsePlaces(Object elements) {
        List<Place> list = new ArrayList<>();
        Utils.LoggerSeguimiento().debug("Parseando places...");
        try {

            for (int i = 0; i < Utils.getSize(elements); i++) {
                Object element = Utils.getElement(elements, i);
                String type = Utils.getAttribute(element, "xsi:type").toString();

                if (type.contains("Place")) {
                    Place place = new Place.PlaceBuilder(Utils.getAttribute(element, "name").toString())
                    .presenceCondition(Utils.getAttribute(element, "id").toString())
                    .build();
                    list.add(place);
                    Utils.LoggerSeguimiento().debug("Place " + list.indexOf(place)+": " + place.toString());
                }
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
                    String typeFull = Utils.getAttribute(element, "xsi:type").toString();
                    Arc arco = new Arc.ArcBuilder(Utils.getAttribute(element, "name").toString())
                    .presenceCondition(Utils.getAttribute(element, "presenceCondition").toString())
                    .type(typeFull.substring(typeFull.indexOf(":")+1))
                    .source(Utils.getAttribute(element, "source").toString())
                    .target(Utils.getAttribute(element, "target").toString())
                    .build();
                    list.add(arco);
                    Utils.LoggerSeguimiento().debug("Arc " + list.indexOf(arco)+": " + arco.toString());
                    if (Utils.getAttribute(element, "source").toString() == "") {
                        Utils.LoggerError().error("Error al parsear los arcos: " + i);
                        
                    }
                }
                

            }
        } catch(Exception e) {
            Utils.LoggerError().error("Error al parsear los arcos: " + e.getMessage());
        }
            return list;
    }
}
