package khModel;

import observer.Observer;
import sim.engine.SimState;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer {
	// Variables used to calculate correlations during simulations
	int n = 0;
	double sX = 0;
	double sY = 0;
	double sX2 = 0;
	double sY2 = 0;
	double sXY = 0;

	public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
			String precision, String[] headers) {
		super(fileName, folderName, state, sweeper, precision, headers);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Resets the variables used in calculating correlations.
	 */
	public void resetVariables() {
		n = 0;
		sX = 0;
		sY = 0;
		sX2 = 0;
		sY2 = 0;
		sXY = 0;
	}

	/**
	 * Takes two agents and gets their attractiveness values and then does the
	 * calculation for preparing the data to calculate a correlation value
	 * 
	 * @param x
	 * @param y
	 */
	public void getData(Agent x, Agent y) {
		getData(x.attractiveness, y.attractiveness);
	}

	/**
	 * Helper method for the above getData(Agent x, Agent y). Updates the variables
	 * used in calculating correlations.
	 * 
	 * @param x
	 * @param y
	 */
	public void getData(double x, double y) {
		sXY += x * y;
		sX += x;
		sY += y;
		sX2 += x * x;
		sY2 += y * y;
		n++;

	}

	/**
	 * Computational correlation formula
	 * 
	 * @return
	 */
	public double correlation() {
		return (sXY - (sX * sY) / n) / Math.sqrt((sX2 - (sX * sX) / n) * (sY2 - (sY * sY) / n));
	}

	/**
	 * Method for printing out the data
	 * 
	 * @param state
	 */

	public void printData(Environment state) {
		int percent = (int) ((n / (double) state.females) * 100.0);
		if (n > 1) {
			System.out.println(state.schedule.getSteps() + "      " + percent + "      " + correlation() + "     "
					+ (sX + sY) / (2 * n));
		}
	}

	/**
	 * Stops the experimenter and removes it from the schedule.
	 * 
	 * @param state
	 */
	public void stop(Environment state) {

		Bag agents = state.sparseSpace.getAllObjects();
		if (agents == null || agents.numObjs == 0) {
			this.event.stop();
		}
	}

	/**
	 * The experimenter with the populations method prepares the agents for the next
	 * time-step of dating.
	 * 
	 * @param state
	 */
	public void populations(Environment state) {
		Bag tempMale, tempFemale;// temp bags to hold agents
		tempMale = state.male; // The male population just finished
		tempFemale = state.female;// The female population just finished
		state.male = state.nextMale;// switch bags for the next male population
		state.female = state.nextFemale;// switch bags for the next female population
		state.nextFemale = tempFemale;// take the old female population and set it to the next
		state.nextMale = tempMale;// take the old male population and set it to the next
		state.nextFemale.clear();// clear the bag to put in the next population females
		state.nextMale.clear();// clear the bag to put in the next population of males
		for (int i = 0; i < state.male.numObjs; i++) {
			Agent a = (Agent) state.male.objs[i];
			a.dated = false;// mark all males as not dated
		}
		for (int i = 0; i < state.female.numObjs; i++) {
			Agent a = (Agent) state.female.objs[i];
			a.dated = false;// mark all females as not dated
		}
	}

	public void pairCorrelation(Environment state) {
		double time = (double) state.schedule.getTime();// get the current time
		this.upDateTimeChart(0, time, correlation(), true, 1000);// update chart #0 with up to a 1000 millisecond delay
	}

	public void attractivness(Environment state) {
		// TODO: take a look at the method 'pairCorrelation',
		// this is what you want to plot: (sX+sY)/(2*n)
		// You should check that n > 0, before dividing and plotting
		double time = (double) state.schedule.getTime();// get the current time
		if (n > 0) {
			double result = (sX + sY) / (2 * n);
			this.upDateTimeChart(1, time, result, true, 1000);// update chart #0 with up to a 1000 millisecond delay
		}
	}

	public void attractivenessDistribution(Environment state) {
		Bag agents = state.sparseSpace.allObjects;// get remaining agents
		double[] data = new double[agents.numObjs];// This is where attractiveness scores are placed
		for (int i = 0; i < data.length; i++) {
			// TODO: fill the data array with the attractiveness scores for
			// the remaining agents that are in the "agents" bag
			Agent a = (Agent)agents.objs[i];
			data[i]= a.attractiveness;
		}
		if (agents.numObjs > 0)
			this.upDateHistogramChart(0, (int) state.schedule.getSteps(), data, 10);// give it the data with a 10
																					// millisecond delay
	}

	public void step(SimState state) {
		super.step(state);
		Environment environment = (Environment) state;
		stop(environment);
		if (step % this.state.dataSamplingInterval == 0) {// If a sampling interval, record data
			// set the dataSamplingInterval to 1 in the model tab window when you run this
			// So that it plots every step
			printData(environment);
			
			pairCorrelation((Environment) state);
			attractivness((Environment) state);
			attractivenessDistribution((Environment) state);
		}
		populations(environment);
	}
}
