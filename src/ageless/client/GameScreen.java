package ageless.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JComponent;

import ageless.Game;
import ageless.GamePlayer;
import ageless.GameState;
import ageless.GameThing;
import ageless.things.Colony;
import ageless.things.Factory;
import ageless.things.Mineral;
import ageless.things.Unit;

public class GameScreen extends JComponent {
	private GameClient client;
	private GameUI ui;
	private GameWindow window;
	private BufferStrategy bufferStrategy;
	private Graphics2D g;
	private int frame;
	private long startTime;
	private Game game;
	private Stroke selectionStroke;
	private Stroke solidStroke;

	public GameScreen(GameWindow window) {
		this.window = window;
		this.client = window.getClient();
		this.ui = window.getClient().getUI();
		this.startTime = System.currentTimeMillis();

		setSize(640, 480);
	}

	public void init() {
		this.bufferStrategy = window.getBufferStrategy();
		this.g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.translate(window.getInsets().left, window.getInsets().top);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		selectionStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[] { 3 }, 0);
		solidStroke = new BasicStroke(1.0f);

		Font font = new Font("Courier", Font.PLAIN, 12);

		g.setFont(font);
	}

	public void render(Game game) {
		if (g == null) {
			return;
		}

		while (bufferStrategy.contentsLost()) {
			init();
		}

		this.game = game;
		draw();
		ui.draw(g, game);

		bufferStrategy.show();
		Toolkit.getDefaultToolkit().sync();
	}

	int x, y;

	public void draw() {
		// AffineTransform oldTransform = g.getTransform();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 640, 480);

		drawState(game.getWorld().getState());

		g.setColor(Color.YELLOW);
		g.drawString(String.valueOf(client.getFPS()), 5, 16);

		frame++;
	}

	// Area selectArea = new Area();

	protected void drawState(GameState state) {
		GamePlayer me = game.getPlayer();

		g.setColor(Color.GREEN);
		g.setStroke(selectionStroke);

		// selectArea.reset();

		for (int thingID : me.getSelected()) {
			GameThing thing = state.getThing(thingID);

			if (thing == null) {
				continue;
			}

			// float s = 4.0f + thing.getSize() * 2.1f;
			// float x = thing.getX() - s * 0.5f;
			// float y = thing.getY() - s * 0.5f;

			drawCircle(thing.getX(), thing.getY(), (float) Math.round(6 + thing.getSize() * 2.1f));
		}

		g.setStroke(solidStroke);

		for (GameThing thing : state.getThings()) {
			drawThing(thing);
		}
	}

	protected void drawThing(GameThing thing) {
		// GamePlayer me = game.getPlayer();
		// GamePlayer owner =
		// game.getWorld().getPlayerByID(thing.getPlayerID());

		if (thing instanceof Mineral) {
			drawMineral((Mineral) thing);
		} else if (thing instanceof Factory) {
			drawFactory((Factory) thing);
		} else if (thing instanceof Colony) {
			drawColony((Colony) thing);
		} else if (thing instanceof Unit) {
			drawUnit((Unit) thing);
		}

		// if (me.isSelected(thing)) {
		// g.setStroke(selectionStroke);
		// g.setColor(Color.GREEN);
		// drawCircle(thing.getX(), thing.getY(), (float)Math.round(4 +
		// thing.getSize() * 2.1f));
		// }

	}

	protected void drawMineral(Mineral mineral) {
		g.setColor(Color.BLUE);
		fillCircle(mineral.getX(), mineral.getY(), mineral.getSize() * 2);
	}

	protected void drawUnit(Unit unit) {
		// GamePlayer player;

		g.setColor(Color.WHITE);
		fillCircle(unit.getX(), unit.getY(), unit.getSize() * 1.5f);
		fillCirclePolar(unit.getX(), unit.getY(), unit.getDirection(), unit.getSize() * 0.50f, unit.getSize());

	}

	protected void drawColony(Colony colony) {
		g.setColor(Color.GRAY);

		for (float a = 0; a < 360; a += 72) {
			float degrees = (float) Math.toRadians(45 + a);

			fillCirclePolar(colony.getX(), colony.getY(), degrees, colony.getSize(), colony.getSize() * 0.25f);
		}

		g.setColor(Color.WHITE);
		drawCircle(colony.getX(), colony.getY(), colony.getSize() * 2);

	}

	protected void fillCirclePolar(float x, float y, float dir, float distance, float size) {
		float tx = (float) (Math.cos(dir) * distance);
		float ty = (float) (Math.sin(dir) * distance);

		fillCircle(x + tx, y + ty, size);
	}

	protected void drawFactory(Factory unit) {
		g.setColor(Color.GRAY);
		fillCircle(unit.getX(), unit.getY(), unit.getSize() * 2);
	}

	protected void fillCircle(float fx, float fy, float size) {
		int x = Math.round(fx - size * 0.5f);
		int y = Math.round(fy - size * 0.5f);
		int s = Math.round(size);

		g.fillOval(x, y, s, s);
	}

	protected void drawCircle(float fx, float fy, float size) {
		int x = Math.round(fx - size * 0.5f);
		int y = Math.round(fy - size * 0.5f);
		int s = Math.round(size) - 1;

		g.drawOval(x, y, s, s);
	}
}
