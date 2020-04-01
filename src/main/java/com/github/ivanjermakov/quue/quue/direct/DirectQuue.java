package com.github.ivanjermakov.quue.quue.direct;

import com.github.ivanjermakov.quue.quue.Quue;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

public class DirectQuue<D> implements Quue<D, D> {

	private final FluxProcessor<D, D> processor;
	private final FluxSink<D> sink;

	public DirectQuue() {
		processor = DirectProcessor.<D>create().serialize();
		sink = this.processor.sink();

		processor.log().subscribe();
	}

	@Override
	public void send(@NotNull D data) {
		sink.next(data);
	}

	@Override
	public Flux<D> subscribe() {
		return processor;
	}

	@Override
	public void complete() {
		sink.complete();
	}
}
