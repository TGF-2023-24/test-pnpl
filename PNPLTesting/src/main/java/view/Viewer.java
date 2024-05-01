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
		"text-alignment: above;"+
		"text-offset: 20px;"+
		"size: 15px;"+
	"}" +

	"node.place {" +
	"	fill-color: red;" +
	"}" +

	"node.transition {" +
	"	fill-color: yellow;" +
	"}" +

	"node.arc {" +
	"	fill-color: green;" +
	"}"
	;

	public Viewer(PNPL modelo) { this.modelo = modelo;}


    public void run() {
        System.setProperty("org.graphstream.ui", "swing");
		
		Graph graph = new SingleGraph("Modelo");

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
