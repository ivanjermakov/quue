package com.github.ivanjermakov.quue.quue.cached;

import com.github.ivanjermakov.quue.element.CachedElement;
import com.github.ivanjermakov.quue.quue.TopicQuue;
import com.github.ivanjermakov.quue.subscribe.CachedTopicSubscriber;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the {@link TopicQuue} supporting caching through {@link CachedTopicSubscriber}
 *
 * @param <D> type of elements pushed into the quue
 */
public class CachedTopicQuue<T, D> implements TopicQuue<T, D, CachedElement<D>>, CachedTopicSubscriber<T, D> {

	private final ConcurrentHashMap<T, CachedQuue<D>> quueMap;

	/**
	 * Create new instance of the cached topic quue.
	 */
	public CachedTopicQuue() {
		this.quueMap = new ConcurrentHashMap<>();
	}

	@Override
	public void send(@NotNull T topic, @NotNull D data) {
		createTopicIfAbsent(topic).send(data);
	}

	@Override
	public Flux<CachedElement<D>> subscribe(@NotNull T topic) {
		return createTopicIfAbsent(topic).subscribe();
	}

	@Override
	public Flux<CachedElement<D>> subscribe(@NotNull T topic, long offset) {
		return createTopicIfAbsent(topic).subscribe(offset);
	}

	@Override
	public Flux<CachedElement<D>> subscribe(@NotNull T topic, LocalDateTime after) {
		return createTopicIfAbsent(topic).subscribe(after);
	}

	@Override
	public void complete() {
		this.quueMap.forEach((topic, quue) -> quue.complete());
	}

	@Override
	public void complete(@NotNull T topic) {
		quueMap.get(topic).complete();
	}

	private CachedQuue<D> createTopicIfAbsent(T topic) {
		CachedQuue<D> quue = quueMap.get(topic);
		if (quue == null) {
			quue = new CachedQuue<>();
			quueMap.putIfAbsent(topic, quue);
		}
		return quue;
	}
}
