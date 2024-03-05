package objects;


import org.graphwalker.core.model.Edge;
import utils.Type;

import java.util.ArrayList;
import java.util.List;

public class Relation extends Edge {
    private String parentNode;
    private Type type;
    private List<String> children;

    private Relation(String parent, Type t, List<String> children) {
        parentNode = parent;
        type = t;
        this.children = children;
    }

    public static class RelationBuilder {
        private String parentNode;
        private Type type;
        private List<String> children;

        public RelationBuilder(String parent) {
            parentNode = parent;
            children = new ArrayList<>();
        }
        public RelationBuilder type(Type type) {
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
