package ageless.things;

import java.util.ArrayList;
import java.util.List;

public class Vehicle extends Unit {
	protected int capacity;
	protected List<Unit> loaded;
	
	public Vehicle(int id) {
		super(id);
		
		this.capacity = 0;
		this.loaded = new ArrayList<>(0);
	}
	
	public Vehicle(Vehicle copyFrom) {
		super(copyFrom);
		
		this.capacity = copyFrom.capacity;
		this.loaded = new ArrayList<>(this.capacity);
	}
	
}