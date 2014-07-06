package ageless;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GamePlayer {
	private String name;
	private int id;
	private int nextThingID = 0;
	protected Set<Integer> selected;
	protected Map<Integer, Set<Integer>> groups;
	
	protected int minerals;
	protected int gas;
	protected long score;

	public GamePlayer(int id) {
		if (id < 0 || id > 255) {
			throw new IllegalArgumentException("Player ID must be 0-255");
		}

		this.id = id;
		this.nextThingID = 0;
		this.selected = new HashSet<>();
		this.groups = new HashMap<>();
		this.gas = 0;
		this.minerals = 0;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int createThingID() {
		nextThingID++;
		return (id) | (nextThingID << 8);
	}

	public void clearSelection() {
		selected.clear();
	}

	public boolean hasSelection() {
		return !selected.isEmpty();
	}

	public void select(GameThing thing) {
		selected.add(thing.getID());
	}

	public boolean isSelected(GameThing thing) {
		return selected.contains(thing.getID());
	}

	public Iterable<Integer> getSelected() {
		return selected;
	}

	public void saveGroup(int slot) {
		Set<Integer> copy = new HashSet<>(selected);
		groups.put(slot, copy);
	}

	public void restoreGroup(int slot) {
		Set<Integer> set = groups.get(slot);

		if (set == null) {
			return;
		}

		this.selected = new HashSet<>(set);
	}
	
	public void addScore(int x) {
		score += x;
	}

	public void addMinerals(int x) {
		addScore(x);
		this.minerals += x;
	}
	
	public void addGas(int x) {
		addScore(x);
		this.gas += x;
	}

	public int getMinerals() {
		return minerals;
	}
	
	public int getGas() {
		return gas;
	}

	public long getScore() {
		return score;
	}
}
