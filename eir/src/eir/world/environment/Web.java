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
	private Anchor source;
	private Anchor target;
	
	private Texture threadTexture;
	private Texture sourceTexture;
	private Texture targetTexture;
	
	private Sprite sourceSprite;
	private Sprite targetSprite;
	private Sprite threadSprite;
	
/*	public Web( String source, String target )
	{
		this.source = source;
		this.target = target;
	}*/
	
	public void draw( SpriteBatch spriteBatch )
	{
		threadSprite.draw(spriteBatch);
		sourceSprite.draw(spriteBatch);
		targetSprite.draw(spriteBatch);
	}
	
	public void init( )
	{
		Asteroid sourceAst = source.getAsteroid();
		Asteroid targetAst = target.getAsteroid();
		
		Vector2 src = Vector2.tmp .set( source.getX() - 0.5f, source.getY() - 0.5f).rotate(sourceAst.getAngle()).add(0.5f,0.5f);
		Vector2 tar = Vector2.tmp2.set( target.getX() - 0.5f, target.getY() - 0.5f).rotate(targetAst.getAngle()).add(0.5f,0.5f);
		
		
		src.set( sourceAst.getX() + (src.x - 0.5f)*sourceAst.getModel().getSprite().getWidth(), 
				 sourceAst.getY() + (src.y - 0.5f)*sourceAst.getModel().getSprite().getHeight() );
		tar.set( targetAst.getX() + (tar.x - 0.5f)*targetAst.getModel().getSprite().getWidth(), 
				 targetAst.getY() + (tar.y - 0.5f)*targetAst.getModel().getSprite().getHeight() );
		
		sourceSprite = new Sprite( new TextureRegion(this.sourceTexture, 512, 256) );
		targetSprite = new Sprite( new TextureRegion(this.targetTexture, 512, 256) );
		threadSprite = new Sprite( new TextureRegion(this.threadTexture, 1024, 256) );
		
		sourceSprite.setPosition( src.x - sourceSprite.getWidth()/2, src.y - sourceSprite.getHeight()/2 );
		targetSprite.setPosition( tar.x - targetSprite.getWidth()/2, tar.y - targetSprite.getHeight()/2 );		
		threadSprite.setPosition( src.x + (tar.x - src.x)/2 - threadSprite.getWidth()/2,
								  src.y + (tar.y - src.y)/2 - threadSprite.getHeight()/2);
		
		Vector2 v = Vector2.tmp.set(tar.x - src.x, tar.y - src.y);
		
		float angle = v.angle();
		targetSprite.rotate( angle );
		sourceSprite.rotate( angle );
		threadSprite.rotate( angle );
		
		float scale = 0.1f; // different sizes of webs?
		
		sourceSprite.setScale(scale);
		targetSprite.setScale(scale);
		threadSprite.setScale((float) (v.len() - sourceSprite.getWidth()*scale)/threadSprite.getWidth(), scale);
	}
	
	public Anchor getSource()
	{
		return source;
	}
	
	public Anchor getTarget()
	{
		return target;
	}
	
	public static class Anchor
	{
		private Asteroid asteroid;
		private float x;
		private float y;
		/**
		 * @return
		 */
		public Asteroid getAsteroid() {	return asteroid; }
		
		public float getX() { return x; }
		public float getY() { return y; }
	}
}

