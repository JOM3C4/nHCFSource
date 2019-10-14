/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3.mutable;

/**
 * A mutable <code>double</code> wrapper.
 * <p>
 * Note that as MutableDouble does not extend Double, it is not treated by
 * String.format as a Double parameter.
 * 
 * @see Double
 * @since 2.1
 */
public class MutableDouble extends Number implements Comparable<MutableDouble>, Mutable<Number> {

	/**
	 * Required for serialization support.
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1587163916L;

	/** The mutable value. */
	private double value;

	/**
	 * Constructs a new MutableDouble with the default value of zero.
	 */
	public MutableDouble() {
		super();
	}

	/**
	 * Constructs a new MutableDouble with the specified value.
	 * 
	 * @param value the initial value to store
	 */
	public MutableDouble(final double value) {
		super();
		this.value = value;
	}

	/**
	 * Constructs a new MutableDouble with the specified value.
	 * 
	 * @param value the initial value to store, not null
	 * @throws NullPointerException if the object is null
	 */
	public MutableDouble(final Number value) {
		super();
		this.value = value.doubleValue();
	}

	/**
	 * Constructs a new MutableDouble parsing the given string.
	 * 
	 * @param value the string to parse, not null
	 * @throws NumberFormatException if the string cannot be parsed into a double
	 * @since 2.5
	 */
	public MutableDouble(final String value) throws NumberFormatException {
		super();
		this.value = Double.parseDouble(value);
	}

