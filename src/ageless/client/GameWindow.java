package ageless.client;

import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ageless.Game;

public class GameWindow extends JFrame {
	private GameClient client;
	private GameScreen screen;

	public GameWindow(GameClient client) {
		this.client = client;
		this.screen = new GameScreen(this);

		setTitle("Ageless");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		updateSize();
		setContentPane(screen);
	}
	
	public GameScreen getScreen() {
		return screen;
	}

	protected void updateSize() {
		Insets insets = getInsets();
		setSize(640 + insets.left + insets.right, 480 + insets.top
				+ insets.bottom);
	}

	public GameClient getClient() {
		return client;
	}

	public void init() throws Exception {
		setVisible(true);

		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				updateSize();
				createBufferStrategy(2);
				screen.init();
			}
		});

	}

	public void render(Game game) {
		if (screen == null) {
			return;
		}

		screen.render(game);
	}

}
