/**
 * 
 */
package eir.resources;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.resources.BodyLoader.Model;
import eir.resources.BodyLoader.RigidBodyModel;
import eir.world.Asteroid;
import eir.world.Level;


/**
 * TODO: this class becomes crumbled, split to rendering and in-game stuff (them probably should go to level)
 * 
 * @author dveyarangi
 */
public class GameFactory
{
	
	public static final String TAG = GameFactory.class.getSimpleName();
	
	private static final String MODELS_PATH = "models/";
	
	private static GameFactory instance;
	
	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Map <String, Texture> textureCache = new HashMap <String, Texture> (); 
	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Map <String, TextureAtlas> atlasCache = new HashMap <String, TextureAtlas> (); 
	
	private static Level level;
	
	
	private GameFactory() { }
	
	public static void init()
	{
		if(instance != null)
			throw new IllegalStateException("Game factory is already initialized.");
		
		instance = new GameFactory();
	}
	
	
	public static void dispose()
	{
		log("Disposing textures");
		for(Texture texture : instance.textureCache.values())
		{
			texture.dispose();
		}
		log("Disposing atlases");
		for(TextureAtlas atlas : instance.atlasCache.values())
		{
			atlas.dispose();
		}	
		
		GameFactory.level = null;
	}

	private static void log(String message)
	{
		Gdx.app.log( TAG, message);
	}

	/**
	 * @param string
	 * @return
	 */
	public static Level loadLevel(String levelName)
	{
		LevelLoader loader = new LevelLoader();
		level = loader.readLevel( levelName );
		return level;
	}
	
	private static final String createBodyPath(String modelId)
	{
		return new StringBuilder()
			.append( MODELS_PATH ).append( modelId ).append(".bog")
			.toString();
	}
	private static final String createImagePath(String modelId)
	{
		return new StringBuilder()
			.append( MODELS_PATH ).append( modelId ).append(".png")
			.toString();
	}
	
	public static PolygonalModel loadAsteroidModel(Asteroid asteroid, String modelId) 
	{
		String modelFile = createBodyPath(modelId);
		log("Loading asteroid model file [" + modelFile + "]");
		
		Model model = BodyLoader.readModel( Gdx.files.internal( modelFile ).readString() );
		RigidBodyModel bodyModel = model.rigidBodies.get( 0 );
		Vector2 [] vertices = bodyModel.shapes.get( 0 ).vertices;
		
		return new PolygonalModel( 
				level.getNavMesh(), asteroid, 
				bodyModel.origin, vertices, bodyModel.polygons,
				asteroid.getSize(), 
				asteroid.getPosition(), 
				asteroid.getAngle());

	} 
	
	public static Sprite createSprite(String modelId, Vector2 position, Vector2 origin, float width, float height, float degrees)
	{
		return createSprite(modelId, position, origin.x, origin.y, width, height, degrees);
	}
	public static Sprite createSprite(String modelId, Vector2 position, float ox, float oy, float width, float height, float degrees)
	{
		Texture texture = loadTexture( createImagePath(modelId) );
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture);
		
		Sprite sprite = new Sprite(region);
		float realOX = ox*sprite.getWidth();
		float realOY = oy*sprite.getHeight();
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
	public static Texture loadTexture(String textureFile)
	{
		Texture texture = instance.textureCache.get( textureFile );
		if(texture == null)
		{
			texture = new Texture(Gdx.files.internal(textureFile));
			instance.textureCache.put(textureFile, texture);
			log("Loaded texture [" + textureFile + "]" );
		}
		return texture;
	}
	
	static NumberFormat ANIMA_NUMBERING = new DecimalFormat( "0000" );
	
	public TextureAtlas loadTextureAtlas(String atlasName)
	{
		TextureAtlas atlas = atlasCache.get( atlasName );
		if(atlas == null)
		{
			atlas = new TextureAtlas(Gdx.files.internal(atlasName));
			atlasCache.put( atlasName, atlas);
		}

		return atlas;
	}
	
	public static Animation loadAnimation(String atlasName, String regionName)
	{
		TextureAtlas atlas = instance.loadTextureAtlas( atlasName );
		
		int size = atlas.getRegions().size;
		TextureRegion[] frames = new TextureRegion[size];

		for(int fidx = 0; fidx < size; fidx ++)
		{
			frames[fidx] = atlas.findRegion( regionName + "." + ANIMA_NUMBERING.format(fidx) );
			if(frames[fidx] == null)
				throw new IllegalArgumentException( "Region array " + regionName + " was not found in atlas " + atlasName );
		}
		
		Animation animation = new Animation( 0.05f, frames );
		return animation;
	}

	/**
	 * TODO: cache!
	 * @param string
	 * @return
	 */
	public static BitmapFont loadFont(String fontName, float scale)
	{
		BitmapFont font =  new BitmapFont(
				 Gdx.files.internal(fontName + ".fnt"), 
				 Gdx.files.internal(fontName + ".png"),
				 false // wat is flip?
				 );
		 
		 font.setScale( scale );
		 
		 return font;
	}

	

}
