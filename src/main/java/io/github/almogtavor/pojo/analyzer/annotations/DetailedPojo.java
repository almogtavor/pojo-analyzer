package io.github.almogtavor.pojo.analyzer.annotations;

import io.github.almogtavor.pojo.analyzer.model.FieldDetails;
import io.github.almogtavor.pojo.analyzer.model.VariableType;

import java.lang.annotation.*;

/**
 * An annotation to put on every POJO that needs a details object.
 * By this annotation, an annotation processor will run and generate a class named DetailedYourClassName,
 * which will contain a public static list of the {@link FieldDetails} class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
@Inherited
public @interface DetailedPojo {
    VariableType variableType() default VariableType.MAP;
}