package com.github.ivanjermakov.quue.quue;

import com.github.ivanjermakov.quue.publish.TopicPublisher;
import com.github.ivanjermakov.quue.subscribe.TopicSubscriber;

public interface TopicQuue<T, P, S> extends TopicPublisher<T, P>, TopicSubscriber<T, S> {

	void complete();

	void complete(T topic);
}
