package com.github.ivanjermakov.quue.publish;

import org.jetbrains.annotations.NotNull;

/**
 * Interface defining pushing data to any kind of typed data stream.
 *
 * @param <D> type of pushed elements
 */
@FunctionalInterface
public interface Publisher<D> {

	/**
	 * Send data element to any kind of a typed data stream.
	 *
	 * @param data element to be sent
	 */
	void send(@NotNull D data);

	/**
	 * Send multiple data elements to any kind of a typed data stream.
	 *
	 * @param data elements to be sent
	 */
	@SuppressWarnings("unchecked")
	default void send(@NotNull D... data) {
		for (var d : data) {
			send(d);
		}
	}
}
