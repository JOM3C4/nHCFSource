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

import gnu.trove.map.TObjectFloatMap;
import gnu.trove.iterator.TObjectFloatIterator;

import java.io.*;
import java.util.*;

//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

/**
 * Wrapper class to make a TObjectFloatMap conform to the <tt>java.util.Map</tt>
 * API. This class simply decorates an underlying TObjectFloatMap and translates
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
public class TObjectFloatMapDecorator<K> extends AbstractMap<K, Float>
		implements Map<K, Float>, Externalizable, Cloneable {

	static final long serialVersionUID = 1L;

	/**
	 * the wrapped primitive map
	 */
	protected TObjectFloatMap<K> _map;

	/**
	 * FOR EXTERNALIZATION ONLY!!
	 */
	public TObjectFloatMapDecorator() {
	}

	/**
	 * Creates a wrapper that decorates the specified primitive map.
	 *
	 * @param map the <tt>TObjectFloatMap</tt> to wrap.
	 */
	public TObjectFloatMapDecorator(TObjectFloatMap<K> map) {
		super();
		this._map = map;
	}

	/**
	 * Returns a reference to the map wrapped by this decorator.
	 *
	 * @return the wrapped <tt>TObjectFloatMap</tt> instance.
	 */
	public TObjectFloatMap<K> getMap() {
		return _map;
	}

	/**
	 * Inserts a key/value pair into the map.
	 *
	 * @param key   an <code>Object</code> value
	 * @param value an <code>Float</code> value
	 * @return the previous value associated with <tt>key</tt>, or Integer(0) if
	 *         none was found.
	 */
	public Float put(K key, Float value) {
		if (value == null)
			return wrapValue(_map.put(key, _map.getNoEntryValue()));
		return wrapValue(_map.put(key, unwrapValue(value)));
	}

	/**
	 * Retrieves the value for <tt>key</tt>
	 *
	 * @param key an <code>Object</code> value
	 * @return the value of <tt>key</tt> or null if no such mapping exists.
	 */
	public Float get(Object key) {
		float v = _map.get(key);
		// There may be a false positive since primitive maps
		// cannot return null, so we have to do an extra
		// check here.
		if (v == _map.getNoEntryValue()) {
			return null;
		} else {
			return wrapValue(v);
		}
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
	public Float remove(Object key) {
		float v = _map.remove(key);
		// There may be a false positive since primitive maps
		// cannot return null, so we have to do an extra
		// check here.
		if (v == _map.getNoEntryValue()) {
			return null;
		} else {
			return wrapValue(v);
		}
	}

	/**
	 * Returns a Set view on the entries of the map.
	 *
	 * @return a <code>Set</code> value
	 */
	public Set<Map.Entry<K, Float>> entrySet() {
		return new AbstractSet<Map.Entry<K, Float>>() {
			public int size() {
				return _map.size();
			}

			public boolean isEmpty() {
				return TObjectFloatMapDecorator.this.isEmpty();
			}

			@SuppressWarnings("rawtypes")
			public boolean contains(Object o) {
				if (o instanceof Map.Entry) {
					Object k = ((Map.Entry) o).getKey();
					Object v = ((Map.Entry) o).getValue();
					return TObjectFloatMapDecorator.this.containsKey(k)
							&& TObjectFloatMapDecorator.this.get(k).equals(v);
				} else {
					return false;
				}
			}

			public Iterator<Map.Entry<K, Float>> iterator() {
				return new Iterator<Map.Entry<K, Float>>() {
					private final TObjectFloatIterator<K> it = _map.iterator();

					public Map.Entry<K, Float> next() {
						it.advance();
						final K key = it.key();
						final Float v = wrapValue(it.value());
						return new Map.Entry<K, Float>() {
							private Float val = v;

							@SuppressWarnings("rawtypes")
							public boolean equals(Object o) {
								return o instanceof Map.Entry && ((Map.Entry) o).getKey().equals(key)
										&& ((Map.Entry) o).getValue().equals(val);
							}

							public K getKey() {
								return key;
							}

							public Float getValue() {
								return val;
							}

							public int hashCode() {
								return key.hashCode() + val.hashCode();
							}

							public Float setValue(Float value) {
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

			public boolean add(Map.Entry<K, Float> o) {
				throw new UnsupportedOperationException();
			}

			@SuppressWarnings("unchecked")
			public boolean remove(Object o) {
				boolean modified = false;
				if (contains(o)) {
					// noinspection unchecked
					K key = ((Map.Entry<K, Float>) o).getKey();
					_map.remove(key);
					modified = true;
				}
				return modified;
			}

			public boolean addAll(Collection<? extends Map.Entry<K, Float>> c) {
				throw new UnsupportedOperationException();
			}

			public void clear() {
				TObjectFloatMapDecorator.this.clear();
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
		return val instanceof Float && _map.containsValue(unwrapValue(val));
	}

	/**
	 * Checks for the present of <tt>key</tt> in the keys of the map.
	 *
	 * @param key an <code>Object</code> value
	 * @return a <code>boolean</code> value
	 */
	public boolean containsKey(Object key) {
		return _map.containsKey(key);
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
		return this._map.size() == 0;
	}

	/**
	 * Copies the key/value mappings in <tt>map</tt> into this map. Note that this
	 * will be a <b>deep</b> copy, as storage is by primitive value.
	 *
	 * @param map a <code>Map</code> value
	 */
	public void putAll(Map<? extends K, ? extends Float> map) {
		Iterator<? extends Entry<? extends K, ? extends Float>> it = map.entrySet().iterator();
		for (int i = map.size(); i-- > 0;) {
			Entry<? extends K, ? extends Float> e = it.next();
			this.put(e.getKey(), e.getValue());
		}
	}

	/**
	 * Wraps a value
	 *
	 * @param k value in the underlying map
	 * @return an Object representation of the value
	 */
	protected Float wrapValue(float k) {
		return Float.valueOf(k);
	}

	/**
	 * Unwraps a value
	 *
	 * @param value wrapped value
	 * @return an unwrapped representation of the value
	 */
	protected float unwrapValue(Object value) {
		return ((Float) value).floatValue();
	}

	// Implements Externalizable
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

		// VERSION
		in.readByte();

		// MAP
		// noinspection unchecked
		_map = (TObjectFloatMap<K>) in.readObject();
	}

	// Implements Externalizable
	public void writeExternal(ObjectOutput out) throws IOException {
		// VERSION
		out.writeByte(0);

		// MAP
		out.writeObject(_map);
	}

} // TObjectFloatMapDecorator
