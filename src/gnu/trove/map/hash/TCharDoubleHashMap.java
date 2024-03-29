///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001, Eric D. Friedman All Rights Reserved.
// Copyright (c) 2009, Rob Eden All Rights Reserved.
// Copyright (c) 2009, Jeff Randall All Rights Reserved.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
///////////////////////////////////////////////////////////////////////////////

package gnu.trove.map.hash;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;

import gnu.trove.TCharCollection;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCharDoubleHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TCharDoubleIterator;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TDoubleIterator;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

import gnu.trove.map.TCharDoubleMap;
import gnu.trove.procedure.TCharDoubleProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TCharSet;

/**
 * An open addressed Map implementation for char keys and double values.
 *
 * @author Eric D. Friedman
 * @author Rob Eden
 * @author Jeff Randall
 * @version $Id: _K__V_HashMap.template,v 1.1.2.16 2010/03/02 04:09:50 robeden
 *          Exp $
 */
public class TCharDoubleHashMap extends TCharDoubleHash implements TCharDoubleMap, Externalizable {
	static final long serialVersionUID = 1L;

	/** the values of the map */
	protected transient double[] _values;

	/**
	 * Creates a new <code>TCharDoubleHashMap</code> instance with the default
	 * capacity and load factor.
	 */
	public TCharDoubleHashMap() {
		super();
	}

	/**
	 * Creates a new <code>TCharDoubleHashMap</code> instance with a prime capacity
	 * equal to or greater than <tt>initialCapacity</tt> and with the default load
	 * factor.
	 *
	 * @param initialCapacity an <code>int</code> value
	 */
	public TCharDoubleHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Creates a new <code>TCharDoubleHashMap</code> instance with a prime capacity
	 * equal to or greater than <tt>initialCapacity</tt> and with the specified load
	 * factor.
	 *
	 * @param initialCapacity an <code>int</code> value
	 * @param loadFactor      a <code>float</code> value
	 */
	public TCharDoubleHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * Creates a new <code>TCharDoubleHashMap</code> instance with a prime capacity
	 * equal to or greater than <tt>initialCapacity</tt> and with the specified load
	 * factor.
	 *
	 * @param initialCapacity an <code>int</code> value
	 * @param loadFactor      a <code>float</code> value
	 * @param noEntryKey      a <code>char</code> value that represents
	 *                        <tt>null</tt> for the Key set.
	 * @param noEntryValue    a <code>double</code> value that represents
	 *                        <tt>null</tt> for the Value set.
	 */
	public TCharDoubleHashMap(int initialCapacity, float loadFactor, char noEntryKey, double noEntryValue) {
		super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
	}

	/**
	 * Creates a new <code>TCharDoubleHashMap</code> instance containing all of the
	 * entries in the map passed in.
	 *
	 * @param keys   a <tt>char</tt> array containing the keys for the matching
	 *               values.
	 * @param values a <tt>double</tt> array containing the values.
	 */
	public TCharDoubleHashMap(char[] keys, double[] values) {
		super(Math.max(keys.length, values.length));

		int size = Math.min(keys.length, values.length);
		for (int i = 0; i < size; i++) {
			this.put(keys[i], values[i]);
		}
	}

	/**
	 * Creates a new <code>TCharDoubleHashMap</code> instance containing all of the
	 * entries in the map passed in.
	 *
	 * @param map a <tt>TCharDoubleMap</tt> that will be duplicated.
	 */
	public TCharDoubleHashMap(TCharDoubleMap map) {
		super(map.size());
		if (map instanceof TCharDoubleHashMap) {
			TCharDoubleHashMap hashmap = (TCharDoubleHashMap) map;
			this._loadFactor = hashmap._loadFactor;
			this.no_entry_key = hashmap.no_entry_key;
			this.no_entry_value = hashmap.no_entry_value;
			// noinspection RedundantCast
			if (this.no_entry_key != (char) 0) {
				Arrays.fill(_set, this.no_entry_key);
			}
			// noinspection RedundantCast
			if (this.no_entry_value != (double) 0) {
				Arrays.fill(_values, this.no_entry_value);
			}
			setUp((int) Math.ceil(DEFAULT_CAPACITY / _loadFactor));
		}
		putAll(map);
	}

	/**
	 * initializes the hashtable to a prime capacity which is at least
	 * <tt>initialCapacity + 1</tt>.
	 *
	 * @param initialCapacity an <code>int</code> value
	 * @return the actual capacity chosen
	 */
	protected int setUp(int initialCapacity) {
		int capacity;

		capacity = super.setUp(initialCapacity);
		_values = new double[capacity];
		return capacity;
	}

