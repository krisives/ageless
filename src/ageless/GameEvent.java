package ageless;


public abstract class GameEvent implements Comparable<GameEvent> {
	private int id;
	private int frame;

	public GameEvent(int id, int frame) {
		this.id = id;
		this.frame = frame;
	}

	/**
	 * Gets a number that uniquely identifies this event.
	 * 
	 * @return
	 */
	public int getID() {
		return id;
	}

	public int getPlayerID() {
		return (id & 0xFF);
	}

	public int getEventIndex() {
		return (id << 8);
	}

	public int getFrame() {
		return frame;
	}

	public abstract void perform(GameWorld room);
	
	public int compareTo(GameEvent o) {
		int a = this.getID();
		int b = (o == null) ? 0 : o.getID();
		
		return a - b;
	}

	public static abstract class ThingEvent extends GameEvent {
		private int thingID;

		public ThingEvent(int id, int frame, int thingID) {
			super(id, frame);
		}

		public void perform(GameWorld room) {
			GameThing thing;

			if (thingID == 0) {
				return;
			}

			thing = room.getThing(thingID);

			if (thing == null) {
				return;
			}

			performThing(room, thing);
		}

		protected abstract void performThing(GameWorld room, GameThing thing);
	}

	public static abstract class MoveThingEvent extends ThingEvent {
		public MoveThingEvent(int id, int frame, int thingID) {
			super(id, frame, thingID);
		}

		protected void performThing(GameWorld room, GameThing thing) {
			// TODO Auto-generated method stub

		}
	}
}
