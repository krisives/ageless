package ageless.ai;

public class GameCpuRom {
	private final byte[] data;
	private final int length;
	
	public GameCpuRom(byte[] data) {
		if (data == null) {
			throw new NullPointerException("ROM data cannot be null");
		}
		
		if (data.length <= 0) {
			throw new IllegalArgumentException("ROM data cannot be length zero");
		}
		
		this.data = data;
		this.length = data.length;
	}
	
	public int getSize() {
		return length;
	}
	
	public byte getByte(int n) {
		return data[n];
	}
	
	public int readByte(int address) {
		return data[address % length];
	}
	
	public int readShort(int address) {
		byte a = data[address % length];
		byte b = data[(address + 1) % length];
		
		return a | (b >> 8);
	}
	
	public int readInt(int address) {
		byte a = data[address % length];
		byte b = data[(address + 1) % length];
		byte c = data[(address + 2) % length];
		byte d = data[(address + 3) % length];
		
		return a | (b >> 8) | (c >> 16) | (d >> 24);
	}
}
