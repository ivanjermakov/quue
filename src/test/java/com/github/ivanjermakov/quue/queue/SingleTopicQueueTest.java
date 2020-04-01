package com.github.ivanjermakov.quue.queue;

import org.junit.Test;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SingleTopicQueueTest {

	@Test
	public void shouldCompleteEmpty() {
		SingleTopicQueue<Integer> queue = new SingleTopicQueue<>();

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe())
				.expectComplete()
				.verifyLater();

		queue.complete();

		verifier.verify();
	}

	@Test
	public void shouldWriteAndComplete() {
		SingleTopicQueue<Integer> queue = new SingleTopicQueue<>();

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe())
				.expectNext(1, 2)
				.expectComplete()
				.verifyLater();

		queue.send(1);
		queue.send(2);
		queue.complete();

		verifier.verify();
	}

	@Test
	public void shouldReadFromMultipleSubscribers() {
		SingleTopicQueue<Integer> queue = new SingleTopicQueue<>();

		List<StepVerifier> verifiers = IntStream
				.range(0, 10)
				.boxed()
				.map(i -> StepVerifier
						.create(queue.subscribe())
						.expectNext(1, 2)
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
	public void shouldNotReadWroteBeforeRead() {
		SingleTopicQueue<Integer> queue = new SingleTopicQueue<>();
		queue.send(1);

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe())
				.expectNext(2)
				.expectComplete()
				.verifyLater();

		queue.send(2);
		queue.complete();

		verifier.verify();
	}
}
