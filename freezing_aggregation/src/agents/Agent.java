package agents;

import sim.engine.SimState;
import sim.util.Bag;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;

public class Agent implements Steppable {
	int x; // the x location of the agent
	int y; // the y location of the agent
	int xdir; //the direction of movement on the x axis
	int ydir; //
	boolean frozen = false; //in agent class
	
	public void move (Environment state){
//		if (state.random.nextBoolean(state.active)) {
//			return;
//		}
		
		 int predicated_x = state.sparseSpace.stx(x+xdir);
	     int predicated_y = state.sparseSpace.stx(y+ydir);
	     Bag b = state.sparseSpace.getObjectsAtLocation(predicated_x, predicated_y);
	     Bag neighbors =  state.sparseSpace.getMooreNeighbors(x, y, 1, SparseGrid2D.TOROIDAL, false);
		if(!(this.frozen)) { // in narrow rule

		      if((b.toArray().length > 0) && ((Agent)b.toArray()[0]).frozen == false)  {
		    	  	frozen = true;
		      }

		      if((b.toArray().length == 0) || ((Agent)b.toArray()[0]).frozen == true){

		    	  state.sparseSpace.setObjectLocation(this, x, y);
		    	 

		}}

		if(state.broadRule) { // in broad rule
			boolean frozenNeighbors = false;
			for (int i = 0; i < neighbors.toArray().length; i++) {
				if (((Agent)neighbors.toArray()[i]).frozen) {
					frozenNeighbors = true;
				} 
			}
		      if(frozenNeighbors == true) { //multiple acceptable way to do this 

		     frozen = true;}

		     else {

		    	 state.sparseSpace.setObjectLocation(this, x, y);

		}
		x = predicated_x;
		y = predicated_y;
		state.sparseSpace.setObjectLocation(this, x, y);
	}
		
		
//		int predicated_x = state.sparseSpace.stx(x+xdir);
//	     int predicated_y = state.sparseSpace.stx(y+ydir);
//	     state.sparseSpace.setObjectLocation(this, predicated_x, predicated_y);
//	     
//	     Bag b = state.sparseSpace.getObjectsAtLocation(predicated_x, predicated_y);
//	     System.out.println(b.toArray()[0]);
//	     System.out.println("asd");
	}
	
	@Override
	public void step(SimState state) {
		// TODO Auto-generated method stub
		move((Environment)state);
	}
	public Agent(int x, int y, int xdir, int ydir) {
		super();
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
	}

}
