package com.github.ivanjermakov.quue.quue.direct;

import com.github.ivanjermakov.quue.quue.direct.DirectQuue;
import org.junit.Test;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DirectQuueTest {

	@Test
	public void shouldCompleteEmpty() {
		DirectQuue<Integer> queue = new DirectQuue<>();

		StepVerifier verifier = StepVerifier
				.create(queue.subscribe())
				.expectComplete()
				.verifyLater();

		queue.complete();

		verifier.verify();
	}

	@Test
	public void shouldWriteAndComplete() {
		DirectQuue<Integer> queue = new DirectQuue<>();

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
		DirectQuue<Integer> queue = new DirectQuue<>();

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
	public void shouldNotReadWroteAfterRead() {
		DirectQuue<Integer> queue = new DirectQuue<>();
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
