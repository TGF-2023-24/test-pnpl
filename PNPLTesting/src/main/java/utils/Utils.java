package utils;


import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.File;
import java.io.FileReader;


public class Utils {
    private static final Logger seguimiento = LogManager.getLogger("seguimiento");
    private static final Logger errores = LogManager.getLogger("errores");

    public static Logger LoggerSeguimiento() { return seguimiento; }
    public static Logger LoggerError() { return errores; }

    private static boolean isJSON(Object file) {
        return file instanceof JSONObject || file instanceof JSONArray;
    }

    public static Object initialize(Object type) {
        try {

            String name = (String) type;
            if (name.endsWith("json"))  {
                JSONParser Jsonparser = new JSONParser();
                return (JSONObject) Jsonparser.parse(new FileReader(name));
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(name));
            doc.getDocumentElement().normalize();
            return doc;
        } catch(Exception e) { return null;}
    }

    public static Object getList(Object element, String name) {
        if (isJSON(element))  {
            JSONObject obj = (JSONObject) element;
            return obj.get(name);
        }
        try {

            Document doc = (Document) element;
            Node array = doc.getElementsByTagName(name).item(0);
            if (array == null) return doc.getElementsByTagName(name);
            return array;
        } catch(Exception e) {
            NodeList nodesList = ((Element) element).getElementsByTagName(name);
            return nodesList;
        }
    }

    public static Object getListComplete(Object element, String name) {
        if (isJSON(element))  {
            JSONObject obj = (JSONObject) element;
            return obj.get(name);
        }
        try {

            Document doc = (Document) element;
            return doc.getElementsByTagName(name);
        } catch(Exception e) {
            NodeList nodesList = ((Element) element).getElementsByTagName(name);
            return nodesList;
        }
    }
    
    public static Object getAttribute(Object element, String name) {
        if (isJSON(element))  {
            JSONObject obj = (JSONObject) element;
            Object valor = obj.get(name);
            if (valor == null) valor = ""; //TODO quitar cuando se solucione lo del src y target
            return valor;
        }

        Node childNode = (Node) element;
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            Element childElement = (Element) childNode;
            Object attribute = childElement.getAttribute(name);
            return attribute;
        }
        return null;
    }

    public static boolean getBooleanAttribute(Object element, String name) {
        if (isJSON(element))  {
            JSONObject obj = (JSONObject) element;
            return (boolean) obj.get(name);
        }

        Node childNode = (Node) element;
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            Element childElement = (Element) childNode;
            Object attribute = childElement.getAttribute(name);
            if (attribute == "true") return true;
        }
        
        return false;
    }

    public static Object getElement(Object element, int index) {
        if (isJSON(element))  {
            JSONArray obj = (JSONArray) element;
            return obj.get(index);
        }
        NodeList array = (NodeList) element;
        return array.item(index);
    }

    public static int getSize(Object element) {
        if (isJSON(element))  {
            JSONArray array = (JSONArray) element;
            return array.size();
        }
        NodeList array = (NodeList) element;
        return array.getLength();
    }
}
