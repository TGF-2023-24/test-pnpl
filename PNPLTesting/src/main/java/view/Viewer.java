package view;
import org.graphstream.graph.*;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

import objects.*;

public class Viewer {

	private PNPL modelo;

	protected String styleSheet =
	"node {" +
		"fill-mode: gradient-horizontal;"+
		"fill-color: yellow;"+
		// "text-background-mode: rounded-box;"+
		// "text-background-color: black;"+
		// "text-color: white;"+
		"text-style: italic;"+
		"text-alignment: under;"+
		"size: 18px;"+
		"shape: circle;"+
		"fill-color: #8facb4;"+
		"stroke-mode: plain;"+
		"stroke-color: black;"+
	"}" +

	"node.place {" +
	"	fill-color: red;" +
	"}" +

	"node.transition {" +
	"	fill-color: yellow;" +
	"}" +

	"node.arc {" +
	"	fill-color: green;" +
	"size: 11px;"+
	"}" +
	" edge {" +
	"size:2px;" +
	"}"
	;

	public Viewer(PNPL modelo) { this.modelo = modelo;}


    public void run() {
        System.setProperty("org.graphstream.ui", "swing");
		
		Graph graph = new MultiGraph("Modelo");

		graph.setAttribute("ui.quality");
		graph.setAttribute("ui.antialias");
		System.setProperty("org.graphstream.ui", "swing");


		for (Place place : modelo.getPlaces()) {
			graph.addNode(place.getName());
			Node n = graph.getNode(place.getName());
			n.setAttribute("ui.label", n.getId());
			n.setAttribute("ui.class", "place");
		}

		for (Transition transition : modelo.getTransitions()) {
			graph.addNode(transition.getName());
			Node n = graph.getNode(transition.getName());
			n.setAttribute("ui.label", n.getId());
			n.setAttribute("ui.class", "transition");

		}

		// OPCION 1: MOSTRANDO ARCOS
		for (Arc arco : modelo.getArcs()) {
			graph.addNode(arco.getName());
			Node n = graph.getNode(arco.getName());
			n.setAttribute("ui.label", n.getId());
			n.setAttribute("ui.class", "arc");		
		}
		for (Arc arco : modelo.getArcs()) {
			graph.addEdge(arco.getName()+"edge1", arco.getSource(), arco.getName(),true);
			graph.addEdge(arco.getName()+"edge2", arco.getName(), arco.getTarget(),true);

		}


		//OPCION 2: SIN MOSTRAR ARCOS
		// for (Arc arco : modelo.getArcs()) {
		// 	graph.addEdge(arco.getName(), arco.getSource(), arco.getTarget(),true);
		// 	Edge n = graph.getEdge(arco.getName());
		// 	n.setAttribute("ui.class", "arc");
		// }


		graph.setAttribute("ui.stylesheet", styleSheet);

		// int i = 0;
		// for (Node node : graph) {
		// 	node.setAttribute("ui.label", node.getId());
		// 	if (i % 2 == 0)
		// 		node.setAttribute("ui.class", "marked");
		// 	i++;

		// }


		graph.display();

    }

}
