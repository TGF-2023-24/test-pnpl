package controller;

import objects.PNPL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import parser.Parser;
import utils.Literal;
import utils.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ControllerImp extends Controller {

    @Override
    public void execute(String jsonPath, String xmiPath) {
        JSONParser Jsonparser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) Jsonparser.parse(new FileReader(jsonPath));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(xmiPath));

            NodeList featureModelList = doc.getElementsByTagName("featuremodel");
            Node featureModelNode = featureModelList.item(0); // Assuming there's only one featuremodel node

            // Access child nodes of featuremodel
            NodeList featureModelChildren = featureModelNode.getChildNodes();
            for (int i = 0; i < featureModelChildren.getLength(); i++) {
                Node childNode = featureModelChildren.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) childNode;
                    // Access attributes or values of child elements as needed
                    String nodeName = childElement.getAttribute("name");
                    String mandatory = childElement.getAttribute("mandatory");
                    String requires = childElement.getAttribute("requires");
                    System.out.println("Node Name: " + nodeName);
                    System.out.println("Mandatory: " + mandatory);
                    System.out.println("Requires: " + requires);
                    // Continue processing other attributes or values if necessary
                }
            }

            PNPL pnpl = Parser.parse(jsonObject);

            //PNPL pnpl_a_probar = Parser.parse(doc);
        } catch (IOException | ParseException | ParserConfigurationException | SAXException e) {
            Utils.LoggerError().error(e.getMessage());
            Utils.LoggerError().error(Literal.FORMATO_INVALIDO);
        }
    }
}
