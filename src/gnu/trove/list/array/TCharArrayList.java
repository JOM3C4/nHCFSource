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

package gnu.trove.list.array;

import gnu.trove.function.TCharFunction;
import gnu.trove.list.TCharList;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.TCharCollection;
import gnu.trove.impl.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

/**
 * A resizable, array-backed list of char primitives.
 */
public class TCharArrayList implements TCharList, Externalizable {
	static final long serialVersionUID = 1L;

	/** the data of the list */
	protected char[] _data;

	/** the index after the last entry in the list */
	protected int _pos;

	/** the default capacity for new lists */
	protected static final int DEFAULT_CAPACITY = Constants.DEFAULT_CAPACITY;

	/** the char value that represents null */
	protected char no_entry_value;

	/**
	 * Creates a new <code>TCharArrayList</code> instance with the default capacity.
	 */
	@SuppressWarnings({})
	public TCharArrayList() {
		this(DEFAULT_CAPACITY, (char) 0);
	}

	/**
	 * Creates a new <code>TCharArrayList</code> instance with the specified
	 * capacity.
	 *
	 * @param capacity an <code>int</code> value
	 */
	@SuppressWarnings({})
	public TCharArrayList(int capacity) {
		this(capacity, (char) 0);
	}

	/**
	 * Creates a new <code>TCharArrayList</code> instance with the specified
	 * capacity.
	 *
	 * @param capacity       an <code>int</code> value
	 * @param no_entry_value an <code>char</code> value that represents null.
	 */
	public TCharArrayList(int capacity, char no_entry_value) {
		_data = new char[capacity];
		_pos = 0;
		this.no_entry_value = no_entry_value;
	}

	/**
	 * Creates a new <code>TCharArrayList</code> instance that contains a copy of
	 * the collection passed to us.
	 *
	 * @param collection the collection to copy
	 */
	public TCharArrayList(TCharCollection collection) {
		this(collection.size());
		addAll(collection);
	}

	/**
	 * Creates a new <code>TCharArrayList</code> instance whose capacity is the
	 * length of <tt>values</tt> array and whose initial contents are the specified
	 * values.
	 * <p>
	 * A defensive copy of the given values is held by the new instance.
	 *
	 * @param values an <code>char[]</code> value
	 */
	public TCharArrayList(char[] values) {
		this(values.length);
		add(values);
	}

	protected TCharArrayList(char[] values, char no_entry_value, boolean wrap) {
		if (!wrap)
			throw new IllegalStateException("Wrong call");

		if (values == null)
			throw new IllegalArgumentException("values can not be null");

		_data = values;
		_pos = values.length;
		this.no_entry_value = no_entry_value;
	}

	/**
	 * Returns a primitive List implementation that wraps around the given primitive
	 * array.
	 * <p/>
	 * NOTE: mutating operation are allowed as long as the List does not grow. In
	 * that case an IllegalStateException will be thrown
	 *
	 * @param values
	 * @return
	 */
	public static TCharArrayList wrap(char[] values) {
		return wrap(values, (char) 0);
	}

	/**
	 * Returns a primitive List implementation that wraps around the given primitive
	 * array.
	 * <p/>
	 * NOTE: mutating operation are allowed as long as the List does not grow. In
	 * that case an IllegalStateException will be thrown
	 *
	 * @param values
	 * @param no_entry_value
	 * @return
	 */
	public static TCharArrayList wrap(char[] values, char no_entry_value) {
		return new TCharArrayList(values, no_entry_value, true) {
			/**
			 * Growing the wrapped external array is not allow
			 */
			@Override
			public void ensureCapacity(int capacity) {
				if (capacity > _data.length)
					throw new IllegalStateException("Can not grow ArrayList wrapped external array");
			}
		};
	}

	/** {@inheritDoc} */
	public char getNoEntryValue() {
		return no_entry_value;
	}

	// sizing

	/**
	 * Grow the internal array as needed to accommodate the specified number of
	 * elements. The size of the array bytes on each resize unless capacity requires
	 * more than twice the current capacity.
	 */
	public void ensureCapacity(int capacity) {
		if (capacity > _data.length) {
			int newCap = Math.max(_data.length << 1, capacity);
			char[] tmp = new char[newCap];
			System.arraycopy(_data, 0, tmp, 0, _data.length);
			_data = tmp;
		}
	}

