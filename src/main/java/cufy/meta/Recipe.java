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
package cufy.meta;

import cufy.convert.BaseConverter;
import cufy.convert.ConvertToken;
import cufy.convert.Converter;
import cufy.lang.Clazz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/**
 * A recipe to construct a value. Given the {@link #value()} is the source value. The {@link #converter()} to convert
 * that value. {@link #type()} is the type of that value.
 *
 * @author LSafer
 * @version 0.1.5
 * @since 0.0.1 ~2019.11.21
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Recipe {
	/**
	 * The reference to the converter to be used to construct the value.
	 *
	 * @return the reference to the converter to be used to construct the value.
	 */
	Where converter() default @Where(BaseConverter.class);

	/**
	 * The clazz of the object.
	 *
	 * @return the clazz of the object.
	 */
	Type type() default @Type(String.class);

	/**
	 * The source string of the value.
	 *
	 * @return the source string of teh value.
	 */
	String value() default "";

	/**
	 * Utilities for this annotation. Since static methods are illegal in annotations.
	 */
	final class Util {
		/**
		 * This is an util class and must not be instanced as an object.
		 *
		 * @throws AssertionError when called.
		 */
		private Util() {
			throw new AssertionError("No instance for you!");
		}

		/**
		 * Construct a value from the given {@link Recipe}.
		 *
		 * @param recipe the value constructing recipe
		 * @param <O>    the type of the returned value
		 * @return a value from the given {@code recipe}.
		 * @throws NullPointerException if the given {@code recipe} is null.
		 * @throws IllegalMetaException if ANY throwable get thrown while constructing it.
		 */
		public static <O> O get(Recipe recipe) {
			Objects.requireNonNull(recipe, "recipe");

			Converter converter = Where.Util.getValue(recipe.converter());
			Clazz<O> type = Type.Util.get(recipe.type());
			String value = recipe.value();

			try {
				return converter.convert(new ConvertToken<>(value, null, Clazz.of(String.class), type));
			} catch (Throwable e) {
				throw new IllegalMetaException(e);
			}
		}
	}
}
