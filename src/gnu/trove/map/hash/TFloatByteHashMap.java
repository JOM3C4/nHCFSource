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

import gnu.trove.TByteCollection;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TFloatByteHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.iterator.TFloatByteIterator;
import gnu.trove.iterator.TFloatIterator;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

import gnu.trove.map.TFloatByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;

/**
 * An open addressed Map implementation for float keys and byte values.
 *
 * @author Eric D. Friedman
 * @author Rob Eden
 * @author Jeff Randall
 * @version $Id: _K__V_HashMap.template,v 1.1.2.16 2010/03/02 04:09:50 robeden
 *          Exp $
 */
public class TFloatByteHashMap extends TFloatByteHash implements TFloatByteMap, Externalizable {
	static final long serialVersionUID = 1L;

	/** the values of the map */
	protected transient byte[] _values;

	/**
	 * Creates a new <code>TFloatByteHashMap</code> instance with the default
	 * capacity and load factor.
	 */
	public TFloatByteHashMap() {
		super();
	}

	/**
	 * Creates a new <code>TFloatByteHashMap</code> instance with a prime capacity
	 * equal to or greater than <tt>initialCapacity</tt> and with the default load
	 * factor.
	 *
	 * @param initialCapacity an <code>int</code> value
	 */
	public TFloatByteHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Creates a new <code>TFloatByteHashMap</code> instance with a prime capacity
	 * equal to or greater than <tt>initialCapacity</tt> and with the specified load
	 * factor.
	 *
	 * @param initialCapacity an <code>int</code> value
	 * @param loadFactor      a <code>float</code> value
	 */
	public TFloatByteHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * Creates a new <code>TFloatByteHashMap</code> instance with a prime capacity
	 * equal to or greater than <tt>initialCapacity</tt> and with the specified load
	 * factor.
	 *
	 * @param initialCapacity an <code>int</code> value
	 * @param loadFactor      a <code>float</code> value
	 * @param noEntryKey      a <code>float</code> value that represents
	 *                        <tt>null</tt> for the Key set.
	 * @param noEntryValue    a <code>byte</code> value that represents
	 *                        <tt>null</tt> for the Value set.
	 */
	public TFloatByteHashMap(int initialCapacity, float loadFactor, float noEntryKey, byte noEntryValue) {
		super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
	}

	/**
	 * Creates a new <code>TFloatByteHashMap</code> instance containing all of the
	 * entries in the map passed in.
	 *
	 * @param keys   a <tt>float</tt> array containing the keys for the matching
	 *               values.
	 * @param values a <tt>byte</tt> array containing the values.
	 */
	public TFloatByteHashMap(float[] keys, byte[] values) {
		super(Math.max(keys.length, values.length));

		int size = Math.min(keys.length, values.length);
		for (int i = 0; i < size; i++) {
			this.put(keys[i], values[i]);
		}
	}

