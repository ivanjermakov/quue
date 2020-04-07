package com.github.ivanjermakov.quue.quue.checkpoint;

import org.junit.Test;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckpointQuueTest {

	@Test
	public void shouldCompleteEmpty() {
		CheckpointQuue<Integer> queue = new CheckpointQuue<>();

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe())
				.expectComplete()
				.verifyLater();

		queue.complete();

		verifier.verify();
	}

	@Test
	public void shouldWriteAndComplete() {
		CheckpointQuue<Integer> queue = new CheckpointQuue<>();

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

	@Test(expected = IllegalStateException.class)
	public void shouldNotReadFromMultipleSubscribers() {
		CheckpointQuue<Integer> queue = new CheckpointQuue<>();

		List<StepVerifier> verifiers = IntStream
				.range(0, 2)
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
	public void shouldSequentiallyRead() {
		CheckpointQuue<Integer> queue = new CheckpointQuue<>();

		queue.send(IntStream.range(1, 11).boxed().toArray(Integer[]::new));
		queue.complete();

		StepVerifier
				.create(queue.subscribe().take(2))
				.assertNext(e -> assertThat(e.data()).isEqualTo(1))
				.assertNext(e -> assertThat(e.data()).isEqualTo(2))
				.thenCancel()
				.verify();

		StepVerifier
				.create(queue.subscribe().take(3))
				.assertNext(e -> assertThat(e.data()).isEqualTo(3))
				.expectNextCount(2)
				.expectComplete()
				.verify();

		StepVerifier
				.create(queue.subscribe())
				.assertNext(e -> assertThat(e.data()).isEqualTo(6))
				.expectNextCount(4)
				.expectComplete()
				.verify();
	}

	@Test
	public void shouldSetCorrectIndices() {
		CheckpointQuue<Integer> queue = new CheckpointQuue<>();
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
		CheckpointQuue<Integer> queue = new CheckpointQuue<>();

		LocalDateTime timestamp1 = LocalDateTime.now();
		queue.send(1);

		LocalDateTime timestamp2 = LocalDateTime.now();
		queue.send(2);

		queue.complete();

		StepVerifier
				.create(queue.subscribe())
				.assertNext(e -> assertThat(ChronoUnit.SECONDS.between(timestamp1, e.timestamp())).isZero())
				.assertNext(e -> assertThat(ChronoUnit.SECONDS.between(timestamp2, e.timestamp())).isZero())
				.expectComplete()
				.verify();
	}
}
