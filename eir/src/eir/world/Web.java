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
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavNode;
import eir.world.environment.nav.SurfaceNavNode;


/**
 * @author dveyarangi
 *
 */
public class Web
{
	private SurfaceNavNode source;
	private SurfaceNavNode target;

	private Texture threadTexture;
	private Texture sourceTexture;
	private Texture targetTexture;

	private Sprite sourceSprite;
	private Sprite targetSprite;
	private Sprite threadSprite;

	public Web() {}

	public Web( final SurfaceNavNode source, final SurfaceNavNode target, final String threadTexture, final String sourceTexture, final String targetTexture )
	{
		this.source = source;

		this.target = target;

		this.sourceTexture = GameFactory.loadTexture( sourceTexture );
		this.targetTexture = GameFactory.loadTexture( targetTexture );
		this.threadTexture = GameFactory.loadTexture( threadTexture );

	}

	public void draw( final SpriteBatch spriteBatch )
	{
		sourceSprite.draw(spriteBatch);
		targetSprite.draw(spriteBatch);
		threadSprite.draw(spriteBatch);
	}

	public void init(final NavMesh <SurfaceNavNode> mesh)
	{

		mesh.linkNodes( source, target, NavEdge.Type.WEB );

		/*		Vector2 src = Vector2.tmp .set( source.getX() - 0.5f, source.getY() - 0.5f).rotate(sourceAst.getAngle()).add(0.5f,0.5f);
		Vector2 tar = Vector2.tmp2.set( target.getX() - 0.5f, target.getY() - 0.5f).rotate(targetAst.getAngle()).add(0.5f,0.5f);


		src.set( sourceAst.getX() + (src.x - 0.5f)*sourceAst.getSize(),
				 sourceAst.getY() + (src.y - 0.5f)*sourceAst.getSize() );
		tar.set( targetAst.getX() + (tar.x - 0.5f)*targetAst.getSize(),
				 targetAst.getY() + (tar.y - 0.5f)*targetAst.getSize() );*/

		Vector2 src = source.getPoint();
		Vector2 tar = target.getPoint();

		sourceSprite = new Sprite( new TextureRegion(this.sourceTexture, 32, 32) );
		targetSprite = new Sprite( new TextureRegion(this.targetTexture, 32, 32) );
		threadSprite = new Sprite( new TextureRegion(this.threadTexture, 1024, 32) );

		sourceSprite.setPosition( src.x - ( sourceSprite.getWidth()/2 ), src.y - ( sourceSprite.getHeight()/2 ) );
		targetSprite.setPosition( tar.x - ( targetSprite.getWidth()/2 ), tar.y - ( targetSprite.getHeight()/2 ) );
		threadSprite.setPosition( ( src.x + ( (tar.x - src.x)/2 ) ) - ( threadSprite.getWidth()/2 ),
				( src.y + ( (tar.y - src.y)/2 ) ) - ( threadSprite.getHeight()/2 ));

		Vector2 v = Vector2.tmp.set(tar.x - src.x, tar.y - src.y);

		float angle = v.angle();
		targetSprite.rotate( angle );
		sourceSprite.rotate( angle );
		threadSprite.rotate( angle );

		float scale = 0.1f; // different sizes of webs?

		sourceSprite.setScale(scale);
		targetSprite.setScale(scale);
		threadSprite.setScale(( v.len()/threadSprite.getWidth() ), 1 * scale);
	}

	public NavNode getSource()
	{
		return source;
	}

	public NavNode getTarget()
	{
		return target;
	}

}

