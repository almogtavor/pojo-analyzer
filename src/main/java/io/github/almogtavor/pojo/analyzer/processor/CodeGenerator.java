package io.github.almogtavor.pojo.analyzer.processor;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.*;
import io.github.almogtavor.pojo.analyzer.annotations.DetailedPojo;
import io.github.almogtavor.pojo.analyzer.model.FieldDetails;
import io.github.almogtavor.pojo.analyzer.model.VariableType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Generated;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.*;

public class CodeGenerator {
    @NotNull
    public TypeSpec.Builder generateDetailedPojoClass(Element typeElementOfAnnotatedClass, String packageName, String typeName, TypeMirror typeOfAnnotatedClass) {
        TypeSpec.Builder classBuilder = getClassBuilder(packageName, typeName);
        classBuilder.addAnnotation(AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", DetailedPojoAnnotationProcessor.class.getCanonicalName()).build());
        TypeName fieldDetailsType = TypeName.get(FieldDetails.class);
        List<VariableElement> enclosedFields = ElementFilter.fieldsIn(typeElementOfAnnotatedClass.getEnclosedElements());

        FieldSpec.Builder fieldDetailsBuilder;
        CodeBlock.Builder codeBlockBuilder;
        Optional<DetailedPojo> optionalDetailedPojo = Arrays.stream(typeElementOfAnnotatedClass.getAnnotationsByType(DetailedPojo.class)).findFirst();
        if (!optionalDetailedPojo.isPresent()) throw new IllegalStateException();
        if (optionalDetailedPojo.get().variableType().equals(VariableType.MAP)) {
            fieldDetailsBuilder = buildMapField(fieldDetailsType);
            codeBlockBuilder = buildMapInitializationCodeBlock(typeOfAnnotatedClass, enclosedFields);
        } else {
            fieldDetailsBuilder = buildListField(fieldDetailsType);
            codeBlockBuilder = buildListInitializationCodeBlock(typeOfAnnotatedClass, enclosedFields);
        }
        fieldDetailsBuilder.initializer(codeBlockBuilder.build());
        classBuilder.addField(fieldDetailsBuilder.build());
        return classBuilder;
    }

    @NotNull
    private CodeBlock.Builder buildMapInitializationCodeBlock(TypeMirror typeOfAnnotatedClass, List<VariableElement> enclosedFields) {
        CodeBlock.Builder codeBlockBuilder;
        codeBlockBuilder = CodeBlock.builder().add("new $T<$T, $T>() {{", HashMap.class, String.class, FieldDetails.class);
        if (!enclosedFields.isEmpty()) {
            for (int i = 0; i < enclosedFields.size(); i++) {
                if (i == 0 && enclosedFields.size() > 1) {
                    addMapElement(codeBlockBuilder, "\n$>$>%s;", enclosedFields.get(i), typeOfAnnotatedClass);
                } else if (i != enclosedFields.size() - 1) {
                    addMapElement(codeBlockBuilder, "\n%s;", enclosedFields.get(i), typeOfAnnotatedClass);
                } else {
                    addMapElement(codeBlockBuilder, "\n%s;$<$<", enclosedFields.get(i), typeOfAnnotatedClass);
                }
            }
            codeBlockBuilder.add("\n}}");
        } else {
            codeBlockBuilder.add("}}");
        }
        return codeBlockBuilder;
    }

    @NotNull
    private CodeBlock.Builder buildListInitializationCodeBlock(TypeMirror typeOfAnnotatedClass, List<VariableElement> enclosedFields) {
        CodeBlock.Builder codeBlockBuilder;
        codeBlockBuilder = CodeBlock.builder().add("$T.asList(", Arrays.class);
        if (!enclosedFields.isEmpty()) {
            for (int i = 0; i < enclosedFields.size(); i++) {
                if (i == 0 && enclosedFields.size() > 1) {
                    addListElement(codeBlockBuilder, "\n$>$>%s,", enclosedFields.get(i), typeOfAnnotatedClass);
                } else if (i != enclosedFields.size() - 1) {
                    addListElement(codeBlockBuilder, "\n%s,", enclosedFields.get(i), typeOfAnnotatedClass);
                } else {
                    addListElement(codeBlockBuilder, "\n%s)$<$<", enclosedFields.get(i), typeOfAnnotatedClass);
                }
            }
        } else {
            codeBlockBuilder.add(")");
        }
        return codeBlockBuilder;
    }

    @NotNull
    private FieldSpec.Builder buildListField(TypeName fieldDetailsType) {
        FieldSpec.Builder fieldDetailsBuilder;
        TypeName listType = ParameterizedTypeName.get(
                ClassName.get(List.class),
                fieldDetailsType);
        fieldDetailsBuilder = FieldSpec.builder(listType, NameStore.Variable.FIELD_DETAILS_LIST_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        return fieldDetailsBuilder;
    }

    @NotNull
    private FieldSpec.Builder buildMapField(TypeName fieldDetailsType) {
        FieldSpec.Builder fieldDetailsBuilder;
        TypeName mapType = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                TypeName.get(String.class),
                fieldDetailsType);
        fieldDetailsBuilder = FieldSpec.builder(mapType, NameStore.Variable.FIELD_DETAILS_MAP_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        return fieldDetailsBuilder;
    }

    private void addMapElement(CodeBlock.Builder builder, String format, VariableElement field, TypeMirror typeElementOfAnnotatedClass) {
        String fieldName = field.getSimpleName().toString();
        builder.add(String.format(format, "put($S, new $T<$T, $T>($S, ($T t) -> t.get$L(), ($T t1, $T t2) -> t1.set$L(t2)))"),
                fieldName,
                FieldDetails.class,
                typeElementOfAnnotatedClass,
                field.asType(),
                fieldName,
                typeElementOfAnnotatedClass,
                CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName),
                typeElementOfAnnotatedClass,
                field.asType(),
                CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName)
        );
    }

    private void addListElement(CodeBlock.Builder builder, String format, VariableElement field, TypeMirror typeElementOfAnnotatedClass) {
        builder.add(String.format(format, "new $T<$T, $T>($S, ($T t) -> t.get$L(), ($T t1, $T t2) -> t1.set$L(t2))"),
                FieldDetails.class,
                typeElementOfAnnotatedClass,
                field.asType(),
                field.getSimpleName().toString(),
                typeElementOfAnnotatedClass,
                CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getSimpleName().toString()),
                typeElementOfAnnotatedClass,
                field.asType(),
                CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getSimpleName().toString())
        );
    }

    private TypeSpec.Builder getClassBuilder(String packageName, String typeName) {
        ClassName generatedClassName = ClassName
                .get(packageName, NameStore.Class.getGeneratedClassName(typeName));
        return TypeSpec.classBuilder(generatedClassName)
                .addModifiers(Modifier.PUBLIC);
    }
}
