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
	 * <pre><code>
	 *
	 *     source ---a--b-----c--d---------e---f------g--|>
	 *                   |                        |
	 *              subscription             subscription
	 *                   v                        v
	 * subscriber ~~~~~~[ ]---c--d--|~~~~~~~~~~~~[ ]--g--|>
	 *                              ^                    ^
	 *                            cancel              complete
	 *
	 * </code></pre>
	 *
	 * @return data stream
	 */
	Flux<D> subscribe();
}
