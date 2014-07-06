package ageless.things;

import ageless.GamePlayer;
import ageless.GameThing;
import ageless.GameWorld;

public class Unit extends GameThing {
	protected int hp;
	protected int maxHP;
	protected int damage;
	protected int armor;
	protected int shield;
	protected int range;
	protected int targetAction;
	protected int targetUnitID;
	protected float targetX;
	protected float targetY;
	protected int skill;
	protected int kills;
	protected int carryingMinerals;
	protected int carryingGas;

	public Unit(int id) {
		super(id);

		this.hp = 1;
		this.maxHP = 1;
	}

	public Unit(Unit copyFrom) {
		super(copyFrom);

		this.hp = copyFrom.hp;
		this.maxHP = copyFrom.maxHP;
		this.damage = copyFrom.damage;
		this.armor = copyFrom.armor;
		this.shield = copyFrom.shield;
		this.range = copyFrom.range;
		this.targetAction = copyFrom.targetAction;
		this.targetUnitID = copyFrom.targetUnitID;
		this.targetX = copyFrom.targetX;
		this.targetY = copyFrom.targetY;
		this.skill = copyFrom.skill;
		this.kills = copyFrom.kills;
		this.carryingMinerals = copyFrom.carryingMinerals;
		this.carryingGas = copyFrom.carryingGas;
	}

	public int getHP() {
		return hp;
	}

	public int getMaxHP() {
		return Math.max(maxHP, 1);
	}

	public boolean isMobile() {
		return true;
	}

	public int getSize() {
		return 8;
	}

	public void command(GameThing target) {
		int otherPlayerID = target.getPlayerID();

		if (otherPlayerID == getPlayerID()) {
			follow(target);
			return;
		}

		if (target instanceof Mineral) {
			harvest(target);
			return;
		}

		attack(target);
	}

	public void actionTo(int action, float targetX, float targetY) {
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetUnitID = 0;
		this.direction = (float) Math.atan2(targetY - y, targetX - x);
		this.changeState(STATE_WALKING);
	}

	public void attack(GameThing targetThing) {
		this.targetX = targetThing.getX();
		this.targetY = targetThing.getY();
		this.targetAction = STATE_ATTACKING;
		this.targetUnitID = targetThing.getID();
		this.direction = (float) Math.atan2(targetY - y, targetX - x);
		this.changeState(STATE_WALKING);
	}

	public void harvest(GameThing targetThing) {
		this.targetX = targetThing.getX();
		this.targetY = targetThing.getY();
		this.targetAction = STATE_HARVESTING;
		this.targetUnitID = targetThing.getID();
		this.direction = (float) Math.atan2(targetY - y, targetX - x);
		this.changeState(STATE_HARVESTING);
	}

	public void walkTo(float targetX, float targetY) {
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetUnitID = 0;
		this.direction = (float) Math.atan2(targetY - y, targetX - x);
		this.changeState(STATE_WALKING);
	}

	public void follow(GameThing target) {
		if (target.getID() == getID()) {
			return;
		}

		this.targetX = target.getX();
		this.targetY = target.getY();
		this.direction = (float) Math.atan2(targetY - y, targetX - x);
		this.targetUnitID = target.getID();
		this.targetAction = STATE_WALKING;
		this.changeState(STATE_WALKING);
	}

	public GameThing createClone() {
		return new Unit(this);
	}

	public void step(GameWorld world) {
		switch (getState()) {
		case STATE_TRAINING:
			stepTraining(world);
			break;

		case STATE_IDLE:
			stepIdle(world);
			break;

		case STATE_WALKING:
			stepWalking(world);
			break;

		case STATE_HARVESTING:
			stepHarvesting(world);
			break;

		case STATE_ATTACKING:
			stepAttacking(world);
			break;

		case STATE_CONSTRUCTING:
			stepConstructing(world);
			break;

		case STATE_GARRISON:
			stepGarrison(world);
			break;

		}
	}

	protected void stepTraining(GameWorld world) {

	}

	protected void stepIdle(GameWorld world) {

	}

	public float getMovementSpeed() {
		return 5f;
	}

