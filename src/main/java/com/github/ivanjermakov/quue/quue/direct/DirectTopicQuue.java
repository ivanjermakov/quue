package com.github.ivanjermakov.quue.quue.direct;

import com.github.ivanjermakov.quue.quue.TopicQuue;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Base implementation of the {@link TopicQuue}
 *
 * @param <D> type of elements containing in the quue
 */
public class DirectTopicQuue<T, D> implements TopicQuue<T, D, D> {

	private final ConcurrentHashMap<T, DirectQuue<D>> quueMap;

	/**
	 * Create new instance of the direct topic quue.
	 */
	public DirectTopicQuue() {
		this.quueMap = new ConcurrentHashMap<>();
	}

	@Override
	public void send(@NotNull T topic, @NotNull D data) {
		createTopicIfAbsent(topic).send(data);
	}

	/**
	 * Subscribe to the data stream, reading specified topic.
	 *
	 * @return data stream with elements from specified topic and received after the subscription
	 */
	@Override
	public Flux<D> subscribe(@NotNull T topic) {
		return createTopicIfAbsent(topic).subscribe();
	}

	@Override
	public void complete() {
		this.quueMap.forEach((topic, quue) -> quue.complete());
	}

	@Override
	public void complete(@NotNull T topic) {
		quueMap.get(topic).complete();
	}

	private DirectQuue<D> createTopicIfAbsent(T topic) {
		DirectQuue<D> quue = quueMap.get(topic);
		if (quue == null) {
			quue = new DirectQuue<>();
			quueMap.putIfAbsent(topic, quue);
		}
		return quue;
	}
}
