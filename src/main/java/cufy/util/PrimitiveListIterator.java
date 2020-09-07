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

import java.util.ListIterator;
import java.util.PrimitiveIterator;

/**
 * A base type for primitive specialization of {@code ListIterator}..
 *
 * @param <E>        the type of the elements.
 * @param <CONSUMER> the type of a consumer consuming the elements.
 * @author LSafer
 * @version 0.1.5
 * @since 0.1.5 ~2020.08.31
 */
public interface PrimitiveListIterator<
		E,
		CONSUMER
		> extends PrimitiveIterator<E, CONSUMER>, ListIterator<E> {
}