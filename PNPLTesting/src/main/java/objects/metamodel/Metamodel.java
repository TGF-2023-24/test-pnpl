package objects.metamodel;

import java.util.ArrayList;
import java.util.List;

public class Metamodel {

    private List<ArcType> arcTypes;
    private List<String> types;

    private Metamodel(List<ArcType> arcTypes, List<String> types) {
        this.arcTypes = arcTypes;
        this.types = types;
    }

    public List<ArcType> getArcTypes() {
        return arcTypes;
    }
    
    public List<String> getTypes() {
        return types;
    }

    public static class MetamodelBuilder {
        private List<ArcType> arcTypes;
        private List<String> types;

        public MetamodelBuilder() {}

        public MetamodelBuilder arcTypes(List<ArcType> arcTypes) {
            this.arcTypes = arcTypes != null ? arcTypes : new ArrayList<>();
            return this;
        }

        public MetamodelBuilder types(List<String> types) {
            this.types = types != null ? types : new ArrayList<>();
            return this;
        }

        public Metamodel build() {
            return new Metamodel(arcTypes, types);
        }
    }
}
