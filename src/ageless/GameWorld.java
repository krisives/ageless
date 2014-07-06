package ageless;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class GameWorld {
	private final int id;
	private GameState state;
	private Random prng;
	private NavigableMap<Integer, SortedSet<GameEvent>> events;
	private GameObserver observer;
	private GameSpace space;
	private GamePhysics physics = new GamePhysics();
	private Map<Integer, GamePlayer> players = new HashMap<>();

	/**
	 * Constructs a world using an existing GameState object.
	 * 
	 * @param id
	 * @param state
	 */
	public GameWorld(int id, GameState state) {
		this.id = id;
		this.prng = new Random();
		this.events = new TreeMap<>();
		this.state = state;
		this.observer = null;
	}

	/**
	 * Constructs a copy of an existing world. This makes a deep copy of the
	 * GameState and a shallow copy of the events.
	 * 
	 * After copying the constructed object will not have any GameObserver.
	 * 
	 * @param copyFrom a world to copy
	 */
	public GameWorld(GameWorld copyFrom) {
		this(copyFrom.id, copyFrom.getState().createCopy());
		SortedSet<GameEvent> set;

		for (int frame : copyFrom.events.keySet()) {
			// Make a shallow copy of the set of events
			set = copyFrom.events.get(frame);
			set = new TreeSet<>(set);
			this.events.put(frame, set);
		}
	}

	/**
	 * Creates a copy of this world. How this works is described in the
	 * {@link #GameWorld(GameWorld) copy constructor}
	 * 
	 * @return
	 */
	public GameWorld createCopy() {
		return new GameWorld(this);
	}

	/**
	 * Changes the GameState to a copy of the passed GameState.
	 * 
	 * @param state a GameState object to copy
	 */
	public void changeState(GameState state) {
		if (state == null) {
			String msg = "Must pass a GameState";
			throw new NullPointerException(msg);
		}

		this.state = state.createCopy();
	}

	public void setObserver(GameObserver observer) {
		this.observer = observer;
	}

	public void step() {
		for (GameEvent event : getFrameEvents(state.getFrame())) {
			event.perform(this);
		}

		state.step(this);

		for (int i = 0; i < 3; i++) {
			physics.solve(state);
		}
	}
	
	public void solvePhysics() {
		for (int i = 0; i < 3; i++) {
			physics.solve(state);
		}
	}

	/**
	 * Gets the current GameState of this world. Only change the returned object
	 * within the game engine. If for some reason you want to work on a
	 * GameState outside the game engine make a copy of it first.
	 * 
	 * @return the current game state
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * Gets the frame of the game state of this world.
	 * 
	 * @return the current frame of the game state in this world
	 */
	public int getFrame() {
		return state.getFrame();
	}

	public GameThing getThing(int thingID) {
		return state.getThing(thingID);
	}
	
	public GamePlayer getPlayer(int id) {
		return players.get(id);
	}
	
	public GamePlayer getPlayer(GameThing thing) {
		if (thing == null) {
			return getPlayer(0);
		}
		
		return getPlayer(thing.getPlayerID());
	}
	
	public <T extends GameThing> T findThing(GamePlayer player, Class<? extends T> c) {
		for (GameThing thing : getState().getThings()) {
			if (c.isInstance(thing)) {
				return (T)thing;
			}
		}
		
		return null;
	}

	private void seed(int salt) {
		int seed;

		// Flip some bits (increases distribution)
		salt = salt ^ 0xA7A7A7A7;
		seed = 0x7A7A7A7A + (salt ^ 0xFFFFFFFF);

		// Seed changes every frame
		seed = seed ^ (state.getFrame() + seed);

		// Each world gets different seeds
		seed = seed ^ (id + seed);

		// Separate rolls on the same frame get different seeds
		seed = seed ^ (salt + seed);

		// Update the PRNG
		this.prng.setSeed(seed);
	}

	public int random(int salt, int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException(
					"Min must be less than or equal to max");
		}

		if (min == max) {
			// Random number with no range (something like level zero spell)
			return min;
		}

		this.seed(salt);
		return min + this.prng.nextInt(max - min);
	}

	public void addEvent(GameEvent e) {
		int frame;
		SortedSet<GameEvent> set;

		frame = e.getFrame();
		set = events.get(frame);

		if (set == null) {
			set = new TreeSet<>();
			events.put(frame, set);
		}

		set.add(e);
	}
	
	public void addPlayer(GamePlayer player) {
		players.put(player.getID(), player);
	}
	
	public GamePlayer getPlayerByID(int id) {
		return players.get(id);
	}

	public Iterable<GameEvent> getFrameEvents(int n) {
		SortedSet<GameEvent> frameEvents = events.get(n);

		if (frameEvents == null) {
			return NO_EVENTS;
		}

		return frameEvents;
	}

	public void observe(GameThing source, int event) {

	}

	private static final List<GameEvent> NO_EVENTS_READABLE;
	private static final List<GameEvent> NO_EVENTS;

	static {
		NO_EVENTS_READABLE = Collections.emptyList();
		NO_EVENTS = Collections.unmodifiableList(NO_EVENTS_READABLE);
	}
}
