package eir.resources.levels;

import com.badlogic.gdx.math.Vector2;

import eir.resources.PolygonalModelHandle;
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

	private PolygonalModelHandle model;

	public String getName() { return name; }

	public float getAngle()	{ return angle;	}
	public Vector2 getPosition() { return position;	}
	public float getSize() { return size; }

	public TextureHandle getTexture() { return texture; }

	public PolygonalModelHandle getModel() { return model; }
}
