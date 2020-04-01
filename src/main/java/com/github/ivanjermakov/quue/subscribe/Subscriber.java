package com.github.ivanjermakov.quue.subscribe;

import reactor.core.publisher.Flux;

@FunctionalInterface
public interface Subscriber<D> {

	Flux<D> subscribe();
}
