package com.github.ivanjermakov.quue.subscribe;

import reactor.core.publisher.Flux;

/**
 * Interface defining reading data from data stream asynchronously that haven't been read yet.
 *
 * @param <D> type of read data elements
 * @see Subscriber
 * @see CachedSubscriber
 */
@FunctionalInterface
public interface CheckpointSubscriber<D> {

	/**
	 * <p>Subscribe to the data stream.</p>
	 * <p>First subscription will start with all the elements sent into quue.
	 * Subsequent subscriptions will start with all the elements sent into quue after the previous subscription was
	 * cancelled.</p>
	 *
	 * <pre><code>
	 *
	 *     source ---a--b-----c--d---------e---f------g--|>
	 *                   |                        |
	 *              subscription             subscription
	 *                   v                        v
	 * subscriber ~~~~~[a, b]-c--d--|~~~~~~~~~~[e, f]-g--|>
	 *                              ^                    ^
	 *                            cancel              complete
	 *
	 * </code></pre>
	 *
	 * @return data stream with elements received after the last received element
	 */
	Flux<D> subscribe();
}