	/** {@inheritDoc} */
	public int size() {
		return _pos;
	}

	/** {@inheritDoc} */
	public boolean isEmpty() {
		return _pos == 0;
	}

	/**
	 * Sheds any excess capacity above and beyond the current size of the list.
	 */
	public void trimToSize() {
		if (_data.length > size()) {
			char[] tmp = new char[size()];
			toArray(tmp, 0, tmp.length);
			_data = tmp;
		}
	}

	// modifying

	/** {@inheritDoc} */
	public boolean add(char val) {
		ensureCapacity(_pos + 1);
		_data[_pos++] = val;
		return true;
	}

	/** {@inheritDoc} */
	public void add(char[] vals) {
		add(vals, 0, vals.length);
	}

	/** {@inheritDoc} */
	public void add(char[] vals, int offset, int length) {
		ensureCapacity(_pos + length);
		System.arraycopy(vals, offset, _data, _pos, length);
		_pos += length;
	}

	/** {@inheritDoc} */
	public void insert(int offset, char value) {
		if (offset == _pos) {
			add(value);
			return;
		}
		ensureCapacity(_pos + 1);
		// shift right
		System.arraycopy(_data, offset, _data, offset + 1, _pos - offset);
		// insert
		_data[offset] = value;
		_pos++;
	}

	/** {@inheritDoc} */
	public void insert(int offset, char[] values) {
		insert(offset, values, 0, values.length);
	}

	/** {@inheritDoc} */
	public void insert(int offset, char[] values, int valOffset, int len) {
		if (offset == _pos) {
			add(values, valOffset, len);
			return;
		}

		ensureCapacity(_pos + len);
		// shift right
		System.arraycopy(_data, offset, _data, offset + len, _pos - offset);
		// insert
		System.arraycopy(values, valOffset, _data, offset, len);
		_pos += len;
	}

	/** {@inheritDoc} */
	public char get(int offset) {
		if (offset >= _pos) {
			throw new ArrayIndexOutOfBoundsException(offset);
		}
		return _data[offset];
	}

	/**
	 * Returns the value at the specified offset without doing any bounds checking.
	 */
	public char getQuick(int offset) {
		return _data[offset];
	}

	/** {@inheritDoc} */
	public char set(int offset, char val) {
		if (offset >= _pos) {
			throw new ArrayIndexOutOfBoundsException(offset);
		}

		char prev_val = _data[offset];
		_data[offset] = val;
		return prev_val;
	}

	/** {@inheritDoc} */
	public char replace(int offset, char val) {
		if (offset >= _pos) {
			throw new ArrayIndexOutOfBoundsException(offset);
		}
		char old = _data[offset];
		_data[offset] = val;
		return old;
	}

	/** {@inheritDoc} */
	public void set(int offset, char[] values) {
		set(offset, values, 0, values.length);
	}

	/** {@inheritDoc} */
	public void set(int offset, char[] values, int valOffset, int length) {
		if (offset < 0 || offset + length > _pos) {
			throw new ArrayIndexOutOfBoundsException(offset);
		}
		System.arraycopy(values, valOffset, _data, offset, length);
	}

	/**
	 * Sets the value at the specified offset without doing any bounds checking.
	 */
	public void setQuick(int offset, char val) {
		_data[offset] = val;
	}

	/** {@inheritDoc} */
	public void clear() {
		clear(DEFAULT_CAPACITY);
	}

	/**
	 * Flushes the internal state of the list, setting the capacity of the empty
	 * list to <tt>capacity</tt>.
	 */
	public void clear(int capacity) {
		_data = new char[capacity];
		_pos = 0;
	}

	/**
	 * Sets the size of the list to 0, but does not change its capacity. This method
	 * can be used as an alternative to the {@link #clear()} method if you want to
	 * recycle a list without allocating new backing arrays.
	 */
	public void reset() {
		_pos = 0;
		Arrays.fill(_data, no_entry_value);
	}

