package ageless;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

/**
 * A collection of GameThing objects
 */
public class GameState {
	private SortedMap<Integer, GameThing> things;
	private Map<Long, Set<GameThing>> buckets;
	private int frame;

	public GameState() {
		this.things = new TreeMap<>();
		this.buckets = new HashMap<>();
		this.frame = 0;
	}

	public GameState(GameState copyFrom) {
		this.frame = copyFrom.frame;
		this.things = new TreeMap<>();
		this.buckets = new HashMap<>();

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
		// Set<GameThing> set;

		if (thing == null) {
			throw new NullPointerException("Must pass a thing to add");
		}

		this.things.put(thing.getID(), thing);
		// set = this.buckets.get(key);
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

	public Iterable<GameThing> getThings(int x, int y) {
		return new BucketIterable(x, y);
	}

	public Set<GameThing> getBucket(int x, int y) {
		long key = (x) | (y << 32);
		Set<GameThing> bucket = buckets.get(key);

		if (bucket == null) {
			return EMPTY_THINGS;
		}

		return bucket;
	}

	public class BucketIterable implements Iterable<GameThing> {
		protected final int x, y;

		public BucketIterable(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Iterator<GameThing> iterator() {
			return new BucketIterator(x, y);
		}
	}

	public class BucketIterator implements Iterator<GameThing> {
		protected Iterator<GameThing> current;
		protected int x, y;
		protected int offsetX, offsetY;

		public BucketIterator(int x, int y) {
			this.x = x;
			this.y = y;
			this.offsetX = -1;
			this.offsetY = -1;
		}

		public boolean hasNext() {
			if (offsetY > 1 && offsetX > 1) {
				return false;
			}

			current = getBucket(x + offsetX, y + offsetY).iterator();
			offsetX++;

			if (offsetX > 1) {
				offsetX = 0;
				offsetY++;
			}

			return current.hasNext();
		}

		public GameThing next() {
			return current.next();
		}
	}

	private static final SortedSet<GameThing> EMPTY_THINGS;
	private static final SortedSet<GameThing> EMPTY_THINGS_READABLE;

	static {
		EMPTY_THINGS_READABLE = Collections.emptySortedSet();
		EMPTY_THINGS = Collections.unmodifiableSortedSet(EMPTY_THINGS_READABLE);
	}

}
