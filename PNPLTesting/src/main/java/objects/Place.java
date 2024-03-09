package objects;

public class Place {
    private String name, pc;

    private Place(String name, String pc) {
        this.name = name;
        this.pc = pc;
    }

    public String getName() { return name; }
    public String getPresenceCondition() { return pc; }

    public static class PlaceBuilder {
        private String name, pc;

        public PlaceBuilder(String name) {
            this.name = name;
        }

        public PlaceBuilder presenceCondition(String id) {
            pc = !id.equals("invalid") ? id : "";
            return this;
        }
        public Place build() {
            return new Place(name,pc);
        }
    }
}
