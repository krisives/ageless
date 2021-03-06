package ageless.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import ageless.Game;
import ageless.GamePlayer;
import ageless.GameState;
import ageless.GameThing;
import ageless.things.Mineral;
import ageless.things.Unit;

public class GameCursor implements MouseListener, MouseMotionListener {
	protected GameUI ui;
	protected GameClient client;
	protected Game game;
	protected int x;
	protected int y;
	protected boolean leftDown;
	protected boolean rightDown;
	protected int selectX, selectY;
	protected Rectangle selectArea = null;
	protected GameThing hoverThing = null;
	protected GameThing targetThing = null;
	protected int action = 0;
	protected int doubleClick = 0;
	private Stroke selectionStroke;
	

	public GameCursor(GameUI ui) {
		this.ui = ui;
		this.client = ui.getClient();
	}

	public void init() {
		this.game = client.getGame();
		
		selectionStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[] { 3 }, 0);

		ui.getScreen().addMouseListener(this);
		ui.getScreen().addMouseMotionListener(this);
	}

	public void startSelection() {
		leftDown = true;
		selectX = x;
		selectY = y;
	}

	public void endSelection() {
		int left = Math.min(x, selectX);
		int top = Math.min(y, selectY);
		int right = Math.max(x, selectX);
		int bottom = Math.max(y, selectY);
		int width = right - left;
		int height = bottom - top;

		leftDown = false;
		selectArea = new Rectangle(left, top, width, height);
	}

	public synchronized void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();

		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			startSelection();
			break;
		case MouseEvent.BUTTON2:
			break;
		case MouseEvent.BUTTON3:
			rightDown = true;
			break;
		}
	}

	public synchronized void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public synchronized void command(int action) {
		this.action = action;
	}

	public synchronized void mouseClicked(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		
		if (e.getClickCount() > 1) {
			doubleClick = 1;
		}
	}

	public synchronized void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();

		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			startSelection();
			break;
		case MouseEvent.BUTTON2:
			break;
		case MouseEvent.BUTTON3:
			if (!leftDown) {
				command(1);
			}

			// rightDown = true;
			break;
		}
	}

	public synchronized void mouseReleased(MouseEvent e) {
		x = e.getX();
		y = e.getY();

		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			if (leftDown) {
				endSelection();
			}

			break;
		case MouseEvent.BUTTON2:
			break;
		case MouseEvent.BUTTON3:
			rightDown = false;
			break;
		}
	}

	public synchronized void mouseEntered(MouseEvent e) {

	}

	public synchronized void mouseExited(MouseEvent e) {

	}

	public void draw(Graphics2D g) {
		if (leftDown) {
			drawSelection(g);
		}

		if (hoverThing != null) {
			drawHover(g);
		}
	}

	protected void drawHover(Graphics2D g) {

		//float p;

		if (hoverThing instanceof Unit) {
			drawHoverBar(g, (Unit)hoverThing);
		} else if (hoverThing instanceof Mineral) {
			drawHoverMineral(g, (Mineral)hoverThing);
		}

		/*
		

		g.setColor(Color.BLACK);
		g.drawLine(x + 1, y + 1, Math.round(hoverThing.getX()) + 1, Math.round(hoverThing.getY()) + 1);

		g.setColor(Color.WHITE);
		g.drawLine(x - 1, y - 1, Math.round(hoverThing.getX()), Math.round(hoverThing.getY()));

		g.setColor(Color.WHITE);
		g.fillRect(x - 2, y - 2, 44, 9);

		g.setColor(Color.BLACK);
		g.fillRect(x - 1, y - 1, 42, 7);

		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y, 40, 5);

		g.setColor(Color.GREEN);
		g.fillRect(x, y, w, 5);
*/
	}
	
	protected void drawOutlineBox(Graphics2D g, int x, int y, int w, int h) {
		g.setColor(Color.BLACK);
		g.drawLine(x + 1, y + 1, Math.round(hoverThing.getX()) + 1, Math.round(hoverThing.getY()) + 1);

		g.setColor(Color.WHITE);
		g.drawLine(x - 1, y - 1, Math.round(hoverThing.getX()), Math.round(hoverThing.getY()));

		g.setColor(Color.WHITE);
		g.fillRect(x - 2, y - 2, w + 4, h + 4);

		g.setColor(Color.BLACK);
		g.fillRect(x - 1, y - 1, w + 2, h + 2);

		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y, w, h);


	}
	
	protected void drawHoverBar(Graphics2D g, Unit unit) {
		int x = Math.round(unit.getX() + 10 + unit.getSize() * 1.1f);
		int y = Math.round(unit.getY() - 10 - unit.getSize() * 1.1f);
		
		g.setColor(Color.BLACK);
		g.drawLine(x + 1, y + 1, Math.round(hoverThing.getX()) + 1, Math.round(hoverThing.getY()) + 1);

		g.setColor(Color.WHITE);
		g.drawLine(x - 1, y - 1, Math.round(hoverThing.getX()), Math.round(hoverThing.getY()));
		
		drawOutlineBox(g, x, y, 40, 5);
		
		float p = getUnitMeter((Unit) hoverThing);
		int w = Math.round(p * 40);
		
		g.setColor(Color.GREEN);
		g.fillRect(x, y, w, 5);
	}
	
	protected void drawHoverMineral(Graphics2D g, Mineral mineral) {
		int x = Math.round(mineral.getX() + 10 + mineral.getSize() * 1.1f);
		int y = Math.round(mineral.getY() - 10 - mineral.getSize() * 1.1f);
		String text = String.valueOf(mineral.getRemaining());
		
		g.setColor(Color.BLACK);
		g.drawLine(x + 1, y + 1, Math.round(hoverThing.getX()) + 1, Math.round(hoverThing.getY()) + 1);

		g.setColor(Color.WHITE);
		g.drawLine(x - 1, y - 1, Math.round(hoverThing.getX()), Math.round(hoverThing.getY()));
		drawOutlineBox(g, x, y, 40, 15);
		
		g.setColor(Color.WHITE);
		g.drawString(text, x + 5, y + 10);
	}
	
	protected float getUnitMeter(Unit unit) {
		int hp = unit.getHP();
		int maxHP = unit.getMaxHP();

		return hp / maxHP;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSelectX() {
		return selectX;
	}

	public int getSelectY() {
		return selectY;
	}

	public synchronized void step(Game game) {
		//this.game = game;
		hoverThing = pickThing(game.getWorld().getState(), x, y);

		if (selectArea != null) {
			selectUnits(game, selectArea);
			selectArea = null;
		} else if (doubleClick > 0) {
			selectAll(game);
			doubleClick = 0;
		}

		if (action != 0) {
			commandThings(game, action);
			action = 0;
		}

		// hoverThing = null;
	}
	
	protected void selectAll(Game game) {
		GamePlayer me = game.getPlayer();
		int myPlayerID = me.getID();
		
		me.clearSelection();
		
		for (GameThing thing : game.getWorld().getState().getThings()) {
			if (thing.getPlayerID() == myPlayerID) {
				me.select(thing);
			}
		}
	}

	protected void selectUnits(Game game, Rectangle area) {
		GamePlayer me = game.getPlayer();
		int myID = me.getID();
		GameState state = game.getWorld().getState();

		if (!ui.isPressed(KeyEvent.VK_SHIFT)) {
			me.clearSelection();
		}

		int left = area.x;
		int top = area.y;
		int right = left + area.width;
		int bottom = top + area.height;

		for (GameThing thing : state.getThings()) {
			if (thing.getPlayerID() == myID) {
				if (thing.touchesBounds(left, top, right, bottom)) {
					me.select(thing);
				}
			}
		}
	}

	protected void commandThings(Game game, int action) {
		GamePlayer me = game.getPlayer();
		GameState state = game.getWorld().getState();
		GameThing thing;

		targetThing = hoverThing;

		for (int thingID : me.getSelected()) {
			thing = state.getThing(thingID);

			if (thing == null) {
				continue;
			}

			if (thing instanceof Unit) {
				commandUnit((Unit) thing, action);
			}
		}
	}

	public GameThing pickThing(GameState state, int x, int y) {
		for (GameThing thing : state.getThings()) {
			if (thing.touches(x, y)) {
				return thing;
			}
		}

		return null;
	}

	protected void commandUnit(Unit unit, int action) {
		if (targetThing == null) {
			unit.walkTo(x, y);
			return;
		}
		
		unit.command(targetThing);
	}

	protected void drawSelection(Graphics2D g) {
		int left = Math.min(getX(), getSelectX());
		int right = Math.max(getX(), getSelectX());
		int top = Math.min(getY(), getSelectY());
		int bottom = Math.max(getY(), getSelectY());
		int width = right - left;
		int height = bottom - top;

		g.setStroke(selectionStroke);
		g.setColor(Color.GREEN);
		g.drawRect(left, top, width, height);
	}

}
