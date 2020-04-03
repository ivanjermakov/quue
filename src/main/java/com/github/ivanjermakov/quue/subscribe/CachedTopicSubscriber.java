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

	Flux<CachedElement<D>> subscribe(@NotNull T topic, long offset);

	Flux<CachedElement<D>> subscribe(@NotNull T topic, LocalDateTime after);
}
