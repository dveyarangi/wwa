/**
 * 
 */
package eir.world.environment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
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
		sourceSprite = GameFactory.createSprite(this.sourceTexture);
		targetSprite = GameFactory.createSprite(this.targetTexture);
		threadSprite = GameFactory.createSprite(this.threadTexture);
		
		float sourceCenterX = (source.getX() + source.getModel().getSprite().getWidth() )/2;
		float sourceCenterY = (source.getY() + source.getModel().getSprite().getHeight())/2;

		float targetCenterX = (target.getX() + target.getModel().getSprite().getWidth() )/2;
		float targetCenterY = (target.getY() + target.getModel().getSprite().getHeight())/2;
		
		
		sourceSprite.scale(0.0001f);
		
		sourceSprite.setPosition( sourceCenterX - sourceSprite.getWidth(), sourceCenterY - sourceSprite.getHeight() );
		//threadSprite.setPosition( targetCenterX - sourceCenterX, targetCenterY - sourceCenterY );
		//sourceSprite.setPosition( targetCenterX, targetCenterY );
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

