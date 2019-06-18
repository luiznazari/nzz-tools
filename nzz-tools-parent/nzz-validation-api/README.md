# Java Validation API

The Validation API defines classes for bean and custom validation. It's useful to decouple validations from logic.

### Features:

- Bean validation;
- Chaining validation rules;
- Building validators;
  - Validation blocks.


## Custom validation

Chains bean validation with custom validation errors suppliers. Runs all validations in a a single validation block.

```java
ValidationResult validationResult = validator
    .validate(object)
    .validate(() -> {
        if (condition) {
            return new I18nValidationError("error.message.key");
        } else {
            return null;
        }
    })
    .validate(() -> {
        if (condition) {
            throw new CustomValidationException("error.message.key");
        }
        return null;
    });
```


## Validator builder

Build a validator for validating a specific object, building validation blocks, chaining validations, errors suppliers, and more.

The validator can validate more than one object with the same rules.

```java
ObjectValidator<BeautifulObject> objectValidator = validator.builder(BeautifulObject.class)
    .with(new NotNullValidationRule()).blocking()
    .withBeanValidation().blocking()
    .with(TestValidationRule.class)
    .with(TestIdNotNullValidator.class).blocking();
    .with(notEmptyValidationErrorSupplier)
    .build();

ValidationResult validationResult = objectValidator.validate(object);
```

With the validation builder, you can chain validation rules specific for an object or any of it's super classes. For example, if an object `A extends B` and object `B extends Object`, a validator builded for object A class will accept validation rules that validates A, B and Object.

When the `.blocking()` method is called, an validation block is created. Validation blocks are executed separately and the validation will be stopped if an validation block returns one or more errors.

Validation blocks are useful for situations when one validation depends on another, for example, if you have a class that validates the content of an object's field, it is necessary that the object and it's field are not _null_. In this case, you can build an validation block that validates if the object or it's field are null and chain with another block that validates the field content.


## Vaidation result

`ValidationResult` is the default interface that represets a validation result and access to it's validation errors. Some defined methods are:

```java
boolean hasErrors = validationResult.hasErrors();
```
```java
Set<ValidationError> errors = validationResult.getErrors();
```
```java
validationResult.onError(errorsConsumer);
```
```java
validationResult.onErrorThrowException();
```
```java
validationResult.ifNoError(runnable);
```

## Dependency Injection on ValidationRule classes

When building an validator, you can pass a validation rule class instead of passing an instanced rule when calling the `.with(...)` method. When this occurs, the Validation API will try to inject the dependency in the validator using a Dependency Injector Container available at runtime.

The current supported Dependency Injector Container are:
- Spring;
- Vert.x + Jspare.

If no container is provided by the application, Validation API will try to instantiate the rule with a default constructor.


# Examples

## Vert.x + Jspare: bean validation on handlers

```java
@SubRouter("/route")
public class YourRoute extends APIHandler {

	@Inject
	private YourService service;
    @Inject
	private CustomValidator validator;

	@Handler
	@Post("/")
	public void save(Object object) {
		validator.validate(object)
            .onError(this::badRequest)
			.ifNoError(() -> {
                Object savedObject = service.save(object);
                success(savedObject);
			});
	}

}
```