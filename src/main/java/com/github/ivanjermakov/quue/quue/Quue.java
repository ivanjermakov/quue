package com.github.ivanjermakov.quue.quue;

import com.github.ivanjermakov.quue.publish.Publisher;
import com.github.ivanjermakov.quue.subscribe.Subscriber;

/**
 * Base interface of {@literal Quues} - real-time data processing queue.
 *
 * @param <P> type of elements pushed into quue
 * @param <S> type of elements retrieved from quue
 */
public interface Quue<P, S> extends Publisher<P>, Subscriber<S> {

	/**
	 * Complete quue and reject future reads and writes
	 */
	void complete();
}
