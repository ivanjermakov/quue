package com.github.ivanjermakov.quue.subscribe;

import com.github.ivanjermakov.quue.element.CachedElement;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Interface defining reading data from data stream asynchronously with the ability to access elements wrote into stream
 * before subscription with topic support.
 *
 * @param <D> type of read data elements
 * @see CachedSubscriber
 */
public interface CachedTopicSubscriber<T, D> extends TopicSubscriber<T, CachedElement<D>> {

	/**
	 * <p>Subscribe to the data stream, retrieving elements with the specified offset from specified topic.</p>
	 *
	 * @param topic  topic to read data from
	 * @param offset if >= 0 acts as index offset from the first pushed element.
	 *               If < 0 acts as prefetch, retrieving {@literal -offset} last pushed elements before subscription
	 * @return data stream
	 * @see CachedElement
	 * @see CachedSubscriber#subscribe(long)
	 */
	Flux<CachedElement<D>> subscribe(@NotNull T topic, long offset);

	/**
	 * <p>Subscribe to the data stream, retrieving elements pushed after specified time into specified topic.</p>
	 *
	 * @param topic topic to read data from
	 * @param after time, after which elements are retrieved
	 * @return data stream
	 * @see CachedElement
	 * @see CachedSubscriber#subscribe(LocalDateTime)
	 */
	Flux<CachedElement<D>> subscribe(@NotNull T topic, LocalDateTime after);
}
