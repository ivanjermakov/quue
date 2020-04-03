package com.github.ivanjermakov.quue.subscribe;

import reactor.core.publisher.Flux;

/**
 * Interface defining reading data from data stream asynchronously.
 *
 * @param <D> type of read data elements
 */
@FunctionalInterface
public interface Subscriber<D> {

	/**
	 * Subscribe to the data stream.
	 *
	 * @return data stream
	 */
	Flux<D> subscribe();
}
