package br.com.nzz.spring.soap;


import br.com.nzz.spring.exception.WebServiceInternalException;

/**
 * Especificações de uma função de transformação que aceita dois parâmetros.
 * Pode lançar uma {@link WebServiceInternalException}.
 *
 * @param <T> tipo do primeiro parâmetro da função.
 * @param <E> tipo do segundo parâmetro da função.
 * @param <R> tipo do retorno da função.
 * @author Luiz.Nazari
 */
@FunctionalInterface
public interface WebServiceBiFunction<T, E, R> {

	R apply(T t, E e) throws WebServiceInternalException;

}
