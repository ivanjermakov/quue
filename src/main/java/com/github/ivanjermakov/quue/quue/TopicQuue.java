package com.github.ivanjermakov.quue.quue;

import com.github.ivanjermakov.quue.publish.TopicPublisher;
import com.github.ivanjermakov.quue.subscribe.TopicSubscriber;

/**
 * Interface of {@link Quue}s supporting topic categorization.
 *
 * @param <T>
 * @param <P>
 * @param <S>
 * @see Quue
 */
public interface TopicQuue<T, P, S> extends TopicPublisher<T, P>, TopicSubscriber<T, S> {

	/**
	 * Complete quue and reject future reads and writes in/from all its topics.
	 */
	void complete();

	/**
	 * Complete quue's topic and reject future reads/writes in/from the specified topic.
	 *
	 * @param topic to complete
	 */
	void complete(T topic);
}
