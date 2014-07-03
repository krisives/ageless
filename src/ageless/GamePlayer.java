package ageless;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GamePlayer {
	private String name;
	private int id;
	private int nextThingID = 0;
	protected Set<Integer> selected = new HashSet<>();
	protected Map<Integer, Set<Integer>> groups = new HashMap<>();
	
	public GamePlayer(int id) {
		if (id <= 0 || id > 255) {
			throw new IllegalArgumentException("Player ID must be 1-255");
		}

		this.id = id;
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
}
