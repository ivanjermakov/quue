package com.github.ivanjermakov.quue.subscribe;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

@FunctionalInterface
public interface TopicSubscriber<T, D> {

	Flux<D> subscribe(@NotNull T topic);
}
