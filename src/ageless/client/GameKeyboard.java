package ageless.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ageless.Game;

public class GameKeyboard implements KeyListener {
	private GameClient client;
	private GameUI ui;
	private List<KeyEvent> events;
	private Set<Integer> pressed;

	public GameKeyboard(GameUI ui) {
		this.ui = ui;
		this.client = ui.getClient();
		this.events = new ArrayList<>();
		this.pressed = new HashSet<>();
	}
	
	public boolean isPressed(int key) {
		return pressed.contains(key);
	}
	
	public boolean isPressed(int a, int b) {
		return pressed.contains(a) && pressed.contains(b);
	}
	
	public void init() {
		this.client.getWindow().addKeyListener(this);
	}
	
	public synchronized void step(Game game) {
		if (events.isEmpty()) {
			return;
		}
		
		for (KeyEvent e : events) {
			switch (e.getID()) {
			case KeyEvent.KEY_PRESSED:
				pressed.add(e.getKeyCode());
				break;
			case KeyEvent.KEY_RELEASED:
				pressed.remove(e.getKeyCode());
				break;
			}
		}
		
		events.clear();
		

		for (int i=0; i < SLOT_BUTTONS.length; i++) {
			stepSlotKey(game, i);
		}
		
	}
	
	protected void stepSlotKey(Game game, int i) {
		int button = SLOT_BUTTONS[i];
		
		if (ui.isPressed(button)) {
			if (ui.isPressed(KeyEvent.VK_CONTROL)) {
				game.getPlayer().saveGroup(i);
			} else {
				game.getPlayer().restoreGroup(i);
			}
		}
	}
	
	protected synchronized void addEvent(KeyEvent e) {
		events.add(e);
	}
	
	/*
	public void sync(GameController controller) {
		for (KeyEvent event : events) {
			switch (event.getID()) {
			case KeyEvent.KEY_TYPED:
				break;
			case KeyEvent.KEY_PRESSED:
				controller.press(event.getKeyCode());
				break;
			case KeyEvent.KEY_RELEASED:
				controller.release(event.getKeyCode());
				break;
			}
		}

		events.clear();
	}
	*/

	public void keyPressed(KeyEvent e) {
		addEvent(e);
	}

	public void keyReleased(KeyEvent e) {
		addEvent(e);
	}

	public void keyTyped(KeyEvent e) {
		addEvent(e);
	}
	
	private static final int[] SLOT_BUTTONS = {
		KeyEvent.VK_1,
		KeyEvent.VK_2,
		KeyEvent.VK_3,
		KeyEvent.VK_4,
		KeyEvent.VK_5,
		KeyEvent.VK_6,
		KeyEvent.VK_7,
		KeyEvent.VK_8,
		KeyEvent.VK_9,
		KeyEvent.VK_0
	};
}
