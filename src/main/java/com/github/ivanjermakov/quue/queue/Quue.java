package com.github.ivanjermakov.quue.queue;

import com.github.ivanjermakov.quue.publish.Publisher;
import com.github.ivanjermakov.quue.subscribe.Subscriber;

public interface Quue<T, D> extends Publisher<D>, Subscriber<D> {

	default void complete() {
	}
}
