
import sim.field.grid.SparseGrid2D;
import spaces.Spaces;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep {
	int n = 100;//number of agents
	
	public SparseGrid2D space;
	public int gridWidth = 100;
	public int gridHeight = 100;
	public double active = 0.0;
	public double p = 0.0; 
	public boolean oneCellPerAgent = false;
	public double aggregate = 0.0;
	public int searchRadius = 1;
	public boolean broadRule = false;
	public boolean bounded = false;
	
//	(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
//            String precision, String[] headers);
	
	public String fileName;
	public String folderName;
	public SimStateSweep state;
	public ParameterSweeper sweeper;
	public String precision;
	public String[] headers;
	
	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}
	
	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}



	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double getActive() {
		return active;
	}

	public void setActive(double active) {
		this.active = active;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public boolean isOneCellPerAgent() {
		return oneCellPerAgent;
	}

	public void setOneCellPerAgent(boolean oneCellPerAgent) {
		this.oneCellPerAgent = oneCellPerAgent;
	}

	public double getAggregate() {
		return aggregate;
	}

	public void setAggregate(double aggregate) {
		this.aggregate = aggregate;
	}

	public int getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}

	public boolean isBroadRule() {
		return broadRule;
	}

	public void setBroadRule(boolean isBroadRule) {
		this.broadRule = isBroadRule;
	}

	public boolean isBounded() {
		return bounded;
	}

	public void setBounded(boolean bounded) {
		this.bounded = bounded;
	}
	
	public void makeAgents() {
		
		Experimenter a = new Experimenter(fileName, folderName, state, sweeper,
	           precision, headers);
		schedule.scheduleRepeating(a);
		sparseSpace.setObjectLocation(a, gridWidth/2, gridHeight/2);
		
		for (int i = 0; i+1 < n; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			boolean frozen = false;
			Experimenter b = new Experimenter(fileName, folderName, state, sweeper,
			           precision, headers);
			schedule.scheduleRepeating(b);
			sparseSpace.setObjectLocation(b, x, y);
		}
	}
	
	
	public void start() {
	       super.start();
	       spaces = Spaces.SPARSE;
	       make2DSpace(spaces, gridWidth, gridHeight);
	       makeAgents();
	       if(observer != null) {
	           observer.initialize(sparseSpace, spaces);//initialize the experimenter by calling initialize in the parent class
	       }
	    }
}
