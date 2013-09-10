/**
 * 
 */
package eir.world.environment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.world.Asteroid;

/**
 * @author dveyarangi
 *
 */
public class Web
{
	private String source;
	private String target;
	
	private Texture threadTexture;
	private Texture sourceTexture;
	private Texture targetTexture;
	
	private Sprite sourceSprite;
	private Sprite targetSprite;
	private Sprite threadSprite;
	
	public Web( String source, String target )
	{
		this.source = source;
		this.target = target;
	}
	
	public void draw( SpriteBatch spriteBatch )
	{
		threadSprite.draw(spriteBatch);
		sourceSprite.draw(spriteBatch);
		targetSprite.draw(spriteBatch);
	}
	
	public void init( Asteroid source, Asteroid target )
	{
		sourceSprite = new Sprite( new TextureRegion(this.sourceTexture, 512, 256) );
		targetSprite = new Sprite( new TextureRegion(this.targetTexture, 512, 256) );
		threadSprite = new Sprite( new TextureRegion(this.threadTexture, 1024, 256) );
		
		sourceSprite.setPosition( source.getX() - sourceSprite.getWidth()/2, source.getY() - sourceSprite.getHeight()/2 );
		targetSprite.setPosition( target.getX() - targetSprite.getWidth()/2, target.getY() - targetSprite.getHeight()/2 );		
		threadSprite.setPosition( source.getX() + (target.getX() - source.getX())/2 - threadSprite.getWidth()/2,
								  source.getY() + (target.getY() - source.getY())/2 - threadSprite.getHeight()/2);
		
		
		Vector2 v = Vector2.tmp.set(target.getX() - source.getX(), target.getY() - source.getY());
		
		float angle = v.angle();
		targetSprite.rotate( angle );
		sourceSprite.rotate( angle );
		threadSprite.rotate( angle );
		
		float scale = 0.1f; // different sizes of webs?
		
		sourceSprite.setScale(scale);
		targetSprite.setScale(scale);
		threadSprite.setScale((float) (v.len() - sourceSprite.getWidth()*scale)/threadSprite.getWidth(), scale);
	}
	
	public String getSource()
	{
		return source;
	}
	
	public String getTarget()
	{
		return target;
	}
}

