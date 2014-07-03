package ageless;

import ageless.things.Colony;
import ageless.things.Factory;
import ageless.things.Mineral;
import ageless.things.Unit;

public class Game {
	private GameWorld world;
	private GameWorld netWorld;
	private GameNetwork network;
	private GamePlayer player;
	private boolean gameExited = false;

	public Game() {
		this.network = null;
		this.netWorld = null;
		this.world = createStartingWorld();
		
		world.solvePhysics();
	}

	public Game(GameNetwork network) {
		this();
		this.network = network;
		this.netWorld = world.createCopy();
	}
	
	public GamePlayer getPlayer() {
		return player;
	}

	public GameWorld createStartingWorld() {
		GameWorld startingWorld;
		GameState startingState;
		int randomID;
		
		
		
		randomID = (int) (Math.random() * 0xFFFFFFFF);
		startingState = new GameState();
		startingWorld = new GameWorld(randomID, startingState);

		this.player = new GamePlayer(0x01);
		startingWorld.addPlayer(this.player);
		
		Colony colony;
		
		colony = new Colony(player.createThingID());
		colony.setPosition(320, 240);
		startingState.addThing(colony);
		
		Unit unit;

		for (int i = 0; i < 25; i++) {
			unit = new Unit(player.createThingID());
			unit.setPosition(100, 100);
			//unit.walkTo(500, 500);
			startingState.addThing(unit);
		}
		
		Mineral m;
		
		for (int i=0; i < 5; i++) {
			m = new Mineral(player.createThingID());
			m.setPosition(400 + i * 30, 400  - i * 30);
			m.setRemaining(5000);
			startingState.addThing(m);
		}
		
		Factory f = new Factory(player.createThingID());
		f.setPosition(150, 100);
		startingState.addThing(f);

		return startingWorld;
	}

	public GameWorld getWorld() {
		return world;
	}

	public boolean hasGameExited() {
		return gameExited;
	}

	public void init() {

	}

	public void step() {
		if (network != null) {
			network.step();
		}

		if (world != null) {
			world.step();
		}
	}
}
