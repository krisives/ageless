package ageless.client;

import java.awt.Color;
import java.awt.Graphics2D;

import ageless.Game;
import ageless.GamePlayer;

public class GameHud {
	protected GameUI ui;
	protected GameClient client;
	protected Game game;

	public GameHud(GameUI ui) {
		this.ui = ui;
		this.client = ui.getClient();
	}

	public void init() {
		this.game = client.getGame();
	}

	public void draw(Graphics2D g) {
		GamePlayer player;
		
		//System.out.println(g.getFont().getFamily());

		player = game.getPlayer();

		if (player == null) {
			return;
		}

		long score = player.getScore();
		int minerals = player.getMinerals();
		int gas = player.getGas();
		
		drawBox(g, 640 - 10 * 4 - 75 * 4, 10, String.valueOf(score));
		drawBox(g, 640 - 10 * 3 - 75 * 3, 10, String.valueOf(minerals));
		drawBox(g, 640 - 10 * 2 - 75 * 2, 10, String.valueOf(gas));
		drawBox(g, 640 - 10 * 1 - 75 * 1, 10, "0 / 10");
	}

	protected void drawBox(Graphics2D g, int x, int y, String str) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, 75, 32);

		g.setColor(Color.WHITE);
		g.drawRect(x, y, 75, 32);

		g.drawString(str, x + 8, y + 23);
	}

	public void step(Game game) {

	}
}
