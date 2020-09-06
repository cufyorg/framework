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
package cufy.primitive.util.function;

/**
 * Represents a function that accepts two {@code boolean}-valued arguments and produces a result.
 * This is the {@code boolean}-consuming primitive specialization for {@link
 * java.util.function.BiFunction}.
 * <p>
 * This is a {@code functional interface} whose functional method is {@link #apply(boolean,
 * boolean)}.
 *
 * @param <R> the type of the result of the function.
 * @author LSafer
 * @version 0.1.5
 * @since 0.1.5 ~2020.09.03
 */
@FunctionalInterface
public interface BooleanBiFunction<R> {
	/**
	 * Applies this function to the given arguments.
	 *
	 * @param value the first function argument.
	 * @param other the second function argument.
	 * @return the function result.
	 * @since 0.1.5 ~2020.09.03
	 */
	R apply(boolean value, boolean other);
}
