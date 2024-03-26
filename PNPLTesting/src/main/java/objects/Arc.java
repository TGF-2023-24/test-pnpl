package objects;

public class Arc {
    private String type, name, pc, src, target;

    public Arc(String type, String name, String presenceCondition, String src, String target) {
        this.type = type;
        this.name = name;
        this.pc = presenceCondition;
        this.src = src;
        this.target = target;
    }

    public String getType() { return type; }
    public String getName() { return name; } 
    public String getPresenceCondition() { return pc; }
    public String getSource() { return src; }
    public String getTarget() { return target; }

    @Override
    public String toString() {
        return "{ Nombre: " +name + "; PC: " + pc + "; From " + src+" to " + target + " }";
    }

    public static class ArcBuilder {
        private String type, name, pc, src, target;

        public ArcBuilder(String name) {
            this.name = name;
        }

        public ArcBuilder presenceCondition(String presenceCondition) {
            pc = !presenceCondition.equals("invalid") ? presenceCondition : "";
            return this;
        }

        public ArcBuilder type(String type) { 
            this.type = type;
            return this;
        }

        public ArcBuilder source(String src) { 
            this.src = src;
            return this;
        }

        public ArcBuilder target(String target) { 
            this.target = target;
            return this;
        }
        
        public Arc build() {
            return new Arc(type, name, pc, src, target);
        }
    }
    
}
