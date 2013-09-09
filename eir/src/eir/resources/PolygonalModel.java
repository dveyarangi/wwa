/**
 * 
 */
package eir.resources;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author dveyarangi
 *
 */
public class PolygonalModel
{
	Vector2 origin;
	Body body;
	Sprite sprite;
	/**
	 * @param origin 
	 * @param body
	 * @param sprite
	 */
	public PolygonalModel(Vector2 origin, Body body, Sprite sprite)
	{
		super();
		this.origin = origin;
		this.body = body;
		this.sprite = sprite;
	}
	
	public Vector2 getOrigin()
	{
		return origin;
	}
	
	/**
	 * @return
	 */
	public Body getBody()
	{
		return body;
	}
	/**
	 * @return
	 */
	public Sprite getSprite()
	{
		return sprite;
	}
	/**
	 * @param batch
	 */
	public void render(SpriteBatch batch)
	{
		sprite.setPosition( body.getPosition().x-sprite.getOriginX(), body.getPosition().y-sprite.getOriginY() );
		sprite.setRotation( body.getAngle() * MathUtils.radiansToDegrees );
		sprite.draw( batch );
	}

}
