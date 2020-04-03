package com.github.ivanjermakov.quue.quue.cached;

import com.github.ivanjermakov.quue.element.CachedElement;
import com.github.ivanjermakov.quue.quue.Quue;
import com.github.ivanjermakov.quue.subscribe.CachedSubscriber;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the {@link Quue} supporting caching through {@link CachedSubscriber}
 *
 * @param <D> type of elements pushed into the quue
 */
public class CachedQuue<D> implements Quue<D, CachedElement<D>>, CachedSubscriber<D> {

	private final FluxProcessor<CachedElement<D>, CachedElement<D>> processor;
	private final FluxSink<CachedElement<D>> sink;
	private final AtomicLong sentCount;

	/**
	 * Create new instance of the cached quue.
	 */
	public CachedQuue() {
		processor = ReplayProcessor.<CachedElement<D>>create().serialize();
		sink = this.processor.sink();
		sentCount = new AtomicLong(0);

		processor.log().subscribe();
	}

	@Override
	public void send(@NotNull D data) {
		sink.next(new CachedElement<>(data, sentCount.getAndIncrement(), LocalDateTime.now()));
	}

	/**
	 * Subscribe to the data stream.
	 *
	 * @return data stream with elements received after the subscription
	 */
	@Override
	public Flux<CachedElement<D>> subscribe() {
		return processor.skip(Duration.ZERO);
	}

	@Override
	public Flux<CachedElement<D>> subscribe(long offset) {
		return processor.skip(offset);
	}

	@Override
	public Flux<CachedElement<D>> subscribe(LocalDateTime after) {
		return processor.skipUntil(e -> e.timestamp().isAfter(after));
	}

	@Override
	public void complete() {
		sink.complete();
	}
}
