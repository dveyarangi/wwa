package eir.resources;

public class LevelParameters
{
	private int factions;

	private int asteroids;

	private int width, height;

	public LevelParameters(final int width, final int height, final int factions, final int asteroids)
	{
		super();

		this.width = width;
		this.height = height;

		this.factions = factions;
		this.asteroids = asteroids;
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public int getFactions() { return factions; }

	public int getAsteroids() {	return asteroids; }


}
