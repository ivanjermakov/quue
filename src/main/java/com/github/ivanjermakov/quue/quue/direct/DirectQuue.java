package com.github.ivanjermakov.quue.quue.direct;

import com.github.ivanjermakov.quue.quue.Quue;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

/**
 * Base implementation of the {@link Quue}
 *
 * @param <D> type of elements containing in the quue
 */
public class DirectQuue<D> implements Quue<D, D> {

	private final FluxProcessor<D, D> processor;
	private final FluxSink<D> sink;

	/**
	 * Create new instance of the direct quue.
	 */
	public DirectQuue() {
		processor = DirectProcessor.<D>create().serialize();
		sink = this.processor.sink();

		processor.log().subscribe();
	}

	@Override
	public void send(@NotNull D data) {
		sink.next(data);
	}

	/**
	 * Subscribe to the data stream.
	 *
	 * @return data stream with elements received after the subscription
	 */
	@Override
	public Flux<D> subscribe() {
		return processor;
	}

	@Override
	public void complete() {
		sink.complete();
	}
}
