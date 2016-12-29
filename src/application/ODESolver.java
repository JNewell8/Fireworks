package application;
/**
 * Ordinary differential equation solver class. Solves a differential equation system as set by the 
 * ODESystem interface. Uses the Runge-Kutta method for solving
 * 
 * @author James
 *
 */
public class ODESolver {
	/**
	 * Solves the ordinary differential equation for the given parameters
	 * @param ODE Class that implements the ODESystem interface
	 * @param time Current time [s]
	 * @param DT Time step [s]
	 * @return New value for the time increase
	 */
	public static double[] getNext(ODESystem ODE, double time, double DT){
		int size = ODE.getSystemSize();
		double[] q1 = new double[size];
		double[] q2 = new double[size];
		double[] q3 = new double[size];
		double[] q4 = new double[size];
		double[] tempVel = new double[size];
		double[] vals = ODE.getVelocity();
		
		double htime = time +DT/2;
		time = time + DT;
		q1 = ODE.getFunction(time, vals);
		int i;
		for (i = 0; i < size; i++)
			tempVel[i] = vals[i] + DT * q1[i] / 2;
		q2 = ODE.getFunction(htime, tempVel);
		for (i = 0; i < size; i++)
			tempVel[i] = vals[i] + DT * q2[i] / 2;
		q3 = ODE.getFunction(htime, tempVel);
		for (i = 0; i < size; i++)
			tempVel[i] = vals[i] + DT * q3[i];
		q4 = ODE.getFunction(time, tempVel);
		for (i = 0; i < size; i++)
			vals[i] = vals[i] + (DT / 6.0) * (q1[i] + 2 * q2[i] + 2 * q3[i] + q4[i]);
		return vals;	
	}// end getNext
}// end ODESolver
