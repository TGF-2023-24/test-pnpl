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
            
            JSONObject jsonObject = (JSONObject) Jsonparser.parse(new FileReader(paths[0]));

            Utils.LoggerSeguimiento().debug("Parseando metamodelo...");
            PNPL pnpl = Parser.parse(jsonObject);
            Utils.LoggerSeguimiento().debug(Literal.SEPARADOR_LARGO);

            List<PNPL> list = new ArrayList<PNPL>();
            for (int i = 1; i < paths.length; i++) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new File(paths[i]));
                Utils.LoggerSeguimiento().debug("Parseando modelo " + i +"...");
                Utils.LoggerSeguimiento().debug(Literal.SEPARADOR_LARGO);
                list.add(Parser.parse(doc));

            }

            for (PNPL metamodelo : list)  {

                Tester tester = new Tester(pnpl, metamodelo);
                List<String> errores = tester.check();
                if (errores.isEmpty()) Utils.LoggerSeguimiento().debug("No se han encontrado errores en el metamodelo número " + list.indexOf(metamodelo)+1);
            }
        } catch (Exception e) {
            Utils.LoggerError().error(e.getMessage());
            Utils.LoggerError().error(Literal.FORMATO_INVALIDO);
        }
    }
}
