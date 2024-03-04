package objects;


import org.graphwalker.core.model.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Node extends Vertex {
    private String _name;
    private boolean _isAbstract, _isMandatory;
    private List<String> _nodeRequirements, _excludes;

    private Node(String name, boolean isAbs, boolean isMand, List<String> req, List<String> exc) {
        super();
        _name = name;
        _isAbstract = isAbs;
        _isMandatory = isMand;
        _nodeRequirements = req != null ? req : new ArrayList<>();
        _excludes = exc != null ? exc : new ArrayList<>();
    }

    public String getName() {
        return _name;
    }

    public boolean isAbstract() {
        return _isAbstract;
    }

    public boolean isMandatory() {
        return _isMandatory;
    }

    public List<String> getNodeRequirements() {
        return _nodeRequirements;
    }

    public List<String> getExcludes() {
        return _excludes;
    }

    public void setNameNode(String name) {
        this._name = name;
    }

    public void setAbstract(boolean isAbstract) {
        this._isAbstract = isAbstract;
    }

    public void setMandatory(boolean isMandatory) {
        this._isMandatory = isMandatory;
    }

    public void setNodeRequirements(List<String> nodeRequirements) {
        this._nodeRequirements = nodeRequirements;
    }

    public void setExcludes(List<String> excludes) {
        this._excludes = excludes;
    }

    public static class NodeBuilder {
        private String _name;
        private boolean _isAbstract, _isMandatory;
        private List<String> _nodeRequirements, _excludes;

        public NodeBuilder(String name) {
            _name = name;
        }
        public NodeBuilder isMandatory(boolean isMandatory) {
            _isMandatory = isMandatory;
            return this;
        }
        public NodeBuilder isAbstract(boolean isAbstract) {
            _isAbstract = isAbstract;
            return this;
        }
        public NodeBuilder requirments(List<String> requirements) {
            _nodeRequirements = requirements != null ? requirements : new ArrayList<>();
            return this;
        }
        public NodeBuilder excludes(List<String> excludes) {
            _excludes = excludes != null ? excludes : new ArrayList<>();
            return this;
        }

        public Vertex build() {
            Vertex v = new Node(_name, _isAbstract, _isMandatory, _nodeRequirements, _excludes);
            return v;
        }
    }
}
