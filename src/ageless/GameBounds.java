package ageless;

public class GameBounds {
	protected float left;
	protected float top;
	protected float right;
	protected float bottom;

	public GameBounds(float left, float top, float right, float bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public static GameBounds fromRect(float x, float y, float width, float height) {
		return new GameBounds(x, y, x + width, y + height);
	}

}
