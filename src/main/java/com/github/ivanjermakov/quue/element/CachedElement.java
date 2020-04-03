package com.github.ivanjermakov.quue.element;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data element placeholder with element index and push timestamp.
 *
 * @param <D> data element type
 */
public class CachedElement<D> {

	private final D data;
	private final long index;
	private final LocalDateTime timestamp;

	/**
	 * Construct new instance of {@link CachedElement}.
	 *
	 * @param data      data element
	 * @param index     index
	 * @param timestamp timestamp
	 */
	public CachedElement(@NotNull D data, long index, @NotNull LocalDateTime timestamp) {
		this.data = data;
		this.index = index;
		this.timestamp = timestamp;
	}

	/**
	 * Retrieve data element.
	 *
	 * @return data element
	 */
	public D data() {
		return data;
	}

	/**
	 * Retrieve index of the element.
	 *
	 * @return index of the element.
	 */
	public long index() {
		return index;
	}

	/**
	 * Retrieve timestamp of the element.
	 *
	 * @return timestamp of the element
	 */
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
