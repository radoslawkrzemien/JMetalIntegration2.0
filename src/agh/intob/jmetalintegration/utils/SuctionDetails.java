package agh.intob.jmetalintegration.utils;

/**
 * Class containing data about suction specification
 */
public class SuctionDetails {
	
	private double x;
	private double y;
	private double z;

	/**
	 * Constructor
	 * @param x - x coordinate
	 * @param y - y coordinate
     * @param z - z coordinate
     */
	public SuctionDetails(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Gets suction x coordinate
	 * @return x coordinate
     */
	public double getX() {
		return x;
	}

	/**
	 * Gets suction y coordinate
	 * @return y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets suction z coordinate
	 * @return z coordinate
	 */
	public double getZ() {
		return z;
	}
	
}
