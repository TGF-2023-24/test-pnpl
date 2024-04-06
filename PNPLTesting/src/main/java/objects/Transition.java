package objects;

public class Transition {
    private String pc, name;

    private Transition(String presencecondition, String name) {
        pc = presencecondition;
        this.name = name;
    }

    public String getPresenceCondition() { return pc; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "{ Nombre: " +name + "; PC: " + pc +" }";
    }
    public static class TransitionBuilder {
        private String name, pc;

        public TransitionBuilder(String name) {
            this.name = name;
        }

        public TransitionBuilder presenceCondition(String presenceCondition) {
            pc = !presenceCondition.equals("invalid") ? presenceCondition : "";
            return this;
        }
        
        public Transition build() {
            return new Transition(pc,name);
        }
    }
    
}
