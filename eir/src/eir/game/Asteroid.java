/**
 * 
 */
package eir.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author dveyarangi
 *
 */
public class Asteroid
{
	Body body;
	Sprite sprite;
	/**
	 * @param body
	 * @param sprite
	 */
	public Asteroid(Body body, Sprite sprite)
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
	
	
}
