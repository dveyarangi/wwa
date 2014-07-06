/**
 *
 */
package eir.resources;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.resources.levels.LevelDef;
import eir.world.Asteroid;
import eir.world.unit.UnitsFactory;


/**
 * TODO: this class becomes crumbled, split to rendering and in-game stuff (them probably should go to level)
 *
 * @author dveyarangi
 */
public class GameFactory
{

	public static final String TAG = GameFactory.class.getSimpleName();

	private static final String MODELS_PATH = "models/";

	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Set <TextureHandle> textureHandles = new HashSet <TextureHandle> ();
	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Set <TextureAtlasHandle> atlasHandles = new HashSet <TextureAtlasHandle> ();
	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Set <AnimationHandle> animationHandles = new HashSet <AnimationHandle> ();

	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Map <TextureHandle, Texture> textureCache = new HashMap <TextureHandle, Texture> ();
	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Map <TextureAtlasHandle, TextureAtlas> atlasCache = new HashMap <TextureAtlasHandle, TextureAtlas> ();
	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Map <AnimationHandle, Animation> animationCache = new HashMap <AnimationHandle, Animation> ();

	public GameFactory() { }


	public void dispose()
	{
		Debug.log("Disposing textures...");
		for(Texture texture : textureCache.values())
		{
			texture.dispose();
		}
		textureCache.clear();
		Debug.log("Disposing animations...");
		for(TextureAtlas atlas : atlasCache.values())
		{
			atlas.dispose();
		}

		textureCache.clear();
//		Debug.log("Disposing animations");
		animationCache.clear();
	}

	/**
	 * @param string
	 * @return
	 */
	public LevelDef readLevelDefs(final String levelName, final UnitsFactory unitsFactory)
	{
		LevelLoader loader = new LevelLoader();

		LevelDef levelDef = loader.readLevel( levelName, this, unitsFactory );

		registerSharedResources();

		return levelDef;
	}

	public final static TextureHandle ROCKET_TXR = new TextureHandle( "anima//bullets//rocket01" );
	public final static TextureHandle FIREBALL_TXR = new TextureHandle( "anima//bullets//fireball" );
	public final static TextureHandle CANNON_HYBRID_TXR = new TextureHandle( "anima//cannons//cannon_hybrid_01" );

	public static final TextureAtlasHandle EXPLOSION_03_ATLAS = new TextureAtlasHandle( "anima//effects//explosion//explosion03.atlas" );
	public static final AnimationHandle EXPLOSION_03_ANIM = new AnimationHandle(EXPLOSION_03_ATLAS, "explosion03");
	public static final TextureAtlasHandle EXPLOSION_04_ATLAS = new TextureAtlasHandle( "anima//effects//explosion//explosion04.atlas" );
	public static final AnimationHandle EXPLOSION_04_ANIM = new AnimationHandle(EXPLOSION_04_ATLAS, "explosion04");
	public static final TextureAtlasHandle EXPLOSION_05_ATLAS = new TextureAtlasHandle( "anima//effects//explosion//explosion05.atlas" );
	public static final AnimationHandle EXPLOSION_05_ANIM = new AnimationHandle(EXPLOSION_05_ATLAS, "explosion05");

	public static final TextureAtlasHandle CROSSHAIR_ATLAS = new TextureAtlasHandle( "anima//ui//crosshair01.atlas" );
	public static final AnimationHandle CROSSHAIR_ANIM = new AnimationHandle(CROSSHAIR_ATLAS, "crosshair");
	public static final TextureAtlasHandle SMOKE_ATLAS = new TextureAtlasHandle( "anima//effects//smoke//smoke.atlas" );
	public static final AnimationHandle SMOKE_ANIM = new AnimationHandle(SMOKE_ATLAS, "smoke");


	private void registerSharedResources()
	{

		registerAnimation( CROSSHAIR_ANIM);
		registerAnimation( SMOKE_ANIM );
		registerAnimation( EXPLOSION_03_ANIM );
		registerAnimation( EXPLOSION_04_ANIM );
		registerAnimation( EXPLOSION_05_ANIM );

		registerTexture( FIREBALL_TXR );
		registerTexture( ROCKET_TXR );
		registerTexture( CANNON_HYBRID_TXR );


	}

