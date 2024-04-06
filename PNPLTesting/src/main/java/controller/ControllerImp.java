package controller;

import objects.PNPL;
import objects.metamodel.Metamodel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import parser.Parser;
import utils.Literal;
import utils.Utils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;


import logic.Tester;

public class ControllerImp extends Controller {

    @Override
    public void execute(String[] paths) {
        JSONParser Jsonparser = new JSONParser();
        try {
            

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(paths[0]));
            doc.getDocumentElement().normalize();
            Utils.LoggerSeguimiento().debug("Parseando metamodelo...");
            Metamodel metamodel = Parser.parseMetamodel(doc);
            Utils.LoggerSeguimiento().debug(Literal.SEPARADOR_LARGO);
    
            List<PNPL> list = new ArrayList<PNPL>();
            for (int i = 1; i < paths.length; i++) {
                dbf = DocumentBuilderFactory.newInstance();
                dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                db = dbf.newDocumentBuilder();
                doc = db.parse(new File(paths[i]));
                doc.getDocumentElement().normalize();
                Utils.LoggerSeguimiento().debug("Parseando modelo " + i +"...");
                Utils.LoggerSeguimiento().debug(Literal.SEPARADOR_LARGO);
                list.add(Parser.parse(doc));

            }


            for (PNPL modelo : list)  {

                Utils.LoggerSeguimiento().debug("Testeando modelo " + list.indexOf(modelo)+1 +"...");
                Tester tester = new Tester(metamodel, modelo);
                List<String> errores = tester.check();
                if (errores.isEmpty()) Utils.LoggerSeguimiento().debug("No se han encontrado errores en el metamodelo número " + list.indexOf(modelo)+1);
                else {
                    Utils.LoggerError().debug("Metamodelo " + list.indexOf(modelo)+1);
                    Utils.LoggerSeguimiento().debug("Metamodelo " + list.indexOf(modelo)+1);
                    for (String error : errores) {
                        Utils.LoggerError().debug(error);
                        Utils.LoggerSeguimiento().debug(error);
                    }
                }
                Utils.LoggerSeguimiento().debug(Literal.SEPARADOR);

            }
        } catch (Exception e) {
            Utils.LoggerError().error(e.getMessage());
            Utils.LoggerError().error(Literal.FORMATO_INVALIDO);
        }
    }
}
