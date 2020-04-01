package com.github.ivanjermakov.quue.quue;

import com.github.ivanjermakov.quue.publish.Publisher;
import com.github.ivanjermakov.quue.subscribe.Subscriber;

public interface Quue<P, S> extends Publisher<P>, Subscriber<S> {

	void complete();
}
