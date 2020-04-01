package com.github.ivanjermakov.quue.publish;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Publisher<D> {
	String DEFAULT_TOPIC = "";

	<T> void send(@NotNull T topic, @NotNull D data);

	default void send(@NotNull D data) {
		send(DEFAULT_TOPIC, data);
	}
}