	/**
	 * rehashes the map to the new capacity.
	 *
	 * @param newCapacity an <code>int</code> value
	 */
	/** {@inheritDoc} */
	protected void rehash(int newCapacity) {
		int oldCapacity = _set.length;

		char oldKeys[] = _set;
		double oldVals[] = _values;
		byte oldStates[] = _states;

		_set = new char[newCapacity];
		_values = new double[newCapacity];
		_states = new byte[newCapacity];

		for (int i = oldCapacity; i-- > 0;) {
			if (oldStates[i] == FULL) {
				char o = oldKeys[i];
				int index = insertKey(o);
				_values[index] = oldVals[i];
			}
		}
	}

	/** {@inheritDoc} */
	public double put(char key, double value) {
		int index = insertKey(key);
		return doPut(key, value, index);
	}

	/** {@inheritDoc} */
	public double putIfAbsent(char key, double value) {
		int index = insertKey(key);
		if (index < 0)
			return _values[-index - 1];
		return doPut(key, value, index);
	}

	private double doPut(char key, double value, int index) {
		double previous = no_entry_value;
		boolean isNewMapping = true;
		if (index < 0) {
			index = -index - 1;
			previous = _values[index];
			isNewMapping = false;
		}
		_values[index] = value;

		if (isNewMapping) {
			postInsertHook(consumeFreeSlot);
		}

		return previous;
	}

	/** {@inheritDoc} */
	public void putAll(Map<? extends Character, ? extends Double> map) {
		ensureCapacity(map.size());
		// could optimize this for cases when map instanceof THashMap
		for (Map.Entry<? extends Character, ? extends Double> entry : map.entrySet()) {
			this.put(entry.getKey().charValue(), entry.getValue().doubleValue());
		}
	}

	/** {@inheritDoc} */
	public void putAll(TCharDoubleMap map) {
		ensureCapacity(map.size());
		TCharDoubleIterator iter = map.iterator();
		while (iter.hasNext()) {
			iter.advance();
			this.put(iter.key(), iter.value());
		}
	}

	/** {@inheritDoc} */
	public double get(char key) {
		int index = index(key);
		return index < 0 ? no_entry_value : _values[index];
	}

	/** {@inheritDoc} */
	public void clear() {
		super.clear();
		Arrays.fill(_set, 0, _set.length, no_entry_key);
		Arrays.fill(_values, 0, _values.length, no_entry_value);
		Arrays.fill(_states, 0, _states.length, FREE);
	}

	/** {@inheritDoc} */
	public boolean isEmpty() {
		return 0 == _size;
	}

	/** {@inheritDoc} */
	public double remove(char key) {
		double prev = no_entry_value;
		int index = index(key);
		if (index >= 0) {
			prev = _values[index];
			removeAt(index); // clear key,state; adjust size
		}
		return prev;
	}

	/** {@inheritDoc} */
	protected void removeAt(int index) {
		_values[index] = no_entry_value;
		super.removeAt(index); // clear key, state; adjust size
	}

	/** {@inheritDoc} */
	public TCharSet keySet() {
		return new TKeyView();
	}

	/** {@inheritDoc} */
	public char[] keys() {
		char[] keys = new char[size()];
		if (keys.length == 0) {
			return keys; // nothing to copy
		}
		char[] k = _set;
		byte[] states = _states;

		for (int i = k.length, j = 0; i-- > 0;) {
			if (states[i] == FULL) {
				keys[j++] = k[i];
			}
		}
		return keys;
	}

	/** {@inheritDoc} */
	public char[] keys(char[] array) {
		int size = size();
		if (size == 0) {
			return array; // nothing to copy
		}
		if (array.length < size) {
			array = new char[size];
		}

		char[] keys = _set;
		byte[] states = _states;

		for (int i = keys.length, j = 0; i-- > 0;) {
			if (states[i] == FULL) {
				array[j++] = keys[i];
			}
		}
		return array;
	}

	/** {@inheritDoc} */
	public TDoubleCollection valueCollection() {
		return new TValueView();
	}

	/** {@inheritDoc} */
	public double[] values() {
		double[] vals = new double[size()];
		if (vals.length == 0) {
			return vals; // nothing to copy
		}
		double[] v = _values;
		byte[] states = _states;

		for (int i = v.length, j = 0; i-- > 0;) {
			if (states[i] == FULL) {
				vals[j++] = v[i];
			}
		}
		return vals;
	}

