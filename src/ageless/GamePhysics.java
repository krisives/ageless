package ageless;

public class GamePhysics {
	private int wiggle;

	public void solve(GameState state) {
		// Seed the wiggle value
		wiggle = 0x7A7A7A7A ^ state.getFrame();

		// Solve each object with respect to another
		for (GameThing a : state.getThings()) {
			if (a.hasParent()) {
				// Things inside buildings vehicles don't have physics
				continue;
			}

			for (GameThing b : state.getThings()) {
				if (a == b) {
					// Don't push things away from themselves!
					continue;
				}
				
				if (b.hasParent()) {
					// Don't push things that are inside of buildings or vehicles
					continue;
				}

				solveThing(a, b);
			}
		}
	}

	protected void solveThing(GameThing a, GameThing b) {
		float d; // distance between two objects
		float dx; // x distance
		float dy; // y distance
		float p; // proportion of how much to push away
		float c; // constraint distance

		if (!a.isMobile() && !b.isMobile()) {
			// Both are immobile
			return;
		}

		// calculate distance
		dx = b.getX() - a.getX();
		dy = b.getY() - a.getY();

		if (dx == 0.0f && dy == 0.0f) {
			// When the delta vector is zero but we still need to push
			// objects around then we create a pseudo-random amount to
			// push them so that there is not the same bias every time
			dx = dx + ((wiggle % 0xFFFF) / (float) 0xFFFF) * 0.25f;
			dy = dy - ((wiggle % 0xFFFF) / (float) 0xFFFF) * 0.25f;

			// Recalculate wiggle so that the next two objects
			// that wiggle don't push away in the same directions
			wiggle = wiggle ^ (a.getID() + b.getID());
		}

		// calculate the length of the distance vector
		d = (float) Math.sqrt(dx * dx + dy * dy);
		
		c = a.getSize() + b.getSize();

		if (d >= c) {
			// Things don't collide
			return;
		}

		if (!a.isMobile()) {
			// Push B away from A
			p = -(d - c) / d;
			b.translate(dx * p, dy * p);
			return;
		}

		if (!b.isMobile()) {
			// Push A away from B
			p = (d - c) / d;
			a.translate(dx * p, dy * p);
			return;
		}

		// Both are mobile and can be moved
		p = (d - c) / d;
		a.translate(dx * 0.5f * p, dy * 0.5f * p);
		b.translate(dx * -0.5f * p, dy * -0.5f * p);
	}
}
