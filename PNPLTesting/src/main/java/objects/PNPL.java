package objects;
import java.util.ArrayList;
import java.util.List;

public class PNPL {
    private List<Node> nodes;
    private List<Relation> relations;
    List<String> presenceConditionList;
    List<Place> places;
    private PNPL(List<Node> nodes, List<Relation> relations, List<String> pcList, List<Place> places) {
        this.nodes = nodes;
        this.relations = relations;
        this.presenceConditionList = pcList;
        this.places = places;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public static class PNPLBuilder {
        private List<Node> nodes;
        private List<Relation> relations;
        List<String> presenceConditionList;
        List<Place> places;
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

        public PNPL build() {
            return new PNPL(nodes, relations, presenceConditionList, places);
        }
    }
}