	/**
	 * Creates a new <code>TFloatByteHashMap</code> instance containing all of the
	 * entries in the map passed in.
	 *
	 * @param map a <tt>TFloatByteMap</tt> that will be duplicated.
	 */
	public TFloatByteHashMap(TFloatByteMap map) {
		super(map.size());
		if (map instanceof TFloatByteHashMap) {
			TFloatByteHashMap hashmap = (TFloatByteHashMap) map;
			this._loadFactor = hashmap._loadFactor;
			this.no_entry_key = hashmap.no_entry_key;
			this.no_entry_value = hashmap.no_entry_value;
			// noinspection RedundantCast
			if (this.no_entry_key != (float) 0) {
				Arrays.fill(_set, this.no_entry_key);
			}
			// noinspection RedundantCast
			if (this.no_entry_value != (byte) 0) {
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
		_values = new byte[capacity];
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

		float oldKeys[] = _set;
		byte oldVals[] = _values;
		byte oldStates[] = _states;

		_set = new float[newCapacity];
		_values = new byte[newCapacity];
		_states = new byte[newCapacity];

		for (int i = oldCapacity; i-- > 0;) {
			if (oldStates[i] == FULL) {
				float o = oldKeys[i];
				int index = insertKey(o);
				_values[index] = oldVals[i];
			}
		}
	}

	/** {@inheritDoc} */
	public byte put(float key, byte value) {
		int index = insertKey(key);
		return doPut(key, value, index);
	}

	/** {@inheritDoc} */
	public byte putIfAbsent(float key, byte value) {
		int index = insertKey(key);
		if (index < 0)
			return _values[-index - 1];
		return doPut(key, value, index);
	}

	private byte doPut(float key, byte value, int index) {
		byte previous = no_entry_value;
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
	public void putAll(Map<? extends Float, ? extends Byte> map) {
		ensureCapacity(map.size());
		// could optimize this for cases when map instanceof THashMap
		for (Map.Entry<? extends Float, ? extends Byte> entry : map.entrySet()) {
			this.put(entry.getKey().floatValue(), entry.getValue().byteValue());
		}
	}

	/** {@inheritDoc} */
	public void putAll(TFloatByteMap map) {
		ensureCapacity(map.size());
		TFloatByteIterator iter = map.iterator();
		while (iter.hasNext()) {
			iter.advance();
			this.put(iter.key(), iter.value());
		}
	}

	/** {@inheritDoc} */
	public byte get(float key) {
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
	public byte remove(float key) {
		byte prev = no_entry_value;
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
	public TFloatSet keySet() {
		return new TKeyView();
	}

	/** {@inheritDoc} */
	public float[] keys() {
		float[] keys = new float[size()];
		if (keys.length == 0) {
			return keys; // nothing to copy
		}
		float[] k = _set;
		byte[] states = _states;

		for (int i = k.length, j = 0; i-- > 0;) {
			if (states[i] == FULL) {
				keys[j++] = k[i];
			}
		}
		return keys;
	}

	/** {@inheritDoc} */
	public float[] keys(float[] array) {
		int size = size();
		if (size == 0) {
			return array; // nothing to copy
		}
		if (array.length < size) {
			array = new float[size];
		}

		float[] keys = _set;
		byte[] states = _states;

		for (int i = keys.length, j = 0; i-- > 0;) {
			if (states[i] == FULL) {
				array[j++] = keys[i];
			}
		}
		return array;
	}

	/** {@inheritDoc} */
	public TByteCollection valueCollection() {
		return new TValueView();
	}

	/** {@inheritDoc} */
	public byte[] values() {
		byte[] vals = new byte[size()];
		if (vals.length == 0) {
			return vals; // nothing to copy
		}
		byte[] v = _values;
		byte[] states = _states;

		for (int i = v.length, j = 0; i-- > 0;) {
			if (states[i] == FULL) {
				vals[j++] = v[i];
			}
		}
		return vals;
	}

	/** {@inheritDoc} */
	public byte[] values(byte[] array) {
		int size = size();
		if (size == 0) {
			return array; // nothing to copy
		}
		if (array.length < size) {
			array = new byte[size];
		}

		byte[] v = _values;
		byte[] states = _states;

		for (int i = v.length, j = 0; i-- > 0;) {
			if (states[i] == FULL) {
				array[j++] = v[i];
			}
		}
		return array;
	}

	/** {@inheritDoc} */
	public boolean containsValue(byte val) {
		byte[] states = _states;
		byte[] vals = _values;

		for (int i = vals.length; i-- > 0;) {
			if (states[i] == FULL && val == vals[i]) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	public boolean containsKey(float key) {
		return contains(key);
	}

	/** {@inheritDoc} */
	public TFloatByteIterator iterator() {
		return new TFloatByteHashIterator(this);
	}

	/** {@inheritDoc} */
	public boolean forEachKey(TFloatProcedure procedure) {
		return forEach(procedure);
	}

	/** {@inheritDoc} */
	public boolean forEachValue(TByteProcedure procedure) {
		byte[] states = _states;
		byte[] values = _values;
		for (int i = values.length; i-- > 0;) {
			if (states[i] == FULL && !procedure.execute(values[i])) {
				return false;
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	public boolean forEachEntry(TFloatByteProcedure procedure) {
		byte[] states = _states;
		float[] keys = _set;
		byte[] values = _values;
		for (int i = keys.length; i-- > 0;) {
			if (states[i] == FULL && !procedure.execute(keys[i], values[i])) {
				return false;
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	public void transformValues(TByteFunction function) {
		byte[] states = _states;
		byte[] values = _values;
		for (int i = values.length; i-- > 0;) {
			if (states[i] == FULL) {
				values[i] = function.execute(values[i]);
			}
		}
	}

	/** {@inheritDoc} */
	public boolean retainEntries(TFloatByteProcedure procedure) {
		boolean modified = false;
		byte[] states = _states;
		float[] keys = _set;
		byte[] values = _values;

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
	public boolean increment(float key) {
		return adjustValue(key, (byte) 1);
	}

	/** {@inheritDoc} */
	public boolean adjustValue(float key, byte amount) {
		int index = index(key);
		if (index < 0) {
			return false;
		} else {
			_values[index] += amount;
			return true;
		}
	}

	/** {@inheritDoc} */
	public byte adjustOrPutValue(float key, byte adjust_amount, byte put_amount) {
		int index = insertKey(key);
		final boolean isNewMapping;
		final byte newValue;
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
	protected class TKeyView implements TFloatSet {

		/** {@inheritDoc} */
		public TFloatIterator iterator() {
			return new TFloatByteKeyHashIterator(TFloatByteHashMap.this);
		}

		/** {@inheritDoc} */
		public float getNoEntryValue() {
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
		public boolean contains(float entry) {
			return TFloatByteHashMap.this.contains(entry);
		}

		/** {@inheritDoc} */
		public float[] toArray() {
			return TFloatByteHashMap.this.keys();
		}

		/** {@inheritDoc} */
		public float[] toArray(float[] dest) {
			return TFloatByteHashMap.this.keys(dest);
		}

		/**
		 * Unsupported when operating upon a Key Set view of a TFloatByteMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean add(float entry) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		public boolean remove(float entry) {
			return no_entry_value != TFloatByteHashMap.this.remove(entry);
		}

		/** {@inheritDoc} */
		public boolean containsAll(Collection<?> collection) {
			for (Object element : collection) {
				if (element instanceof Float) {
					float ele = ((Float) element).floatValue();
					if (!TFloatByteHashMap.this.containsKey(ele)) {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}

		/** {@inheritDoc} */
		public boolean containsAll(TFloatCollection collection) {
			TFloatIterator iter = collection.iterator();
			while (iter.hasNext()) {
				if (!TFloatByteHashMap.this.containsKey(iter.next())) {
					return false;
				}
			}
			return true;
		}

		/** {@inheritDoc} */
		public boolean containsAll(float[] array) {
			for (float element : array) {
				if (!TFloatByteHashMap.this.contains(element)) {
					return false;
				}
			}
			return true;
		}

		/**
		 * Unsupported when operating upon a Key Set view of a TFloatByteMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean addAll(Collection<? extends Float> collection) {
			throw new UnsupportedOperationException();
		}

		/**
		 * Unsupported when operating upon a Key Set view of a TFloatByteMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean addAll(TFloatCollection collection) {
			throw new UnsupportedOperationException();
		}

		/**
		 * Unsupported when operating upon a Key Set view of a TFloatByteMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean addAll(float[] array) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		@SuppressWarnings({})
		public boolean retainAll(Collection<?> collection) {
			boolean modified = false;
			TFloatIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(Float.valueOf(iter.next()))) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}

		/** {@inheritDoc} */
		public boolean retainAll(TFloatCollection collection) {
			if (this == collection) {
				return false;
			}
			boolean modified = false;
			TFloatIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(iter.next())) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}

		/** {@inheritDoc} */
		public boolean retainAll(float[] array) {
			boolean changed = false;
			Arrays.sort(array);
			float[] set = _set;
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
				if (element instanceof Float) {
					float c = ((Float) element).floatValue();
					if (remove(c)) {
						changed = true;
					}
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public boolean removeAll(TFloatCollection collection) {
			if (this == collection) {
				clear();
				return true;
			}
			boolean changed = false;
			TFloatIterator iter = collection.iterator();
			while (iter.hasNext()) {
				float element = iter.next();
				if (remove(element)) {
					changed = true;
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public boolean removeAll(float[] array) {
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
			TFloatByteHashMap.this.clear();
		}

		/** {@inheritDoc} */
		public boolean forEach(TFloatProcedure procedure) {
			return TFloatByteHashMap.this.forEachKey(procedure);
		}

		@Override
		public boolean equals(Object other) {
			if (!(other instanceof TFloatSet)) {
				return false;
			}
			final TFloatSet that = (TFloatSet) other;
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
			forEachKey(new TFloatProcedure() {
				private boolean first = true;

				public boolean execute(float key) {
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
	protected class TValueView implements TByteCollection {

		/** {@inheritDoc} */
		public TByteIterator iterator() {
			return new TFloatByteValueHashIterator(TFloatByteHashMap.this);
		}

		/** {@inheritDoc} */
		public byte getNoEntryValue() {
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
		public boolean contains(byte entry) {
			return TFloatByteHashMap.this.containsValue(entry);
		}

		/** {@inheritDoc} */
		public byte[] toArray() {
			return TFloatByteHashMap.this.values();
		}

		/** {@inheritDoc} */
		public byte[] toArray(byte[] dest) {
			return TFloatByteHashMap.this.values(dest);
		}

		public boolean add(byte entry) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		public boolean remove(byte entry) {
			byte[] values = _values;
			float[] set = _set;

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
				if (element instanceof Byte) {
					byte ele = ((Byte) element).byteValue();
					if (!TFloatByteHashMap.this.containsValue(ele)) {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}

		/** {@inheritDoc} */
		public boolean containsAll(TByteCollection collection) {
			TByteIterator iter = collection.iterator();
			while (iter.hasNext()) {
				if (!TFloatByteHashMap.this.containsValue(iter.next())) {
					return false;
				}
			}
			return true;
		}

		/** {@inheritDoc} */
		public boolean containsAll(byte[] array) {
			for (byte element : array) {
				if (!TFloatByteHashMap.this.containsValue(element)) {
					return false;
				}
			}
			return true;
		}

		/** {@inheritDoc} */
		public boolean addAll(Collection<? extends Byte> collection) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		public boolean addAll(TByteCollection collection) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		public boolean addAll(byte[] array) {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc} */
		@SuppressWarnings({})
		public boolean retainAll(Collection<?> collection) {
			boolean modified = false;
			TByteIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(Byte.valueOf(iter.next()))) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}

		/** {@inheritDoc} */
		public boolean retainAll(TByteCollection collection) {
			if (this == collection) {
				return false;
			}
			boolean modified = false;
			TByteIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(iter.next())) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}

		/** {@inheritDoc} */
		public boolean retainAll(byte[] array) {
			boolean changed = false;
			Arrays.sort(array);
			byte[] values = _values;
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
				if (element instanceof Byte) {
					byte c = ((Byte) element).byteValue();
					if (remove(c)) {
						changed = true;
					}
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public boolean removeAll(TByteCollection collection) {
			if (this == collection) {
				clear();
				return true;
			}
			boolean changed = false;
			TByteIterator iter = collection.iterator();
			while (iter.hasNext()) {
				byte element = iter.next();
				if (remove(element)) {
					changed = true;
				}
			}
			return changed;
		}

		/** {@inheritDoc} */
		public boolean removeAll(byte[] array) {
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
			TFloatByteHashMap.this.clear();
		}

		/** {@inheritDoc} */
		public boolean forEach(TByteProcedure procedure) {
			return TFloatByteHashMap.this.forEachValue(procedure);
		}

		/** {@inheritDoc} */
		@Override
		public String toString() {
			final StringBuilder buf = new StringBuilder("{");
			forEachValue(new TByteProcedure() {
				private boolean first = true;

				public boolean execute(byte value) {
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

	class TFloatByteKeyHashIterator extends THashPrimitiveIterator implements TFloatIterator {

		/**
		 * Creates an iterator over the specified map
		 *
		 * @param hash the <tt>TPrimitiveHash</tt> we will be iterating over.
		 */
		TFloatByteKeyHashIterator(TPrimitiveHash hash) {
			super(hash);
		}

		/** {@inheritDoc} */
		public float next() {
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
				TFloatByteHashMap.this.removeAt(_index);
			} finally {
				_hash.reenableAutoCompaction(false);
			}

			_expectedSize--;
		}
	}

	class TFloatByteValueHashIterator extends THashPrimitiveIterator implements TByteIterator {

		/**
		 * Creates an iterator over the specified map
		 *
		 * @param hash the <tt>TPrimitiveHash</tt> we will be iterating over.
		 */
		TFloatByteValueHashIterator(TPrimitiveHash hash) {
			super(hash);
		}

		/** {@inheritDoc} */
		public byte next() {
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
				TFloatByteHashMap.this.removeAt(_index);
			} finally {
				_hash.reenableAutoCompaction(false);
			}

			_expectedSize--;
		}
	}

	class TFloatByteHashIterator extends THashPrimitiveIterator implements TFloatByteIterator {

		/**
		 * Creates an iterator over the specified map
		 *
		 * @param map the <tt>TFloatByteHashMap</tt> we will be iterating over.
		 */
		TFloatByteHashIterator(TFloatByteHashMap map) {
			super(map);
		}

		/** {@inheritDoc} */
		public void advance() {
			moveToNextIndex();
		}

		/** {@inheritDoc} */
		public float key() {
			return _set[_index];
		}

		/** {@inheritDoc} */
		public byte value() {
			return _values[_index];
		}

		/** {@inheritDoc} */
		public byte setValue(byte val) {
			byte old = value();
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
				TFloatByteHashMap.this.removeAt(_index);
			} finally {
				_hash.reenableAutoCompaction(false);
			}
			_expectedSize--;
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TFloatByteMap)) {
			return false;
		}
		TFloatByteMap that = (TFloatByteMap) other;
		if (that.size() != this.size()) {
			return false;
		}
		byte[] values = _values;
		byte[] states = _states;
		byte this_no_entry_value = getNoEntryValue();
		byte that_no_entry_value = that.getNoEntryValue();
		for (int i = values.length; i-- > 0;) {
			if (states[i] == FULL) {
				float key = _set[i];
				byte that_value = that.get(key);
				byte this_value = values[i];
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
		forEachEntry(new TFloatByteProcedure() {
			private boolean first = true;

			public boolean execute(float key, byte value) {
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
				out.writeFloat(_set[i]);
				out.writeByte(_values[i]);
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
			float key = in.readFloat();
			byte val = in.readByte();
			put(key, val);
		}
	}
} // TFloatByteHashMap
