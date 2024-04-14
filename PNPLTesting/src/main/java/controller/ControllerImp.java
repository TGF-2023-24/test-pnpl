package controller;

import objects.PNPL;
import objects.metamodel.Metamodel;
import parser.Parser;
import utils.Literal;
import utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;


import logic.Tester;

public class ControllerImp extends Controller {

    @Override
    public void execute(String[] paths) {
        try {

            Document doc = (Document) Utils.initialize(paths[0]);

            Utils.LoggerSeguimiento().debug("Parseando metamodelo...");
            Metamodel metamodel = Parser.parseMetamodel(doc); //Parseamos el metamodelo
            Utils.LoggerSeguimiento().debug(Literal.SEPARADOR_LARGO);
    
            List<PNPL> list = new ArrayList<PNPL>();
            for (int i = 1; i < paths.length; i++) { //Parseamos los modelos
                Object archivo = Utils.initialize(paths[i]);
                Utils.LoggerSeguimiento().debug("Parseando modelo " + i +"...");
                Utils.LoggerSeguimiento().debug(Literal.SEPARADOR_LARGO);
                list.add(Parser.parse(archivo));
            }

            for (PNPL modelo : list)  {

                Utils.LoggerSeguimiento().debug("Testeando modelo " + list.indexOf(modelo)+1 +"...");
                Tester tester = new Tester(metamodel, modelo); 
                List<String> errores = tester.check(); //Validamos los modelos en base al metamodelo
                if (errores.isEmpty()) Utils.LoggerSeguimiento().debug("No se han encontrado errores en el metamodelo número " + list.indexOf(modelo)+1);
                else { //Si hay errores, se muestran
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
