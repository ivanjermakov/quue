package com.github.ivanjermakov.quue.quue.checkpoint;

import com.github.ivanjermakov.quue.element.CachedElement;
import com.github.ivanjermakov.quue.quue.TopicQuue;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Base implementation of the {@link TopicQuue}
 *
 * @param <D> type of elements containing in the quue
 * @see CheckpointQuue
 */
public class CheckpointTopicQuue<T, D> implements TopicQuue<T, D, CachedElement<D>> {

	private final ConcurrentHashMap<T, CheckpointQuue<D>> quueMap;

	/**
	 * Create new instance of the checkpoint topic quue.
	 */
	public CheckpointTopicQuue() {
		this.quueMap = new ConcurrentHashMap<>();
	}

	@Override
	public void send(@NotNull T topic, @NotNull D data) {
		createIfAbsent(topic).send(data);
	}

	@Override
	public Flux<CachedElement<D>> subscribe(@NotNull T topic) {
		return createIfAbsent(topic).subscribe();
	}

	@Override
	public void complete() {
		this.quueMap.forEach((topic, quue) -> quue.complete());
	}

	@Override
	public void complete(@NotNull T topic) {
		quueMap.get(topic).complete();
	}

	private CheckpointQuue<D> createIfAbsent(T topic) {
		CheckpointQuue<D> quue = quueMap.get(topic);
		if (quue == null) {
			quue = new CheckpointQuue<>();
			quueMap.putIfAbsent(topic, quue);
		}
		return quue;
	}
}
