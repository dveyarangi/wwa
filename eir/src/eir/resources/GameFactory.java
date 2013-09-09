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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import eir.world.Asteroid;
import eir.world.environment.Web;


/**
 * @author dveyarangi
 *
 */
public class GameFactory
{
	
	private static final String MODELS_PATH = "models/";
	
	/**
	 * Loaded textures by name (filename, actually)
	 */
	private final Map <String, Texture> textureCache = new HashMap <String, Texture> (); 
	
	private final World world;
	
	public GameFactory(World world)
	{
		this.world = world;
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
	
	public PolygonalModel loadModel(String modelId, int size) 
	{
		System.out.println("Loading model " + modelId);
		
		// 0. Create a loader for the file saved from the editor.
	    BodyLoader loader = new BodyLoader(Gdx.files.internal(createBodyPath(modelId)));
	 
	    // 1. Create a BodyDef, as usual.
	    BodyDef bd = new BodyDef();
//	    bd.position.set(x, y);
	    bd.type = BodyType.StaticBody;
	 
	    // 2. Create a FixtureDef, as usual.
	    FixtureDef fd = new FixtureDef();
	    fd.density = 1;
	    fd.friction = 0.5f;
	    fd.restitution = 0.3f;
	 
	    // 3. Create a Body, as usual.
	    Body body = world.createBody(bd);

	    Vector2 origin = new Vector2();
	    
	    // 4. Create the body fixture automatically by using the loader.
	    loader.attachFixture(body, modelId, fd, origin, size);
	    
	    Sprite sprite = createSprite(createImagePath( modelId ));

	    sprite.setSize(size, size);
		sprite.setOrigin( origin.x, origin.y );

	    return new PolygonalModel( origin, body, sprite );
	} 
	
	public Sprite createSprite(String textureName)
	{
		Texture texture = loadTexture( textureName );
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 512);
		
		Sprite sprite = new Sprite(region);
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
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
			texture = new Texture(Gdx.files.internal(textureFile));
		
		return texture;
	}
	
	public void dispose()
	{
		for(Texture texture : textureCache.values())
		{
			texture.dispose();
		}
	}

}
