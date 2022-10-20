package io.github.almogtavor.pojo.analyzer.model;

/**
 * Declares the type of the static variable that's going to get generated.
 */
public enum VariableType {
    MAP("map"),
    LIST("list");

    VariableType(String stringBasedVariableType) {
        this.stringBasedVariableType = stringBasedVariableType;
    }

    public String getStringBasedVariableType() {
        return stringBasedVariableType;
    }

    private final String stringBasedVariableType;
}