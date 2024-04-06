package objects;
import java.util.ArrayList;
import java.util.List;

public class PNPL {
    private List<Node> nodes;
    private List<Relation> relations;
    List<String> presenceConditionList;
    List<Place> places;
    List<Transition> transitions;
    List<Arc> arcs;

    private PNPL(List<Node> nodes, List<Relation> relations, List<String> pcList, List<Transition> transitions, List<Arc> arcs, List<Place> places) {
        this.nodes = nodes;
        this.relations = relations;
        this.presenceConditionList = pcList;
        this.transitions = transitions;
        this.arcs = arcs;
        this.places = places;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public List<String> getPCs() { 
        return presenceConditionList;
    }

    public List<Place> getPlaces() { 
        return places;
    }

    public List<Transition> getTransitions() { 
        return transitions;
    }

    public List<Arc> getArcs() { 
        return arcs;
    }

    public static class PNPLBuilder {
        private List<Node> nodes;
        private List<Relation> relations;
        List<String> presenceConditionList;
        List<Place> places;
        List<Transition> transitions;
        List<Arc> arcs;
        public PNPLBuilder nodes(List<Node> nodes) {
            if (nodes == null) nodes = new ArrayList<>();
            this.nodes = nodes;
            return this;
        }
        public PNPLBuilder relations(List<Relation> relations) {
            if (relations == null) relations = new ArrayList<>();
            this.relations = relations;
            return this;
        }
        public PNPLBuilder presenceCondition(List<String> pcList) {
            if (pcList == null) pcList = new ArrayList<>();
            this.presenceConditionList = pcList;
            return this;
        }
        public PNPLBuilder places(List<Place> places) {
            if (places == null) places = new ArrayList<>();
            this.places = places;
            return this;
        }
        public PNPLBuilder transitions(List<Transition> transitions) {
            if (transitions == null) transitions = new ArrayList<>();
            this.transitions = transitions;
            return this;
        }
        public PNPLBuilder arcs(List<Arc> arcs) {
            if (arcs == null) arcs = new ArrayList<>();
            this.arcs = arcs;
            return this;
        }
        public PNPL build() {
            return new PNPL(nodes, relations, presenceConditionList, transitions, arcs, places);
        }
    }
}
