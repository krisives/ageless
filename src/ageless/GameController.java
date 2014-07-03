package ageless;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import ageless.client.GameClient;

public class GameController {
	private SortedMap<Integer, Integer> pressed;
	private SortedMap<Integer, SortedSet<Integer>> frames;
	private int frame;

	public GameController(GameClient client) {
		this.pressed = new TreeMap<>();
		this.frames = new TreeMap<>();
		this.frame = 0;
	}

	/**
	 * Gets the current frame that the GameController is on. Each time step() is
	 * called the frame is advanced allowing it to track how long a button has
	 * been pressed.
	 * 
	 * The frame is advanced with the {@link #step()} method.
	 * 
	 * @return
	 */
	public int getCurrentFrame() {
		return this.frame;
	}

	/**
	 * Checks if a button is currently pressed.
	 * 
	 * @param button a button ID
	 * @return true if the button is currently being held down
	 */
	public boolean isPressed(int button) {
		return this.pressed.containsKey(button);
	}

	/**
	 * Returns the game frame of when this button started being pressed. This
	 * will return 0 if the button is not currently pressed, but you should use
	 * {@link #isPressed(int)} for that.
	 * 
	 * @param button
	 * @return
	 */
	public int getButtonFrame(int button) {
		return this.pressed.get(button);
	}

	/**
	 * Gets all the buttons currently pressed. The order of the buttons returned
	 * is sorted by their IDs.
	 * 
	 * @return an iteration of all the currently pressed buttons
	 */
	public Iterable<Integer> getPressed() {
		return this.pressed.keySet();
	}

	/**
	 * Get a sequence of buttons pressed since a game frame. The sequence will
	 * be in order they were pressed.
	 * 
	 * @param frame
	 * @param pressed
	 * @return
	 */
	public int getPressedSince(int frame, int[] pressed) {
		int i = 0;

		if (pressed == null) {
			throw new NullPointerException(
					"Must pass an array to hold button sequence");
		}

		if (pressed.length <= 0) {
			throw new IllegalArgumentException("Array cannot be empty");
		}

		for (SortedSet<Integer> set : frames.tailMap(frame).values()) {
			for (int button : set) {
				pressed[i] = button;
				i++;

				if (i >= pressed.length) {
					return i;
				}
			}
		}

		return i;
	}

	public void press(int button) {
		SortedSet<Integer> set;

		if (isPressed(button)) {
			return;
		}

		pressed.put(button, frame);
		set = frames.get(frame);

		if (set == null) {
			set = new TreeSet<>();
			frames.put(frame, set);
		}

		set.add(button);
	}

	public void release(int button) {
		Integer frame;
		SortedSet<Integer> set;

		frame = pressed.remove(button);

		if (frame == null) {
			// Release called on button not pressed
			return;
		}

		// Remove the button from the Set
		set = frames.get(frame);
		set.remove(button);

		// Remove empty sets from the map
		if (set.isEmpty()) {
			frames.remove(frame);
		}
	}

	public void toggle(int button) {
		if (isPressed(button)) {
			release(button);
		} else {
			press(button);
		}
	}

	public void set(int button, boolean flag) {
		if (flag) {
			press(button);
		} else {
			release(button);
		}
	}

	public void step() {
		this.frame++;
	}
}
