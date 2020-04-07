package com.github.ivanjermakov.quue.subscribe;

import com.github.ivanjermakov.quue.element.CachedElement;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Interface defining reading data from data stream asynchronously with the ability to access elements wrote into stream
 * before subscription.
 *
 * @param <D> type of read data elements
 */
public interface CachedSubscriber<D> extends Subscriber<CachedElement<D>> {

	/**
	 * <p>Subscribe to the data stream, retrieving elements with the specified offset.</p>
	 *
	 * <br>
	 * <p>offset 0:</p>
	 * <pre><code>
	 *
	 *     source ---a--b-----c--d---------e---f-------------g--|>
	 *                   |                        |
	 *              subscription             subscription
	 *                   v                        v
	 * subscriber ~~~~[a, b]--c--d--|~~~~[a, b, c, d, e, f]--g--|>
	 *                              ^                           ^
	 *                            cancel                      complete
	 *
	 * </code></pre>
	 *
	 * <br>
	 * <p>offset 4:</p>
	 * <pre><code>
	 *
	 *     source ---a--b-----c--d---------e---f----------g--|>
	 *                   |                        |
	 *              subscription             subscription
	 *                   v                        v
	 * subscriber ~~~~~~[ ]---c--d--|~~~~~~~[c, d, e, f]--g--|>
	 *                              ^                        ^
	 *                            cancel                   complete
	 *
	 * </code></pre>
	 *
	 * <br>
	 * <p>offset -2:</p>
	 * <pre><code>
	 *
	 *     source ---a--b-----c--d---------e---f----------g--|>
	 *                   |                        |
	 *              subscription             subscription
	 *                   v                        v
	 * subscriber ~~~~[a, b]--c--d--|~~~~~~~~~~[e, f]-----g--|>
	 *                              ^                        ^
	 *                            cancel                   complete
	 *
	 * </code></pre>
	 *
	 * @param offset if >= 0 acts as index offset from the first pushed element.
	 *               If < 0 acts as prefetch, retrieving {@literal -offset} last pushed elements before subscription
	 * @return data stream
	 */
	Flux<CachedElement<D>> subscribe(long offset);

	/**
	 * <p>Subscribe to the data stream, retrieving elements pushed after specified time.</p>
	 *
	 * <br>
	 * <p>after now():</p>
	 * <pre><code>
	 *
	 *     source ---a--b-----c--d---------e---f------g--|>
	 *                   |                        |
	 *              subscription             subscription
	 *                   v                        v
	 * subscriber ~~~~~~[ ]---c--d--|~~~~~~~~~~~~[ ]--g--|>
	 *                              ^                    ^
	 *                            cancel              complete
	 *
	 * </code></pre>
	 *
	 * <br>
	 * <p>after 12:00:</p>
	 * <pre><code>
	 *
	 *             11:30  12:20
	 *               v     v
	 *     source ---a-----b-----c--d---------e---f------------g--|>
	 *                      |                        |
	 *                 subscription             subscription
	 *                      v                        v
	 * subscriber ~~~~~~~~~[b]---c--d--|~~~~~~[b, c, d, e, f]--g--|>
	 *                                 ^                          ^
	 *                               cancel                    complete
	 *
	 * </code></pre>
	 *
	 * <br>
	 * <p>after 16:00:</p>
	 * <pre><code>
	 *
	 *             11:30  12:20                 14:20  18:00
	 *               v     v                      v      v
	 *     source ---a-----b-----c--d---------e---f------g--|>
	 *                      |                        |
	 *                 subscription             subscription
	 *                      v                        v
	 * subscriber ~~~~~~~~~[ ]---------|~~~~~~~~~~~~[ ]--g--|>
	 *                                 ^                    ^
	 *                               cancel              complete
	 *
	 * </code></pre>
	 *
	 * @param after time, after which elements are retrieved
	 * @return data stream
	 */
	Flux<CachedElement<D>> subscribe(@NotNull LocalDateTime after);
}
