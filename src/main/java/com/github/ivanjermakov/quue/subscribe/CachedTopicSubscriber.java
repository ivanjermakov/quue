package com.github.ivanjermakov.quue.subscribe;

import com.github.ivanjermakov.quue.element.CachedElement;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface CachedTopicSubscriber<T, D> extends TopicSubscriber<T, CachedElement<D>> {

	Flux<CachedElement<D>> subscribe(@NotNull T topic, long offset);

	Flux<CachedElement<D>> subscribe(@NotNull T topic, LocalDateTime after);
}