	// -----------------------------------------------------------------------
	/**
	 * Gets the value as a Double instance.
	 * 
	 * @return the value as a Double, never null
	 */
	@Override
	public Double getValue() {
		return Double.valueOf(this.value);
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the value to set
	 */
	public void setValue(final double value) {
		this.value = value;
	}

	/**
	 * Sets the value from any Number instance.
	 * 
	 * @param value the value to set, not null
	 * @throws NullPointerException if the object is null
	 */
	@Override
	public void setValue(final Number value) {
		this.value = value.doubleValue();
	}

	// -----------------------------------------------------------------------
	/**
	 * Checks whether the double value is the special NaN value.
	 * 
	 * @return true if NaN
	 */
	public boolean isNaN() {
		return Double.isNaN(value);
	}

	/**
	 * Checks whether the double value is infinite.
	 * 
	 * @return true if infinite
	 */
	public boolean isInfinite() {
		return Double.isInfinite(value);
	}

	// -----------------------------------------------------------------------
	/**
	 * Increments the value.
	 *
	 * @since Commons Lang 2.2
	 */
	public void increment() {
		value++;
	}

	/**
	 * Increments this instance's value by 1; this method returns the value
	 * associated with the instance immediately prior to the increment operation.
	 * This method is not thread safe.
	 *
	 * @return the value associated with the instance before it was incremented
	 * @since 3.5
	 */
	public double getAndIncrement() {
		final double last = value;
		value++;
		return last;
	}

	/**
	 * Increments this instance's value by 1; this method returns the value
	 * associated with the instance immediately after the increment operation. This
	 * method is not thread safe.
	 *
	 * @return the value associated with the instance after it is incremented
	 * @since 3.5
	 */
	public double incrementAndGet() {
		value++;
		return value;
	}

	/**
	 * Decrements the value.
	 *
	 * @since Commons Lang 2.2
	 */
	public void decrement() {
		value--;
	}

	/**
	 * Decrements this instance's value by 1; this method returns the value
	 * associated with the instance immediately prior to the decrement operation.
	 * This method is not thread safe.
	 *
	 * @return the value associated with the instance before it was decremented
	 * @since 3.5
	 */
	public double getAndDecrement() {
		final double last = value;
		value--;
		return last;
	}

	/**
	 * Decrements this instance's value by 1; this method returns the value
	 * associated with the instance immediately after the decrement operation. This
	 * method is not thread safe.
	 *
	 * @return the value associated with the instance after it is decremented
	 * @since 3.5
	 */
	public double decrementAndGet() {
		value--;
		return value;
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds a value to the value of this instance.
	 * 
	 * @param operand the value to add
	 * @since Commons Lang 2.2
	 */
	public void add(final double operand) {
		this.value += operand;
	}

	/**
	 * Adds a value to the value of this instance.
	 * 
	 * @param operand the value to add, not null
	 * @throws NullPointerException if the object is null
	 * @since Commons Lang 2.2
	 */
	public void add(final Number operand) {
		this.value += operand.doubleValue();
	}

	/**
	 * Subtracts a value from the value of this instance.
	 * 
	 * @param operand the value to subtract, not null
	 * @since Commons Lang 2.2
	 */
	public void subtract(final double operand) {
		this.value -= operand;
	}

	/**
	 * Subtracts a value from the value of this instance.
	 * 
	 * @param operand the value to subtract, not null
	 * @throws NullPointerException if the object is null
	 * @since Commons Lang 2.2
	 */
	public void subtract(final Number operand) {
		this.value -= operand.doubleValue();
	}

	/**
	 * Increments this instance's value by {@code operand}; this method returns the
	 * value associated with the instance immediately after the addition operation.
	 * This method is not thread safe.
	 *
	 * @param operand the quantity to add, not null
	 * @return the value associated with this instance after adding the operand
	 * @since 3.5
	 */
	public double addAndGet(final double operand) {
		this.value += operand;
		return value;
	}

	/**
	 * Increments this instance's value by {@code operand}; this method returns the
	 * value associated with the instance immediately after the addition operation.
	 * This method is not thread safe.
	 *
	 * @param operand the quantity to add, not null
	 * @throws NullPointerException if {@code operand} is null
	 * @return the value associated with this instance after adding the operand
	 * @since 3.5
	 */
	public double addAndGet(final Number operand) {
		this.value += operand.doubleValue();
		return value;
	}

	/**
	 * Increments this instance's value by {@code operand}; this method returns the
	 * value associated with the instance immediately prior to the addition
	 * operation. This method is not thread safe.
	 *
	 * @param operand the quantity to add, not null
	 * @return the value associated with this instance immediately before the
	 *         operand was added
	 * @since 3.5
	 */
	public double getAndAdd(final double operand) {
		final double last = value;
		this.value += operand;
		return last;
	}

	/**
	 * Increments this instance's value by {@code operand}; this method returns the
	 * value associated with the instance immediately prior to the addition
	 * operation. This method is not thread safe.
	 *
	 * @param operand the quantity to add, not null
	 * @throws NullPointerException if {@code operand} is null
	 * @return the value associated with this instance immediately before the
	 *         operand was added
	 * @since 3.5
	 */
	public double getAndAdd(final Number operand) {
		final double last = value;
		this.value += operand.doubleValue();
		return last;
	}

	// -----------------------------------------------------------------------
	// shortValue and byteValue rely on Number implementation
	/**
	 * Returns the value of this MutableDouble as an int.
	 *
	 * @return the numeric value represented by this object after conversion to type
	 *         int.
	 */
	@Override
	public int intValue() {
		return (int) value;
	}

	/**
	 * Returns the value of this MutableDouble as a long.
	 *
	 * @return the numeric value represented by this object after conversion to type
	 *         long.
	 */
	@Override
	public long longValue() {
		return (long) value;
	}

	/**
	 * Returns the value of this MutableDouble as a float.
	 *
	 * @return the numeric value represented by this object after conversion to type
	 *         float.
	 */
	@Override
	public float floatValue() {
		return (float) value;
	}

	/**
	 * Returns the value of this MutableDouble as a double.
	 *
	 * @return the numeric value represented by this object after conversion to type
	 *         double.
	 */
	@Override
	public double doubleValue() {
		return value;
	}

	// -----------------------------------------------------------------------
	/**
	 * Gets this mutable as an instance of Double.
	 *
	 * @return a Double instance containing the value from this mutable, never null
	 */
	public Double toDouble() {
		return Double.valueOf(doubleValue());
	}

	// -----------------------------------------------------------------------
	/**
	 * Compares this object against the specified object. The result is
	 * <code>true</code> if and only if the argument is not <code>null</code> and is
	 * a <code>Double</code> object that represents a double that has the identical
	 * bit pattern to the bit pattern of the double represented by this object. For
	 * this purpose, two <code>double</code> values are considered to be the same if
	 * and only if the method {@link Double#doubleToLongBits(double)}returns the
	 * same long value when applied to each.
	 * <p>
	 * Note that in most cases, for two instances of class
	 * <code>Double</code>,<code>d1</code> and <code>d2</code>, the value of
	 * <code>d1.equals(d2)</code> is <code>true</code> if and only if <blockquote>
	 * 
	 * <pre>
	 * d1.doubleValue() == d2.doubleValue()
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * also has the value <code>true</code>. However, there are two exceptions:
	 * <ul>
	 * <li>If <code>d1</code> and <code>d2</code> both represent
	 * <code>Double.NaN</code>, then the <code>equals</code> method returns
	 * <code>true</code>, even though <code>Double.NaN==Double.NaN</code> has the
	 * value <code>false</code>.
	 * <li>If <code>d1</code> represents <code>+0.0</code> while <code>d2</code>
	 * represents <code>-0.0</code>, or vice versa, the <code>equal</code> test has
	 * the value <code>false</code>, even though <code>+0.0==-0.0</code> has the
	 * value <code>true</code>. This allows hashtables to operate properly.
	 * </ul>
	 * 
	 * @param obj the object to compare with, null returns false
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(final Object obj) {
		return obj instanceof MutableDouble
				&& Double.doubleToLongBits(((MutableDouble) obj).value) == Double.doubleToLongBits(value);
	}

	/**
	 * Returns a suitable hash code for this mutable.
	 * 
	 * @return a suitable hash code
	 */
	@Override
	public int hashCode() {
		final long bits = Double.doubleToLongBits(value);
		return (int) (bits ^ bits >>> 32);
	}

	// -----------------------------------------------------------------------
	/**
	 * Compares this mutable to another in ascending order.
	 * 
	 * @param other the other mutable to compare to, not null
	 * @return negative if this is less, zero if equal, positive if greater
	 */
	@Override
	public int compareTo(final MutableDouble other) {
		return Double.compare(this.value, other.value);
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the String value of this mutable.
	 * 
	 * @return the mutable value as a string
	 */
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
