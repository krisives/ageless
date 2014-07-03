package ageless.things;

import ageless.GameState;
import ageless.GameWorld;

public class Colony extends Unit {
	public Colony(int id) {
		super(id);
	}
	
	public Colony(Colony copyFrom) {
		super(copyFrom);
	}
	
	public boolean isMobile() {
		return false;
	}
	
	public int getSize() {
		return 50;
	}
	
	public void step(GameWorld world) {
		
	}
}
