package ageless;

/**
 * Each thing in the game world is represented by a GameThing object and help
 * compose the current GameState. All of the logic within this object must be
 * deterministic; otherwise games will not synchronize over the network.
 */
public abstract class GameThing {
	protected final int id;
	private int salt;
	protected int parentID;
	protected int state;
	protected int flags;
	protected float x;
	protected float y;
	protected float direction;

	/**
	 * Create a new thing with default state
	 * 
	 * @param id a unique number - the least significant 8-bits of this is also
	 *            the player ID
	 */
	public GameThing(int id) {
		this.id = id;
		this.salt = (0x7A7A7A7A + id) ^ (id ^ 0xFFFFFFFF);
	}

	/**
	 * Copy an existing thing and all of it's state. The created object will
	 * have the same ID. Don't use this to make new in-game copies of objects,
	 * as they will be duplicates.
	 * 
	 * @param from
	 */
	public GameThing(GameThing from) {
		this(from.getID());
		this.salt = from.salt;
		this.state = from.state;
		this.flags = from.flags;
	}

	public boolean isMobile() {
		return false;
	}
	
	public boolean hasParent() {
		return this.parentID != 0;
	}
	
	public int getParentID() {
		return this.parentID;
	}
	
	public void setParent(GameThing thing) {
		this.parentID = thing.getID();
	}
	
	public void detach() {
		this.parentID = 0;
	}

	/**
	 * Gets a unique number that identifies this GameThing. The least signficant
	 * 8-bits of this is the player ID, with the remaining being a unique
	 * identifier for this specific GameThing.
	 * 
	 * The ID of a GameThing cannot change.
	 * 
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * Gets the ID of the player that owns this GameThing.
	 * 
	 * @return
	 */
	public int getPlayerID() {
		return (id & 0xFF);
	}

	/**
	 * Gets the index of this thing with respect to it's owner.
	 * 
	 * @return
	 */
	public int getThingIndex() {
		return (id << 8);
	}

	/**
	 * Gets a number that changes each time this GameThing rolls a random
	 * number. This makes it possible for the next rolled number to have a
	 * different value.
	 * 
	 * @return
	 */
	public int getSalt() {
		return salt;
	}

	public float getDirection() {
		return direction;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void translate(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public int getSize() {
		return 16;
	}

	public boolean withinBounds(float left, float top, float right, float bottom) {
		float s = getSize();
		float l = x - s;// * 0.5f;
		float r = x + s;// * 0.5f;
		float t = y - s;// * 0.5f;
		float b = y + s;// * 0.5f;
		
		return (l >= left && r >= top && t < right && b < bottom);
	}
	
	public boolean touchesBounds(float left, float top, float right, float bottom) {
		float s = getSize();
		float l = x - s;// * 0.5f;
		float r = x + s;// * 0.5f;
		float t = y - s;// * 0.5f;
		float b = y + s;// * 0.5f;
		
		return !(right < l || r < left || bottom < t || b < top);
	}
	
	public boolean touches(float px, float py) {
		float s = getSize();
		float l = this.x - s;// * 0.5f;
		float r = this.x + s;// * 0.5f;
		float t = this.y - s;// * 0.5f;
		float b = this.y + s;// * 0.5f;
		
		return px >= l && px < r && py >= t && py < b;
	}
	
	public float distanceFrom(float x, float y) {
		float dx = this.x - x;
		float dy = this.y - y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	public float distanceFrom(GameThing other) {
		float dx = this.x - other.x;
		float dy = this.y - other.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	public boolean withinRange(float x, float y) {
		return this.distanceFrom(x, y) <= getSize();
	}

	public boolean withinRange(GameThing other) {
		float c = this.getSize() + other.getSize();
		return this.distanceFrom(other) <= c * 1.1f;
	}

	/**
	 * Checks if a user flag is set on this GameThing object. This works at a
	 * bitwise level, so you can pass it a combination of flags too:
	 * 
	 * <pre>
	 * checkFlag(STATUS_FOO | STATUS_BAR)
	 * </pre>
	 * 
	 * @param flag the flag to check (unique to each GameThing)
	 * @return true of the flag is set (or flags if this is a combination)
	 */
	public boolean checkFlag(int flag) {
		return (this.flags & flag) == flag;
	}

	/**
	 * Sets a flag as enabled. See {@link #checkFlag(int)} for how to combine
	 * multiple flags.
	 * 
	 * @param flag
	 */
	public void setFlag(int flag) {
		flags = flags | flag;
	}

	/**
	 * Clears a flag.
	 * 
	 * @param flag
	 */
	public void clearFlag(int flag) {
		flags = flag & (~flag);
	}

	/**
	 * Sets or clears a flag. When passing a combination of flags this method
	 * will clear or set them all. See {@link #checkFlag(int)} for how to
	 * combine multiple flags.
	 * 
	 * @param flag the affected flag(s)
	 * @param set true to set or false to clear
	 */
	public void setFlag(int flag, boolean set) {
		if (set) {
			this.setFlag(flag);
		} else {
			this.clearFlag(flag);
		}
	}

	/**
	 * Toggles if a flag is set or cleared. When passing a combination of flags
	 * this toggles each one individually. See {@link #checkFlag(int)} for how
	 * to combine multiple flags.
	 * 
	 * @param flag the flag(s) to toggle
	 */
	public void toggleFlag(int flag) {
		this.flags = this.flags ^ flag;
	}

	public void changeState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	/**
	 * Gets a set of flags that can be enabled or disabled. Each flag is a bit
	 * set in the bit field. For most cases you should probably use the
	 * {@link #checkFlag()} method
	 * 
	 * @return
	 */
	public int getFlags() {
		return flags;
	}

	/**
	 * Calculates a hash that uniquely identifies this item based on it's
	 * current state. If any part of this object changes the hash should change
	 * to reflect that. It should be as fast as possible to compute.
	 * 
	 * @return
	 */
	public int hash() {
		int hash;

		hash = salt ^ (id + salt);
		hash = hash ^ (state + hash);
		hash = hash ^ (flags + hash);

		return hash;
	}

	/**
	 * Creates a clone of this object. Don't use this to copy in-game objects.
	 * 
	 * @return
	 */
	public abstract GameThing createClone();

	/**
	 * Advances the simulation of this GameThing object by a single frame.
	 * Within this method you can update this object and touch other objects in
	 * the GameRoom in any way you wish as long as it's deterministic.
	 * 
	 * For example, you cannot use Math.random() because that is not
	 * synchronized. Instead, you should use GameRoom.roll()
	 * 
	 * @param room Room this object is in
	 */
	public abstract void step(GameWorld world);

	public int random(GameWorld room, int min, int max) {
		int num = room.random(salt, min, max);

		// Change the salt so that multiple dice rolls
		// in the same frame return different results
		this.salt = salt ^ (salt + id + num);

		return num;
	}

	public int random(GameWorld room, int max) {
		return random(room, 0, max);
	}
}
