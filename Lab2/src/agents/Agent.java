package agents;
import java.util.Random;
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
		 System.out.println("**************new Rounds of step****************");
		 if(this.frozen) {
			 return;
		 }
		 
		 Bag neighbors;
		 Random ran = new Random();
		 int newXdir = ran.nextInt(3)-1;
		 int newYdir = ran.nextInt(3)-1;

		 
		 int predicated_x, predicated_y;
	     if (state.bounded) {	    	 
	    	 if(x+newXdir>=state.gridWidth) {
	    		 newXdir = -newXdir; 
	    	 }
	    	 if(y+newYdir>=state.gridHeight) {
	    		 newYdir = -newYdir; 
	    	 }
	    	 if(x+newXdir<=0) {
	    		 newXdir = -newXdir; 
	    	 }
	    	 if(y+newYdir<=0) {
	    		 newYdir = -newYdir; 
	    	 }
			  predicated_x = x+newXdir;
			  predicated_y = y+newYdir;
	     }else {
			  predicated_x = state.sparseSpace.stx(x+ newXdir);
		      predicated_y = state.sparseSpace.stx(y+ newYdir);
	     }
	     
	     Bag b = state.sparseSpace.getObjectsAtLocation(predicated_x, predicated_y);
	     
	    if(!state.broadRule) {
			 if(b!= null) { // in narrow rule
				 
			      if(((Agent)b.toArray()[0]).frozen == true) {
			    	  System.out.println("Hello");
			    	  	this.frozen = true;
			    
			      }
			 }
			 else {
			    	  x = predicated_x;
			    	  y = predicated_y;
			    	  state.sparseSpace.setObjectLocation(this, x,  y);
			      }    	
	    }else {
			 // in broad rule
	    		if(state.bounded) {
	    			neighbors =  state.sparseSpace.getMooreNeighbors(x, y, 1, SparseGrid2D.BOUNDED, false);
	    		}else {
	    			neighbors =  state.sparseSpace.getMooreNeighbors(x, y, 1, SparseGrid2D.TOROIDAL, false);
	    		}
	    		
				System.out.println(neighbors.toArray().length);
				
				for (int i = 0; i < neighbors.toArray().length; i++) {
					

					if (((Agent)neighbors.toArray()[i]).frozen) {	
						this.frozen= true;
					
						System.out.println("broad");
						return;
					}

				}
		    	 state.sparseSpace.setObjectLocation(this, predicated_x, predicated_y);
		    	 x = predicated_x;
		    	 y = predicated_y;
		    	 System.out.println("wuhu");
	    }
	}
	
	@Override
	public void step(SimState state) {
		// TODO Auto-generated method stub
		move((Environment)state);
	}
	
	public Agent(int x, int y, int xdir, int ydir, boolean frozen) {
		super();
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
		this.frozen= frozen;
	}

}
