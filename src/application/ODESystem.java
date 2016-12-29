package application;
/**
 * Ordinary differential equation solver interface
 *
 */
public interface ODESystem {
	/**
	 * Returns the size of the differential system
	 * @return System size
	 */
	int getSystemSize();
	/**
	 * Returns the current solution to each equation
	 * @return The current values which solve the differential equation
	 */
	double[] getVelocity();
	/**
	 * Returns the values of the differential equations
	 * @param time Current time [s]
	 * @param values The solutions to the differential equations from the previous time interval
	 * @return The values of the differential equations at this time
	 */
	double[] getFunction(double time, double[] values);
} // end ODESystem interface

