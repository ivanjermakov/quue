package com.github.ivanjermakov.quue.queue;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

public class SingleTopicQueue<D> implements Quue<Void, D> {
	private final FluxProcessor<D, D> processor;
	private final FluxSink<D> sink;

	public SingleTopicQueue() {
		this.processor = DirectProcessor.<D>create().serialize();
		this.sink = this.processor.sink();
		this.processor.log().subscribe();
	}

	@Override
	public <T> void send(@NotNull T topic, @NotNull D data) {
		sink.next(data);
	}

	@Override
	public <T> Flux<D> subscribe(@NotNull T topic) {
		return processor;
	}

	@Override
	public void complete() {
		sink.complete();
	}
}
