package objects;

import org.graphwalker.core.model.Edge;
import org.graphwalker.core.model.Vertex;

import java.util.ArrayList;
import java.util.List;

public class PNPL {
    private List<Vertex> nodes;
    private List<Edge> relations;

    private PNPL(List<Vertex> nodes, List<Edge> relations) {
        this.nodes = nodes;
        this.relations = relations;
    }

    public List<Vertex> getNodes() {
        return nodes;
    }

    public List<Edge> getRelations() {
        return relations;
    }

    public static class PNPLBuilder {
        private List<Vertex> nodes;
        private List<Edge> relations;

        public PNPLBuilder nodes(List<Vertex> nodes) {
            if (nodes == null) nodes = new ArrayList<>();
            this.nodes = nodes;
            return this;
        }

        public PNPLBuilder relations(List<Edge> relations) {
            if (relations == null) relations = new ArrayList<>();
            this.relations = relations;
            return this;
        }

        public PNPL build() {
            return new PNPL(nodes, relations);
        }
    }
}
