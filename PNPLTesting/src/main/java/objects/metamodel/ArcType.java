package objects.metamodel;

public class ArcType {
    private String name, src, target;

    private ArcType(String name, String src, String target) {
        this.name = name;
        this.src = src;
        this.target = target;
    }
    
    public String getName() {
        return name;
    }

    public String getSource() {
        return src;
    }

    public String getTarget() {
        return target;
    }
    public static class ArcTypeBuilder{
        private String name, src, target;
        public ArcTypeBuilder(String name) {
            this.name = name;
        }

        public ArcTypeBuilder src(String src) {
            this.src = src;
            return this;
        }

        public ArcTypeBuilder target(String target) {
            this.target = target;
            return this;
        }

        public ArcType build() {
            return new ArcType(name, src, target);
        }
    }
}
