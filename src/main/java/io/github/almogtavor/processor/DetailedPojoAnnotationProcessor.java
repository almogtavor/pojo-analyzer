package io.github.almogtavor.processor;

import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.*;
import io.github.almogtavor.annotations.DetailedPojo;
import io.github.almogtavor.model.FieldDetails;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Generated;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

@SupportedAnnotationTypes("io.github.almogtavor.annotations.DetailedPojo")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DetailedPojoAnnotationProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    /**
     * @param annotations list of unique annotations that are getting processed
     * @param roundEnv interface that enables querying for information about the current annotation processing round
     * @return boolean value indicates the operation have been successful
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            // find all the classes that uses the supported annotations
            for (Element typeElementOfAnnotatedClass : roundEnv.getElementsAnnotatedWith(DetailedPojo.class)) {
                String packageName = elementUtils.getPackageOf(typeElementOfAnnotatedClass).getQualifiedName().toString();
                String typeName = typeElementOfAnnotatedClass.getSimpleName().toString();
                TypeMirror typeOfAnnotatedClass = typeElementOfAnnotatedClass.asType();
                TypeSpec.Builder classBuilder = generateClass(typeElementOfAnnotatedClass, packageName, typeName, typeOfAnnotatedClass);
                createTargetJavaFile(typeElementOfAnnotatedClass, packageName, classBuilder);
            }
        }
        return true;
    }

    private void createTargetJavaFile(Element typeElement, String packageName, TypeSpec.Builder classBuilder) {
        // write the defines class to a java file
        try {
            JavaFile.builder(packageName,
                            classBuilder.build())
                    .indent(NameStore.Package.INDENTATION)
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.toString(), typeElement);
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(
                DetailedPojo.class.getCanonicalName()));
    }
}