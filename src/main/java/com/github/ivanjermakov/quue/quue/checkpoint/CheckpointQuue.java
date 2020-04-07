package com.github.ivanjermakov.quue.quue.checkpoint;

import com.github.ivanjermakov.quue.element.CachedElement;
import com.github.ivanjermakov.quue.quue.Quue;
import com.github.ivanjermakov.quue.subscribe.CachedSubscriber;
import com.github.ivanjermakov.quue.subscribe.CheckpointSubscriber;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the {@link Quue} supporting caching through {@link CachedSubscriber}
 *
 * @param <D> type of elements pushed into the quue
 */
public class CheckpointQuue<D> implements Quue<D, CachedElement<D>>, CheckpointSubscriber<CachedElement<D>> {

	private final FluxProcessor<CachedElement<D>, CachedElement<D>> processor;
	private final FluxSink<CachedElement<D>> sink;
	private final AtomicLong sentCount;
	private final AtomicLong readCount;
	private final AtomicBoolean connected;

	/**
	 * Create new instance of the checkpoint quue.
	 */
	public CheckpointQuue() {
		processor = ReplayProcessor.<CachedElement<D>>create().serialize();
		sink = this.processor.sink();

		sentCount = new AtomicLong(0);
		readCount = new AtomicLong(0);
		connected = new AtomicBoolean(false);

		processor.log().subscribe();
	}

	@Override
	public void send(@NotNull D data) {
		sink.next(new CachedElement<>(data, sentCount.getAndIncrement(), LocalDateTime.now()));
	}

	@Override
	public Flux<CachedElement<D>> subscribe() {
		System.out.println("new sub");
		if (connected.get()) throw new IllegalStateException("Only single subscriber is allowed.");
		connected.set(true);

		return processor
				.skip(readCount.get())
				.doOnNext((e) -> readCount.incrementAndGet())
				.doOnCancel(() -> connected.set(false))
				.doOnTerminate(() -> connected.set(false));
	}

	@Override
	public void complete() {
		sink.complete();
	}
}
