package ageless.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameCpuEvolver {
	private final Random random;
	private final List<GameCpuRom> population;
	
	public GameCpuEvolver() {
		this.random = new Random();
		this.population = new ArrayList<>();
	}
	
	public void epoch(int populationSize, int romSize) {
		for (int i=0; i < populationSize; i++) {
			this.population.add(generateRandom(romSize));
		}
	}
	
	private GameCpuRom generateRandom(int size) {
		byte[] childRom;
		
		childRom = new byte[size];
		random.nextBytes(childRom);
		
		return new GameCpuRom(childRom);
	}
	
	public GameCpuRom crossover(GameCpuRom a, GameCpuRom b) {
		int childSize;
		byte[] childRom;
		byte x, y;
		
		childSize = (a.getSize() + b.getSize()) / 2;
		childRom = new byte[childSize];
		
		for (int i=0; i < childSize; i++) {
			if (i > a.getSize()) {
				x = (byte)(random.nextInt() & 0xFF);
			} else {
				x = a.getByte(i);
			}
			
			if (i > b.getSize()) {
				y = (byte)(random.nextInt() & 0xFF);
			} else {
				y = b.getByte(i);
			}
			
			childRom[i] = crossoverByte(x, y);
		}
		
		if (random.nextInt(1000) < 1) {
			mutate(childRom);
		}
		
		return new GameCpuRom(childRom);
	}
	
	private void mutate(byte[] rom) {
		int pos = random.nextInt(rom.length);
		
		switch (random.nextInt(4)) {
		case 0:
			// Flip a single random bit
			rom[pos] = (byte)(rom[pos] ^ (1 << random.nextInt(8)));
			break;
		case 1:
			// Roll a new byte
			rom[pos] = (byte)random.nextInt(0xFF);
			break;
		case 2:
			// Flip all bits in a byte
			rom[pos] = (byte)(rom[pos] ^ 0xFF);
			break;
		case 3:
			// Randomly flip all the bits in a byte
			rom[pos] = (byte)(rom[pos] ^ random.nextInt(0xFF));
			break;
		}
	}
	
	private byte crossoverByte(int x, int y) {
		int z = 0;
		
		z = z | crossoverBit(x, y, 1);
		z = z | crossoverBit(x, y, 2);
		z = z | crossoverBit(x, y, 4);
		z = z | crossoverBit(x, y, 8);
		z = z | crossoverBit(x, y, 16);
		z = z | crossoverBit(x, y, 32);
		z = z | crossoverBit(x, y, 64);
		z = z | crossoverBit(x, y, 128);
		
		return (byte)z;
	}
	
	private int crossoverBit(int x, int y, int mask) {
		if (random.nextInt(100) > 50) {
			return (x & mask);
		} else {
			return (y & mask);
		}
	}
	
}
