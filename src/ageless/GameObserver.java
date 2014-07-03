package ageless;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameObserver {
	private final Map<Integer, Set<Integer>> triggered;

	public GameObserver() {
		this.triggered = new HashMap<>();
	}

	public void trigger(GameThing thing, int trigger) {
		Set<Integer> set;

		set = triggered.get(trigger);

		if (set == null) {
			set = new HashSet<>();
			triggered.put(trigger, set);
		}

		set.add(thing.getID());
	}

	public Iterable<Integer> getTriggers() {
		return triggered.keySet();
	}

	public boolean wasTriggered(int trigger) {
		if (triggered.containsKey(trigger)) {
			return false;
		}

		return !triggered.get(trigger).isEmpty();
	}

	public Set<Integer> consumeTrigger(int trigger) {
		Set<Integer> things;

		things = triggered.get(trigger);

		if (things == null) {
			return EMPTY_INT_SET;
		}

		things.remove(trigger);
		return things;
	}

	private static final Set<Integer> EMPTY_INT_SET_READABLE;
	private static final Set<Integer> EMPTY_INT_SET;

	static {
		EMPTY_INT_SET_READABLE = Collections.emptySet();
		EMPTY_INT_SET = Collections.unmodifiableSet(EMPTY_INT_SET_READABLE);
	}
}
