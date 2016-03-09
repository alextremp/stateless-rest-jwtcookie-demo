package dev.axt.demo.domain;

/**
 * Simple parametrized message
 *
 * @author alextremp
 * @param <T>
 */
public class MessageSimple<T> {

	private final T response;

	public MessageSimple(T response) {
		this.response = response;
	}

	public T getResponse() {
		return response;
	}

}