	public void loadLevelResources()
	{
		loadTextures( );
		loadTextureAtlases( );
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

	public static PolygonalModel loadAsteroidModel(final Asteroid asteroid, final String modelId)
	{
		String modelFile = createBodyPath(modelId);
		Debug.log("Loading asteroid model file [" + modelFile + "]");

		ShapeLoader.RigidBodyModel bodyModel = ShapeLoader.readShape( Gdx.files.internal( modelFile ).readString() ).rigidBodies.get( 0 );
		Vector2 [] vertices = bodyModel.shapes.get( 0 ).vertices;

		return new PolygonalModel(
				vertices,
				bodyModel.origin,
				asteroid.getPosition(),
				asteroid.getSize(),
				asteroid.getAngle()
				);

	}

/*	public Body loadBody(final String modelId, final Asteroid asteroid)
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
		Body body = context.environment.getPhyisics().createBody(bd);

		body.setUserData( MazeType.ASTEROID );
		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(body, "Name", fd, asteroid.getSize());

		return body;
	}*/

	public Sprite createSprite(final TextureHandle handle, final Vector2 position, final Vector2 origin, final float width, final float height, final float degrees)
	{
		return createSprite( handle, position, origin.x, origin.y, width, height, degrees);
	}

	public Sprite createSprite( final TextureHandle handle, final Vector2 position, final float ox, final float oy, final float width, final float height, final float degrees)
	{
		Texture texture = getTexture( handle );
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

	public Texture getTexture( final TextureHandle handle )
	{
		Texture texture = textureCache.get( handle );
		if( texture == null)
			throw new IllegalArgumentException("No texture registered for handle " + handle );

		return texture;
	}

	public Sprite createSprite( final TextureHandle handle )
	{
		TextureRegion region = new TextureRegion( getTexture( handle ) );

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

	public TextureHandle registerTexture( final TextureHandle handle )
	{
		if(!textureHandles.contains( handle ))
		{
			textureHandles.add( handle );
		}

		return handle;
	}
	/**
	 * @param world
	 * @param textureFile
	 * @param size
	 * @return
	 */
	public void loadTextures()
	{
		for( TextureHandle handle : textureHandles )
		{
			Texture texture = new Texture(Gdx.files.internal( handle.getPath() ) + ".png");
			textureCache.put(handle, texture);

			Debug.log("Loaded texture [" + handle + "]" );

		}
	}

	static NumberFormat ANIMA_NUMBERING = new DecimalFormat( "0000" );


	public AnimationHandle registerAnimation(  final AnimationHandle animationHandle )
	{
		TextureAtlasHandle atlasHandle = animationHandle.getAtlas();
		if(!atlasHandles.contains( atlasHandle ))
		{
			atlasHandles.add( atlasHandle );
		}
		if(!animationHandles.contains( animationHandle ))
		{
			animationHandles.add( animationHandle );
		}

		return animationHandle;

	}

	public void loadTextureAtlases()
	{
		for(TextureAtlasHandle handle : atlasHandles)
		{
			TextureAtlas atlas = new TextureAtlas( Gdx.files.internal(handle.getPath()) );
			atlasCache.put( handle, atlas);
		}

		for(AnimationHandle handle : animationHandles)
		{
			animationCache.put( handle, createAnimation( handle ) );
		}
	}


	private Animation createAnimation(final AnimationHandle handle)
	{
		TextureAtlas atlas = atlasCache.get( handle.getAtlas() );

		int size = atlas.getRegions().size;
		TextureRegion[] frames = new TextureRegion[size];

		for(int fidx = 0; fidx < size; fidx ++)
		{
			frames[fidx] = atlas.findRegion( handle.getRegionName() + "." + ANIMA_NUMBERING.format(fidx) );
			if(frames[fidx] == null)
				throw new IllegalArgumentException( "Region array " + handle.getRegionName() + " was not found in atlas " + handle );
		}

		Debug.log("Loaded animation [" + handle + "].");

		Animation animation = new Animation( 0.05f, frames );
		return animation;
	}

	public Animation getAnimation( final AnimationHandle handle )
	{
		Animation animation = animationCache.get( handle );
		if( animation == null)
			throw new IllegalArgumentException("No animation registered for handle " + handle );

		return animation;
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
