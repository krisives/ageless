package ageless.client;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import ageless.Game;

public class GameUI {
	protected GameClient client;
	protected GameMinimap minimap;
	protected GameHud hud;
	protected GameCommandUI commandUI;
	protected GameKeyboard keyboard;
	protected GameCursor cursor;

	public GameUI(GameClient client) {
		this.client = client;
		this.minimap = new GameMinimap(this);
		this.hud = new GameHud(this);
		this.commandUI = new GameCommandUI(this);
		this.keyboard = new GameKeyboard(this);
		this.cursor = new GameCursor(this);
	}

	public GameClient getClient() {
		return client;
	}

	public GameWindow getWindow() {
		return client.getWindow();
	}

	public GameKeyboard getKeyboard() {
		return keyboard;
	}

	public GameCursor getCursor() {
		return cursor;
	}

	public GameScreen getScreen() {
		return getWindow().getScreen();
	}

	public boolean isPressed(int key) {
		return keyboard.isPressed(key);
	}

	public boolean isPressed(int a, int b) {
		return keyboard.isPressed(a, b);
	}

	public void init() {

		this.minimap.init();
		this.hud.init();
		this.commandUI.init();
		this.keyboard.init();
		this.cursor.init();
	}

	public void step(Game game) {
		keyboard.step(game);
		cursor.step(game);
		
		minimap.step(game);
		hud.step(game);
		commandUI.step(game);
	}

	private Stroke stroke = new BasicStroke(1.0f);

	public void draw(Graphics2D g, Game game) {
		g.setStroke(stroke);
		cursor.draw(g);

		g.setStroke(stroke);
		minimap.draw(g);

		g.setStroke(stroke);
		hud.draw(g);

		g.setStroke(stroke);
		commandUI.draw(g);
	}

}
