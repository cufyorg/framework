/* with char|boolean|byte|double|float|int|long|short t*/
/*
 *	Copyright 2020 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package cufy.util.function;

import cufy.util.Objects;

/**
 * Represents a predicate (boolean-valued function) of two {@code char}-valued argument. This is the
 * {@code char}-consuming primitive type specialization of {@link java.util.function.BiPredicate}.
 * <p>
 * This is a {@code functional interface} whose functional method is {@link #test(char, char)}.
 *
 * @author LSafer
 * @version 0.1.5
 * @since 0.1.5 ~2020.09.03
 */
@FunctionalInterface
public interface CharBiPredicate {
	/**
	 * Returns a composed predicate that represents a short-circuiting logical AND of this predicate
	 * and another. When evaluating the composed predicate, if this predicate is {@code false}, then
	 * the {@code other} predicate is not evaluated.
	 * <p>
	 * Any exceptions thrown during evaluation of either predicate are relayed to the caller; if
	 * evaluation of this predicate throws an exception, the {@code other} predicate will not be
	 * evaluated.
	 *
	 * @param other a predicate that will be logically-ANDed with this predicate.
	 * @return a composed predicate that represents the short-circuiting logical. AND of this
	 * 		predicate and the {@code other} predicate.
	 * @throws NullPointerException if the given {@code other} is null.
	 * @since 0.1.5 ~2020.09.03
	 */
	default CharBiPredicate and(CharBiPredicate other) {
		Objects.requireNonNull(other, "other");
		return (v, o) -> this.test(v, o) && other.test(v, o);
	}

	/**
	 * Returns a predicate that represents the logical negation of this predicate.
	 *
	 * @return a predicate that represents the logical negation of this predicate.
	 * @since 0.1.5 ~2020.09.03
	 */
	default CharBiPredicate negate() {
		return (v, o) -> !this.test(v, o);
	}

	/**
	 * Returns a composed predicate that represents a short-circuiting logical OR of this predicate
	 * and another. When evaluating the composed predicate, if this predicate is {@code true}, then
	 * the {@code other} predicate is not evaluated.
	 * <p>
	 * Any exceptions thrown during evaluation of either predicate are relayed to the caller; if
	 * evaluation of this predicate throws an exception, the {@code other} predicate will not be
	 * evaluated.
	 *
	 * @param other a predicate that will be logically-ORed with this predicate.
	 * @return a composed predicate that represents the short-circuiting logical OR of this
	 * 		predicate and the {@code other} predicate.
	 * @throws NullPointerException if the given {@code other} is null.
	 * @since 0.1.5 ~2020.09.03
	 */
	default CharBiPredicate or(CharBiPredicate other) {
		Objects.requireNonNull(other, "other");
		return (v, o) -> this.test(v, o) || other.test(v, o);
	}

	/**
	 * Evaluates this predicate on the given arguments.
	 *
	 * @param value the first input argument.
	 * @param other the second input argument.
	 * @return {@code true} if the input arguments match the predicate, otherwise {@code false}.
	 * @since 0.1.5 ~2020.09.03
	 */
	boolean test(char value, char other);
}