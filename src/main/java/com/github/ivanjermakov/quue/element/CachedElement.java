package com.github.ivanjermakov.quue.element;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class CachedElement<D> {

	private final D data;
	private final long index;
	private final LocalDateTime timestamp;

	public CachedElement(@NotNull D data, long index, @NotNull LocalDateTime timestamp) {
		this.data = data;
		this.index = index;
		this.timestamp = timestamp;
	}

	public D data() {
		return data;
	}

	public long index() {
		return index;
	}

	public LocalDateTime timestamp() {
		return timestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CachedElement)) return false;
		CachedElement<?> that = (CachedElement<?>) o;
		return index == that.index &&
				data.equals(that.data) &&
				timestamp.equals(that.timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(data, index, timestamp);
	}
}
