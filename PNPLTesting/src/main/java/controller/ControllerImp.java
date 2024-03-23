package controller;

import objects.PNPL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import parser.Parser;
import utils.Literal;
import utils.Utils;

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
        } catch (IOException | ParseException e) {
            Utils.LoggerError().error(e.getMessage());
            Utils.LoggerError().error(Literal.FORMATO_INVALIDO);
        }
    }
}
