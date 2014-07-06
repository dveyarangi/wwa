package eir.resources;

import com.badlogic.gdx.math.Vector2;

public class LevelInitialSettings
{

	private Vector2 cameraPosition;

	public LevelInitialSettings(final Vector2 cameraPosition)
	{
		this.cameraPosition = cameraPosition;
	}


	public Vector2 getCameraPosition() { return cameraPosition; }
}
