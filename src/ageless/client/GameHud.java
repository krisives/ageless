package ageless.client;

import java.awt.Color;
import java.awt.Graphics2D;

import ageless.Game;

public class GameHud {
	public GameHud(GameUI gameUI) {
		// TODO Auto-generated constructor stub
	}

	public void draw(Graphics2D g) {
		drawBox(g, 640 - 10 * 3 - 75 * 3, 10, "0");
		drawBox(g, 640 - 10 * 2 - 75 * 2, 10, "0");
		drawBox(g, 640 - 10 * 1 - 75 * 1, 10, "0 / 10");
	}
	
	protected void drawBox(Graphics2D g, int x, int y, String str) {
		g.setColor(Color.BLACK);
		g.fillRect(x,  y, 75, 32);
		
		g.setColor(Color.WHITE);
		g.drawRect(x, y, 75, 32);
		
		g.drawString(str, x + 8, y + 23);
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public void step(Game game) {
		// TODO Auto-generated method stub
		
	}
}
