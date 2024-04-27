package objects;


import org.graphwalker.core.model.Edge;

import java.util.ArrayList;
import java.util.List;

public class Relation extends Edge {
    private String parentNode;
    private String type;
    private List<String> children;

    private Relation(String parent, String t, List<String> children) {
        parentNode = parent;
        type = t;
        this.children = children;
    }

    public String getParent() {
        return parentNode;
    }

    public String getType() {
        return type;
    }

    public List<String> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "{ Origen: " +parentNode + "; Tipo: " + type + "; Destinos: " + children.toString() + "}";
    }
    public static class RelationBuilder {
        private String parentNode;
        private String type;
        private List<String> children;

        public RelationBuilder(String parent) {
            parentNode = parent;
            children = new ArrayList<>();
        }
        public RelationBuilder type(String type) {
            this.type = type;
            return this;
        }
        public RelationBuilder children(List<String> children) {
            this.children = children;
            return this;
        }

        public Relation build() {
            return new Relation(parentNode, type, children);
        }
    }
}
