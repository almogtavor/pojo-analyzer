package io.github.almogtavor.processor;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.*;
import io.github.almogtavor.model.FieldDetails;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Generated;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.Arrays;
import java.util.List;

public class CodeGenerator {
    @NotNull
    public TypeSpec.Builder generateDetailedPojoClass(Element typeElementOfAnnotatedClass, String packageName, String typeName, TypeMirror typeOfAnnotatedClass) {
        TypeSpec.Builder classBuilder = getClassBuilder(packageName, typeName);
        classBuilder.addAnnotation(AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", DetailedPojoAnnotationProcessor.class.getCanonicalName()).build());

        TypeName fieldDetailsType = TypeName.get(
                FieldDetails.class);
        TypeName entriesListType = ParameterizedTypeName.get(
                ClassName.get(List.class),
                fieldDetailsType);

        FieldSpec.Builder fieldDetailsBuilder = FieldSpec
                .builder(entriesListType,
                        NameStore.Variable.FIELD_DETAILS_LIST_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder().add("$T.asList(", Arrays.class);
        List<VariableElement> enclosedFields = ElementFilter.fieldsIn(typeElementOfAnnotatedClass.getEnclosedElements());
        if (!enclosedFields.isEmpty()) {
            for (int i = 0; i < enclosedFields.size(); i++) {
                if (i == 0 && enclosedFields.size() > 1) { // first element
                    addListElement(codeBlockBuilder, "\n$>$>%s,", enclosedFields.get(i), typeOfAnnotatedClass);
                } else if (i == enclosedFields.size() - 1) { // last element
                    addListElement(codeBlockBuilder, "\n%s)$<$<", enclosedFields.get(i), typeOfAnnotatedClass);
                } else { // middle elements
                    addListElement(codeBlockBuilder, "\n%s,", enclosedFields.get(i), typeOfAnnotatedClass);
                }
            }
        } else {
            codeBlockBuilder.add(")");
        }
        fieldDetailsBuilder.initializer(codeBlockBuilder.build());
        classBuilder.addField(fieldDetailsBuilder.build());
        return classBuilder;
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
