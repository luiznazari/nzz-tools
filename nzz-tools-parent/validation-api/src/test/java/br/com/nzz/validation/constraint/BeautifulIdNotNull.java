package br.com.nzz.validation.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validate that an {@link br.com.nzz.validation.BeautifulId is not {@code null} and
 * has a non {@code null} {@link br.com.nzz.validation.BeautifulId#getId() id}.
 *
 * @author Luiz.Nazari
 */
@Documented
@Constraint(validatedBy = BeautifulIdNotNullValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface BeautifulIdNotNull {

	String message() default "test.id.empty";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
