package khModel;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import java.util.Random;

public class Agent implements Steppable {
	int x;//x,y coordinates
	int y;
	int dirx;//the direction of movement
	int diry;
	//KH model
	public boolean female;//determines whether an agent is a female == true, male == false
	public double attractiveness;//Attractiveness of this agent
	public double dates = 0;//starts with 0 and incremented by 1 with each date.
	public boolean dated = false;//flag for dating on each round
	public Stoppable event;//allows to remove an agent from the simulation.

	
	public Agent(int x, int y, int dirx, int diry, boolean female, double attractiveness) {
		super();
		this.x = x;
		this.y = y;
		this.dirx = dirx;
		this.diry = diry;
		this.female = female;
		this.attractiveness = attractiveness;
	}
	
    public Agent(boolean female, double attractiveness) {
		super();
		this.female = female;
		this.attractiveness = attractiveness;
	}

	public Agent(int x, int y, boolean female, double attractiveness) {
		super();
		this.x = x;
		this.y = y;
		this.female = female;
		this.attractiveness = attractiveness;
	}
     
	/**
	 * Finds a date for an agent of the opposite sex randomly from the male or female populations.
	 * @param state
	 * @return
	 */
	public Agent findDate(Environment state) {
		if(female ) {//agent gender
			if(state.male.numObjs==0)
				return null;//if empty return null
			return (Agent)state.male.objs[state.random.nextInt(state.male.numObjs)];
		}
		else {
			if(state.female.numObjs==0)
				return null;
			return (Agent)state.female.objs[state.random.nextInt(state.female.numObjs)];
		}
	}
	
	/**
	 * This method finds a date from the local neighborhood in space of this agent.
	 * @param state
	 * @return
	 */
	
	public Agent findLocalDate(Environment state) {
		Bag neighbors = state.sparseSpace.getMooreNeighbors(x, y, state.searchRadius, state.sparseSpace.TOROIDAL, false);
		//draw agents randomly from the bag if it is not empty.  If an agent is of the opposite sex and not dated, then
		//return it.
		while(!neighbors.isEmpty()) {
			Agent a = (Agent) neighbors.objs[state.random.nextInt(neighbors.objs.length)];
			if(this.female) {
				if(!a.female&&!a.dated) {
					return a;
				}
			}else {
				if(a.female&&!a.dated) {
					return a;
				}
			}
			neighbors.remove(a);
		}
		return null;
	}
	
	
	/**
	 * KH closing time rule
	 * @param state
	 * @param p
	 * @return
	 */
	public double ctRule(Environment state, double p) {
		return Math.pow(p, ct(state));
	}
	
	public double ct(Environment state) {
		if (state.maxDates-dates > 0)
			return (state.maxDates-dates)/state.maxDates;
		else
			return 0.0;
	}
	/**
	 * Attractiveness rule
	 * @param state
	 * @param a
	 * @return
	 */
	public double p1(Environment state, Agent a) {
		return Math.pow(a.attractiveness, state.choosiness)/Math.pow(state.maxAttractiveness, state.choosiness);
	}
	
	public double p2(Environment state, Agent a) {
		return Math.pow(state.maxAttractiveness-Math.abs(this.attractiveness - a.attractiveness), state.choosiness)/
				Math.pow(state.maxAttractiveness, state.choosiness);
	}
	
	/**
	 * Mixed rule
	 * @param state
	 * @param a
	 * @return
	 */
	public double p3(Environment state, Agent a) {
		return (p1(state,a)+p2(state,a))/2.0;
	}
	
	/**
	 * Implements the frustration rule;
	 * @param state
	 * @param a
	 * @return
	 */
	
	public double p4(Environment state, Agent a) {
		//TODO
		double FR = 0;
		if (dates <= state.fMax) {
			FR = (state.fMax - (double)dates)/(state.fMax);
		}

		return (FR*p1(state,a) +(1-FR)*p2(state,a) ); //when finished this should return a probability you calculate
	}
	
	public void remove(Environment state) {
		if(female) {
			state.female.remove(this);//remove from the population
			replicate(state, true);
			}
		else {
			state.male.remove(this);
			replicate(state, false);
		}
		state.sparseSpace.remove(this);//remove it from space
		
		event.stop();//remove from the schedule
	}
	
	public void replicate (Environment state, boolean gender) {
		if (state.replacement == true) {
		Random ran = new Random();
		double attractiveness = ran.nextInt((int)state.maxAttractiveness)+1;
		
		if (gender == true) {
			int x = ran.nextInt(state.gridWidth);
			int y = ran.nextInt(state.gridHeight);
			Agent f = new Agent(x, y, true,attractiveness);
			f.event = state.schedule.scheduleRepeating(f);
			state.sparseSpace.setObjectLocation(f,ran.nextInt(state.gridWidth), ran.nextInt(state.gridHeight));
			state.gui.setOvalPortrayal2DColor(f, (float)1, (float)0, (float)0, (float)(attractiveness/state.maxAttractiveness));
			state.female.add(f);
		}else {
			int x = ran.nextInt(state.gridWidth);
			int y = ran.nextInt(state.gridHeight);
			Agent m = new Agent(x, y, false,attractiveness);
			m.event = state.schedule.scheduleRepeating(m);
			state.sparseSpace.setObjectLocation(m,ran.nextInt(state.gridWidth), ran.nextInt(state.gridHeight));
			state.gui.setOvalPortrayal2DColor(m, (float)0, (float)0, (float)1, (float)(attractiveness/state.maxAttractiveness));
			state.male.add(m);
		}
		}else {
			
		}
	}
	
	
	public void nextPopulationStep(Environment state) {
		dated = true; //set dated to true.
		if(female) {
			state.nextFemale.add(this);
			state.female.remove(this);
		}
		else {
			state.nextMale.add(this);
			state.male.remove(this);
		}
	}
	
