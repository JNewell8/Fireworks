package application;
/**
 * This class creates the environment under which the firework will exist.
 * <p>
 * This class contains the wind speed, fluid density and the gravitational constant.
 * <p>
 * The default values are 0 km/h for wind speed, 1.2 kg/m^3 for fluid density and 9.81 m/s^2 for the 
 * gravitational constant.
 * 
 * @author James Newell
 *
 */
public class Environment {
	/**
	 * Gravitaional acceleration on the Earth [m/s^2]
	 */
	public final static double G = 9.807; 
	/**
	 * Air density [kg/m^3]
	 */
	public final static double DENSITY_AIR = 1.2; 
	private final double WIND_MAX = 20; // +/- km/h
	private double wind;

	/**
	 * Environment object constructor 
	 * @param w Horizontal wind speed [km/h]
	 * @throws IllegalWindException If the wind speed is not between -20 and 20 km/h
	 */
	public Environment(double w) throws EnvironmentException{
		if(Math.abs(w) > WIND_MAX)
			throw new EnvironmentException("Illegal value input for wind speed (must be between "+WIND_MAX+" and "+(-WIND_MAX)); 
		wind = w/3.6;
	}// end Environment constructor
	/**
	 * Returns the wind speed of the environment
	 * @return Horizontal wind speed [m/s]
	 */
	public double getWindVelocity(){
		return wind;
	}// end getWind

	/**
	 * Clones the Environment object
	 */
	@Override
	public Environment clone(){
		try {
			return new Environment(wind*3.6);
		} catch (EnvironmentException e) {
			e.printStackTrace();
		}
		return null;
	}// end clone
	public void setWindVelocity(double wind) {
		this.wind = wind / 3.6;
	}
}// end Environment