	protected void walkStep(float dx, float dy) {
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		if (d <= getMovementSpeed()) {
			return;
		}

		dx = (dx / d) * getMovementSpeed();
		dy = (dy / d) * getMovementSpeed();
		this.direction = (float) Math.atan2(dy, dx);
		translate(dx, dy);
	}

	protected void walkStep(GameThing target) {
		float dx = (target.getX() - this.getX());
		float dy = (target.getY() - this.getY());

		walkStep(dx, dy);
	}

	protected void stepWalking(GameWorld world) {
		float dx = this.targetX - this.x;
		float dy = this.targetY - this.y;
		// float d = (float) Math.sqrt(dx * dx + dy * dy);
		GameThing thing = null;

		if (this.targetUnitID != 0) {
			thing = world.getThing(this.targetUnitID);
		}

		if (thing == null) {
			// Target unit doesn't exist so just walk to the location

			if (withinRange(this.targetX, this.targetY)) {
				finishWalking(world, thing);
				return;
			}

			walkStep(dx, dy);
		} else {
			if (withinRange(thing)) {
				finishWalking(world, thing);
				return;
			}

			dx = thing.getX() - this.x;
			dy = thing.getY() - this.y;
			walkStep(dx, dy);

			// if (withinRange(thing)) {
			// finishWalking(world, thing);
			// }
		}
	}

	protected void finishWalking(GameWorld world, GameThing target) {
		changeState(targetAction);
	}

	protected void stepAttacking(GameWorld world) {
		GameThing target = world.getThing(targetUnitID);

		if (target == null) {
			// Thing being attacked is gone
			finishAttacking(world);
			return;
		}

		// if (target instanceof Mineral) {
		// stepAttackingMineral(world, (Mineral) target);
		// } else

		if (target instanceof Unit) {
			stepAttackingUnit(world, (Unit) target);
		}
	}

	protected void stepAttackingUnit(GameWorld world, Unit targetUnit) {
		targetUnit.takeDamage(world, this, damage);
	}

	protected void stepHarvesting(GameWorld world) {
		GameThing target = world.getThing(targetUnitID);

		if (carryingMinerals >= 10) {
			stepHarvestingReturn(world);
			return;
		}

		if (target == null) {
			// Thing being attacked is gone
			// finishAttacking(world);
			return;
		}

		if (!withinRange(target)) {
			walkStep(target);
			return;
		}

		if (target instanceof Mineral) {
			stepAttackingMineral(world, (Mineral) target);
		}
	}

	protected void stepAttackingMineral(GameWorld world, Mineral mineral) {
		this.carryingMinerals += mineral.harvest(world, this, damage);
	}

	protected void stepHarvestingReturn(GameWorld world) {
		Colony home;
		GamePlayer player = world.getPlayer(this);

		if (player == null) {
			return;
		}

		home = world.findThing(player, Colony.class);

		if (home == null) {
			return;
		}
		
		if (withinRange(home)) {
			player.addMinerals(carryingMinerals);
			carryingMinerals = 0;
			return;
		}

		walkStep(home);
	}

	public void takeDamage(GameWorld world, GameThing from, int damage) {
		if (damage <= 0) {
			damage = 1;
		}
	}

	protected void finishAttacking(GameWorld world) {
		// TODO Auto-generated method stub

	}

	protected void stepConstructing(GameWorld world) {

	}

	protected void stepGarrison(GameWorld world) {

	}

	/** Unit is currently being trained */
	public static final int STATE_TRAINING = 0;

	/** Unit is standing around not doing anything */
	public static final int STATE_IDLE = 1;

	/** Unit is walking to target */
	public static final int STATE_WALKING = 2;
	public static final int STATE_HARVESTING = 3;
	public static final int STATE_ATTACKING = 4;
	public static final int STATE_CONSTRUCTING = 5;
	public static final int STATE_GARRISON = 6;

	public static final int ACTION_NOTHING = 0;
	public static final int ACTION_FOLLOW = 1;
	public static final int ACTION_ATTACK = 2;
	public static final int ACTION_ENTER = 3;
	public static final int ACTION_REPAIR = 4;

}
