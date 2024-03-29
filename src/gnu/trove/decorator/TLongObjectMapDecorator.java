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

import gnu.trove.map.TLongObjectMap;
import gnu.trove.iterator.TLongObjectIterator;

import java.io.*;
import java.util.*;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

/**
 * Wrapper class to make a TLongObjectMap conform to the <tt>java.util.Map</tt>
 * API. This class simply decorates an underlying TLongObjectMap and translates
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
public class TLongObjectMapDecorator<V> extends AbstractMap<Long, V>
		implements Map<Long, V>, Externalizable, Cloneable {

	static final long serialVersionUID = 1L;

	/** the wrapped primitive map */
	protected TLongObjectMap<V> _map;

	/**
	 * FOR EXTERNALIZATION ONLY!!
	 */
	public TLongObjectMapDecorator() {
	}

	/**
	 * Creates a wrapper that decorates the specified primitive map.
	 *
	 * @param map the <tt>TLongObjectMap</tt> to wrap.
	 */
	public TLongObjectMapDecorator(TLongObjectMap<V> map) {
		super();
		this._map = map;
	}

	/**
	 * Returns a reference to the map wrapped by this decorator.
	 *
	 * @return the wrapped <tt>TLongObjectMap</tt> instance.
	 */
	public TLongObjectMap<V> getMap() {
		return _map;
	}

	/**
	 * Inserts a key/value pair into the map.
	 *
	 * @param key   an <code>Long</code> value
	 * @param value an <code>Object</code> value
	 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if
	 *         none was found.
	 */
	public V put(Long key, V value) {
		long k;
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
		long k;
		if (key != null) {
			if (key instanceof Long) {
				k = unwrapKey((Long) key);
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
		long k;
		if (key != null) {
			if (key instanceof Long) {
				k = unwrapKey((Long) key);
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
	public Set<Map.Entry<Long, V>> entrySet() {
		return new AbstractSet<Map.Entry<Long, V>>() {
			public int size() {
				return _map.size();
			}

			public boolean isEmpty() {
				return TLongObjectMapDecorator.this.isEmpty();
			}

			@SuppressWarnings("rawtypes")
			public boolean contains(Object o) {
				if (o instanceof Map.Entry) {
					Object k = ((Map.Entry) o).getKey();
					Object v = ((Map.Entry) o).getValue();
					return TLongObjectMapDecorator.this.containsKey(k) && TLongObjectMapDecorator.this.get(k).equals(v);
				} else {
					return false;
				}
			}

			public Iterator<Map.Entry<Long, V>> iterator() {
				return new Iterator<Map.Entry<Long, V>>() {
					private final TLongObjectIterator<V> it = _map.iterator();

					public Map.Entry<Long, V> next() {
						it.advance();
						long k = it.key();
						final Long key = (k == _map.getNoEntryKey()) ? null : wrapKey(k);
						final V v = it.value();
						return new Map.Entry<Long, V>() {
							private V val = v;

							@SuppressWarnings("rawtypes")
							public boolean equals(Object o) {
								return o instanceof Map.Entry && ((Map.Entry) o).getKey().equals(key)
										&& ((Map.Entry) o).getValue().equals(val);
							}

							public Long getKey() {
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

			public boolean add(Map.Entry<Long, V> o) {
				throw new UnsupportedOperationException();
			}

			@SuppressWarnings("unchecked")
			public boolean remove(Object o) {
				boolean modified = false;
				if (contains(o)) {
					// noinspection unchecked
					Long key = ((Map.Entry<Long, V>) o).getKey();
					_map.remove(unwrapKey(key));
					modified = true;
				}
				return modified;
			}

			public boolean addAll(Collection<? extends Map.Entry<Long, V>> c) {
				throw new UnsupportedOperationException();
			}

			public void clear() {
				TLongObjectMapDecorator.this.clear();
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
		return key instanceof Long && _map.containsKey(((Long) key).longValue());
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
	public void putAll(Map<? extends Long, ? extends V> map) {
		Iterator<? extends Entry<? extends Long, ? extends V>> it = map.entrySet().iterator();
		for (int i = map.size(); i-- > 0;) {
			Entry<? extends Long, ? extends V> e = it.next();
			this.put(e.getKey(), e.getValue());
		}
	}

	/**
	 * Wraps a key
	 *
	 * @param k key in the underlying map
	 * @return an Object representation of the key
	 */
	protected Long wrapKey(long k) {
		return Long.valueOf(k);
	}

	/**
	 * Unwraps a key
	 *
	 * @param key wrapped key
	 * @return an unwrapped representation of the key
	 */
	protected long unwrapKey(Long key) {
		return key.longValue();
	}

	// Implements Externalizable
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		// VERSION
		in.readByte();

		// MAP
		// noinspection unchecked
		_map = (TLongObjectMap<V>) in.readObject();
	}

	// Implements Externalizable
	public void writeExternal(ObjectOutput out) throws IOException {
		// VERSION
		out.writeByte(0);

		// MAP
		out.writeObject(_map);
	}

} // TLongObjectHashMapDecorator
