package ageless.ai;

import ageless.Game;
import ageless.GamePlayer;
import ageless.GameThing;
import ageless.GameWorld;

public class GameCpu {
	private final Game game;
	private final GamePlayer player;
	private final GameWorld world;

	// Read only memory (ROM) makes up the instruction data
	private final GameCpuRom rom;

	// Random access memory (RAM) makes up the memory
	private final GameCpuMemory memory;

	// Program Counter (Instruction Pointer)
	private int pc;

	// Current opcode where the (lower 5 are instruction upper 3 are arguments)
	private int opcode;

	// Number of stupid commands AI has tried to make
	private int stupid;

	public GameCpu(Game game, GamePlayer player, GameCpuMemory memory, GameCpuRom rom) {
		this.game = game;
		this.player = player;
		this.world = game.getWorld();
		this.memory = memory;
		this.rom = rom;
		this.pc = 0;
	}

	public int getStupid() {
		return stupid;
	}

	/**
	 * Run a single instruction of the AI. This reads an opcode from ROM,
	 * decodes it, and executes it.
	 */
	public void tick() {
		// Read the next opcode
		this.opcode = readByte();

		switch (opcode & OPCODE_LOWER4) {
		case OP_NOP:
			break;

		case OP_OR:
			or();
			break;
		case OP_AND:
			and();
			break;
		case OP_XOR:
			xor();
			break;

		case OP_ADD:
			add();
			break;
		case OP_SUB:
			sub();
			break;
		case OP_MUL:
			mul();
			break;
		case OP_DIV:
			div();
			break;
		case OP_MIN:
			min();
			break;
		case OP_MAX:
			max();
			break;

		case OP_JUMP:
			jump();
			break;

		case OP_SELECT:
			select();
			break;
		case OP_UNSELECT:
			unselect();
			break;
		case OP_COMMAND:
			command();
			break;
		case OP_DISCOVER:
			discover();
			break;

		case OP_NOP2:
			break;

		}

	}

	// 5-bits for opcode
	private final static int OPCODE_LOWER4 = (1 | 2 | 4 | 8);

	// No operation
	private final static int OP_NOP = 0;

	// Bitwise operators
	private final static int OP_OR = 1;
	private final static int OP_AND = 2;
	private final static int OP_XOR = 3;

	// Basic arithmetic
	private final static int OP_ADD = 4;
	private final static int OP_SUB = 5;
	private final static int OP_MUL = 6;
	private final static int OP_DIV = 7;
	private final static int OP_MAX = 8;
	private final static int OP_MIN = 9;

	// Control flow
	private final static int OP_JUMP = 10;

	private final static int OP_SELECT = 11;
	private final static int OP_UNSELECT = 12;
	private final static int OP_COMMAND = 13;
	private final static int OP_DISCOVER = 14;

	private final static int OP_NOP2 = 15;

	/**
	 * Reads a single 8-bit value from the ROM at the current program counter.
	 * This increments the program counter by one. If the program counter gets
	 * to the end of the ROM it wraps around.
	 * 
	 * @return the next 8-bit value in the ROM
	 */
	private int readByte() {
		int t;
		t = rom.readByte(pc);
		pc += 1;
		return t;
	}
	
	/**
	 * Reads a 32-bit integer from ROM and increments the program counter by 4.
	 * 
	 * @return the next 32-bit value in the ROM
	 */
	private int readInt() {
		int t;
		t = rom.readInt(pc);
		pc += 4;
		return t;
	}
	
	/**
	 * Executes a binary math operation by reading three memory addresses
	 * from ROM and performing an operation on those memory addresses.
	 * 
	 * @param binary a binary operation to perform
	 */
	private void binary(final Binary binary) {
		int r = memory.read(readInt());
		int x = memory.read(readInt());
		int y = memory.read(readInt());
		memory.write(r, binary.eval(x, y));
	}

	private void add() {
		binary(BINARY_ADD);
	}

	private void sub() {
		binary(BINARY_SUB);
	}

	private void xor() {
		binary(BINARY_XOR);
	}

	private void or() {
		binary(BINARY_OR);
	}

	private void and() {
		binary(BINARY_AND);
	}

	private void mul() {
		binary(BINARY_MUL);
	}

	private void div() {
		binary(BINARY_DIV);
	}

	private void min() {
		binary(BINARY_MIN);
	}

	private void max() {
		binary(BINARY_MAX);
	}

	private void jump() {
		int mode;
		int target;
		int x, y;

		mode = (opcode << 4) & 0xF;
		mode = (mode % 7);

		target = readInt();

		if (mode == 0) {
			pc = target;
			return;
		}

		x = memory.read(readInt());
		y = memory.read(readInt());

		switch (mode) {
		case 1:
			if (x == y) {
				pc = target;
			}

			break;
		case 2:
			if (x != y) {
				pc = target;
			}

			break;
		case 3:
			if (x > y) {
				pc = target;
			}

			break;
		case 4:
			if (x >= y) {
				pc = target;
			}

			break;
		case 5:
			if (x < y) {
				pc = target;
			}

			break;
		case 6:
			if (x <= y) {
				pc = target;
			}

			break;
		}

	}

	private void select() {
		int thingID;
		GameThing thing;

		thingID = memory.read(readInt());
		thing = world.getThing(thingID);

		if (thing == null) {
			stupid += 1;
			return;
		}

		player.select(thing);
	}

	private void unselect() {
		int thingID;
		GameThing thing;

		thingID = memory.read(readInt());
		thing = world.getThing(thingID);

		if (thing == null) {
			stupid += 1;
			return;
		}

		player.select(thing);
	}

	private void discover() {

	}

	private void command() {
		int thingID;
		GameThing thing;

		thingID = memory.read(readInt());
		thing = world.getThing(thingID);

		if (thing == null) {
			stupid += 1;
			return;
		}

	}

	private static abstract class Binary {
		public abstract int eval(int x, int y);
	}

	private static class Add extends Binary {
		public int eval(int x, int y) {
			return x + y;
		}
	}

	private static class Sub extends Binary {
		public int eval(int x, int y) {
			return x - y;
		}
	}

	private static class Mul extends Binary {
		public int eval(int x, int y) {
			return x * y;
		}
	}

	private static class Div extends Binary {
		public int eval(int x, int y) {
			if (y == 0) {
				return 0;
			}

			return x / y;
		}
	}

	private static class Xor extends Binary {
		public int eval(int x, int y) {
			return x ^ y;
		}
	}

	private static class Or extends Binary {
		public int eval(int x, int y) {
			return x | y;
		}
	}

	private static class And extends Binary {
		public int eval(int x, int y) {
			return x & y;
		}
	}

	private static class Min extends Binary {
		public int eval(int x, int y) {
			return (x < y) ? x : y;
		}
	}

	private static class Max extends Binary {
		public int eval(int x, int y) {
			return (x > y) ? x : y;
		}
	}

	private static final Add BINARY_ADD = new Add();
	private static final Sub BINARY_SUB = new Sub();
	private static final Mul BINARY_MUL = new Mul();
	private static final Div BINARY_DIV = new Div();
	private static final Xor BINARY_XOR = new Xor();
	private static final Or BINARY_OR = new Or();
	private static final And BINARY_AND = new And();
	private static final Max BINARY_MAX = new Max();
	private static final Min BINARY_MIN = new Min();
}
