package view;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

public class Viewer {


    public void run() {
        System.setProperty("org.graphstream.ui", "swing");
		
		Graph graph = new SingleGraph("Tutorial 1");

		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");

		for (Node node : graph) {
			node.setAttribute("ui.label", "   "+node.getId());
		}

		graph.display();

    }
}
