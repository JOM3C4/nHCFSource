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

import gnu.trove.map.TByteObjectMap;
import gnu.trove.iterator.TByteObjectIterator;

import java.io.*;
import java.util.*;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

/**
 * Wrapper class to make a TByteObjectMap conform to the <tt>java.util.Map</tt>
 * API. This class simply decorates an underlying TByteObjectMap and translates
 * the Object-based APIs into their Trove primitive analogs.
 * <p/>
 * Note that wrapping and unwrapping primitive values is extremely inefficient.
 * If possible, users of this class should override the appropriate methods in
 * this class and use a table of canonical values.
 * <p/>
 * Created: Mon Sep 23 22:07:40 PDT 2002
 *
 * @author Eric D. Friedman
 * @author Robert D. Eden
 * @author Jeff Randall
 */
public class TByteObjectMapDecorator<V> extends AbstractMap<Byte, V>
		implements Map<Byte, V>, Externalizable, Cloneable {

	static final long serialVersionUID = 1L;

	/** the wrapped primitive map */
	protected TByteObjectMap<V> _map;

	/**
	 * FOR EXTERNALIZATION ONLY!!
	 */
	public TByteObjectMapDecorator() {
	}

	/**
	 * Creates a wrapper that decorates the specified primitive map.
	 *
	 * @param map the <tt>TByteObjectMap</tt> to wrap.
	 */
	public TByteObjectMapDecorator(TByteObjectMap<V> map) {
		super();
		this._map = map;
	}

	/**
	 * Returns a reference to the map wrapped by this decorator.
	 *
	 * @return the wrapped <tt>TByteObjectMap</tt> instance.
	 */
	public TByteObjectMap<V> getMap() {
		return _map;
	}

	/**
	 * Inserts a key/value pair into the map.
	 *
	 * @param key   an <code>Byte</code> value
	 * @param value an <code>Object</code> value
	 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if
	 *         none was found.
	 */
	public V put(Byte key, V value) {
		byte k;
		if (key == null) {
			k = _map.getNoEntryKey();
		} else {
			k = unwrapKey(key);
		}
		return _map.put(k, value);
	}

	/**
	 * Retrieves the value for <tt>key</tt>
	 *
	 * @param key an <code>Object</code> value
	 * @return the value of <tt>key</tt> or null if no such mapping exists.
	 */
	public V get(Object key) {
		byte k;
		if (key != null) {
			if (key instanceof Byte) {
				k = unwrapKey((Byte) key);
			} else {
				return null;
			}
		} else {
			k = _map.getNoEntryKey();
		}
		return _map.get(k);
	}

	/**
	 * Empties the map.
	 */
	public void clear() {
		this._map.clear();
	}

	/**
	 * Deletes a key/value pair from the map.
	 *
	 * @param key an <code>Object</code> value
	 * @return the removed value, or Integer(0) if it was not found in the map
	 */
	public V remove(Object key) {
		byte k;
		if (key != null) {
			if (key instanceof Byte) {
				k = unwrapKey((Byte) key);
			} else {
				return null;
			}
		} else {
			k = _map.getNoEntryKey();
		}
		return _map.remove(k);
	}

	/**
	 * Returns a Set view on the entries of the map.
	 *
	 * @return a <code>Set</code> value
	 */
	public Set<Map.Entry<Byte, V>> entrySet() {
		return new AbstractSet<Map.Entry<Byte, V>>() {
			public int size() {
				return _map.size();
			}

			public boolean isEmpty() {
				return TByteObjectMapDecorator.this.isEmpty();
			}

			@SuppressWarnings("rawtypes")
			public boolean contains(Object o) {
				if (o instanceof Map.Entry) {
					Object k = ((Map.Entry) o).getKey();
					Object v = ((Map.Entry) o).getValue();
					return TByteObjectMapDecorator.this.containsKey(k) && TByteObjectMapDecorator.this.get(k).equals(v);
				} else {
					return false;
				}
			}

			public Iterator<Map.Entry<Byte, V>> iterator() {
				return new Iterator<Map.Entry<Byte, V>>() {
					private final TByteObjectIterator<V> it = _map.iterator();

					public Map.Entry<Byte, V> next() {
						it.advance();
						byte k = it.key();
						final Byte key = (k == _map.getNoEntryKey()) ? null : wrapKey(k);
						final V v = it.value();
						return new Map.Entry<Byte, V>() {
							private V val = v;

							@SuppressWarnings("rawtypes")
							public boolean equals(Object o) {
								return o instanceof Map.Entry && ((Map.Entry) o).getKey().equals(key)
										&& ((Map.Entry) o).getValue().equals(val);
							}

							public Byte getKey() {
								return key;
							}

							public V getValue() {
								return val;
							}

							public int hashCode() {
								return key.hashCode() + val.hashCode();
							}

							public V setValue(V value) {
								val = value;
								return put(key, value);
							}
						};
					}

					public boolean hasNext() {
						return it.hasNext();
					}

					public void remove() {
						it.remove();
					}
				};
			}

			public boolean add(Map.Entry<Byte, V> o) {
				throw new UnsupportedOperationException();
			}

			@SuppressWarnings("unchecked")
			public boolean remove(Object o) {
				boolean modified = false;
				if (contains(o)) {
					// noinspection unchecked
					Byte key = ((Map.Entry<Byte, V>) o).getKey();
					_map.remove(unwrapKey(key));
					modified = true;
				}
				return modified;
			}

			public boolean addAll(Collection<? extends Map.Entry<Byte, V>> c) {
				throw new UnsupportedOperationException();
			}

			public void clear() {
				TByteObjectMapDecorator.this.clear();
			}
		};
	}

	/**
	 * Checks for the presence of <tt>val</tt> in the values of the map.
	 *
	 * @param val an <code>Object</code> value
	 * @return a <code>boolean</code> value
	 */
	public boolean containsValue(Object val) {
		return _map.containsValue(val);
	}

	/**
	 * Checks for the present of <tt>key</tt> in the keys of the map.
	 *
	 * @param key an <code>Object</code> value
	 * @return a <code>boolean</code> value
	 */
	public boolean containsKey(Object key) {
		if (key == null)
			return _map.containsKey(_map.getNoEntryKey());
		return key instanceof Byte && _map.containsKey(((Byte) key).byteValue());
	}

	/**
	 * Returns the number of entries in the map.
	 *
	 * @return the map's size.
	 */
	public int size() {
		return this._map.size();
	}

	/**
	 * Indicates whether map has any entries.
	 *
	 * @return true if the map is empty
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Copies the key/value mappings in <tt>map</tt> into this map. Note that this
	 * will be a <b>deep</b> copy, as storage is by primitive value.
	 *
	 * @param map a <code>Map</code> value
	 */
	public void putAll(Map<? extends Byte, ? extends V> map) {
		Iterator<? extends Entry<? extends Byte, ? extends V>> it = map.entrySet().iterator();
		for (int i = map.size(); i-- > 0;) {
			Entry<? extends Byte, ? extends V> e = it.next();
			this.put(e.getKey(), e.getValue());
		}
	}

	/**
	 * Wraps a key
	 *
	 * @param k key in the underlying map
	 * @return an Object representation of the key
	 */
	protected Byte wrapKey(byte k) {
		return Byte.valueOf(k);
	}

	/**
	 * Unwraps a key
	 *
	 * @param key wrapped key
	 * @return an unwrapped representation of the key
	 */
	protected byte unwrapKey(Byte key) {
		return key.byteValue();
	}

	// Implements Externalizable
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		// VERSION
		in.readByte();

		// MAP
		// noinspection unchecked
		_map = (TByteObjectMap<V>) in.readObject();
	}

	// Implements Externalizable
	public void writeExternal(ObjectOutput out) throws IOException {
		// VERSION
		out.writeByte(0);

		// MAP
		out.writeObject(_map);
	}

} // TByteObjectHashMapDecorator
