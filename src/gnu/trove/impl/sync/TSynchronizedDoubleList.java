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

package gnu.trove.impl.sync;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

////////////////////////////////////////////////////////////
// THIS IS AN IMPLEMENTATION CLASS. DO NOT USE DIRECTLY!  //
// Access to these methods should be through TCollections //
////////////////////////////////////////////////////////////

import gnu.trove.procedure.*;
import gnu.trove.list.*;
import gnu.trove.function.*;
import java.util.RandomAccess;
import java.util.Random;

public class TSynchronizedDoubleList extends TSynchronizedDoubleCollection implements TDoubleList {

	static final long serialVersionUID = -7754090372962971524L;

	final TDoubleList list;

	public TSynchronizedDoubleList(TDoubleList list) {
		super(list);
		this.list = list;
	}

	public TSynchronizedDoubleList(TDoubleList list, Object mutex) {
		super(list, mutex);
		this.list = list;
	}

	public boolean equals(Object o) {
		synchronized (mutex) {
			return list.equals(o);
		}
	}

	public int hashCode() {
		synchronized (mutex) {
			return list.hashCode();
		}
	}

	public double get(int index) {
		synchronized (mutex) {
			return list.get(index);
		}
	}

	public double set(int index, double element) {
		synchronized (mutex) {
			return list.set(index, element);
		}
	}

	public void set(int offset, double[] values) {
		synchronized (mutex) {
			list.set(offset, values);
		}
	}

	public void set(int offset, double[] values, int valOffset, int length) {
		synchronized (mutex) {
			list.set(offset, values, valOffset, length);
		}
	}

	public double replace(int offset, double val) {
		synchronized (mutex) {
			return list.replace(offset, val);
		}
	}

	public void remove(int offset, int length) {
		synchronized (mutex) {
			list.remove(offset, length);
		}
	}

	public double removeAt(int offset) {
		synchronized (mutex) {
			return list.removeAt(offset);
		}
	}

	public void add(double[] vals) {
		synchronized (mutex) {
			list.add(vals);
		}
	}

	public void add(double[] vals, int offset, int length) {
		synchronized (mutex) {
			list.add(vals, offset, length);
		}
	}

	public void insert(int offset, double value) {
		synchronized (mutex) {
			list.insert(offset, value);
		}
	}

	public void insert(int offset, double[] values) {
		synchronized (mutex) {
			list.insert(offset, values);
		}
	}

	public void insert(int offset, double[] values, int valOffset, int len) {
		synchronized (mutex) {
			list.insert(offset, values, valOffset, len);
		}
	}

	public int indexOf(double o) {
		synchronized (mutex) {
			return list.indexOf(o);
		}
	}

	public int lastIndexOf(double o) {
		synchronized (mutex) {
			return list.lastIndexOf(o);
		}
	}

//        public TListDoubleIterator listIterator() {
//            return list.listIterator(); // Must be manually synched by user
//        }

//        public TListDoubleIterator listIterator( int index ) {
//            return list.listIterator( index ); // Must be manually synched by user
//        }

	public TDoubleList subList(int fromIndex, int toIndex) {
		synchronized (mutex) {
			return new TSynchronizedDoubleList(list.subList(fromIndex, toIndex), mutex);
		}
	}

	public double[] toArray(int offset, int len) {
		synchronized (mutex) {
			return list.toArray(offset, len);
		}
	}

	public double[] toArray(double[] dest, int offset, int len) {
		synchronized (mutex) {
			return list.toArray(dest, offset, len);
		}
	}

	public double[] toArray(double[] dest, int source_pos, int dest_pos, int len) {
		synchronized (mutex) {
			return list.toArray(dest, source_pos, dest_pos, len);
		}
	}

	public int indexOf(int offset, double value) {
		synchronized (mutex) {
			return list.indexOf(offset, value);
		}
	}

	public int lastIndexOf(int offset, double value) {
		synchronized (mutex) {
			return list.lastIndexOf(offset, value);
		}
	}

	public void fill(double val) {
		synchronized (mutex) {
			list.fill(val);
		}
	}

	public void fill(int fromIndex, int toIndex, double val) {
		synchronized (mutex) {
			list.fill(fromIndex, toIndex, val);
		}
	}

	public void reverse() {
		synchronized (mutex) {
			list.reverse();
		}
	}

	public void reverse(int from, int to) {
		synchronized (mutex) {
			list.reverse(from, to);
		}
	}

	public void shuffle(Random rand) {
		synchronized (mutex) {
			list.shuffle(rand);
		}
	}

	public void sort() {
		synchronized (mutex) {
			list.sort();
		}
	}

	public void sort(int fromIndex, int toIndex) {
		synchronized (mutex) {
			list.sort(fromIndex, toIndex);
		}
	}

	public int binarySearch(double value) {
		synchronized (mutex) {
			return list.binarySearch(value);
		}
	}

	public int binarySearch(double value, int fromIndex, int toIndex) {
		synchronized (mutex) {
			return list.binarySearch(value, fromIndex, toIndex);
		}
	}

	public TDoubleList grep(TDoubleProcedure condition) {
		synchronized (mutex) {
			return list.grep(condition);
		}
	}

	public TDoubleList inverseGrep(TDoubleProcedure condition) {
		synchronized (mutex) {
			return list.inverseGrep(condition);
		}
	}

	public double max() {
		synchronized (mutex) {
			return list.max();
		}
	}

	public double min() {
		synchronized (mutex) {
			return list.min();
		}
	}

	public double sum() {
		synchronized (mutex) {
			return list.sum();
		}
	}

	public boolean forEachDescending(TDoubleProcedure procedure) {
		synchronized (mutex) {
			return list.forEachDescending(procedure);
		}
	}

	public void transformValues(TDoubleFunction function) {
		synchronized (mutex) {
			list.transformValues(function);
		}
	}

	/**
	 * SynchronizedRandomAccessList instances are serialized as SynchronizedList
	 * instances to allow them to be deserialized in pre-1.4 JREs (which do not have
	 * SynchronizedRandomAccessList). This method inverts the transformation. As a
	 * beneficial side-effect, it also grafts the RandomAccess marker onto
	 * SynchronizedList instances that were serialized in pre-1.4 JREs.
	 *
	 * Note: Unfortunately, SynchronizedRandomAccessList instances serialized in
	 * 1.4.1 and deserialized in 1.4 will become SynchronizedList instances, as this
	 * method was missing in 1.4.
	 */
	private Object readResolve() {
		return (list instanceof RandomAccess ? new TSynchronizedRandomAccessDoubleList(list) : this);
	}
}
