package com.github.ivanjermakov.quue.publish;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TopicPublisher<T, D> {

	void send(@NotNull T topic, @NotNull D data);
}
