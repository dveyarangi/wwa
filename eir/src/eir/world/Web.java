/**
 * 
 */
package eir.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.environment.nav.NavEdge;
import eir.world.environment.nav.NavNode;


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
	
	public Web() {}
	
	public Web( NavNode source, NavNode target, String threadTexture, String sourceTexture, String targetTexture )
	{
		this.source = new Anchor();
		this.source.asteroid = source.getDescriptor().getObject();
		this.source.navIdx = source.getDescriptor().getIndex();
		
		this.target = new Anchor();
		this.target.asteroid = target.getDescriptor().getObject();
		this.target.navIdx = target.getDescriptor().getIndex();
		
		this.sourceTexture = GameFactory.loadTexture( sourceTexture );
		this.targetTexture = GameFactory.loadTexture( targetTexture );
		this.threadTexture = GameFactory.loadTexture( threadTexture );
		
	}
	
	public void draw( SpriteBatch spriteBatch )
	{
		threadSprite.draw(spriteBatch);
		sourceSprite.draw(spriteBatch);
		targetSprite.draw(spriteBatch);
	}
	
	public void init(Level level)
	{	
		Asteroid sourceAst = source.getAsteroid();
		Asteroid targetAst = target.getAsteroid();
		
		NavNode sourceNode = sourceAst.getModel().getNavNode(source.getNavNodeIdx());
		NavNode targetNode = targetAst.getModel().getNavNode(target.getNavNodeIdx());
		
		level.getNavMesh().linkNodes( sourceNode, targetNode, NavEdge.Type.WEB );
		
/*		Vector2 src = Vector2.tmp .set( source.getX() - 0.5f, source.getY() - 0.5f).rotate(sourceAst.getAngle()).add(0.5f,0.5f);
		Vector2 tar = Vector2.tmp2.set( target.getX() - 0.5f, target.getY() - 0.5f).rotate(targetAst.getAngle()).add(0.5f,0.5f);
		
		
		src.set( sourceAst.getX() + (src.x - 0.5f)*sourceAst.getSize(), 
				 sourceAst.getY() + (src.y - 0.5f)*sourceAst.getSize() );
		tar.set( targetAst.getX() + (tar.x - 0.5f)*targetAst.getSize(), 
				 targetAst.getY() + (tar.y - 0.5f)*targetAst.getSize() );*/
		
		Vector2 src = sourceNode.getPoint();
		Vector2 tar = targetNode.getPoint();
		
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
		
		float scale = v.len()/1000; // different sizes of webs?
		
		sourceSprite.setScale(scale);
		targetSprite.setScale(scale);
		threadSprite.setScale((v.len() - sourceSprite.getWidth()*scale)/threadSprite.getWidth(), scale);
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
		private int navIdx;
		/**
		 * @return
		 */
		public Asteroid getAsteroid() {	return asteroid; }
		
		public int getNavNodeIdx() { return navIdx; }
		
		public boolean isSame(NavNode node)
		{
			return node.getDescriptor().getObject() == getAsteroid()
				&& node.getDescriptor().getIndex() == navIdx;
		}
	}
}

