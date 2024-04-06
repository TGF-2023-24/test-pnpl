package objects.metamodel;

import java.util.ArrayList;
import java.util.List;

public class Metamodel {

    private List<ArcType> arcTypes;

    private Metamodel(List<ArcType> arcTypes) {
        this.arcTypes = arcTypes;
    }

    public List<ArcType> getArcTypes() {
        return arcTypes;
    }
    
    public static class MetamodelBuilder {
        private List<ArcType> arcTypes;

        public MetamodelBuilder() {}

        public MetamodelBuilder arcTypes(List<ArcType> arcTypes) {
            this.arcTypes = arcTypes != null ? arcTypes : new ArrayList<>();
            return this;
        }

        public Metamodel build() {
            return new Metamodel(arcTypes);
        }
    }
}