	/** {@inheritDoc} */
	public double[] values(double[] array) {
		int size = size();
		if (size == 0) {
			return array; // nothing to copy
		}
		if (array.length < size) {
			array = new double[size];
		}

		double[] v = _values;
		byte[] states = _states;

		for (int i = v.length, j = 0; i-- > 0;) {
			if (states[i] == FULL) {
				array[j++] = v[i];
			}
		}
		return array;
	}

	/** {@inheritDoc} */
	public boolean containsValue(double val) {
		byte[] states = _states;
		double[] vals = _values;

		for (int i = vals.length; i-- > 0;) {
			if (states[i] == FULL && val == vals[i]) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	public boolean containsKey(char key) {
		return contains(key);
	}

	/** {@inheritDoc} */
	public TCharDoubleIterator iterator() {
		return new TCharDoubleHashIterator(this);
	}

	/** {@inheritDoc} */
	public boolean forEachKey(TCharProcedure procedure) {
		return forEach(procedure);
	}

	/** {@inheritDoc} */
	public boolean forEachValue(TDoubleProcedure procedure) {
		byte[] states = _states;
		double[] values = _values;
		for (int i = values.length; i-- > 0;) {
			if (states[i] == FULL && !procedure.execute(values[i])) {
				return false;
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	public boolean forEachEntry(TCharDoubleProcedure procedure) {
		byte[] states = _states;
		char[] keys = _set;
		double[] values = _values;
		for (int i = keys.length; i-- > 0;) {
			if (states[i] == FULL && !procedure.execute(keys[i], values[i])) {
				return false;
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	public void transformValues(TDoubleFunction function) {
		byte[] states = _states;
		double[] values = _values;
		for (int i = values.length; i-- > 0;) {
			if (states[i] == FULL) {
				values[i] = function.execute(values[i]);
			}
		}
	}

	/** {@inheritDoc} */
	public boolean retainEntries(TCharDoubleProcedure procedure) {
		boolean modified = false;
		byte[] states = _states;
		char[] keys = _set;
		double[] values = _values;

		// Temporarily disable compaction. This is a fix for bug #1738760
		tempDisableAutoCompaction();
		try {
			for (int i = keys.length; i-- > 0;) {
				if (states[i] == FULL && !procedure.execute(keys[i], values[i])) {
					removeAt(i);
					modified = true;
				}
			}
		} finally {
			reenableAutoCompaction(true);
		}

		return modified;
	}

	/** {@inheritDoc} */
	public boolean increment(char key) {
		return adjustValue(key, (double) 1);
	}

	/** {@inheritDoc} */
	public boolean adjustValue(char key, double amount) {
		int index = index(key);
		if (index < 0) {
			return false;
		} else {
			_values[index] += amount;
			return true;
		}
	}

	/** {@inheritDoc} */
	public double adjustOrPutValue(char key, double adjust_amount, double put_amount) {
		int index = insertKey(key);
		final boolean isNewMapping;
		final double newValue;
		if (index < 0) {
			index = -index - 1;
			newValue = (_values[index] += adjust_amount);
			isNewMapping = false;
		} else {
			newValue = (_values[index] = put_amount);
			isNewMapping = true;
		}

		if (isNewMapping) {
			postInsertHook(consumeFreeSlot);
		}

		return newValue;
	}

	/** a view onto the keys of the map. */
	protected class TKeyView implements TCharSet {

		/** {@inheritDoc} */
		public TCharIterator iterator() {
			return new TCharDoubleKeyHashIterator(TCharDoubleHashMap.this);
		}

		/** {@inheritDoc} */
		public char getNoEntryValue() {
			return no_entry_key;
		}

		/** {@inheritDoc} */
		public int size() {
			return _size;
		}

		/** {@inheritDoc} */
		public boolean isEmpty() {
			return 0 == _size;
		}

		/** {@inheritDoc} */
		public boolean contains(char entry) {
			return TCharDoubleHashMap.this.contains(entry);
		}

		/** {@inheritDoc} */
		public char[] toArray() {
			return TCharDoubleHashMap.this.keys();
		}

		/** {@inheritDoc} */
		public char[] toArray(char[] dest) {
			return TCharDoubleHashMap.this.keys(dest);
		}

		/**
		 * Unsupported when operating upon a Key Set view of a TCharDoubleMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean add(char entry) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		public boolean remove(char entry) {
			return no_entry_value != TCharDoubleHashMap.this.remove(entry);
		}

		/** {@inheritDoc} */
		public boolean containsAll(Collection<?> collection) {
			for (Object element : collection) {
				if (element instanceof Character) {
					char ele = ((Character) element).charValue();
					if (!TCharDoubleHashMap.this.containsKey(ele)) {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}

		/** {@inheritDoc} */
		public boolean containsAll(TCharCollection collection) {
			TCharIterator iter = collection.iterator();
			while (iter.hasNext()) {
				if (!TCharDoubleHashMap.this.containsKey(iter.next())) {
					return false;
				}
			}
			return true;
		}

		/** {@inheritDoc} */
		public boolean containsAll(char[] array) {
			for (char element : array) {
				if (!TCharDoubleHashMap.this.contains(element)) {
					return false;
				}
			}
			return true;
		}

		/**
		 * Unsupported when operating upon a Key Set view of a TCharDoubleMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean addAll(Collection<? extends Character> collection) {
			throw new UnsupportedOperationException();
		}

		/**
		 * Unsupported when operating upon a Key Set view of a TCharDoubleMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean addAll(TCharCollection collection) {
			throw new UnsupportedOperationException();
		}

		/**
		 * Unsupported when operating upon a Key Set view of a TCharDoubleMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean addAll(char[] array) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		@SuppressWarnings({})
		public boolean retainAll(Collection<?> collection) {
			boolean modified = false;
			TCharIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(Character.valueOf(iter.next()))) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}

		/** {@inheritDoc} */
		public boolean retainAll(TCharCollection collection) {
			if (this == collection) {
				return false;
			}
			boolean modified = false;
			TCharIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(iter.next())) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}

		/** {@inheritDoc} */
		public boolean retainAll(char[] array) {
			boolean changed = false;
			Arrays.sort(array);
			char[] set = _set;
			byte[] states = _states;

			for (int i = set.length; i-- > 0;) {
				if (states[i] == FULL && (Arrays.binarySearch(array, set[i]) < 0)) {
					removeAt(i);
					changed = true;
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public boolean removeAll(Collection<?> collection) {
			boolean changed = false;
			for (Object element : collection) {
				if (element instanceof Character) {
					char c = ((Character) element).charValue();
					if (remove(c)) {
						changed = true;
					}
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public boolean removeAll(TCharCollection collection) {
			if (this == collection) {
				clear();
				return true;
			}
			boolean changed = false;
			TCharIterator iter = collection.iterator();
			while (iter.hasNext()) {
				char element = iter.next();
				if (remove(element)) {
					changed = true;
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public boolean removeAll(char[] array) {
			boolean changed = false;
			for (int i = array.length; i-- > 0;) {
				if (remove(array[i])) {
					changed = true;
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public void clear() {
			TCharDoubleHashMap.this.clear();
		}

		/** {@inheritDoc} */
		public boolean forEach(TCharProcedure procedure) {
			return TCharDoubleHashMap.this.forEachKey(procedure);
		}

		@Override
		public boolean equals(Object other) {
			if (!(other instanceof TCharSet)) {
				return false;
			}
			final TCharSet that = (TCharSet) other;
			if (that.size() != this.size()) {
				return false;
			}
			for (int i = _states.length; i-- > 0;) {
				if (_states[i] == FULL) {
					if (!that.contains(_set[i])) {
						return false;
					}
				}
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hashcode = 0;
			for (int i = _states.length; i-- > 0;) {
				if (_states[i] == FULL) {
					hashcode += HashFunctions.hash(_set[i]);
				}
			}
			return hashcode;
		}

		@Override
		public String toString() {
			final StringBuilder buf = new StringBuilder("{");
			forEachKey(new TCharProcedure() {
				private boolean first = true;

				public boolean execute(char key) {
					if (first) {
						first = false;
					} else {
						buf.append(", ");
					}

					buf.append(key);
					return true;
				}
			});
			buf.append("}");
			return buf.toString();
		}
	}

	/** a view onto the values of the map. */
	protected class TValueView implements TDoubleCollection {

		/** {@inheritDoc} */
		public TDoubleIterator iterator() {
			return new TCharDoubleValueHashIterator(TCharDoubleHashMap.this);
		}

		/** {@inheritDoc} */
		public double getNoEntryValue() {
			return no_entry_value;
		}

		/** {@inheritDoc} */
		public int size() {
			return _size;
		}

		/** {@inheritDoc} */
		public boolean isEmpty() {
			return 0 == _size;
		}

		/** {@inheritDoc} */
		public boolean contains(double entry) {
			return TCharDoubleHashMap.this.containsValue(entry);
		}

		/** {@inheritDoc} */
		public double[] toArray() {
			return TCharDoubleHashMap.this.values();
		}

		/** {@inheritDoc} */
		public double[] toArray(double[] dest) {
			return TCharDoubleHashMap.this.values(dest);
		}

		public boolean add(double entry) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		public boolean remove(double entry) {
			double[] values = _values;
			char[] set = _set;

			for (int i = values.length; i-- > 0;) {
				if ((set[i] != FREE && set[i] != REMOVED) && entry == values[i]) {
					removeAt(i);
					return true;
				}
			}
			return false;
		}

		/** {@inheritDoc} */
		public boolean containsAll(Collection<?> collection) {
			for (Object element : collection) {
				if (element instanceof Double) {
					double ele = ((Double) element).doubleValue();
					if (!TCharDoubleHashMap.this.containsValue(ele)) {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}

		/** {@inheritDoc} */
		public boolean containsAll(TDoubleCollection collection) {
			TDoubleIterator iter = collection.iterator();
			while (iter.hasNext()) {
				if (!TCharDoubleHashMap.this.containsValue(iter.next())) {
					return false;
				}
			}
			return true;
		}

		/** {@inheritDoc} */
		public boolean containsAll(double[] array) {
			for (double element : array) {
				if (!TCharDoubleHashMap.this.containsValue(element)) {
					return false;
				}
			}
			return true;
		}

		/** {@inheritDoc} */
		public boolean addAll(Collection<? extends Double> collection) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		public boolean addAll(TDoubleCollection collection) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		public boolean addAll(double[] array) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		@SuppressWarnings({})
		public boolean retainAll(Collection<?> collection) {
			boolean modified = false;
			TDoubleIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(Double.valueOf(iter.next()))) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}

		/** {@inheritDoc} */
		public boolean retainAll(TDoubleCollection collection) {
			if (this == collection) {
				return false;
			}
			boolean modified = false;
			TDoubleIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(iter.next())) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}

		/** {@inheritDoc} */
		public boolean retainAll(double[] array) {
			boolean changed = false;
			Arrays.sort(array);
			double[] values = _values;
			byte[] states = _states;

			for (int i = values.length; i-- > 0;) {
				if (states[i] == FULL && (Arrays.binarySearch(array, values[i]) < 0)) {
					removeAt(i);
					changed = true;
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public boolean removeAll(Collection<?> collection) {
			boolean changed = false;
			for (Object element : collection) {
				if (element instanceof Double) {
					double c = ((Double) element).doubleValue();
					if (remove(c)) {
						changed = true;
					}
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public boolean removeAll(TDoubleCollection collection) {
			if (this == collection) {
				clear();
				return true;
			}
			boolean changed = false;
			TDoubleIterator iter = collection.iterator();
			while (iter.hasNext()) {
				double element = iter.next();
				if (remove(element)) {
					changed = true;
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public boolean removeAll(double[] array) {
			boolean changed = false;
			for (int i = array.length; i-- > 0;) {
				if (remove(array[i])) {
					changed = true;
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public void clear() {
			TCharDoubleHashMap.this.clear();
		}

		/** {@inheritDoc} */
		public boolean forEach(TDoubleProcedure procedure) {
			return TCharDoubleHashMap.this.forEachValue(procedure);
		}

		/** {@inheritDoc} */
		@Override
		public String toString() {
			final StringBuilder buf = new StringBuilder("{");
			forEachValue(new TDoubleProcedure() {
				private boolean first = true;

				public boolean execute(double value) {
					if (first) {
						first = false;
					} else {
						buf.append(", ");
					}

					buf.append(value);
					return true;
				}
			});
			buf.append("}");
			return buf.toString();
		}
	}

	class TCharDoubleKeyHashIterator extends THashPrimitiveIterator implements TCharIterator {

		/**
		 * Creates an iterator over the specified map
		 *
		 * @param hash the <tt>TPrimitiveHash</tt> we will be iterating over.
		 */
		TCharDoubleKeyHashIterator(TPrimitiveHash hash) {
			super(hash);
		}

		/** {@inheritDoc} */
		public char next() {
			moveToNextIndex();
			return _set[_index];
		}

		/** @{inheritDoc} */
		public void remove() {
			if (_expectedSize != _hash.size()) {
				throw new ConcurrentModificationException();
			}

			// Disable auto compaction during the remove. This is a workaround for bug
			// 1642768.
			try {
				_hash.tempDisableAutoCompaction();
				TCharDoubleHashMap.this.removeAt(_index);
			} finally {
				_hash.reenableAutoCompaction(false);
			}

			_expectedSize--;
		}
	}

	class TCharDoubleValueHashIterator extends THashPrimitiveIterator implements TDoubleIterator {

		/**
		 * Creates an iterator over the specified map
		 *
		 * @param hash the <tt>TPrimitiveHash</tt> we will be iterating over.
		 */
		TCharDoubleValueHashIterator(TPrimitiveHash hash) {
			super(hash);
		}

		/** {@inheritDoc} */
		public double next() {
			moveToNextIndex();
			return _values[_index];
		}

		/** @{inheritDoc} */
		public void remove() {
			if (_expectedSize != _hash.size()) {
				throw new ConcurrentModificationException();
			}

			// Disable auto compaction during the remove. This is a workaround for bug
			// 1642768.
			try {
				_hash.tempDisableAutoCompaction();
				TCharDoubleHashMap.this.removeAt(_index);
			} finally {
				_hash.reenableAutoCompaction(false);
			}

			_expectedSize--;
		}
	}

	class TCharDoubleHashIterator extends THashPrimitiveIterator implements TCharDoubleIterator {

		/**
		 * Creates an iterator over the specified map
		 *
		 * @param map the <tt>TCharDoubleHashMap</tt> we will be iterating over.
		 */
		TCharDoubleHashIterator(TCharDoubleHashMap map) {
			super(map);
		}

		/** {@inheritDoc} */
		public void advance() {
			moveToNextIndex();
		}

		/** {@inheritDoc} */
		public char key() {
			return _set[_index];
		}

		/** {@inheritDoc} */
		public double value() {
			return _values[_index];
		}

		/** {@inheritDoc} */
		public double setValue(double val) {
			double old = value();
			_values[_index] = val;
			return old;
		}

		/** @{inheritDoc} */
		public void remove() {
			if (_expectedSize != _hash.size()) {
				throw new ConcurrentModificationException();
			}
			// Disable auto compaction during the remove. This is a workaround for bug
			// 1642768.
			try {
				_hash.tempDisableAutoCompaction();
				TCharDoubleHashMap.this.removeAt(_index);
			} finally {
				_hash.reenableAutoCompaction(false);
			}
			_expectedSize--;
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TCharDoubleMap)) {
			return false;
		}
		TCharDoubleMap that = (TCharDoubleMap) other;
		if (that.size() != this.size()) {
			return false;
		}
		double[] values = _values;
		byte[] states = _states;
		double this_no_entry_value = getNoEntryValue();
		double that_no_entry_value = that.getNoEntryValue();
		for (int i = values.length; i-- > 0;) {
			if (states[i] == FULL) {
				char key = _set[i];
				double that_value = that.get(key);
				double this_value = values[i];
				if ((this_value != that_value) && (this_value != this_no_entry_value)
						&& (that_value != that_no_entry_value)) {
					return false;
				}
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		int hashcode = 0;
		byte[] states = _states;
		for (int i = _values.length; i-- > 0;) {
			if (states[i] == FULL) {
				hashcode += HashFunctions.hash(_set[i]) ^ HashFunctions.hash(_values[i]);
			}
		}
		return hashcode;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder("{");
		forEachEntry(new TCharDoubleProcedure() {
			private boolean first = true;

			public boolean execute(char key, double value) {
				if (first)
					first = false;
				else
					buf.append(", ");

				buf.append(key);
				buf.append("=");
				buf.append(value);
				return true;
			}
		});
		buf.append("}");
		return buf.toString();
	}

	/** {@inheritDoc} */
	public void writeExternal(ObjectOutput out) throws IOException {
		// VERSION
		out.writeByte(0);

		// SUPER
		super.writeExternal(out);

		// NUMBER OF ENTRIES
		out.writeInt(_size);

		// ENTRIES
		for (int i = _states.length; i-- > 0;) {
			if (_states[i] == FULL) {
				out.writeChar(_set[i]);
				out.writeDouble(_values[i]);
			}
		}
	}

	/** {@inheritDoc} */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// VERSION
		in.readByte();

		// SUPER
		super.readExternal(in);

		// NUMBER OF ENTRIES
		int size = in.readInt();
		setUp(size);

		// ENTRIES
		while (size-- > 0) {
			char key = in.readChar();
			double val = in.readDouble();
			put(key, val);
		}
	}
} // TCharDoubleHashMap
