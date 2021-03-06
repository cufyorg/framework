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
package cufy.util;

import java.util.SortedSet;
import java.util.Spliterator;

/**
 * A sorted set specialized for primitive values.
 *
 * @param <E> the type of the elements.
 * @param <C> the type of the consumer.
 * @param <P> the type of the predicate.
 * @author LSafer
 * @version 0.1.5
 * @since 0.1.5 ~2020.09.01
 */
public interface PrimitiveSortedSet
		<E, C, P>
		extends
		PrimitiveSet<E, C, P>,
		SortedSet<E> {
	@Override
	PrimitiveComparator<E, ?, ?, ?, ?, ?> comparator();

	@Override
	PrimitiveSortedSet<E, C, P> headSet(E endElement);

	@Override
	Spliterator.OfPrimitive<E, C, ?> spliterator();

	@Override
	PrimitiveSortedSet<E, C, P> subSet(E beginElement, E endElement);

	@Override
	PrimitiveSortedSet<E, C, P> tailSet(E beginElement);

	//primitive firstChar()
	//PrimitiveSortedSet headSet(primitive endElement)
	//primitive lastChar()
	//PrimitiveSortedSet subSet(primitive beginElement, endElement)
	//PrimitiveSortedSet tailSet(primitive beginElement)
}
