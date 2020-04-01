package com.github.ivanjermakov.quue.subscribe;

import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface CachedSubscriber<D> extends Subscriber<D> {

	Flux<D> subscribe(long offset);

	Flux<D> subscribe(LocalDateTime after);
}
