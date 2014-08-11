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

	private float rotation;

	public AsteroidDef() {} // for json loader

	public AsteroidDef(final String name, final Vector2 position, final float angle, final float size,
			final TextureHandle texture, final PolygonalModelHandle model, final float rotation)
	{
		super();
		this.name = name;
		this.position = position;
		this.angle = angle;
		this.size = size;
		this.texture = texture;
		this.model = model;
		this.rotation = rotation;
	}

	public String getName() { return name; }

	public float getAngle()	{ return angle;	}
	public Vector2 getPosition() { return position;	}
	public float getSize() { return size; }

	public TextureHandle getTexture() { return texture; }

	public PolygonalModelHandle getModel() { return model; }

	public float getRotation() { return rotation; }
}
