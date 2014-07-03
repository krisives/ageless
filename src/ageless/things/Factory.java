package ageless.things;

import java.util.LinkedList;

import ageless.GameWorld;

public class Factory extends Unit {
	protected final LinkedList<Unit> queue = new LinkedList<>();
	protected Unit training;
	protected int trainingTime;
	protected int trainingProgress;
	protected int damageTech;
	protected int armorTech;
	protected int rangeTech;
	protected int shieldTech;
	protected int hpTech;
	protected int trainingTech;
	protected int capacityTech;

	public Factory(int id) {
		super(id);

		this.training = null;
		this.trainingTime = 10 * 10;
		this.trainingProgress = 0;
		this.damageTech = 0;
		this.armorTech = 0;
		this.rangeTech = 0;
		this.shieldTech = 0;
		this.hpTech = 0;
		this.trainingTech = 1;
		this.capacityTech = 0;
	}

	public Factory(Factory copyFrom) {
		super(copyFrom);

		this.training = copyFrom.training;
		this.trainingTime = copyFrom.trainingTime;
		this.trainingProgress = copyFrom.trainingProgress;
		this.damageTech = copyFrom.damageTech;
		this.armorTech = copyFrom.armorTech;
		this.rangeTech = copyFrom.rangeTech;
		this.shieldTech = copyFrom.shieldTech;
		this.hpTech = copyFrom.hpTech;
		this.trainingTech = copyFrom.trainingTech;
		this.capacityTech = copyFrom.capacityTech;
	}
	
	public boolean isMobile() {
		return false;
	}

	public int getSize() {
		return 25;
	}

	public void queueUnit(Unit unit) {
		this.queue.add(unit);
	}

	public void step(GameWorld world) {
		if (training == null && !queue.isEmpty()) {
			startTraining(world, queue.removeFirst());
		}

		if (training != null) {
			trainingProgress++;

			if (trainingProgress >= trainingTime) {
				finishTraining(world);
			}
		}
	}

	protected void startTraining(GameWorld world, Unit unit) {
		training = queue.removeFirst();
		training.changeState(Unit.STATE_TRAINING);
		trainingProgress = 0;
	}

	protected void finishTraining(GameWorld world) {
		if (training == null) {
			return;
		}

		training.changeState(Unit.STATE_IDLE);
		training.walkTo(targetX, targetY);
		training = null;
	}

}
