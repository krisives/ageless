package ageless.ai;

public class GameCpuMemory {
	private final int[] data;
	private final int length;
	
	public GameCpuMemory(int[] data) {
		this.data = data;
		this.length = data.length;
	}
	
	public int read(int address) {
		return data[address % length];
	}
	
	public void write(int address, int value) {
		data[address % length] = value;
	}
}
