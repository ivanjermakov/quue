package com.github.ivanjermakov.quue.subscribe;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

/**
 * Interface defining asynchronous reading data from the data stream categorized into topics.
 *
 * @param <D> type of read data elements
 * @see Subscriber
 */
@FunctionalInterface
public interface TopicSubscriber<T, D> {

	/**
	 * Subscribe to the data stream, reading specified topic.
	 *
	 * @param topic topic to read data from
	 * @return data stream
	 * @see Subscriber#subscribe()
	 */
	Flux<D> subscribe(@NotNull T topic);
}
