package ageless.things;

import ageless.GameThing;
import ageless.GameWorld;

public class Mineral extends GameThing {
	protected int remaining;

	public Mineral(int id) {
		super(id);
	}

	public Mineral(Mineral copyFrom) {
		super(copyFrom);

		this.remaining = copyFrom.remaining;
	}

	public GameThing createClone() {
		return new Mineral(this);
	}

	public int getSize() {
		return 12;
	}

	public void step(GameWorld world) {

	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}
	
	public int getRemaining() {
		return remaining;
	}

}
