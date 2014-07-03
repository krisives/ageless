package ageless;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * A GameTimeline object keeps track of the state of a GameRoom object making it
 * possible to seek backwards in the timeline. Whenever new events are
 * available, add the events to the game room then rewind to before the events
 * occurred.
 */
public class GameTimeline {
	private final GameWorld room;
	private final int startFrame;
	private final GameState startState;
	private NavigableMap<Integer, GameState> snapshots;

	/**
	 * Construct a new GameTimeline given a GameRoom to track. A snapshot of the
	 * current GameState is saved as the starting point of this timeline (it
	 * cannot be rewinded before that)
	 * 
	 * @param room a GameRoom to track the timeline
	 */
	public GameTimeline(GameWorld room) {
		this.room = room;
		this.snapshots = new TreeMap<>();
		this.startState = room.getState().createCopy();
		this.startFrame = startState.getFrame();
		this.snapshots.put(this.startFrame, this.startState);
	}

	/**
	 * Gets the frame the GameRoom state is currently on
	 * 
	 * @return the frame number
	 */
	public int getFrame() {
		return room.getFrame();
	}

	/**
	 * Take a snapshot of the GameRoom current state. This makes it possible to
	 * rewind to the current frame quickly. If there was a snapshot at this
	 * frame this replaces it.
	 * 
	 * @return the GameState snapshot (a copy of the current state)
	 */
	public GameState keyframe() {
		GameState state = room.getState().createCopy();
		this.snapshots.put(state.getFrame(), state);
		return state;
	}

	public void rewind(int targetFrame) {
		Integer snapshotFrame;
		GameState snapshot;

		if (targetFrame < this.startFrame) {
			String msg = "Target frame is before start of this timeline";
			throw new IllegalArgumentException(msg);
		}

		if (targetFrame >= getFrame()) {
			String msg = "Target frame must be before current frame";
			throw new IllegalArgumentException(msg);
		}

		// Get closest keyframe
		snapshotFrame = snapshots.floorKey(targetFrame);

		if (snapshotFrame == null) {
			// Should always return a keyframe (at worst startState)
			String msg = "Unable to find snapshot";
			throw new IllegalStateException(msg);
		}

		snapshot = snapshots.get(snapshotFrame);

		if (snapshot == null) {
			String msg = "Missing a snapshot";
			throw new IllegalStateException(msg);
		}

		// Remove all the snapshots afterwards (since time will change)
		this.snapshots.tailMap(snapshotFrame + 1);

		this.room.changeState(snapshot);

		// Walk the game state forward
		while (room.getFrame() < targetFrame) {
			room.step();
		}
	}
}
