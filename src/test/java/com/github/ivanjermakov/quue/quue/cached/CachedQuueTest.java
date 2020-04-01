package com.github.ivanjermakov.quue.quue.cached;

import com.github.ivanjermakov.quue.element.CachedElement;
import org.junit.Test;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CachedQuueTest {

	@Test
	public void shouldCompleteEmpty() {
		CachedQuue<Integer> queue = new CachedQuue<>();

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe())
				.expectComplete()
				.verifyLater();

		queue.complete();

		verifier.verify();
	}

	@Test
	public void shouldWriteAndComplete() {
		CachedQuue<Integer> queue = new CachedQuue<>();

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe())
				.assertNext(e -> assertThat(e.data()).isEqualTo(1))
				.assertNext(e -> assertThat(e.data()).isEqualTo(2))
				.expectComplete()
				.verifyLater();

		queue.send(1);
		queue.send(2);
		queue.complete();

		verifier.verify();
	}

	@Test
	public void shouldReadFromMultipleSubscribers() {
		CachedQuue<Integer> queue = new CachedQuue<>();

		List<StepVerifier> verifiers = IntStream
				.range(0, 10)
				.boxed()
				.map(i -> StepVerifier
						.create(queue.subscribe())
						.assertNext(e -> assertThat(e.data()).isEqualTo(1))
						.assertNext(e -> assertThat(e.data()).isEqualTo(2))
						.expectComplete()
						.verifyLater()
				)
				.collect(Collectors.toList());

		queue.send(1);
		queue.send(2);
		queue.complete();

		verifiers.forEach(StepVerifier::verify);
	}

	@Test
	public void shouldReadWroteAfterRead() {
		CachedQuue<Integer> queue = new CachedQuue<>();

		queue.send(1);
		queue.send(2);
		queue.complete();

		StepVerifier
				.create(queue.subscribe())
				.assertNext(e -> assertThat(e.data()).isEqualTo(1))
				.assertNext(e -> assertThat(e.data()).isEqualTo(2))
				.expectComplete()
				.verify();
	}

	@Test
	public void shouldReadWroteAfterAndBeforeRead() {
		CachedQuue<Integer> queue = new CachedQuue<>();
		queue.send(1);

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe())
				.assertNext(e -> assertThat(e.data()).isEqualTo(1))
				.assertNext(e -> assertThat(e.data()).isEqualTo(2))
				.expectComplete()
				.verifyLater();

		queue.send(2);
		queue.complete();

		verifier.verify();
	}

	@Test
	public void shouldSetCorrectIndices() {
		CachedQuue<Integer> queue = new CachedQuue<>();
		queue.send(1);
		queue.send(2);
		queue.complete();

		StepVerifier
				.create(queue.subscribe())
				.assertNext(e -> assertThat(e.index()).isEqualTo(0))
				.assertNext(e -> assertThat(e.index()).isEqualTo(1))
				.expectComplete()
				.verify();
	}

	@Test
	public void shouldSetCorrectTimestamps() {
		CachedQuue<Integer> queue = new CachedQuue<>();

		LocalDateTime timestamp1 = LocalDateTime.now();
		queue.send(1);

		LocalDateTime timestamp2 = LocalDateTime.now();
		queue.send(2);

		queue.complete();

		StepVerifier
				.create(queue.subscribe())
				.assertNext(e -> assertThat(ChronoUnit.SECONDS.between(timestamp1, e.timestamp())).isEqualTo(0))
				.assertNext(e -> assertThat(ChronoUnit.SECONDS.between(timestamp2, e.timestamp())).isEqualTo(0))
				.expectComplete()
				.verify();

		List<CachedElement<Integer>> elements = queue
				.subscribe()
				.collectList()
				.block();
		assertThat(elements).hasSize(2);
	}

	@Test
	public void shouldSubscribeWithOffset() {
		CachedQuue<Integer> queue = new CachedQuue<>();
		queue.send(1);
		queue.send(2);
		queue.complete();

		StepVerifier
				.create(queue.subscribe(1))
				.assertNext(e -> assertThat(e.data()).isEqualTo(2))
				.expectComplete()
				.verify();
	}

	@Test
	public void shouldSubscribeWithAfter() {
		CachedQuue<Integer> queue = new CachedQuue<>();
		queue.send(1);
		queue.send(2);

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe(LocalDateTime.now()))
				.assertNext(e -> assertThat(e.data()).isEqualTo(3))
				.assertNext(e -> assertThat(e.data()).isEqualTo(4))
				.expectComplete()
				.verifyLater();

		queue.send(3);
		queue.send(4);
		queue.complete();

		verifier.verify();
	}
}
