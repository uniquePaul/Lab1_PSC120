package agents;

import spaces.Spaces;
import sweep.SimStateSweep;

public class Env extends SimStateSweep {
	int n = 100;//number of agents
	public Env(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}
	
	public void makeAgents() {
		for (int i = 0; i < n; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			Agent a = new Agent(x,y,xdir,ydir);
			schedule.scheduleRepeating(a);
			sparseSpace.setObjectLocation(a, x, y);
		}
	}
	public void start() {
		super.start();
		spaces = Spaces.SPARSE;
		make2DSpace(spaces,gridWidth,gridHeight);
		makeAgents();
	}
}
