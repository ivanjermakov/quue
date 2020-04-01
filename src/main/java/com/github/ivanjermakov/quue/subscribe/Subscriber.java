package com.github.ivanjermakov.quue.subscribe;

import com.github.ivanjermakov.quue.publish.Publisher;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

@FunctionalInterface
public interface Subscriber<D> {

	<T> Flux<D> subscribe(@NotNull T topic);

	default Flux<D> subscribe() {
		return subscribe(Publisher.DEFAULT_TOPIC);
	}
}
