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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import eir.debug.Debug;
import eir.world.Asteroid;
import eir.world.Level;
import eir.world.environment.MazeType;
import eir.world.environment.nav.FloydWarshal;
import eir.world.environment.nav.NavMesh;
import gnu.trove.map.hash.TIntObjectHashMap;


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

	private static int ANIMATION_ID = 0;

	private static TIntObjectHashMap<Animation> animations = new TIntObjectHashMap <Animation> ();

	private GameFactory() { }

	public static void init()
	{
		if(instance != null)
			throw new IllegalStateException("Game factory is already initialized.");

		instance = new GameFactory();
	}


	public static void dispose()
	{
		Debug.log("Disposing textures");
		for(Texture texture : instance.textureCache.values())
		{
			texture.dispose();
		}
		Debug.log("Disposing atlases");
		for(TextureAtlas atlas : instance.atlasCache.values())
		{
			atlas.dispose();
		}

		animations.clear();
		ANIMATION_ID = 0;

		GameFactory.level = null;
	}

	/**
	 * @param string
	 * @return
	 */
	public static Level loadLevel(final String levelName)
	{
		final LevelLoadingContext context = new LevelLoadingContext();
		LevelLoader loader = new LevelLoader();

		level = loader.readLevel( levelName, context );

		level.init(context);

		return level;
	}

	private static final String createBodyPath(final String modelId)
	{
		return new StringBuilder()
		.append( MODELS_PATH ).append( modelId ).append(".bog")
		.toString();
	}
	private static final String createImagePath(final String modelId)
	{
		return new StringBuilder()
		.append( MODELS_PATH ).append( modelId ).append(".png")
		.toString();
	}

	public static PolygonalModel loadAsteroidModel(final NavMesh mesh, final Asteroid asteroid, final String modelId)
	{
		String modelFile = createBodyPath(modelId);
		Debug.log("Loading asteroid model file [" + modelFile + "]");

		ShapeLoader.RigidBodyModel bodyModel = ShapeLoader.readShape( Gdx.files.internal( modelFile ).readString() ).rigidBodies.get( 0 );
		Vector2 [] vertices = bodyModel.shapes.get( 0 ).vertices;

		return new PolygonalModel(
				(FloydWarshal)mesh,
				asteroid,
				bodyModel.origin,
				vertices,
				bodyModel.polygons);

	}

	public static Body loadBody(final String modelId, final Asteroid asteroid)
	{
		String modelFile = createBodyPath(modelId);
		// 0. Create a loader for the file saved from the editor.
		BodyLoader loader = new BodyLoader(Gdx.files.internal( modelFile));

		// 1. Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.position.set(asteroid.getPosition());
		bd.angle = (float)(asteroid.getAngle()/(2f*Math.PI));

		bd.type = BodyType.StaticBody;

		// 2. Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;

		// 3. Create a Body, as usual.
		Body body = level.getEnvironment().getPhyisics().createBody(bd);

		body.setUserData( MazeType.ASTEROID );
		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(body, "Name", fd, asteroid.getSize());

		return body;
	}

	public static Sprite createSprite(final String modelId, final Vector2 position, final Vector2 origin, final float width, final float height, final float degrees)
	{
		return createSprite(modelId, position, origin.x, origin.y, width, height, degrees);
	}
	public static Sprite createSprite(final String modelId, final Vector2 position, final float ox, final float oy, final float width, final float height, final float degrees)
	{
		Texture texture = loadTexture( createImagePath(modelId) );
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion region = new TextureRegion(texture);

		Sprite sprite = new Sprite(region);
		float realOX = sprite.getWidth()/2;
		float realOY = sprite.getHeight()/2;
		sprite.setOrigin(realOX, realOY);

		float scaleX = width/region.getRegionWidth();
		float scaleY = height/region.getRegionHeight();
		sprite.setScale( scaleX, scaleY );

		sprite.setRotation( degrees );

		sprite.setPosition( position.x-realOX, position.y-realOY );

		return sprite;
	}

	public static Sprite createSprite(final String textureFlie)
	{
		TextureRegion region = new TextureRegion( GameFactory.loadTexture( textureFlie ) );

		Sprite sprite = new Sprite(region);
		//		float realOX = sprite.getWidth()/2;
		//		float realOY = sprite.getHeight()/2;
		//		sprite.setOrigin(realOX, realOY);


		return sprite;
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
	public static Texture loadTexture(final String textureFile)
	{
		Texture texture = instance.textureCache.get( textureFile );
		if(texture == null)
		{
			texture = new Texture(Gdx.files.internal(textureFile));
			instance.textureCache.put(textureFile, texture);
			Debug.log("Loaded texture [" + textureFile + "]" );
		}
		return texture;
	}

	static NumberFormat ANIMA_NUMBERING = new DecimalFormat( "0000" );

	public TextureAtlas loadTextureAtlas(final String atlasName)
	{
		TextureAtlas atlas = atlasCache.get( atlasName );
		if(atlas == null)
		{
			atlas = new TextureAtlas(Gdx.files.internal(atlasName));
			atlasCache.put( atlasName, atlas);
		}

		return atlas;
	}

	public static Animation getAnimation(final int animationId)
	{
		return animations.get(animationId);
	}

	public static String getAnimationName(final String atlasName, final String regionName)
	{
		return atlasName + "/" + regionName;
	}

	private static Animation loadAnimation(final String atlasName, final String regionName)
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

		Debug.log("Loaded animation [" + atlasName + "] (region " + regionName + ")");

		Animation animation = new Animation( 0.05f, frames );
		return animation;
	}

	public static int registerAnimation(final String atlasName, final String regionName)
	{
		Animation animation = loadAnimation(atlasName, regionName);
		animations.put(ANIMATION_ID, animation);
		return ANIMATION_ID ++;
	}
	/**
	 * TODO: cache!
	 * @param string
	 * @return
	 */
	public static BitmapFont loadFont(final String fontName, final float scale)
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
