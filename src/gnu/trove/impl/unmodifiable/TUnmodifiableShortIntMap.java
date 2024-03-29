///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2008, Robert D. Eden All Rights Reserved.
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

package gnu.trove.impl.unmodifiable;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

////////////////////////////////////////////////////////////
// THIS IS AN IMPLEMENTATION CLASS. DO NOT USE DIRECTLY!  //
// Access to these methods should be through TCollections //
////////////////////////////////////////////////////////////

import gnu.trove.iterator.*;
import gnu.trove.procedure.*;
import gnu.trove.set.*;
import gnu.trove.function.*;
import gnu.trove.map.*;
import gnu.trove.*;

import java.util.Map;
import java.io.Serializable;

public class TUnmodifiableShortIntMap implements TShortIntMap, Serializable {
	private static final long serialVersionUID = -1034234728574286014L;

	private final TShortIntMap m;

	public TUnmodifiableShortIntMap(TShortIntMap m) {
		if (m == null)
			throw new NullPointerException();
		this.m = m;
	}

	public int size() {
		return m.size();
	}

	public boolean isEmpty() {
		return m.isEmpty();
	}

	public boolean containsKey(short key) {
		return m.containsKey(key);
	}

	public boolean containsValue(int val) {
		return m.containsValue(val);
	}

	public int get(short key) {
		return m.get(key);
	}

	public int put(short key, int value) {
		throw new UnsupportedOperationException();
	}

	public int remove(short key) {
		throw new UnsupportedOperationException();
	}

	public void putAll(TShortIntMap m) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends Short, ? extends Integer> map) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	private transient TShortSet keySet = null;
	private transient TIntCollection values = null;

	public TShortSet keySet() {
		if (keySet == null)
			keySet = TCollections.unmodifiableSet(m.keySet());
		return keySet;
	}

	public short[] keys() {
		return m.keys();
	}

	public short[] keys(short[] array) {
		return m.keys(array);
	}

	public TIntCollection valueCollection() {
		if (values == null)
			values = TCollections.unmodifiableCollection(m.valueCollection());
		return values;
	}

	public int[] values() {
		return m.values();
	}

	public int[] values(int[] array) {
		return m.values(array);
	}

	public boolean equals(Object o) {
		return o == this || m.equals(o);
	}

	public int hashCode() {
		return m.hashCode();
	}

	public String toString() {
		return m.toString();
	}

	public short getNoEntryKey() {
		return m.getNoEntryKey();
	}

	public int getNoEntryValue() {
		return m.getNoEntryValue();
	}

	public boolean forEachKey(TShortProcedure procedure) {
		return m.forEachKey(procedure);
	}

	public boolean forEachValue(TIntProcedure procedure) {
		return m.forEachValue(procedure);
	}

	public boolean forEachEntry(TShortIntProcedure procedure) {
		return m.forEachEntry(procedure);
	}

	public TShortIntIterator iterator() {
		return new TShortIntIterator() {
			TShortIntIterator iter = m.iterator();

			public short key() {
				return iter.key();
			}

			public int value() {
				return iter.value();
			}

			public void advance() {
				iter.advance();
			}

			public boolean hasNext() {
				return iter.hasNext();
			}

			public int setValue(int val) {
				throw new UnsupportedOperationException();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public int putIfAbsent(short key, int value) {
		throw new UnsupportedOperationException();
	}

	public void transformValues(TIntFunction function) {
		throw new UnsupportedOperationException();
	}

	public boolean retainEntries(TShortIntProcedure procedure) {
		throw new UnsupportedOperationException();
	}

	public boolean increment(short key) {
		throw new UnsupportedOperationException();
	}

	public boolean adjustValue(short key, int amount) {
		throw new UnsupportedOperationException();
	}

	public int adjustOrPutValue(short key, int adjust_amount, int put_amount) {
		throw new UnsupportedOperationException();
	}
}
