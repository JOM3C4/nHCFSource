/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.Unmodifiable;

/**
 * Decorates an ordered map iterator such that it cannot be modified.
 * <p>
 * Attempts to modify it will result in an UnsupportedOperationException.
 *
 * @since 3.0
 * @version $Id: UnmodifiableOrderedMapIterator.java 1686855 2015-06-22
 *          13:00:27Z tn $
 */
public final class UnmodifiableOrderedMapIterator<K, V> implements OrderedMapIterator<K, V>, Unmodifiable {

	/** The iterator being decorated */
	private final OrderedMapIterator<? extends K, ? extends V> iterator;

	// -----------------------------------------------------------------------
	/**
	 * Decorates the specified iterator such that it cannot be modified.
	 *
	 * @param <K>      the key type
	 * @param <V>      the value type
	 * @param iterator the iterator to decorate
	 * @return a new unmodifiable ordered map iterator
	 * @throws NullPointerException if the iterator is null
	 */
	public static <K, V> OrderedMapIterator<K, V> unmodifiableOrderedMapIterator(
			final OrderedMapIterator<K, ? extends V> iterator) {

		if (iterator == null) {
			throw new NullPointerException("OrderedMapIterator must not be null");
		}
		if (iterator instanceof Unmodifiable) {
			@SuppressWarnings("unchecked") // safe to upcast
			final OrderedMapIterator<K, V> tmpIterator = (OrderedMapIterator<K, V>) iterator;
			return tmpIterator;
		}
		return new UnmodifiableOrderedMapIterator<K, V>(iterator);
	}

	// -----------------------------------------------------------------------
	/**
	 * Constructor.
	 *
	 * @param iterator the iterator to decorate
	 */
	private UnmodifiableOrderedMapIterator(final OrderedMapIterator<K, ? extends V> iterator) {
		super();
		this.iterator = iterator;
	}

	// -----------------------------------------------------------------------
	public boolean hasNext() {
		return iterator.hasNext();
	}

	public K next() {
		return iterator.next();
	}

	public boolean hasPrevious() {
		return iterator.hasPrevious();
	}

	public K previous() {
		return iterator.previous();
	}

	public K getKey() {
		return iterator.getKey();
	}

	public V getValue() {
		return iterator.getValue();
	}

	public V setValue(final V value) {
		throw new UnsupportedOperationException("setValue() is not supported");
	}

	public void remove() {
		throw new UnsupportedOperationException("remove() is not supported");
	}

}
