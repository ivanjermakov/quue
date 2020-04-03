package com.github.ivanjermakov.quue.publish;

import org.jetbrains.annotations.NotNull;

/**
 * Interface defining pushing data to any kind of typed data stream categorized into topics.
 *
 * @param <D> type of pushed data elements
 */
@FunctionalInterface
public interface TopicPublisher<T, D> {

	/**
	 * Send data element to any kind of a typed data stream into specified topic.
	 *
	 * @param topic element will be sent to
	 * @param data  element to be sent
	 */
	void send(@NotNull T topic, @NotNull D data);

	/**
	 * Send multiple data elements to any kind of a typed data stream into specified topic.
	 *
	 * @param topic elements will be sent to
	 * @param data  elements to be sent
	 */
	@SuppressWarnings("unchecked")
	default void send(@NotNull T topic, @NotNull D... data) {
		for (var d : data) {
			send(topic, d);
		}
	}
}
