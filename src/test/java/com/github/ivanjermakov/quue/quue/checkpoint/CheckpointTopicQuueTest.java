package com.github.ivanjermakov.quue.quue.checkpoint;

import org.junit.Test;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckpointTopicQuueTest {
	private static final String TOPIC = "topic";

	@Test
	public void shouldCompleteEmpty() {
		CheckpointTopicQuue<String, Integer> queue = new CheckpointTopicQuue<>();

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe(TOPIC))
				.expectComplete()
				.verifyLater();

		queue.complete();

		verifier.verify();
	}

	@Test
	public void shouldWriteAndComplete() {
		CheckpointTopicQuue<String, Integer> queue = new CheckpointTopicQuue<>();

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe(TOPIC))
				.assertNext(e -> assertThat(e.data()).isEqualTo(1))
				.assertNext(e -> assertThat(e.data()).isEqualTo(2))
				.expectComplete()
				.verifyLater();

		queue.send(TOPIC, 1);
		queue.send(TOPIC, 2);
		queue.complete(TOPIC);

		verifier.verify();
	}

	@Test(expected = IllegalStateException.class)
	public void shouldNotReadFromMultipleSubscribers() {
		CheckpointTopicQuue<String, Integer> queue = new CheckpointTopicQuue<>();

		List<StepVerifier> verifiers = IntStream
				.range(0, 2)
				.boxed()
				.map(i -> StepVerifier
						.create(queue.subscribe(TOPIC))
						.assertNext(e -> assertThat(e.data()).isEqualTo(1))
						.assertNext(e -> assertThat(e.data()).isEqualTo(2))
						.expectComplete()
						.verifyLater()
				)
				.collect(Collectors.toList());

		queue.send(TOPIC, 1);
		queue.send(TOPIC, 2);
		queue.complete(TOPIC);

		verifiers.forEach(StepVerifier::verify);
	}

	@Test
	public void shouldSequentiallyRead() {
		CheckpointTopicQuue<String, Integer> queue = new CheckpointTopicQuue<>();

		queue.send(TOPIC, IntStream.range(1, 11).boxed().toArray(Integer[]::new));
		queue.complete();

		StepVerifier
				.create(queue.subscribe(TOPIC).take(2))
				.assertNext(e -> assertThat(e.data()).isEqualTo(1))
				.assertNext(e -> assertThat(e.data()).isEqualTo(2))
				.thenCancel()
				.verify();

		StepVerifier
				.create(queue.subscribe(TOPIC).take(3))
				.assertNext(e -> assertThat(e.data()).isEqualTo(3))
				.expectNextCount(2)
				.expectComplete()
				.verify();

		StepVerifier
				.create(queue.subscribe(TOPIC))
				.assertNext(e -> assertThat(e.data()).isEqualTo(6))
				.expectNextCount(4)
				.expectComplete()
				.verify();
	}

	@Test
	public void shouldSetCorrectIndices() {
		CheckpointTopicQuue<String, Integer> queue = new CheckpointTopicQuue<>();
		queue.send(TOPIC, 1);
		queue.send(TOPIC, 2);
		queue.complete();

		StepVerifier
				.create(queue.subscribe(TOPIC))
				.assertNext(e -> assertThat(e.index()).isEqualTo(0))
				.assertNext(e -> assertThat(e.index()).isEqualTo(1))
				.expectComplete()
				.verify();
	}

	@Test
	public void shouldSetCorrectTimestamps() {
		CheckpointTopicQuue<String, Integer> queue = new CheckpointTopicQuue<>();

		LocalDateTime timestamp1 = LocalDateTime.now();
		queue.send(TOPIC, 1);

		LocalDateTime timestamp2 = LocalDateTime.now();
		queue.send(TOPIC, 2);

		queue.complete();

		StepVerifier
				.create(queue.subscribe(TOPIC))
				.assertNext(e -> assertThat(ChronoUnit.SECONDS.between(timestamp1, e.timestamp())).isZero())
				.assertNext(e -> assertThat(ChronoUnit.SECONDS.between(timestamp2, e.timestamp())).isZero())
				.expectComplete()
				.verify();
	}
}
