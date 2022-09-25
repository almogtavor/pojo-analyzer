package io.github.almogtavor.model;

import java.util.function.BiConsumer;
import java.util.function.Function;

// TODO: change the name of this class to ObjectFieldDetails
//  And create an XFieldDetails for every common type, so ObjectFieldDetails
//  will only be used as a last resort. Make sure all others implements a FieldDetails interface
public class FieldDetails<ClassTypeT, FieldTypeT> {
    private String fieldName;
    private Function<ClassTypeT, FieldTypeT> fieldGetter;
    private BiConsumer<ClassTypeT, FieldTypeT> fieldSetter;

    public FieldDetails(String fieldName, Function<ClassTypeT, FieldTypeT> fieldGetter, BiConsumer<ClassTypeT, FieldTypeT> fieldSetter) {
        this.fieldName = fieldName;
        this.fieldGetter = fieldGetter;
        this.fieldSetter = fieldSetter;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Function<ClassTypeT, FieldTypeT> getFieldGetter() {
        return fieldGetter;
    }

    public void setFieldGetter(Function<ClassTypeT, FieldTypeT> fieldGetter) {
        this.fieldGetter = fieldGetter;
    }

    public BiConsumer<ClassTypeT, FieldTypeT> getFieldSetter() {
        return fieldSetter;
    }

    public void setFieldSetter(BiConsumer<ClassTypeT, FieldTypeT> fieldSetter) {
        this.fieldSetter = fieldSetter;
    }
}