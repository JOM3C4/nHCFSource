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

public class TUnmodifiableIntDoubleMap implements TIntDoubleMap, Serializable {
	private static final long serialVersionUID = -1034234728574286014L;

	private final TIntDoubleMap m;

	public TUnmodifiableIntDoubleMap(TIntDoubleMap m) {
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

	public boolean containsKey(int key) {
		return m.containsKey(key);
	}

	public boolean containsValue(double val) {
		return m.containsValue(val);
	}

	public double get(int key) {
		return m.get(key);
	}

	public double put(int key, double value) {
		throw new UnsupportedOperationException();
	}

	public double remove(int key) {
		throw new UnsupportedOperationException();
	}

	public void putAll(TIntDoubleMap m) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends Integer, ? extends Double> map) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	private transient TIntSet keySet = null;
	private transient TDoubleCollection values = null;

	public TIntSet keySet() {
		if (keySet == null)
			keySet = TCollections.unmodifiableSet(m.keySet());
		return keySet;
	}

	public int[] keys() {
		return m.keys();
	}

	public int[] keys(int[] array) {
		return m.keys(array);
	}

	public TDoubleCollection valueCollection() {
		if (values == null)
			values = TCollections.unmodifiableCollection(m.valueCollection());
		return values;
	}

	public double[] values() {
		return m.values();
	}

	public double[] values(double[] array) {
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

	public int getNoEntryKey() {
		return m.getNoEntryKey();
	}

	public double getNoEntryValue() {
		return m.getNoEntryValue();
	}

	public boolean forEachKey(TIntProcedure procedure) {
		return m.forEachKey(procedure);
	}

	public boolean forEachValue(TDoubleProcedure procedure) {
		return m.forEachValue(procedure);
	}

	public boolean forEachEntry(TIntDoubleProcedure procedure) {
		return m.forEachEntry(procedure);
	}

	public TIntDoubleIterator iterator() {
		return new TIntDoubleIterator() {
			TIntDoubleIterator iter = m.iterator();

			public int key() {
				return iter.key();
			}

			public double value() {
				return iter.value();
			}

			public void advance() {
				iter.advance();
			}

			public boolean hasNext() {
				return iter.hasNext();
			}

			public double setValue(double val) {
				throw new UnsupportedOperationException();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public double putIfAbsent(int key, double value) {
		throw new UnsupportedOperationException();
	}

	public void transformValues(TDoubleFunction function) {
		throw new UnsupportedOperationException();
	}

	public boolean retainEntries(TIntDoubleProcedure procedure) {
		throw new UnsupportedOperationException();
	}

	public boolean increment(int key) {
		throw new UnsupportedOperationException();
	}

	public boolean adjustValue(int key, double amount) {
		throw new UnsupportedOperationException();
	}

	public double adjustOrPutValue(int key, double adjust_amount, double put_amount) {
		throw new UnsupportedOperationException();
	}
}
