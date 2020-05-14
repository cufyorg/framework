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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/**
 * Class ranging annotation. A way to specify a range of classes.
 * <p>
 * The levels of Overriding:
 * <ul>
 *     <li>{@link #exclude()}</li>
 *     <li>{@link #include()}</li>
 *     <li>{@link #value()}</li>
 *     <li>{@link #excludeAll()}</li>
 *     <li>{@link #includeAll()}</li>
 * </ul>
 *
 * @author lsafer
 * @version 0.1.3
 * @since 21-Nov-2019
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Filter {
	/**
	 * Classes not in range (subclasses NOT included).
	 *
	 * @return absolute classes not in range
	 */
	Class<?>[] exclude() default {};
	/**
	 * Classes not in range (subclasses included).
	 *
	 * @return super classes not in range
	 */
	Class<?>[] excludeAll() default {};
	/**
	 * Classes in range (subclasses NOT included).
	 *
	 * @return absolute classes in range
	 */
	Class<?>[] include() default {};
	/**
	 * Classes in range (subclasses included).
	 *
	 * @return super classes in range
	 */
	Class<?>[] includeAll() default {};
	/**
	 * Classes in range (subclasses NOT included).
	 *
	 * @return absolute classes in range
	 */
	Class<?>[] value() default {};

	/**
	 * Utilities for this annotation. Since static methods are illegal in annotations.
	 */
	final class util {
		/**
		 * This is a util class. And shall not be instanced as an object.
		 *
		 * @throws AssertionError when called
		 */
		private util() {
			throw new AssertionError("No instance for you!");
		}

		/**
		 * Check whether the given class is in the given family or not.
		 *
		 * @param family to check if the class is in
		 * @param klass  to be checked
		 * @return whether the given class is in the given family or not
		 * @throws NullPointerException if the given 'family' or 'klass' is null
		 */
		public static boolean test(Filter family, Class klass) {
			Objects.requireNonNull(family, "family");
			Objects.requireNonNull(klass, "klass");

			for (Class<?> exclude : family.exclude())
				if (exclude == klass)
					return false;

			for (Class<?> include : family.include())
				if (include == klass)
					return true;

			for (Class<?> include : family.value())
				if (include == klass)
					return true;

			for (Class<?> exclude : family.excludeAll())
				if (exclude.isAssignableFrom(klass))
					return false;

			for (Class<?> include : family.includeAll())
				if (include.isAssignableFrom(klass))
					return true;

			return false;
		}
	}
}
