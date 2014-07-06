package eir.resources.levels;

import com.badlogic.gdx.math.Vector2;

import eir.resources.TextureHandle;

public class AsteroidDef
{

	/**
	 * Asteroid id for whatever we will need tihs
	 */
	private String name;

	/**
	 * Position of asteroid
	 */
	private Vector2 position;

	/**
	 * Angle, huh
	 */
	private float angle;

	/**
	 * Size
	 */
	private float size;

	/**
	 *
	 */
	private TextureHandle texture;

	private String modelId;

	public String getName() { return name; }

	public float getAngle()	{ return angle;	}
	public Vector2 getPosition() { return position;	}
	public float getSize() { return size; }

	public TextureHandle getTexture() { return texture; }

	public String getModelId() { return modelId; }
}
