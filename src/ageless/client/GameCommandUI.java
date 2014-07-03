package ageless.client;

import java.awt.Color;
import java.awt.Graphics2D;

import ageless.Game;

public class GameCommandUI {
	private GameUI ui;
	private GameClient client;
	protected int width = 150;
	protected int height = 150;

	public GameCommandUI(GameUI ui) {
		this.ui = ui;
		this.client = ui.getClient();
	}

	public void draw(Graphics2D g) {
		int x = 640 - width;
		int y = 480 - height;
		
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);

		g.setColor(Color.WHITE);
		g.drawRect(x, y, width, height);
		
		g.drawString("test", x + 25, y + 25);
	}

	public void init() {
		
		
	}

	public void step(Game game) {
		
	}
}