	/**
	 * Sets the size of the list to 0, but does not change its capacity. This method
	 * can be used as an alternative to the {@link #clear()} method if you want to
	 * recycle a list without allocating new backing arrays. This method differs
	 * from {@link #reset()} in that it does not clear the old values in the backing
	 * array. Thus, it is possible for getQuick to return stale data if this method
	 * is used and the caller is careless about bounds checking.
	 */
	public void resetQuick() {
		_pos = 0;
	}

	/** {@inheritDoc} */
	public boolean remove(char value) {
		for (int index = 0; index < _pos; index++) {
			if (value == _data[index]) {
				remove(index, 1);
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	public char removeAt(int offset) {
		char old = get(offset);
		remove(offset, 1);
		return old;
	}

	/** {@inheritDoc} */
	public void remove(int offset, int length) {
		if (length == 0)
			return;
		if (offset < 0 || offset >= _pos) {
			throw new ArrayIndexOutOfBoundsException(offset);
		}

		if (offset == 0) {
			// data at the front
			System.arraycopy(_data, length, _data, 0, _pos - length);
		} else if (_pos - length == offset) {
			// no copy to make, decrementing pos "deletes" values at
			// the end
		} else {
			// data in the middle
			System.arraycopy(_data, offset + length, _data, offset, _pos - (offset + length));
		}
		_pos -= length;
		// no need to clear old values beyond _pos, because this is a
		// primitive collection and 0 takes as much room as any other
		// value
	}

	/** {@inheritDoc} */
	public TCharIterator iterator() {
		return new TCharArrayIterator(0);
	}

	/** {@inheritDoc} */
	public boolean containsAll(Collection<?> collection) {
		for (Object element : collection) {
			if (element instanceof Character) {
				char c = ((Character) element).charValue();
				if (!contains(c)) {
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
		if (this == collection) {
			return true;
		}
		TCharIterator iter = collection.iterator();
		while (iter.hasNext()) {
			char element = iter.next();
			if (!contains(element)) {
				return false;
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	public boolean containsAll(char[] array) {
		for (int i = array.length; i-- > 0;) {
			if (!contains(array[i])) {
				return false;
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	public boolean addAll(Collection<? extends Character> collection) {
		boolean changed = false;
		for (Character element : collection) {
			char e = element.charValue();
			if (add(e)) {
				changed = true;
			}
		}
		return changed;
	}

	/** {@inheritDoc} */
	public boolean addAll(TCharCollection collection) {
		boolean changed = false;
		TCharIterator iter = collection.iterator();
		while (iter.hasNext()) {
			char element = iter.next();
			if (add(element)) {
				changed = true;
			}
		}
		return changed;
	}

	/** {@inheritDoc} */
	public boolean addAll(char[] array) {
		boolean changed = false;
		for (char element : array) {
			if (add(element)) {
				changed = true;
			}
		}
		return changed;
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
		char[] data = _data;

		for (int i = _pos; i-- > 0;) {
			if (Arrays.binarySearch(array, data[i]) < 0) {
				remove(i, 1);
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
		if (collection == this) {
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
	public void transformValues(TCharFunction function) {
		for (int i = _pos; i-- > 0;) {
			_data[i] = function.execute(_data[i]);
		}
	}

	/** {@inheritDoc} */
	public void reverse() {
		reverse(0, _pos);
	}

	/** {@inheritDoc} */
	public void reverse(int from, int to) {
		if (from == to) {
			return; // nothing to do
		}
		if (from > to) {
			throw new IllegalArgumentException("from cannot be greater than to");
		}
		for (int i = from, j = to - 1; i < j; i++, j--) {
			swap(i, j);
		}
	}

	/** {@inheritDoc} */
	public void shuffle(Random rand) {
		for (int i = _pos; i-- > 1;) {
			swap(i, rand.nextInt(i));
		}
	}

	/**
	 * Swap the values at offsets <tt>i</tt> and <tt>j</tt>.
	 *
	 * @param i an offset into the data array
	 * @param j an offset into the data array
	 */
	private void swap(int i, int j) {
		char tmp = _data[i];
		_data[i] = _data[j];
		_data[j] = tmp;
	}

	// copying

	/** {@inheritDoc} */
	public TCharList subList(int begin, int end) {
		if (end < begin) {
			throw new IllegalArgumentException("end index " + end + " greater than begin index " + begin);
		}
		if (begin < 0) {
			throw new IndexOutOfBoundsException("begin index can not be < 0");
		}
		if (end > _data.length) {
			throw new IndexOutOfBoundsException("end index < " + _data.length);
		}
		TCharArrayList list = new TCharArrayList(end - begin);
		for (int i = begin; i < end; i++) {
			list.add(_data[i]);
		}
		return list;
	}

	/** {@inheritDoc} */
	public char[] toArray() {
		return toArray(0, _pos);
	}

	/** {@inheritDoc} */
	public char[] toArray(int offset, int len) {
		char[] rv = new char[len];
		toArray(rv, offset, len);
		return rv;
	}

	/** {@inheritDoc} */
	public char[] toArray(char[] dest) {
		int len = dest.length;
		if (dest.length > _pos) {
			len = _pos;
			dest[len] = no_entry_value;
		}
		toArray(dest, 0, len);
		return dest;
	}

	/** {@inheritDoc} */
	public char[] toArray(char[] dest, int offset, int len) {
		if (len == 0) {
			return dest; // nothing to copy
		}
		if (offset < 0 || offset >= _pos) {
			throw new ArrayIndexOutOfBoundsException(offset);
		}
		System.arraycopy(_data, offset, dest, 0, len);
		return dest;
	}

	/** {@inheritDoc} */
	public char[] toArray(char[] dest, int source_pos, int dest_pos, int len) {
		if (len == 0) {
			return dest; // nothing to copy
		}
		if (source_pos < 0 || source_pos >= _pos) {
			throw new ArrayIndexOutOfBoundsException(source_pos);
		}
		System.arraycopy(_data, source_pos, dest, dest_pos, len);
		return dest;
	}

	// comparing

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof TCharList))
			return false;

		if (other instanceof TCharArrayList) {
			TCharArrayList that = (TCharArrayList) other;
			if (that.size() != this.size())
				return false;

			for (int i = _pos; i-- > 0;) {
				if (this._data[i] != that._data[i]) {
					return false;
				}
			}
			return true;
		} else {
			TCharList that = (TCharList) other;
			if (that.size() != this.size())
				return false;

			for (int i = 0; i < _pos; i++) {
				if (this._data[i] != that.get(i)) {
					return false;
				}
			}
			return true;
		}
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		int h = 0;
		for (int i = _pos; i-- > 0;) {
			h += HashFunctions.hash(_data[i]);
		}
		return h;
	}

	// procedures

	/** {@inheritDoc} */
	public boolean forEach(TCharProcedure procedure) {
		for (int i = 0; i < _pos; i++) {
			if (!procedure.execute(_data[i])) {
				return false;
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	public boolean forEachDescending(TCharProcedure procedure) {
		for (int i = _pos; i-- > 0;) {
			if (!procedure.execute(_data[i])) {
				return false;
			}
		}
		return true;
	}

	// sorting

	/** {@inheritDoc} */
	public void sort() {
		Arrays.sort(_data, 0, _pos);
	}

	/** {@inheritDoc} */
	public void sort(int fromIndex, int toIndex) {
		Arrays.sort(_data, fromIndex, toIndex);
	}

	// filling

	/** {@inheritDoc} */
	public void fill(char val) {
		Arrays.fill(_data, 0, _pos, val);
	}

	/** {@inheritDoc} */
	public void fill(int fromIndex, int toIndex, char val) {
		if (toIndex > _pos) {
			ensureCapacity(toIndex);
			_pos = toIndex;
		}
		Arrays.fill(_data, fromIndex, toIndex, val);
	}

	// searching

	/** {@inheritDoc} */
	public int binarySearch(char value) {
		return binarySearch(value, 0, _pos);
	}

	/** {@inheritDoc} */
	public int binarySearch(char value, int fromIndex, int toIndex) {
		if (fromIndex < 0) {
			throw new ArrayIndexOutOfBoundsException(fromIndex);
		}
		if (toIndex > _pos) {
			throw new ArrayIndexOutOfBoundsException(toIndex);
		}

		int low = fromIndex;
		int high = toIndex - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			char midVal = _data[mid];

			if (midVal < value) {
				low = mid + 1;
			} else if (midVal > value) {
				high = mid - 1;
			} else {
				return mid; // value found
			}
		}
		return -(low + 1); // value not found.
	}

	/** {@inheritDoc} */
	public int indexOf(char value) {
		return indexOf(0, value);
	}

	/** {@inheritDoc} */
	public int indexOf(int offset, char value) {
		for (int i = offset; i < _pos; i++) {
			if (_data[i] == value) {
				return i;
			}
		}
		return -1;
	}

	/** {@inheritDoc} */
	public int lastIndexOf(char value) {
		return lastIndexOf(_pos, value);
	}

	/** {@inheritDoc} */
	public int lastIndexOf(int offset, char value) {
		for (int i = offset; i-- > 0;) {
			if (_data[i] == value) {
				return i;
			}
		}
		return -1;
	}

	/** {@inheritDoc} */
	public boolean contains(char value) {
		return lastIndexOf(value) >= 0;
	}

	/** {@inheritDoc} */
	public TCharList grep(TCharProcedure condition) {
		TCharArrayList list = new TCharArrayList();
		for (int i = 0; i < _pos; i++) {
			if (condition.execute(_data[i])) {
				list.add(_data[i]);
			}
		}
		return list;
	}

	/** {@inheritDoc} */
	public TCharList inverseGrep(TCharProcedure condition) {
		TCharArrayList list = new TCharArrayList();
		for (int i = 0; i < _pos; i++) {
			if (!condition.execute(_data[i])) {
				list.add(_data[i]);
			}
		}
		return list;
	}

	/** {@inheritDoc} */
	public char max() {
		if (size() == 0) {
			throw new IllegalStateException("cannot find maximum of an empty list");
		}
		char max = Character.MIN_VALUE;
		for (int i = 0; i < _pos; i++) {
			if (_data[i] > max) {
				max = _data[i];
			}
		}
		return max;
	}

	/** {@inheritDoc} */
	public char min() {
		if (size() == 0) {
			throw new IllegalStateException("cannot find minimum of an empty list");
		}
		char min = Character.MAX_VALUE;
		for (int i = 0; i < _pos; i++) {
			if (_data[i] < min) {
				min = _data[i];
			}
		}
		return min;
	}

	/** {@inheritDoc} */
	public char sum() {
		char sum = 0;
		for (int i = 0; i < _pos; i++) {
			sum += _data[i];
		}
		return sum;
	}

	// stringification

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder("{");
		for (int i = 0, end = _pos - 1; i < end; i++) {
			buf.append(_data[i]);
			buf.append(", ");
		}
		if (size() > 0) {
			buf.append(_data[_pos - 1]);
		}
		buf.append("}");
		return buf.toString();
	}

	/** TCharArrayList iterator */
	class TCharArrayIterator implements TCharIterator {

		/** Index of element to be returned by subsequent call to next. */
		private int cursor = 0;

		/**
		 * Index of element returned by most recent call to next or previous. Reset to
		 * -1 if this element is deleted by a call to remove.
		 */
		int lastRet = -1;

		TCharArrayIterator(int index) {
			cursor = index;
		}

		/** {@inheritDoc} */
		public boolean hasNext() {
			return cursor < size();
		}

		/** {@inheritDoc} */
		public char next() {
			try {
				char next = get(cursor);
				lastRet = cursor++;
				return next;
			} catch (IndexOutOfBoundsException e) {
				throw new NoSuchElementException();
			}
		}

		/** {@inheritDoc} */
		public void remove() {
			if (lastRet == -1)
				throw new IllegalStateException();

			try {
				TCharArrayList.this.remove(lastRet, 1);
				if (lastRet < cursor)
					cursor--;
				lastRet = -1;
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		// VERSION
		out.writeByte(0);

		// POSITION
		out.writeInt(_pos);

		// NO_ENTRY_VALUE
		out.writeChar(no_entry_value);

		// ENTRIES
		int len = _data.length;
		out.writeInt(len);
		for (int i = 0; i < len; i++) {
			out.writeChar(_data[i]);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		// VERSION
		in.readByte();

		// POSITION
		_pos = in.readInt();

		// NO_ENTRY_VALUE
		no_entry_value = in.readChar();

		// ENTRIES
		int len = in.readInt();
		_data = new char[len];
		for (int i = 0; i < len; i++) {
			_data[i] = in.readChar();
		}
	}
} // TCharArrayList
