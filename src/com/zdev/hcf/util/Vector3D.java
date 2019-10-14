package com.zdev.hcf.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Vector3D {
	public static final Vector3D ORIGIN;
	private final double x;
	private final double y;
	private final double z;

	static {
		ORIGIN = new Vector3D(0.0, 0.0, 0.0);
	}

	public Vector3D(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	public Vector3D(final Location location) {
		this(location.toVector());
	}

	public Vector3D(final Vector vector) {
		if (vector == null) {
			throw new IllegalArgumentException("Vector cannot be NULL.");
		}
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
	}

	public Vector toVector() {
		return new Vector(this.x, this.y, this.z);
	}

	public Vector3D add(final Vector3D other) {
		if (other == null) {
			throw new IllegalArgumentException("other cannot be NULL");
		}
		return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	public Vector3D add(final double x, final double y, final double z) {
		return new Vector3D(this.x + x, this.y + y, this.z + z);
	}

	public Vector3D subtract(final Vector3D other) {
		if (other == null) {
			throw new IllegalArgumentException("other cannot be NULL");
		}
		return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	public Vector3D subtract(final double x, final double y, final double z) {
		return new Vector3D(this.x - x, this.y - y, this.z - z);
	}

	public Vector3D multiply(final int factor) {
		return new Vector3D(this.x * factor, this.y * factor, this.z * factor);
	}

	public Vector3D multiply(final double factor) {
		return new Vector3D(this.x * factor, this.y * factor, this.z * factor);
	}

	public Vector3D divide(final int divisor) {
		if (divisor == 0) {
			throw new IllegalArgumentException("Cannot divide by null.");
		}
		return new Vector3D(this.x / divisor, this.y / divisor, this.z / divisor);
	}

	public Vector3D divide(final double divisor) {
		if (divisor == 0.0) {
			throw new IllegalArgumentException("Cannot divide by null.");
		}
		return new Vector3D(this.x / divisor, this.y / divisor, this.z / divisor);
	}

	public Vector3D abs() {
		return new Vector3D(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
	}

	@Override
	public String toString() {
		return String.format("[x: %s, y: %s, z: %s]", this.x, this.y, this.z);
	}
}
