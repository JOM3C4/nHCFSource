///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2002, Eric D. Friedman All Rights Reserved.
// Copyright (c) 2009, Robert D. Eden All Rights Reserved.
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

package gnu.trove.decorator;

import gnu.trove.set.TDoubleSet;
import gnu.trove.iterator.TDoubleIterator;

import java.io.*;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

/**
 * Wrapper class to make a TDoubleSet conform to the <tt>java.util.Set</tt> API.
 * This class simply decorates an underlying TDoubleSet and translates the
 * Object-based APIs into their Trove primitive analogs.
 * <p/>
 * <p/>
 * Note that wrapping and unwrapping primitive values is extremely inefficient.
 * If possible, users of this class should override the appropriate methods in
 * this class and use a table of canonical values.
 * </p>
 * <p/>
 * Created: Tue Sep 24 22:08:17 PDT 2002
 *
 * @author Eric D. Friedman
 * @author Robert D. Eden
 * @author Jeff Randall
 */
public class TDoubleSetDecorator extends AbstractSet<Double> implements Set<Double>, Externalizable {

	static final long serialVersionUID = 1L;

	/** the wrapped primitive set */
	protected TDoubleSet _set;

	/**
	 * FOR EXTERNALIZATION ONLY!!
	 */
	public TDoubleSetDecorator() {
	}

	/**
	 * Creates a wrapper that decorates the specified primitive set.
	 *
	 * @param set the <tt>TDoubleSet</tt> to wrap.
	 */
	public TDoubleSetDecorator(TDoubleSet set) {
		super();
		this._set = set;
	}

	/**
	 * Returns a reference to the set wrapped by this decorator.
	 *
	 * @return the wrapped <tt>TDoubleSet</tt> instance.
	 */
	public TDoubleSet getSet() {
		return _set;
	}

	/**
	 * Inserts a value into the set.
	 *
	 * @param value true if the set was modified by the insertion
	 */
	public boolean add(Double value) {
		return value != null && _set.add(value.doubleValue());
	}

	/**
	 * Compares this set with another set for equality of their stored entries.
	 *
	 * @param other an <code>Object</code> value
	 * @return true if the sets are identical
	 */
	public boolean equals(Object other) {
		if (_set.equals(other)) {
			return true; // comparing two trove sets
		} else if (other instanceof Set) {
			Set<?> that = (Set<?>) other;
			if (that.size() != _set.size()) {
				return false; // different sizes, no need to compare
			} else { // now we have to do it the hard way
				Iterator<?> it = that.iterator();
				for (int i = that.size(); i-- > 0;) {
					Object val = it.next();
					if (val instanceof Double) {
						double v = ((Double) val).doubleValue();
						if (_set.contains(v)) {
							// match, ok to continue
						} else {
							return false; // no match: we're done
						}
					} else {
						return false; // different type in other set
					}
				}
				return true; // all entries match
			}
		} else {
			return false;
		}
	}

	/**
	 * Empties the set.
	 */
	public void clear() {
		this._set.clear();
	}

	/**
	 * Deletes a value from the set.
	 *
	 * @param value an <code>Object</code> value
	 * @return true if the set was modified
	 */
	public boolean remove(Object value) {
		return value instanceof Double && _set.remove(((Double) value).doubleValue());
	}

	/**
	 * Creates an iterator over the values of the set.
	 *
	 * @return an iterator with support for removals in the underlying set
	 */
	public Iterator<Double> iterator() {
		return new Iterator<Double>() {
			private final TDoubleIterator it = _set.iterator();

			public Double next() {
				return Double.valueOf(it.next());
			}

			public boolean hasNext() {
				return it.hasNext();
			}

			public void remove() {
				it.remove();
			}
		};
	}

	/**
	 * Returns the number of entries in the set.
	 *
	 * @return the set's size.
	 */
	public int size() {
		return this._set.size();
	}

	/**
	 * Indicates whether set has any entries.
	 *
	 * @return true if the set is empty
	 */
	public boolean isEmpty() {
		return this._set.size() == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		if (!(o instanceof Double))
			return false;
		return _set.contains(((Double) o).doubleValue());
	}

	// Implements Externalizable
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		// VERSION
		in.readByte();

		// SET
		_set = (TDoubleSet) in.readObject();
	}

	// Implements Externalizable
	public void writeExternal(ObjectOutput out) throws IOException {
		// VERSION
		out.writeByte(0);

		// SET
		out.writeObject(_set);
	}
} // TDoubleHashSetDecorator
