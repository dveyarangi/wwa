/**
 * 
 */
package eir.resources;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.resources.BodyLoader.Model;
import eir.resources.BodyLoader.RigidBodyModel;
import eir.world.Asteroid;
import eir.world.environment.NavMesh;


/**
 * @author dveyarangi
 *
 */
public class GameFactory
{
	
	public static final String TAG = GameFactory.class.getSimpleName();
	
	private static final String MODELS_PATH = "models/";
	
	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Map <String, Texture> textureCache = new HashMap <String, Texture> (); 
	
	private final NavMesh navMesh;
	
	public GameFactory(NavMesh navMesh)
	{
		this.navMesh = navMesh;
	}
	

	/**
	 * @param string
	 * @return
	 */
	public Level loadLevel(String levelName)
	{
		LevelLoader loader = new LevelLoader();
		return loader.readLevel( this, levelName );	
	}
	
	private final String createBodyPath(String modelId)
	{
		return new StringBuilder()
			.append( MODELS_PATH ).append( modelId ).append(".bog")
			.toString();
	}
	private final String createImagePath(String modelId)
	{
		return new StringBuilder()
			.append( MODELS_PATH ).append( modelId ).append(".png")
			.toString();
	}
	
	public PolygonalModel loadAsteroidModel(Asteroid asteroid, String modelId) 
	{
		String modelFile = createBodyPath(modelId);
		log("Loading asteroid model file [" + modelFile + "]");
		
		Model model = BodyLoader.readModel( Gdx.files.internal( modelFile ).readString() );
		RigidBodyModel bodyModel = model.rigidBodies.get( 0 );
		Vector2 [] vertices = bodyModel.shapes.get( 0 ).vertices;
		
		return new PolygonalModel( 
				navMesh, bodyModel.origin, vertices, 
				asteroid.getSize(), 
				asteroid.getPosition(), 
				asteroid.getAngle());

	} 
	
	public Sprite createSprite(String modelId, Vector2 position, Vector2 origin, float width, float height, float degrees)
	{
		Texture texture = loadTexture( createImagePath(modelId) );
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture);
		
		Sprite sprite = new Sprite(region);
		float realOX = origin.x*sprite.getWidth();
		float realOY = origin.y*sprite.getHeight();
		sprite.setOrigin(realOX, realOY);
		
		float scaleX = width/region.getRegionWidth();
		float scaleY = height/region.getRegionHeight();
		sprite.setScale( scaleX, scaleY );
		
		sprite.setRotation( degrees );
		
		sprite.setPosition( position.x-realOX, position.y-realOY );
		
		return sprite;
	}
	
	public static Sprite createSprite(Texture texture)
	{
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 512);
		return new Sprite(region);
	}
	
//	public static Web createWeb( Asteroid source, Asteroid target )
//	{
//		Sprite sourceSprite = createSprite("web_srouce_01.png");
//		Sprite targetSprite = createSprite("web_target_01.png");
//		Sprite threadSprite = createSprite("web_thread_01.png");
//		
//		sourceSprite.setPosition(source.getX(), source.getY());
//		threadSprite.setPosition(source.getX()-target.getX(), source.getY()-target.getY());
//		targetSprite.setPosition(target.getX(), target.getY());
//		
//		return new Web(sourceSprite, threadSprite, targetSprite);
//	}
//	
	
	/**
	 * @param world
	 * @param textureFile
	 * @param size
	 * @return
	 */
	public Texture loadTexture(String textureFile)
	{
		Texture texture = textureCache.get( textureFile );
		if(texture == null)
		{
			texture = new Texture(Gdx.files.internal(textureFile));
			textureCache.put(textureFile, texture);
			log("Loaded texture [" + textureFile + "]" );
		}
		return texture;
	}
	
	public void dispose()
	{
		log("Disposing textures");
		for(Texture texture : textureCache.values())
		{
			texture.dispose();
		}
	}

	private void log(String message)
	{
		Gdx.app.log( TAG, message);
	}


	/**
	 * 
	 */
	public NavMesh getNavMesh()
	{
		return navMesh;
	}
}
