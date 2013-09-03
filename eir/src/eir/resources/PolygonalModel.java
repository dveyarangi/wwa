/**
 * 
 */
package eir.resources;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author dveyarangi
 *
 */
public class PolygonalModel
{
	Body body;
	Sprite sprite;
	/**
	 * @param body
	 * @param sprite
	 */
	public PolygonalModel(Body body, Sprite sprite)
	{
		super();
		this.body = body;
		this.sprite = sprite;
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
		sprite.setPosition( body.getPosition().x, body.getPosition().y );
		sprite.setRotation( body.getAngle() * MathUtils.radiansToDegrees );
		sprite.draw( batch );
	}

}
