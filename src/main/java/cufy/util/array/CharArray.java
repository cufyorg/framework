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
package cufy.util.array;

import cufy.util.Objects;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.*;
import java.util.stream.IntStream;

/**
 * A holder for an array of {@link Object}s.
 *
 * @author LSafer
 * @version 0.1.5
 * @since 0.1.5 ~2020.08.03
 */
public class CharArray extends Array<char[], Character> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3201994039505608491L;

	/**
	 * Construct a new array backed by a new actual array that have the given {@code length}.
	 *
	 * @param length the length of the new actual array backing the construct array.
	 * @throws NegativeArraySizeException if the given {@code length} is negative.
	 * @see java.lang.reflect.Array#newInstance(Class, int)
	 * @since 0.1.5 ~2020.08.13
	 */
	public CharArray(int length) {
		super(new char[length]);
	}

	/**
	 * Construct a new array backed by the given {@code array}.
	 *
	 * @param array the array to be backing the constructed array.
	 * @throws NullPointerException if the given {@code array} is null.
	 * @since 0.1.5 ~2020.08.05
	 */
	public CharArray(char[] array) {
		super(array);
	}

	/**
	 * Construct a new array backed by the specified range of the given {@code array}. The range
	 * starts at the given {@code beginIndex} and ends before the given {@code endIndex}.
	 *
	 * @param array      the array to be backing the constructed array.
	 * @param beginIndex the first index of the area at the given {@code array} to be backing the
	 *                   constructed array.
	 * @param endIndex   one past the last index of the area at the given {@code array} to be
	 *                   backing the constructed array.
	 * @throws NullPointerException      if the given {@code array} is null.
	 * @throws IndexOutOfBoundsException if {@code beginIndex < 0} or {@code endIndex >
	 *                                   array.length}.
	 * @throws IllegalArgumentException  if {@code beginIndex > endIndex}.
	 * @since 0.1.5 ~2020.08.05
	 */
	public CharArray(char[] array, int beginIndex, int endIndex) {
		super(array, beginIndex, endIndex);
	}

	/**
	 * Construct a new array backed by a new array from the given {@code collection} using {@link
	 * #from(Collection)}.
	 *
	 * @param collection the collection to construct a new array from to be backing the constructed
	 *                   array.
	 * @throws NullPointerException if the given {@code collection} is null.
	 * @since 0.1.5 ~2020.08.12
	 */
	public CharArray(java.util.Collection<Character> collection) {
		super(CharArray.from(collection));
	}

	/**
	 * Construct a new array backed by a new array from the given {@code map} using {@link
	 * #from(java.util.Map)}.
	 *
	 * @param map the map to construct a new array from to be backing the constructed array.
	 * @throws NullPointerException if the given {@code map} is null.
	 * @since 0.1.5 ~2020.08.12
	 */
	public CharArray(java.util.Map<Character, Character> map) {
		super(CharArray.from(map));
	}

	/**
	 * Determine if the given {@code array} does equals the given {@code other} in length, elements,
	 * and order.
	 *
	 * @param array the first array to be matched.
	 * @param other the second array to be matched.
	 * @return true, if the given {@code array} does equals the given {@code other} in length,
	 * 		elements, and order.
	 * @see java.util.Arrays#equals(char[], char[])
	 * @since 0.1.5 ~2020.07.24
	 */
	public static boolean equals(char[] array, char[] other) {
		if (array == other)
			return true;
		if (array.length == other.length)
			for (int i = 0; i < array.length; i++) {
				char element = other[i];
				char e = array[i];

				if (element == e)
					continue;

				return false;
			}

		return false;
	}

	/**
	 * Get an array from the given {@code collection}.
	 *
	 * @param collection the collection to get an array from it.
	 * @return an array from the given {@code collection}.
	 * @throws NullPointerException if the given {@code collection} is null.
	 * @throws ArrayStoreException  if an item in the given {@code collection} can not be stored in
	 *                              the product array.
	 * @since 0.1.5 ~2020.08.11
	 */
	public static char[] from(java.util.Collection<Character> collection) {
		Objects.requireNonNull(collection, "collection");
		char[] array = new char[collection.size()];

		java.util.Iterator iterator = collection.iterator();
		for (int i = 0; i < array.length; i++) {
			Object element = iterator.next();

			if (element instanceof Character)
				array[i] = (char) element;
			else
				throw new ArrayStoreException();
		}

		return array;
	}

	/**
	 * Get an array from the given {@code map}.
	 *
	 * @param map the map to get an array from it.
	 * @return an array from the given {@code map}.
	 * @throws NullPointerException if the given {@code map} is null.
	 * @throws ArrayStoreException  if an item in the given {@code map} can not be stored in the
	 *                              product array.
	 * @since 0.1.5 ~2020.08.11
	 */
	public static char[] from(java.util.Map<Character, Character> map) {
		Objects.requireNonNull(map, "map");
		char[] array = new char[map.size() << 1];

		java.util.Iterator<? extends java.util.Map.Entry> iterator = map.entrySet().iterator();
		for (int i = 0; i < array.length; i += 2) {
			java.util.Map.Entry entry = iterator.next();
			Object key = entry.getKey();
			Object value = entry.getValue();

			if (key instanceof Character && value instanceof Character) {
				array[i] = (char) key;
				array[i + 1] = (char) value;
			} else
				throw new ArrayStoreException();
		}

		return array;
	}

	/**
	 * Calculate the hash code of the elements of the given {@code array}.
	 *
	 * @param array the array to compute its hash code.
	 * @return the hash code of the elements of the given {@code array}.
	 * @see java.util.Arrays#hashCode(char[])
	 * @since 0.1.5 ~2020.07.24
	 */
	public static int hashCode(char[] array) {
		if (array == null)
			return 0;

		int hashCode = 1;
		for (int i = 0; i < array.length; i++) {
			char e = array[i];
			hashCode = 31 * hashCode + Character.hashCode(e);
		}

		return hashCode;
	}

	/**
	 * Build a string representation of the contents of the given {@code array}.
	 *
	 * @param array the array to build a string representation for it.
	 * @return a string representation of the contents of the given {@code array}.
	 * @see java.util.Arrays#toString(char[])
	 * @since 0.1.5 ~2020.07.24
	 */
	public static String toString(char[] array) {
		if (array == null)
			return "null";
		if (array.length == 0)
			return "[]";

		StringBuilder builder = new StringBuilder("[");

		int i = 0;
		while (true) {
			char e = array[i];

			builder.append(e);

			i++;
			if (i >= array.length)
				return builder.append("]")
						.toString();

			builder.append(", ");
		}
	}

	@Override
	public char[] array(int length) {
		if (length < 0)
			throw new NegativeArraySizeException("length(" + length + ") < 0");
		char[] array = new char[length];

		System.arraycopy(
				this.array,
				this.beginIndex,
				array,
				0,
				Math.min(this.length(), length)
		);

		return array;
	}

	@Override
	public void arraycopy(char[] array, int pos) {
		Objects.requireNonNull(array, "array");

		int length = this.length();

		if (pos < 0)
			throw new IndexOutOfBoundsException("pos(" + pos + ") < 0");
		if (pos + length < array.length)
			throw new IndexOutOfBoundsException(
					"pos(" + pos + ") + length(" + length + ") > array.length(" + array.length +
					")");

		System.arraycopy(
				this.array,
				this.beginIndex,
				array,
				pos,
				length
		);
	}

	@Override
	public int binarySearch(Character element) {
		int low = this.beginIndex;
		int high = this.endIndex - 1;
		while (low <= high) {
			int mid = low + high >>> 1;
			char midVal = this.array[mid];
			if (midVal < element)
				low = mid + 1;
			else if (midVal > element)
				high = mid - 1;
			else
				return this.thumb(mid); // key found
		}
		return this.thumb(-(low + 1));  // key not found.
	}

	@Override
	public CharArray clone() {
		// noinspection OverridableMethodCallDuringObjectConstruction,CloneCallsConstructors
		return new CharArray(this.array());
	}

	@Override
	public void copy(Object array, int pos) {
		Objects.requireNonNull(array, "array");

		if (array instanceof char[])
			this.arraycopy((char[]) array, pos);
		else if (array instanceof Object[])
			this.hardcopy((Object[]) array, pos);
		else
			throw new ArrayStoreException(
					"copy: type mismatch: can not copy char[] into " +
					array.getClass().getSimpleName());
	}

	@Override
	public boolean equals(Object object) {
		if (object == this)
			//same identity
			return true;
		if (object instanceof CharArray) {
			//same class
			CharArray array = (CharArray) object;

			if (array.length() == this.length()) {
				//same length

				for (int i = array.beginIndex, j = this.beginIndex; i < array.endIndex; i++, j++) {
					//for each element
					Object element = array.array[i];
					char e = this.array[j];

					if (this.eq(element, e))
						continue;

					return false;
				}

				//all elements are equal
				return true;
			}
		}

		//not equal
		return false;
	}

	@Override
	public void fill(Character element) {
		for (int i = this.beginIndex; i < this.endIndex; i++)
			this.array[i] = element;
	}

	@Override
	public void forEach(Consumer<? super Character> consumer) {
		Objects.requireNonNull(consumer, "consumer");
		for (int i = this.beginIndex; i < this.endIndex; i++) {
			char e = this.array[i];

			consumer.accept(e);
		}
	}

	@Override
	public Character get(int thumb) {
		return this.array[this.index(thumb)];
	}

	@Override
	public void hardcopy(Object[] array, int pos) {
		Objects.requireNonNull(array, "array");

		int length = this.length();

		if (pos < 0)
			throw new IndexOutOfBoundsException("pos(" + pos + ") < 0");
		if (pos + length > array.length)
			throw new IndexOutOfBoundsException(
					"pos(" + pos + ") + length(" + length + ") > array.length(" + array.length +
					")");

		try {
			for (int i = this.beginIndex, j = pos; i < this.endIndex; i++, j++)
				array[j] = this.array[i];
		} catch (IllegalArgumentException e) {
			throw new ArrayStoreException(e.getMessage());
		}
	}

	@Override
	public int hashCode() {
		int hashCode = 1;

		for (int i = this.beginIndex; i < this.endIndex; i++) {
			char e = this.array[i];

			hashCode = 31 * hashCode + Character.hashCode(e);
		}

		return hashCode;
	}

	@Override
	public Iterator iterator() {
		return new Iterator();
	}

	@Override
	public List list() {
		return new List();
	}

	@Override
	public ListIterator listIterator() {
		return new ListIterator();
	}

	@Override
	public Map map() {
		return new Map();
	}

	@Override
	public void parallelPrefix(BinaryOperator<Character> operator) {
		//manual
		Character[] temp = new Character[this.length()];

		for (int i = this.beginIndex, j = 0; i < this.endIndex; i++, j++)
			temp[j] = this.array[i];

		java.util.Arrays.parallelPrefix(temp, operator);

		for (int i = this.beginIndex, j = 0; i < this.endIndex; i++, j++)
			this.array[i] = temp[j];
	}

	@Override
	public void parallelSetAll(IntFunction<? extends Character> function) {
		Objects.requireNonNull(function, "function");
		IntStream.range(this.beginIndex, this.endIndex)
				.parallel()
				.forEach(i -> this.array[i] = function.apply(this.thumb(i)));
	}

	@Override
	public void parallelSort() {
		//manual
		java.util.Arrays.parallelSort(
				this.array,
				this.beginIndex,
				this.endIndex
		);
	}

	@Override
	public void set(int thumb, Character element) {
		this.array[this.index(thumb)] = element;
	}

	@Override
	public void setAll(IntFunction<? extends Character> function) {
		Objects.requireNonNull(function, "function");
		for (int i = this.beginIndex, j = 0; i < this.endIndex; i++, j++)
			this.array[i] = function.apply(j);
	}

	@Override
	public void sort() {
		//manual
		java.util.Arrays.sort(
				this.array,
				this.beginIndex,
				this.endIndex
		);
	}

	@Override
	public Spliterator spliterator() {
		return new Spliterator();
	}

	@Override
	public CharArray sub(int beginThumb, int endThumb) {
		this.range(beginThumb, endThumb);
		return new CharArray(
				this.array,
				this.beginIndex + beginThumb,
				this.beginIndex + endThumb
		);
	}

	@Override
	public String toString() {
		if (this.endIndex <= this.beginIndex)
			return "[]";

		StringBuilder builder = new StringBuilder("[");

		int i = this.beginIndex;
		while (true) {
			char e = this.array[i];

			builder.append(e);

			i++;
			if (i >= this.endIndex)
				return builder.append("]")
						.toString();

			builder.append(", ");
		}
	}

	/**
	 * Get the element at the given {@code thumb} in this array.
	 *
	 * @param thumb the thumb to get the element from.
	 * @return the element at the given {@code thumb} in this array.
	 * @throws ArrayIndexOutOfBoundsException if {@code thumb < 0} or {@code thumb >= length}.
	 * @see java.lang.reflect.Array#getChar(Object, int)
	 * @since 0.1.5 ~2020.08.13
	 */
	public char getChar(int thumb) {
		return this.array[this.index(thumb)];
	}

	/**
	 * Set the element at the given {@code thumb} in this array to the given {@code element}.
	 *
	 * @param thumb   the thumb to set the given {@code element} to.
	 * @param element the element to be set.
	 * @throws ArrayIndexOutOfBoundsException if {@code thumb < 0} or {@code thumb >= length}.
	 * @throws ArrayStoreException            if the given {@code element} can not be stored to the
	 *                                        array.
	 * @see java.lang.reflect.Array#setChar(Object, int, char)
	 * @since 0.1.5 ~2020.08.13
	 */
	public void setChar(int thumb, char element) {
		this.array[this.index(thumb)] = element;
	}

	/**
	 * Determine if the given two elements are equal or not. This is the base equality check (for
	 * object-primitive) in this class and it should be for its subclasses.
	 *
	 * @param element the first element.
	 * @param e       the second element.
	 * @return true, if the given {@code element} equals the given {@code e} in this class's
	 * 		standard.
	 * @since 0.1.5 ~2020.08.18
	 */
	protected boolean eq(Object element, char e) {
		return element != null && element.equals(e);
	}

	/**
	 * Determine if the given two elements are equal or not. This is the base equality check (for
	 * primitive-primitive) in this class and it should be for its subclasses.
	 *
	 * @param element the first element.
	 * @param e       the second element.
	 * @return true, if the given {@code element} equals the given {@code e} in this class's
	 * 		standard.
	 * @since 0.1.5 ~2020.08.18
	 */
	protected boolean eq(char element, char e) {
		return element == e;
	}

	/**
	 * An iterator iterating the elements in the enclosing array.
	 *
	 * @author LSafer
	 * @version 0.1.5
	 * @since 0.1.5 ~2020.07.24
	 */
	public class Iterator extends Array<char[], Character>.Iterator {
		/**
		 * Construct a new iterator iterating the elements in the enclosing array.
		 *
		 * @since 0.1.5 ~2020.08.06
		 */
		public Iterator() {
		}

		/**
		 * Construct a new iterator iterating the elements in the enclosing array.
		 *
		 * @param beginThumb the initial position of the constructed iterator.
		 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index > length}.
		 * @since 0.1.5 ~2020.08.06
		 */
		public Iterator(int beginThumb) {
			super(beginThumb);
		}

		@Override
		public void forEachRemaining(Consumer<? super Character> consumer) {
			Objects.requireNonNull(consumer, "consumer");
			int index = this.index;
			this.index = CharArray.this.endIndex;

			for (int i = index; i < CharArray.this.endIndex; i++) {
				char e = CharArray.this.array[i];

				consumer.accept(e);
			}
		}

		@Override
		public Character next() {
			int index = this.index;

			if (index < CharArray.this.endIndex) {
				this.index++;

				return CharArray.this.array[index];
			}

			throw new NoSuchElementException();
		}
	}

	/**
	 * A list backed by the enclosing array.
	 *
	 * @author LSafer
	 * @version 0.1.5
	 * @since 0.1.5 ~2020.07.24
	 */
	public class List extends Array<char[], Character>.List {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 848985287158674978L;

		@Override
		public boolean contains(Object object) {
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i++) {
				char e = CharArray.this.array[i];

				if (CharArray.this.eq(object, e))
					return true;
			}

			return false;
		}

		@Override
		public boolean equals(Object object) {
			if (object == this)
				//same identity
				return true;
			if (object instanceof java.util.List) {
				//same class
				java.util.List list = (java.util.List) object;

				if (list.size() == this.size()) {
					//same length

					int i = CharArray.this.beginIndex;
					for (Object element : list) {
						//for each element

						if (i < CharArray.this.endIndex) {
							//still same length
							char e = CharArray.this.array[i++];

							if (CharArray.this.eq(element, e))
								continue;
						}

						return false;
					}

					//all elements are equal
					return true;
				}
			}

			//not equal
			return false;
		}

		@Override
		public Character get(int thumb) {
			return CharArray.this.array[CharArray.this.index(thumb)];
		}

		@Override
		public int hashCode() {
			int hashCode = 1;

			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i++) {
				char e = CharArray.this.array[i];

				hashCode = 31 * hashCode + Character.hashCode(e);
			}

			return hashCode;
		}

		@Override
		public int indexOf(Object object) {
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i++) {
				char e = CharArray.this.array[i];

				if (CharArray.this.eq(object, e))
					return i - CharArray.this.beginIndex;
			}

			return -1;
		}

		@Override
		public int lastIndexOf(Object object) {
			for (int i = CharArray.this.endIndex - 1;
				 i >= CharArray.this.beginIndex; i--) {
				char e = CharArray.this.array[i];

				if (CharArray.this.eq(object, e))
					return i - CharArray.this.beginIndex;
			}

			return -1;
		}

		@Override
		public ListIterator listIterator(int beginThumb) {
			return new ListIterator();
		}

		@Override
		public boolean removeIf(Predicate<? super Character> predicate) {
			Objects.requireNonNull(predicate, "predicate");

			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i++) {
				char e = CharArray.this.array[i];

				if (predicate.test(e))
					//can not remove
					throw new UnsupportedOperationException("remove");
			}

			//nothing to remove
			return false;
		}

		@Override
		public void replaceAll(UnaryOperator<Character> operator) {
			Objects.requireNonNull(operator, "operator");
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i++) {
				char e = CharArray.this.array[i];
				char n = operator.apply(e);

				CharArray.this.array[i] = n;
			}
		}

		@Override
		public Character set(int thumb, Character element) {
			Objects.requireNonNull(element, "element");
			int i = CharArray.this.index(thumb);
			char old = CharArray.this.array[i];
			CharArray.this.array[i] = element;
			return old;
		}

		@Override
		public void sort(Comparator<? super Character> comparator) {//hard manual
			if (comparator == null)
				CharArray.this.sort();
			Character[] temp = new Character[this.size()];
			for (int i = CharArray.this.beginIndex, j = 0;
				 i < CharArray.this.endIndex; i++, j++)
				temp[j] = CharArray.this.array[i];
			java.util.Arrays.sort(temp, comparator);
			for (int i = CharArray.this.beginIndex, j = 0;
				 i < CharArray.this.endIndex; i++, j++)
				CharArray.this.array[i] = temp[j];
		}
	}

	/**
	 * A list iterator iterating the elements in the enclosing array.
	 *
	 * @author LSafer
	 * @version 0.1.5
	 * @since 0.1.5 ~2020.07.24
	 */
	public class ListIterator extends Array<char[], Character>.ListIterator {
		/**
		 * Construct a new list iterator iterating the elements in the enclosing array.
		 *
		 * @since 0.1.5 ~2020.08.06
		 */
		public ListIterator() {
		}

		/**
		 * Construct a new list iterator iterating the elements in the enclosing array, starting
		 * from the given {@code index}.
		 *
		 * @param beginThumb the initial position of the constructed iterator.
		 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index > length}.
		 * @since 0.1.5 ~2020.08.06
		 */
		public ListIterator(int beginThumb) {
			super(beginThumb);
		}

		@Override
		public void forEachRemaining(Consumer<? super Character> consumer) {
			Objects.requireNonNull(consumer, "consumer");
			int index = this.index;
			this.index = CharArray.this.endIndex;
			this.last = CharArray.this.endIndex - 1;

			for (int i = index; i < CharArray.this.endIndex; i++) {
				char e = CharArray.this.array[i];

				consumer.accept(e);
			}
		}

		@Override
		public Character next() {
			int index = this.index;

			if (index < CharArray.this.endIndex) {
				this.index++;
				this.last = index;

				return CharArray.this.array[index];
			}

			throw new NoSuchElementException();
		}

		@Override
		public Character previous() {
			int index = this.index - 1;

			if (index >= CharArray.this.beginIndex) {
				this.index--;
				this.last = index;

				return CharArray.this.array[index];
			}

			throw new NoSuchElementException();
		}

		@Override
		public void set(Character element) {
			Objects.requireNonNull(element, "element");
			int index = this.last;

			if (index == -1)
				throw new IllegalStateException();

			CharArray.this.array[index] = element;
		}
	}

	/**
	 * A map backed by the enclosing array.
	 *
	 * @author LSafer
	 * @version 0.1.5
	 * @since 0.1.5 ~2020.08.03
	 */
	public class Map extends Array<char[], Character>.Map<Character, Character> {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -2840280796050057228L;

		@Override
		public Character compute(Character key, BiFunction<? super Character, ? super Character, ? extends Character> function) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(function, "function");

			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k)) {
					char v = CharArray.this.array[i + 1];
					Character value = function.apply(k, v);

					if (value == null)
						//old:notnull new:null
						throw new UnsupportedOperationException("remove");

					//old:found
					CharArray.this.array[i + 1] = value;
					return value;
				}
			}

			//old:notfound
			throw new UnsupportedOperationException("put");
		}

		@Override
		public Character computeIfAbsent(Character key, Function<? super Character, ? extends Character> function) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(function, "function");

			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k))
					//old:notnull
					return CharArray.this.array[i + 1];
			}

			//old:notfound
			throw new UnsupportedOperationException("put");
		}

		@Override
		public Character computeIfPresent(Character key, BiFunction<? super Character, ? super Character, ? extends Character> function) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(function, "function");

			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k)) {
					char v = CharArray.this.array[i + 1];
					Character value = function.apply(k, v);

					if (value == null)
						//old:notnull new:null
						throw new UnsupportedOperationException("remove");

					//old:notnull new:notnull
					CharArray.this.array[i + 1] = value;
					return value;
				}
			}

			//old:notfound
			return null;
		}

		@Override
		public boolean containsKey(Object key) {
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k))
					return true;
			}

			return false;
		}

		@Override
		public boolean containsValue(Object value) {
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char v = CharArray.this.array[i];

				if (CharArray.this.eq(value, v))
					return true;
			}

			return false;
		}

		@Override
		public EntrySet entrySet() {
			return new EntrySet();
		}

		@Override
		public boolean equals(Object object) {
			if (object == this)
				return true;
			if (object instanceof java.util.Map) {
				java.util.Map<?, ?> map = (java.util.Map) object;

				if (map.size() == this.size()) {
					for0:
					for (java.util.Map.Entry entry : map.entrySet()) {
						Object key = entry.getKey();

						for (int i = CharArray.this.beginIndex;
							 i < CharArray.this.endIndex; i += 2) {
							char k = CharArray.this.array[i];

							if (CharArray.this.eq(key, k)) {
								Object value = entry.getValue();
								char v = CharArray.this.array[i + 1];

								if (CharArray.this.eq(value, v))
									continue for0;

								break;
							}
						}

						return false;
					}

					return true;
				}
			}

			return false;
		}

		@Override
		public void forEach(BiConsumer<? super Character, ? super Character> consumer) {
			Objects.requireNonNull(consumer, "consumer");

			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];
				char v = CharArray.this.array[i + 1];
				consumer.accept(k, v);
			}
		}

		@Override
		public Character get(Object key) {
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k))
					return CharArray.this.array[i + 1];
			}

			return null;
		}

		@Override
		public Character getOrDefault(Object key, Character defaultValue) {
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k))
					return CharArray.this.array[i + 1];
			}

			return defaultValue;
		}

		@Override
		public int hashCode() {
			int hashCode = 0;

			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];
				char v = CharArray.this.array[i + 1];
				hashCode += Character.hashCode(k) ^
							Character.hashCode(v);
			}

			return hashCode;
		}

		@Override
		public KeySet keySet() {
			return new KeySet();
		}

		@Override
		public Character merge(Character key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> function) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(value, "value");
			Objects.requireNonNull(function, "function");

			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k)) {
					char v = CharArray.this.array[i + 1];
					Character newValue = function.apply(v, value);

					if (newValue == null)
						//old:found new:null
						throw new UnsupportedOperationException("remove");

					//old:found new:notnull
					CharArray.this.array[i + 1] = newValue;
					return newValue;
				}
			}

			//old:notfound new:notnull
			throw new UnsupportedOperationException("put");
		}

		@Override
		public Character put(Character key, Character value) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(value, "value");
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k)) {
					//old:found
					char v = CharArray.this.array[i + 1];
					CharArray.this.array[i + 1] = value;
					return v;
				}
			}

			//old:notfound
			throw new UnsupportedOperationException("put");
		}

		@Override
		public void putAll(java.util.Map<? extends Character, ? extends Character> map) {
			Objects.requireNonNull(map, "map");

			for0:
			for (java.util.Map.Entry<? extends Character, ? extends Character> entry : map.entrySet()) {
				char key = entry.getKey();

				for (int i = CharArray.this.beginIndex;
					 i < CharArray.this.endIndex; i += 2) {
					char k = CharArray.this.array[i];

					if (CharArray.this.eq(key, k)) {
						char value = entry.getValue();
						CharArray.this.array[i + 1] = value;
						continue for0;
					}
				}

				//old:notfound
				throw new UnsupportedOperationException("put");
			}
		}

		@Override
		public Character putIfAbsent(Character key, Character value) {
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k))
					//old:found
					return CharArray.this.array[i + 1];
			}

			//old:notfound
			throw new UnsupportedOperationException("put");
		}

		@Override
		public boolean remove(Object key, Object value) {
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k)) {
					char v = CharArray.this.array[i + 1];

					if (CharArray.this.eq(value, v))
						//old:match
						throw new UnsupportedOperationException("remove");

					break;
				}
			}

			//old:nomatch
			return false;
		}

		@Override
		public boolean replace(Character key, Character oldValue, Character newValue) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(newValue, "newValue");
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k)) {
					char v = CharArray.this.array[i + 1];

					if (CharArray.this.eq(oldValue, v)) {
						//old:match
						CharArray.this.array[i + 1] = newValue;
						return true;
					}

					break;
				}
			}

			//old:nomatch
			return false;
		}

		@Override
		public Character replace(Character key, Character value) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(value, "value");
			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];

				if (CharArray.this.eq(key, k)) {
					//old:match
					char v = CharArray.this.array[i + 1];
					CharArray.this.array[i + 1] = value;
					return v;
				}
			}

			//old:nomatch
			return null;
		}

		@Override
		public void replaceAll(BiFunction<? super Character, ? super Character, ? extends Character> function) {
			Objects.requireNonNull(function, "function");

			for (int i = CharArray.this.beginIndex; i < CharArray.this.endIndex; i += 2) {
				char k = CharArray.this.array[i];
				char v = CharArray.this.array[i + 1];
				char n = function.apply(k, v);

				CharArray.this.array[i + 1] = n;
			}
		}

		@Override
		public String toString() {
			if (this.isEmpty())
				return "{}";

			StringBuilder builder = new StringBuilder("{");

			int i = CharArray.this.beginIndex;
			while (true) {
				char k = CharArray.this.array[i];
				char v = CharArray.this.array[i + 1];

				builder.append(k)
						.append("=")
						.append(v);

				i += 2;
				if (i >= CharArray.this.endIndex)
					return builder.append("}")
							.toString();

				builder.append(", ");
			}
		}

		@Override
		public Values values() {
			return new Values();
		}

		/**
		 * An entry backed by a range from {@code index} to {@code index + 1} in the enclosing
		 * array.
		 *
		 * @author LSafer
		 * @version 0.1.5
		 * @since 0.1.5 ~2020.08.03
		 */
		public class Entry extends Array<char[], Character>.Map<Character, Character>.Entry {
			@SuppressWarnings("JavaDoc")
			private static final long serialVersionUID = 5973497615323125824L;

			/**
			 * Construct a new entry backed by a range from {@code index} to {@code index + 1} in
			 * the enclosing array.
			 *
			 * @param thumb the index to where the key (followed by the value) will be in the
			 *              constructed entry.
			 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index + 1 >=
			 *                                   length}.
			 * @since 0.1.5 ~2020.08.06
			 */
			public Entry(int thumb) {
				super(thumb);
			}

			@Override
			public boolean equals(Object object) {
				if (object == this)
					return true;
				if (object instanceof java.util.Map.Entry) {
					java.util.Map.Entry entry = (java.util.Map.Entry) object;
					Object key = entry.getKey();
					char k = CharArray.this.array[this.index];

					if (CharArray.this.eq(key, k)) {
						Object value = entry.getValue();
						char v = CharArray.this.array[this.index + 1];

						return CharArray.this.eq(value, v);
					}
				}

				return false;
			}

			@Override
			public Character getKey() {
				return CharArray.this.array[this.index];
			}

			@Override
			public Character getValue() {
				return CharArray.this.array[this.index + 1];
			}

			@Override
			public int hashCode() {
				char k = CharArray.this.array[this.index];
				char v = CharArray.this.array[this.index + 1];
				return Character.hashCode(k) ^
					   Character.hashCode(v);
			}

			@Override
			public Character setValue(Character value) {
				Objects.requireNonNull(value, "value");
				char v = CharArray.this.array[this.index + 1];
				CharArray.this.array[this.index + 1] = value;
				return v;
			}

			@Override
			public String toString() {
				char k = CharArray.this.array[this.index];
				char v = CharArray.this.array[this.index + 1];
				return k + "=" + v;
			}
		}

		/**
		 * A set backed by the entries in the enclosing array.
		 *
		 * @author LSafer
		 * @version 0.1.5
		 * @since 0.1.5 ~2020.08.03
		 */
		public class EntrySet extends Array<char[], Character>.Map<Character, Character>.EntrySet {
			@SuppressWarnings("JavaDoc")
			private static final long serialVersionUID = -4823635378224028987L;

			@Override
			public boolean contains(Object object) {
				if (object instanceof java.util.Map.Entry) {
					java.util.Map.Entry entry = (java.util.Map.Entry) object;
					Object key = entry.getKey();

					for (int i = CharArray.this.beginIndex;
						 i < CharArray.this.endIndex; i += 2) {
						char k = CharArray.this.array[i];

						if (CharArray.this.eq(key, k)) {
							Object value = entry.getValue();
							char v = CharArray.this.array[i + 1];

							if (CharArray.this.eq(value, v))
								return true;

							break;
						}
					}
				}

				return false;
			}

			@Override
			public boolean equals(Object object) {
				if (object == this)
					return true;
				if (object instanceof java.util.Set) {
					java.util.Set set = (java.util.Set) object;

					if (set.size() == this.size()) {
						for0:
						for (Object object1 : set) {
							if (object1 instanceof java.util.Map.Entry) {
								java.util.Map.Entry entry = (java.util.Map.Entry) object1;
								Object key = entry.getKey();

								for (int i = CharArray.this.beginIndex;
									 i < CharArray.this.endIndex; i += 2) {
									char k = CharArray.this.array[i];

									if (CharArray.this.eq(key, k)) {
										Object value = entry.getValue();
										char v = CharArray.this.array[i + 1];

										if (CharArray.this.eq(value, v))
											continue for0;

										break;
									}
								}
							}

							return false;
						}

						return true;
					}
				}

				return false;
			}

			@Override
			public void forEach(Consumer<? super java.util.Map.Entry<Character, Character>> consumer) {
				Objects.requireNonNull(consumer, "consumer");

				int i = 0;
				int l = CharArray.this.length();
				for (; i < l; i += 2) {
					Entry entry = new Entry(i);//trimmed index

					consumer.accept(entry);
				}
			}

			@Override
			public int hashCode() {
				int hashCode = 0;

				for (int i = CharArray.this.beginIndex;
					 i < CharArray.this.endIndex; i += 2) {
					char k = CharArray.this.array[i];
					char v = CharArray.this.array[i + 1];
					hashCode += Character.hashCode(k) ^ Character.hashCode(v);
				}

				return hashCode;
			}

			@Override
			public Iterator iterator() {
				return new Iterator();
			}

			@Override
			public boolean removeIf(Predicate<? super java.util.Map.Entry<Character, Character>> predicate) {
				Objects.requireNonNull(predicate, "predicate");

				int i = 0;
				int l = CharArray.this.length();
				for (; i < l; i += 2) {
					Entry entry = new Entry(i); //trimmed index

					if (predicate.test(entry))
						//can not remove
						throw new UnsupportedOperationException("remove");
				}

				//no match
				return false;
			}

			@Override
			public Spliterator spliterator() {
				return new Spliterator();
			}

			@Override
			public Object[] toArray() {
				int length = this.size();
				Object[] product = new Object[length];

				int i = 0;
				int l = CharArray.this.length();
				for (int j = 0; i < l; i += 2, j++) {
					Entry entry = new Entry(i);//trimmed index

					product[j] = entry;
				}

				return product;
			}

			@Override
			public <T> T[] toArray(T[] array) {
				Objects.requireNonNull(array, "array");
				int length = this.size();
				T[] product = array;

				if (array.length < length)
					product = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), length);
				else if (array.length > length)
					product[length] = null;

				//should trim the index for the entry creation
				int i = 0;
				int l = CharArray.this.length();
				for (int j = 0; i < l; i += 2, j++) {
					Entry entry = new Entry(i);//trimmed index

					product[j] = (T) entry;
				}

				return product;
			}

			@Override
			public String toString() {
				if (this.isEmpty())
					return "[]";

				StringBuilder builder = new StringBuilder("[");

				int i = CharArray.this.beginIndex;
				while (true) {
					char k = CharArray.this.array[i];
					char v = CharArray.this.array[i + 1];

					builder.append(k)
							.append("=")
							.append(v);

					i += 2;
					if (i >= CharArray.this.endIndex)
						return builder.append("]")
								.toString();

					builder.append(", ");
				}
			}

			/**
			 * An iterator iterating the entries in the enclosing array.
			 *
			 * @author LSafer
			 * @version 0.1.5
			 * @since 0.1.5 ~2020.08.03
			 */
			public class Iterator extends Array<char[], Character>.Map<Character, Character>.EntrySet.Iterator {
				/**
				 * Construct a new iterator iterating the entries in the enclosing array.
				 *
				 * @since 0.1.5 ~2020.08.06
				 */
				public Iterator() {
				}

				/**
				 * Construct a new iterator iterating the entries in the enclosing array, starting
				 * from the given {@code index}.
				 *
				 * @param beginThumb the initial position of the constructed iterator.
				 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >
				 *                                   length}.
				 * @since 0.1.5 ~2020.08.06
				 */
				public Iterator(int beginThumb) {
					super(beginThumb);
				}

				@Override
				public void forEachRemaining(Consumer<? super java.util.Map.Entry<Character, Character>> consumer) {
					Objects.requireNonNull(consumer, "consumer");
					int index = this.index;
					this.index = CharArray.this.endIndex;

					int i = CharArray.this.thumb(index);
					int l = CharArray.this.length();
					for (; i < l; i += 2) {
						Entry entry = new Entry(i);//trimmed index

						consumer.accept(entry);
					}
				}

				@Override
				public Entry next() {
					int index = this.index;

					if (index < CharArray.this.endIndex) {
						this.index += 2;

						int i = CharArray.this.thumb(index);
						return new Entry(i);//trimmed index
					}

					throw new NoSuchElementException();
				}
			}

			/**
			 * A spliterator iterating the entries in the enclosing array.
			 *
			 * @author LSafer
			 * @version 0.1.5
			 * @since 0.1.5 ~2020.08.02
			 */
			public class Spliterator extends Array<char[], Character>.Map<Character, Character>.EntrySet.Spliterator {
				/**
				 * Construct a new spliterator iterating the entries in the enclosing array.
				 *
				 * @since 0.1.5 ~2020.08.06
				 */
				public Spliterator() {
				}

				/**
				 * Construct a new spliterator iterating the entries in the enclosing array,
				 * starting from the given {@code index}.
				 *
				 * @param beginThumb the initial position of the constructed spliterator.
				 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >
				 *                                   length}.
				 * @since 0.1.5 ~2020.08.06
				 */
				public Spliterator(int beginThumb) {
					super(beginThumb);
				}

				@Override
				public void forEachRemaining(Consumer<? super java.util.Map.Entry<Character, Character>> consumer) {
					Objects.requireNonNull(consumer, "consumer");
					int index = this.index;
					this.index = CharArray.this.endIndex;

					int i = 0;
					int l = CharArray.this.length();
					for (; i < l; i += 2) {
						Entry entry = new Entry(i);//trimmed index

						consumer.accept(entry);
					}
				}

				@Override
				public boolean tryAdvance(Consumer<? super java.util.Map.Entry<Character, Character>> consumer) {
					Objects.requireNonNull(consumer, "consumer");
					int index = this.index;

					if (index < CharArray.this.endIndex) {
						this.index += 2;

						int i = CharArray.this.thumb(index);
						Entry entry = new Entry(i);//trimmed index
						consumer.accept(entry);
						return true;
					}

					return false;
				}
			}
		}

		/**
		 * A set backed by the keys in the enclosing array.
		 *
		 * @author LSafer
		 * @version 0.1.5
		 * @since 0.1.5 ~2020.08.03
		 */
		public class KeySet extends Array<char[], Character>.Map<Character, Character>.KeySet {
			@SuppressWarnings("JavaDoc")
			private static final long serialVersionUID = 7793360078444812816L;

			@Override
			public boolean equals(Object object) {
				if (object == this)
					return true;
				if (object instanceof java.util.Set) {
					java.util.Set set = (java.util.Set) object;

					if (set.size() == this.size()) {
						for0:
						for (Object key : set) {
							for (int i = CharArray.this.beginIndex;
								 i < CharArray.this.endIndex; i += 2) {
								char k = CharArray.this.array[i];

								if (CharArray.this.eq(key, k))
									continue for0;
							}

							return false;
						}

						return true;
					}
				}

				return false;
			}

			@Override
			public void forEach(Consumer<? super Character> consumer) {
				Objects.requireNonNull(consumer, "consumer");
				for (int i = CharArray.this.beginIndex;
					 i < CharArray.this.endIndex; i += 2) {
					char k = CharArray.this.array[i];
					consumer.accept(k);
				}
			}

			@Override
			public int hashCode() {
				int hashCode = 0;

				for (int i = CharArray.this.beginIndex;
					 i < CharArray.this.endIndex; i += 2) {
					char k = CharArray.this.array[i];
					hashCode += Character.hashCode(k);
				}

				return hashCode;
			}

			@Override
			public Iterator iterator() {
				return new Iterator();
			}

			@Override
			public boolean removeIf(Predicate<? super Character> predicate) {
				Objects.requireNonNull(predicate, "predicate");

				for (int i = CharArray.this.beginIndex;
					 i < CharArray.this.endIndex; i += 2) {
					char k = CharArray.this.array[i];
					if (predicate.test(k))
						//can not remove
						throw new UnsupportedOperationException("remove");
				}

				//nothing to remove
				return false;
			}

			@Override
			public Spliterator spliterator() {
				return new Spliterator();
			}

			@Override
			public Object[] toArray() {
				int length = this.size();
				Object[] product = new Object[length];

				for (int i = CharArray.this.beginIndex, j = 0;
					 i < CharArray.this.endIndex; i += 2, j++) {
					char k = CharArray.this.array[i];

					product[j] = k;
				}

				return product;
			}

			@Override
			public <T> T[] toArray(T[] array) {
				Objects.requireNonNull(array, "array");
				int length = this.size();
				T[] product = array;

				if (array.length < length)
					product = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), length);
				else if (array.length > length)
					product[length] = null;

				for (int i = CharArray.this.beginIndex, j = 0;
					 i < CharArray.this.endIndex; i += 2, j++) {
					char k = CharArray.this.array[i];

					product[j] = (T) (Character) k;
				}

				return product;
			}

			@Override
			public String toString() {
				if (this.isEmpty())
					return "[]";

				StringBuilder builder = new StringBuilder("[");

				int i = CharArray.this.beginIndex;
				while (true) {
					char k = CharArray.this.array[i];

					builder.append(k);

					i += 2;
					if (i >= CharArray.this.endIndex)
						return builder.append("]")
								.toString();

					builder.append(", ");
				}
			}

			/**
			 * An iterator iterating the keys in the enclosing array.
			 *
			 * @author LSafer
			 * @version 0.1.5
			 * @since 0.1.5 ~2020.08.03
			 */
			public class Iterator extends Array<char[], Character>.Map<Character, Character>.KeySet.Iterator {
				/**
				 * Construct a new iterator iterating the keys in the enclosing array.
				 *
				 * @since 0.1.5 ~2020.08.06
				 */
				public Iterator() {
				}

				/**
				 * Construct a new iterator iterating the keys in the enclosing array, starting from
				 * the given {@code index}.
				 *
				 * @param beginThumb the initial position of the constructed iterator.
				 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >
				 *                                   length}.
				 * @since 0.1.5 ~2020.08.06
				 */
				public Iterator(int beginThumb) {
					super(beginThumb);
				}

				@Override
				public void forEachRemaining(Consumer<? super Character> consumer) {
					Objects.requireNonNull(consumer, "consumer");
					int index = this.index;
					this.index = CharArray.this.endIndex;

					for (int i = index; i < CharArray.this.endIndex; i += 2) {
						char k = CharArray.this.array[i];

						consumer.accept(k);
					}
				}

				@Override
				public Character next() {
					int index = this.index;

					if (index < CharArray.this.endIndex) {
						this.index += 2;

						return CharArray.this.array[index];
					}

					throw new NoSuchElementException();
				}
			}

			/**
			 * A spliterator iterating the keys in the enclosing array.
			 *
			 * @author LSafer
			 * @version 0.1.5
			 * @since 0.1.5 ~2020.08.02
			 */
			public class Spliterator extends Array<char[], Character>.Map<Character, Character>.KeySet.Spliterator {
				/**
				 * Construct a new spliterator iterating the keys in the enclosing array.
				 *
				 * @since 0.1.5 ~2020.08.06
				 */
				public Spliterator() {
				}

				/**
				 * Construct a new spliterator iterating the keys in the enclosing array, starting
				 * from the given {@code index}.
				 *
				 * @param beginThumb the initial position of the constructed spliterator.
				 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >
				 *                                   length}.
				 * @since 0.1.5 ~2020.08.06
				 */
				public Spliterator(int beginThumb) {
					super(beginThumb);
				}

				@Override
				public void forEachRemaining(Consumer<? super Character> consumer) {
					Objects.requireNonNull(consumer, "consumer");
					int index = this.index;
					this.index = CharArray.this.endIndex;

					for (int i = index; i < CharArray.this.endIndex; i += 2) {
						char k = CharArray.this.array[i];

						consumer.accept(k);
					}
				}

				@Override
				public boolean tryAdvance(Consumer<? super Character> consumer) {
					Objects.requireNonNull(consumer, "consumer");
					int index = this.index;

					if (index < CharArray.this.endIndex) {
						this.index += 2;

						char k = CharArray.this.array[index];
						consumer.accept(k);
						return true;
					}

					return false;
				}
			}
		}

		/**
		 * A collection backed by the values in the enclosing array.
		 *
		 * @author LSafer
		 * @version 0.1.5
		 * @since 0.1.5 ~2020.08.03
		 */
		public class Values extends Array<char[], Character>.Map<Character, Character>.Values {
			@SuppressWarnings("JavaDoc")
			private static final long serialVersionUID = -7937502933699082438L;

			@Override
			public boolean equals(Object object) {
				return object == this;
			}

			@Override
			public void forEach(Consumer<? super Character> consumer) {
				Objects.requireNonNull(consumer, "consumer");
				for (int i = CharArray.this.beginIndex + 1;
					 i < CharArray.this.endIndex; i += 2) {
					char v = CharArray.this.array[i];

					consumer.accept(v);
				}
			}

			@Override
			public int hashCode() {
				int hashCode = 0;

				for (int i = CharArray.this.beginIndex + 1;
					 i < CharArray.this.endIndex; i += 2) {
					char v = CharArray.this.array[i];

					hashCode += Character.hashCode(v);
				}

				return hashCode;
			}

			@Override
			public Iterator iterator() {
				return new Iterator();
			}

			@Override
			public boolean removeIf(Predicate<? super Character> predicate) {
				Objects.requireNonNull(predicate, "predicate");

				for (int i = CharArray.this.beginIndex + 1;
					 i < CharArray.this.endIndex; i += 2) {
					char v = CharArray.this.array[i];

					if (predicate.test(v))
						//can not remove
						throw new UnsupportedOperationException("remove");
				}

				//nothing to remove
				return false;
			}

			@Override
			public Spliterator spliterator() {
				return new Spliterator();
			}

			@Override
			public Object[] toArray() {
				int length = CharArray.this.endIndex - CharArray.this.beginIndex >>> 1;
				Object[] product = new Object[length];

				for (int i = CharArray.this.beginIndex + 1, j = 0;
					 i < CharArray.this.endIndex; i += 2, j++) {
					char v = CharArray.this.array[i];

					product[j] = v;
				}

				return product;
			}

			@Override
			public <T> T[] toArray(T[] array) {
				Objects.requireNonNull(array, "array");
				int length = CharArray.this.endIndex - CharArray.this.beginIndex >>> 1;
				T[] product = array;

				if (array.length < length)
					product = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), length);
				else if (array.length > length)
					product[length] = null;

				for (int i = CharArray.this.beginIndex + 1, j = 0;
					 i < CharArray.this.endIndex; i += 2, j++) {
					char v = CharArray.this.array[i];

					product[j] = (T) (Character) v;
				}

				return product;
			}

			@Override
			public String toString() {
				if (this.isEmpty())
					return "[]";

				StringBuilder builder = new StringBuilder("[");

				int i = CharArray.this.beginIndex + 1;
				while (true) {
					char v = CharArray.this.array[i];

					builder.append(v);

					i += 2;
					if (i >= CharArray.this.endIndex)
						return builder.append("]")
								.toString();

					builder.append(", ");
				}
			}

			/**
			 * An iterator iterating the values in the enclosing array.
			 *
			 * @author LSafer
			 * @version 0.1.5
			 * @since 0.1.5 ~2020.08.03
			 */
			public class Iterator extends Array<char[], Character>.Map<Character, Character>.Values.Iterator {
				/**
				 * Construct a new iterator iterating the values in the enclosing array.
				 *
				 * @since 0.1.5 ~2020.08.06
				 */
				public Iterator() {
				}

				/**
				 * Construct a new iterator iterating the values in the enclosing array, starting
				 * from the given {@code index}.
				 *
				 * @param beginThumb the initial position of the constructed iterator.
				 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >
				 *                                   length}.
				 * @since 0.1.5 ~2020.08.06
				 */
				public Iterator(int beginThumb) {
					super(beginThumb);
				}

				@Override
				public void forEachRemaining(Consumer<? super Character> consumer) {
					Objects.requireNonNull(consumer, "consumer");
					int index = this.index;
					this.index = CharArray.this.endIndex;

					for (int i = index + 1; i < CharArray.this.endIndex; i += 2) {
						char v = CharArray.this.array[i];

						consumer.accept(v);
					}
				}

				@Override
				public Character next() {
					int index = this.index;

					if (index < CharArray.this.endIndex) {
						this.index += 2;

						return CharArray.this.array[index + 1];
					}

					throw new NoSuchElementException();
				}
			}

			/**
			 * A spliterator iterating the values in the enclosing array.
			 *
			 * @author LSafer
			 * @version 0.1.5
			 * @since 0.1.5 ~2020.08.02
			 */
			public class Spliterator extends Array<char[], Character>.Map<Character, Character>.Values.Spliterator {
				/**
				 * Construct a new spliterator iterating the values in the enclosing array.
				 *
				 * @since 0.1.5 ~2020.08.06
				 */
				public Spliterator() {
				}

				/**
				 * Construct a new spliterator iterating the values in the enclosing array, starting
				 * from the given {@code index}.
				 *
				 * @param beginThumb the initial position of the constructed spliterator.
				 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >
				 *                                   length}.
				 * @since 0.1.5 ~2020.08.06
				 */
				public Spliterator(int beginThumb) {
					super(beginThumb);
				}

				@Override
				public void forEachRemaining(Consumer<? super Character> consumer) {
					Objects.requireNonNull(consumer, "consumer");
					int index = this.index;
					this.index = CharArray.this.endIndex;

					for (int i = index + 1; i < CharArray.this.endIndex; i += 2) {
						char v = CharArray.this.array[i];

						consumer.accept(v);
					}
				}

				@Override
				public boolean tryAdvance(Consumer<? super Character> consumer) {
					Objects.requireNonNull(consumer, "consumer");
					int index = this.index;

					if (index < CharArray.this.endIndex) {
						this.index += 2;

						char v = CharArray.this.array[index + 1];
						consumer.accept(v);
						return true;
					}

					return false;
				}
			}
		}
	}

	/**
	 * A spliterator iterating the elements in the enclosing array.
	 *
	 * @author LSafer
	 * @version 0.1.5
	 * @since 0.1.5 ~2020.08.02
	 */
	public class Spliterator extends Array<char[], Character>.Spliterator {
		/**
		 * Construct a new spliterator iterating the elements in the enclosing array, starting from
		 * the given {@code index}.
		 *
		 * @since 0.1.5 ~2020.08.06
		 */
		public Spliterator() {
		}

		/**
		 * Construct a new spliterator iterating the elements in the enclosing array, starting from
		 * the given {@code index}.
		 *
		 * @param beginThumb the initial position of the constructed spliterator.
		 * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index > length}.
		 * @since 0.1.5 ~2020.08.06
		 */
		public Spliterator(int beginThumb) {
			super(beginThumb);
		}

		@Override
		public void forEachRemaining(Consumer<? super Character> consumer) {
			Objects.requireNonNull(consumer, "consumer");
			int index = this.index;
			this.index = CharArray.this.endIndex;

			for (int i = index; i < CharArray.this.endIndex; i++) {
				char e = CharArray.this.array[i];

				consumer.accept(e);
			}
		}

		@Override
		public boolean tryAdvance(Consumer<? super Character> consumer) {
			Objects.requireNonNull(consumer, "consumer");
			int index = this.index;

			if (index < CharArray.this.endIndex) {
				this.index += 2;
				char e = CharArray.this.array[index];

				consumer.accept(e);
				return true;
			}

			return false;
		}
	}
}