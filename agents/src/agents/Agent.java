package agents;

import sim.engine.SimState;
import sim.engine.Steppable;

public class Agent implements Steppable {
	int x; // the x location of the agent
	int y; // the y location of the agent
	int xdir; //the direction of movement on the x axis
	int ydir; //
	
	public void move (Env state){
		if (state.random.nextBoolean(state.active)) {
			return;
		}
		x = state.sparseSpace.stx(x+xdir);
		y = state.sparseSpace.stx(y+ydir);
		state.sparseSpace.setObjectLocation(this, x, y);
	}
	
	@Override
	public void step(SimState state) {
		// TODO Auto-generated method stub
		move((Env)state);
	}
	public Agent(int x, int y, int xdir, int ydir) {
		super();
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
	}

}
