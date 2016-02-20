package agh.intob.jmetalintegration.utils;

/**
 * Class for storing information about pump localization
 */
public class PumpLocation {
	
	private double x;
	private double y;
	private double z;

	/**
	 * Constructor
	 * @param x - x coordinate
	 * @param y - y coordinate
     * @param z - z coordinate
     */
	public PumpLocation(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Gets pump x coordinate
	 * @return x coordinate
     */
	public double getX() {
		return x;
	}

	/**
	 * Gets pump y coordinate
	 * @return y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets pump z coordinate
	 * @return z coordinate
	 */
	public double getZ() {
		return z;
	}
	
}
