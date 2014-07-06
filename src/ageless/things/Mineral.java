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

	public int harvest(GameWorld world, Unit unit, int damage) {
		if (this.remaining <= 0) {
			return 0;
		}
		
		if (damage <= 0) {
			damage = 1;
		}
		
		this.remaining = this.remaining - damage;
		
		if (this.remaining <= 0) {
			damage += this.remaining;
			this.remaining = 0;
		}
		
		world.getPlayer(unit).addScore(damage);
		return damage;
	}

}
