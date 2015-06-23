package framework.rest;

import java.util.function.Function;

/**
 * Function that take in param a {@link RestClientParameters}
 * @param <T> the type of the result of the function
 */
public interface RestClientFunction<T> extends Function<RestClientParameters, T> {

}
