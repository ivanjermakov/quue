package com.github.ivanjermakov.quue.quue.direct;

import org.junit.Test;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DirectTopicQuueTest {
	private static final String TOPIC = "topic";

	@Test
	public void shouldCompleteEmpty() {
		DirectTopicQuue<String, Integer> queue = new DirectTopicQuue<>();

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe(TOPIC))
				.expectComplete()
				.verifyLater();

		queue.complete();

		verifier.verify();
	}

	@Test
	public void shouldWriteAndComplete() {
		DirectTopicQuue<String, Integer> queue = new DirectTopicQuue<>();

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe(TOPIC))
				.expectNext(1, 2)
				.expectComplete()
				.verifyLater();

		queue.send(TOPIC, 1);
		queue.send(TOPIC, 2);
		queue.complete(TOPIC);

		verifier.verify();
	}

	@Test
	public void shouldReadFromMultipleSubscribers() {
		DirectTopicQuue<String, Integer> queue = new DirectTopicQuue<>();

		List<StepVerifier> verifiers = IntStream
				.range(0, 10)
				.boxed()
				.map(i -> StepVerifier
						.create(queue.subscribe(TOPIC))
						.expectNext(1, 2)
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
	public void shouldNotReadWroteAfterRead() {
		DirectTopicQuue<String, Integer> queue = new DirectTopicQuue<>();
		queue.send(TOPIC, 1);

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe(TOPIC))
				.expectNext(2)
				.expectComplete()
				.verifyLater();

		queue.send(TOPIC, 2);
		queue.complete();

		verifier.verify();
	}
}
