package ageless.client;

import java.awt.Color;
import java.awt.Graphics2D;

import ageless.Game;

public class GameMinimap {
	protected int width = 100;
	protected int height = 100;
	
	public GameMinimap(GameUI ui) {
		// TODO Auto-generated constructor stub
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 480 - height, width, height);

		g.setColor(Color.WHITE);
		g.drawRect(0, 480 - height, width, height);
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public void step(Game game) {
		// TODO Auto-generated method stub
		
	}
}
