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

public class CachedQuue<D> implements Quue<D, CachedElement<D>>, CachedSubscriber<CachedElement<D>> {

	private final FluxProcessor<CachedElement<D>, CachedElement<D>> processor;
	private final FluxSink<CachedElement<D>> sink;
	private final AtomicLong sentCount;

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
