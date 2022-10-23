package io.github.almogtavor.pojo.analyzer.model;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The FieldDetails class adds access to the getter, setter, and String name of a field, at compile time.
 * @param <ClassTypeT> The type of the class that the field belongs to.
 * @param <FieldTypeT> The type of the field that the details are based on
 */
public class FieldDetails<ClassTypeT, FieldTypeT> {
    private final String fieldName;
    private final Function<ClassTypeT, FieldTypeT> fieldGetter;
    private final BiConsumer<ClassTypeT, FieldTypeT> fieldSetter;

    public FieldDetails(String fieldName, Function<ClassTypeT, FieldTypeT> fieldGetter, BiConsumer<ClassTypeT, FieldTypeT> fieldSetter) {
        this.fieldName = fieldName;
        this.fieldGetter = fieldGetter;
        this.fieldSetter = fieldSetter;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Function<ClassTypeT, FieldTypeT> getFieldGetter() {
        return fieldGetter;
    }

    public BiConsumer<ClassTypeT, FieldTypeT> getFieldSetter() {
        return fieldSetter;
    }

    public FieldTypeT getFieldValue(ClassTypeT clazz) {
        return getFieldGetter().apply(clazz);
    }
}