	public void dateNonSpatial(Environment state, Agent a) {
		double p;
		double q;
		switch(state.rule) {
		case ATTRACTIVE:
			p = p1(state,a);
			q = a.p1(state, this);
			break;
		case SIMILAR:
			p = p2(state,a);
			q = a.p2(state, this);
			break;
		case MIXED:
			p = p3(state,a);
			q = a.p3(state, this);
			break;
		case FRUSTRATED:
			p = p4(state,a);
			q = a.p4(state, this);
			break;
		default:
			p = p1(state,a);
			q = a.p1(state, this);
			break;
		}
		p = ctRule(state,p);
		q = ctRule(state,q);

		if(state.random.nextBoolean(p)&& state.random.nextBoolean(q)) {//couple decison
			if(female) {
				state.experimenter.getData(this, a);
			}
			else {
				state.experimenter.getData(a, this);
			}
			remove(state);
			a.remove(state);
		} //end if test
		else {
			this.nextPopulationStep(state);
			a.nextPopulationStep(state);
		}
		if(dates < state.maxDates) {
			dates++;
		}
		if(a.dates < state.maxDates) {
			a.dates++;
		}
		
	}
	
	/**
	 * Handles dates for non-spatial and spatial models
	 * @param state
	 */
	public void date(Environment state) {
		if(state.nonSpatialModel) {
			Agent a = findDate(state);
			if(a!= null) {
				dateNonSpatial(state, a);
			}
		}
		else {
			Agent a = findLocalDate(state);
			if(a!= null) {
				dateNonSpatial(state, a);
			}
		}
	}

	public void placeAgent(Environment state) {
        if(state.oneCellPerAgent) {//only one agent per cell
             int tempx = state.sparseSpace.stx(x + dirx);//tempx and tempy location
             int tempy = state.sparseSpace.sty(y + diry);
             Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);
             if(b == null){//if empty, agent moves to new location
                   x = tempx;
                   y = tempy;
                   state.sparseSpace.setObjectLocation(this, x, y);
             }//otherwise it does not move.
        }
        else {               
             x = state.sparseSpace.stx(x + dirx);
             y = state.sparseSpace.sty(y + diry);
             state.sparseSpace.setObjectLocation(this, x, y);
        }
   }
	/**
	 * Agents move randomly to a new location for either one agent per cell or possibly
	 * multiple agents per cell.
	 * @param state
	 */
	public void move(Environment state) {
		if(!state.random.nextBoolean(state.active)) {
			return;
		}
		if(state.random.nextBoolean(state.p)) {
			dirx = state.random.nextInt(3)-1;
			diry = state.random.nextInt(3)-1;
		}
		placeAgent(state);
	}
	
	
	public int decideX(Environment state, Bag neighbors) {
		int posX =0, negX =0;
		for(int i=0;i<neighbors.numObjs;i++) {
			Agent a = (Agent)neighbors.objs[i];
			if(a.x > x) {
				posX++;
			}
			else if(a.x < x) {
				negX++;
			}
		}
		if(posX > negX) {
			return 1;
		}
		else if (negX > posX) {
			return -1;
		}
		else {
			return state.random.nextInt(3)-1;
		}
	}
	
	public int decideY(Environment state, Bag neighbors) {
		int posY =0, negY =0;
		for(int i=0;i<neighbors.numObjs;i++) {
			Agent a = (Agent)neighbors.objs[i];
			if(a.y > y) {
				posY++;
			}
			else if(a.y < y) {
				negY++;
			}
		}
		if(posY > negY) {
			return 1;
		}
		else if (negY > posY) {
			return -1;
		}
		else {
			return state.random.nextInt(3)-1;
		}
	}
	
	public void aggregate (Environment state) {
		Bag b = state.sparseSpace.getMooreNeighbors(x, y, state.searchRadius, state.sparseSpace.TOROIDAL, false);
		dirx = decideX(state,b);
		diry = decideY(state,b);
		placeAgent(state);
	}

	public void step(SimState state) {
		Environment environment = (Environment)state;
		
		//You will have to uncomment the code below to simulate movement in space and aggregation
		
		if(environment.random.nextBoolean(environment.aggregate)) {
			aggregate (environment);
		}
		else {
			move(environment);
		}
		
		if(!dated)
			date(environment);
		
	}

}