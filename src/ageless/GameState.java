package ageless;

import java.util.Collections;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

/**
 * A collection of GameThing objects
 */
public class GameState {
	private SortedMap<Integer, GameThing> things;
	private int frame;

	public GameState() {
		this.things = new TreeMap<>();
		// this.thingTypes = new HashMap<>();
		this.frame = 0;
	}

	public GameState(GameState copyFrom) {
		this.frame = copyFrom.frame;
		this.things = new TreeMap<>();

		for (GameThing thing : copyFrom.things.values()) {
			addThing(thing.createClone());
		}
	}

	public GameState createCopy() {
		return new GameState(this);
	}

	/**
	 * Adds a GameThing to this state. Adding something more than once is stupid
	 * and considered an error.
	 * 
	 * @param thing the thing to add
	 */
	public void addThing(GameThing thing) {
		if (thing == null) {
			throw new NullPointerException("Must pass a thing to add");
		}

		this.things.put(thing.getID(), thing);
	}

	public void removeThing(GameThing thing) {
		if (thing == null) {
			// Ya can't remove nutin!
			return;
		}

		this.things.remove(thing.getID());
	}

	public void step(GameWorld world) {
		for (GameThing thing : things.values()) {
			thing.step(world);
		}

		frame++;
	}

	public int getFrame() {
		return frame;
	}

	public boolean hasThing(int id) {
		return things.containsKey(id);
	}

	public GameThing getThing(int id) {
		return things.get(id);
	}

	public int getThingCount() {
		return things.size();
	}
	
	public Iterable<GameThing> getThings() {
		return things.values();
	}

	private static final SortedSet<GameThing> EMPTY_THINGS;
	private static final SortedSet<GameThing> EMPTY_THINGS_READABLE;

	static {
		EMPTY_THINGS_READABLE = Collections.emptySortedSet();
		EMPTY_THINGS = Collections.unmodifiableSortedSet(EMPTY_THINGS_READABLE);
	}
}
