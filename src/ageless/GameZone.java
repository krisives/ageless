package ageless;

public class GameZone {
	protected final int column;
	protected final int row;
	protected int seed;

	public GameZone(int column, int row, int seed) {
		this.column = column;
		this.row = row;
		this.seed = seed;
	}

	public void populate(GameWorld world) {
		int totalMinerals = 5000 + 5000;
		int patchCount = 0 + 10;
		int patchMinerals;
		
		for (int i=0; i < patchCount; i++) {
			patchMinerals = (totalMinerals / 10);
			
			if (patchMinerals > totalMinerals) {
				patchMinerals = totalMinerals;
			}
			
			totalMinerals -= patchMinerals;
			addMineralPatch(patchMinerals);
		}
	}
	
	protected void addMineralPatch(int minerals) {
		if (minerals <= 0) {
			return;
		}
	}
}
