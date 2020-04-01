package com.github.ivanjermakov.quue.element;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CachedElementTest {

	private final LocalDateTime time = LocalDateTime.of(2020, 4, 1, 0, 0);

	@Test
	public void shouldBeEqual() {
		assertThat(new CachedElement<>(1, 0, time)).isEqualTo(new CachedElement<>(1, 0, time));
	}

	@Test
	public void shouldBeEqualByReference() {
		CachedElement<Integer> element = new CachedElement<>(1, 0, time);
		assertThat(element).isEqualTo(element);
	}

	@Test
	public void shouldNotBeEqualToNull() {
		LocalDateTime time = LocalDateTime.of(2020, 4, 1, 0, 0);
		assertThat(new CachedElement<>(1, 0, time)).isNotEqualTo(null);
	}

	@Test
	public void shouldNotBeEqualByType() {
		LocalDateTime time = LocalDateTime.of(2020, 4, 1, 0, 0);
		assertThat(new CachedElement<>(1, 0, time)).isNotEqualTo(new Object());
	}
}
