package ageless.client;

import ageless.Game;

public class GameClient implements Runnable {
	public static void main(String[] args) {
		GameClient client = new GameClient();
		client.run();
	}

	private String dir;
	private String uri;
	private GameWindow window;
	private GameUI ui;
	private Game game;
	private boolean gameQuit = false;
	private long lastFrameTime;
	private long lastSampleTime;
	private int frame = 0;
	private int fps = 0;
	private int lastFrameCount = 0;

	public GameClient() {
		// Setup directory and URLs
		this.dir = System.getProperty("user.dir").replace("\\", "/");
		this.uri = "file:/" + dir;

		System.out.println("Game directory is " + dir);
		System.out.println("Game directory is " + uri);
		
		this.ui = new GameUI(this);
		this.window = new GameWindow(this);
		this.game = new Game();
	}
	
	public Game getGame() {
		return game;
	}
	
	public int getFPS() {
		return fps;
	}

	public GameWindow getWindow() {
		return window;
	}

	public String getPath(String child) {
		return dir + child;
	}

	public String getURI(String child) {
		return uri + child;
	}

	public GameUI getUI() {
		return ui;
	}

	public void run() {
		try {
			window.init();
			ui.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		lastFrameTime = System.currentTimeMillis();
		lastSampleTime = lastFrameTime;

		while (!gameQuit) {
			tick();
		}
	}

	public void tick() {
		long now = System.currentTimeMillis();
		long diff;// = now - lastFrameTime;
		//long remaining = (17 - diff);

		//if (remaining > 0) {
			try {
				Thread.sleep(16);// remaining);
			} catch (Exception e) {
				e.printStackTrace();
			}
		//}
		
		diff = now - lastSampleTime;
		
		if (diff >= 1000) {
			lastSampleTime = now;
			fps = frame - lastFrameCount;
			lastFrameCount = frame;
		}
		
		if ((frame % 6) == 0) {
			ui.step(game);
			game.step();
		}

		window.render(game);
		this.frame++;
		lastFrameTime = now;
	}
}
