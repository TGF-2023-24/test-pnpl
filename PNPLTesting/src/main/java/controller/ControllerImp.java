package controller;

import objects.PNPL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import parser.Parser;
import utils.Literal;

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.parser.ParseException;

public class ControllerImp extends Controller {

    @Override
    public void execute(String jsonPath) {
        JSONParser Jsonparser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) Jsonparser.parse(new FileReader(jsonPath));
            PNPL pnpl = Parser.parse(jsonObject);
            System.out.println(pnpl.getNodes());
        } catch (IOException | ParseException e) {
            System.err.println(e.getMessage());
            System.err.println(Literal.FORMATO_INVALIDO);
        }
    }
}
