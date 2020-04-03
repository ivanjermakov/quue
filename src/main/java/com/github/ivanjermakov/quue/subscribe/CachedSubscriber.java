package com.github.ivanjermakov.quue.subscribe;

import com.github.ivanjermakov.quue.element.CachedElement;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Interface defining reading data from data stream asynchronously with the ability to access elements wrote into stream
 * before subscription.
 *
 * @param <D> type of read data elements
 */
public interface CachedSubscriber<D> extends Subscriber<CachedElement<D>> {

	/**
	 * Subscribe to the data stream, retrieving elements with the specified offset.
	 *
	 * @param offset if >= 0 acts as index offset from the first pushed element.
	 *               If < 0 acts as prefetch, retrieving {@literal -offset} last pushed elements before subscription
	 * @return data stream
	 */
	Flux<CachedElement<D>> subscribe(long offset);

	/**
	 * Subscribe to the data stream, retrieving elements pushed after specified time.
	 *
	 * @param after time, after which elements are retrieved
	 * @return data stream
	 */
	Flux<CachedElement<D>> subscribe(@NotNull LocalDateTime after);
}
