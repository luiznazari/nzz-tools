package br.com.senior.validation.di;

/**
 * A Dependency Injection container. Knows how to provide instances for specific classes.
 *
 * @author Luiz.Nazari
 */
public interface DIContainer {

	/**
	 * Verify whether or not this Dependency Injection container can provide an instance
	 * of the specified class.
	 *
	 * @param clazz the class to be validated.
	 * @param <T>   the type of the desired provided object.
	 * @return {@code true} if this container can provide an instance of the class,
	 * {@code false} otherwise.
	 */
	<T> boolean canProvide(Class<T> clazz);

	/**
	 * Provides an instance of the specified class.
	 *
	 * @param clazz the class to be provided.
	 * @param <T>   the type of the desired provided object.
	 * @return the provided instance or {@code null} if none could be provided.
	 */
	<T> T provide(Class<T> clazz);


}
