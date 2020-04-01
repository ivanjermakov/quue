package com.github.ivanjermakov.quue.publish;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Publisher<D> {

	void send(@NotNull D data);

	default void send(@NotNull D... data) {
		for (var d : data) {
			send(d);
		}
	}
}
