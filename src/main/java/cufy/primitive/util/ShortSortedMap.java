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
package cufy.primitive.util;

import cufy.primitive.util.function.ShortBiConsumer;
import cufy.primitive.util.function.ShortBinaryOperator;
import cufy.util.PrimitiveSortedMap;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A sorted map specialized for {@code short} values.
 *
 * @param <ENTRY_SET> the type of the set of the entries.
 * @author LSafer
 * @version 0.1.5
 * @since 0.1.5 ~2020.09.03
 */
public interface ShortSortedMap<
		ENTRY_SET extends Set<Map.Entry<Short, Short>>
		> extends PrimitiveSortedMap<
		Short,
		Short,
		ShortBiConsumer,
		ShortBinaryOperator,
		ShortComparator,
		ENTRY_SET,
		ShortSet,
		ShortCollection,
		ShortMap<ENTRY_SET>,
		ShortSortedMap<ENTRY_SET>
		>, ShortMap<ENTRY_SET> {
	@Override
	default Short firstKey() {
		return this.firstShortKey();
	}

	@Override
	default ShortSortedMap<ENTRY_SET> headMap(Short endKey) {
		return this.headMap((short) endKey);
	}

	@Override
	default Short lastKey() {
		return this.lastShortKey();
	}

	@Override
	default ShortSortedMap<ENTRY_SET> subMap(Short beginKey, Short endKey) {
		return this.subMap((short) beginKey, (short) endKey);
	}

	@Override
	default ShortSortedMap<ENTRY_SET> tailMap(Short beginKey) {
		return this.tailMap((short) beginKey);
	}

	/**
	 * Returns the first (lowest) key currently in this map.
	 *
	 * @return the first (lowest) key currently in this map.
	 * @throws NoSuchElementException if this map is empty.
	 * @since 0.1.5 ~2020.09.03
	 */
	short firstShortKey();

	/**
	 * Returns a view of the portion of this map whose keys are strictly less than {@code endKey}.
	 * The returned map is backed by this map, so changes in the returned map are reflected in this
	 * map, and vice-versa. The returned map supports all optional map operations that this map
	 * supports.
	 * <p>
	 * The returned map will throw an {@code IllegalArgumentException} on an attempt to insert a key
	 * outside its range.
	 *
	 * @param endKey high endpoint (exclusive) of the keys in the returned map.
	 * @return a view of the portion of this map whose keys are strictly less than {@code endKey}.
	 * @throws IllegalArgumentException if this map itself has a restricted range, and {@code
	 *                                  endKey} lies outside the bounds of the range.
	 * @since 0.1.5 ~2020.09.03
	 */
	ShortSortedMap<ENTRY_SET> headMap(short endKey);

	/**
	 * Returns the last (highest) key currently in this map.
	 *
	 * @return the last (highest) key currently in this map
	 * @throws NoSuchElementException if this map is empty.
	 * @since 0.1.5 ~2020.09.03
	 */
	short lastShortKey();

	/**
	 * Returns a view of the portion of this map whose keys range from {@code beginKey}, inclusive,
	 * to {@code endKey}, exclusive. (If {@code beginKey} and {@code endKey} are equal, the returned
	 * map is empty.) The returned map is backed by this map, so changes in the returned map are
	 * reflected in this map, and vice-versa. The returned map supports all optional map operations
	 * that this map supports.
	 * <p>
	 * The returned map will throw an {@code IllegalArgumentException} on an attempt to insert a key
	 * outside its range.
	 *
	 * @param beginKey low endpoint (inclusive) of the keys in the returned map.
	 * @param endKey   high endpoint (exclusive) of the keys in the returned map.
	 * @return a view of the portion of this map whose keys range from {@code beginKey}, inclusive,
	 * 		to {@code endKey}, exclusive.
	 * @throws IllegalArgumentException if {@code beginKey} is greater than {@code endKey}; or if
	 *                                  this map itself has a restricted range, and {@code beginKey}
	 *                                  or {@code endKey} lies outside the bounds of the range
	 * @since 0.1.5 ~2020.09.03
	 */
	ShortSortedMap<ENTRY_SET> subMap(short beginKey, short endKey);

	/**
	 * Returns a view of the portion of this map whose keys are greater than or equal to {@code
	 * beginKey}. The returned map is backed by this map, so changes in the returned map are
	 * reflected in this map, and vice-versa. The returned map supports all optional map operations
	 * that this map supports.
	 * <p>
	 * The returned map will throw an {@code IllegalArgumentException} on an attempt to insert a key
	 * outside its range.
	 *
	 * @param beginKey low endpoint (inclusive) of the keys in the returned map
	 * @return a view of the portion of this map whose keys are greater than or equal to {@code
	 * 		beginKey}.
	 * @throws IllegalArgumentException if this map itself has a restricted range, and {@code
	 *                                  beginKey} lies outside the bounds of the range.
	 * @since 0.1.5 ~2020.09.03
	 */
	ShortSortedMap<ENTRY_SET> tailMap(short beginKey);
}
