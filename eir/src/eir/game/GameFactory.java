/**
 * 
 */
package eir.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import eir.resources.BodyLoader;

/**
 * @author dveyarangi
 *
 */
public class GameFactory
{
	public static Asteroid createAsteroidHead(float x, float y, World world, String name) {
	    // 0. Create a loader for the file saved from the editor.
	    BodyLoader loader = new BodyLoader(Gdx.files.internal("units/" + name + ".bog"));
	 
	    // 1. Create a BodyDef, as usual.
	    BodyDef bd = new BodyDef();
	    bd.position.set(x, y);
	    bd.type = BodyType.DynamicBody;
	 
	    // 2. Create a FixtureDef, as usual.
	    FixtureDef fd = new FixtureDef();
	    fd.density = 1;
	    fd.friction = 0.5f;
	    fd.restitution = 0.3f;
	 
	    // 3. Create a Body, as usual.
	    Body body = world.createBody(bd);
	    
	 
	    // 4. Create the body fixture automatically by using the loader.
	    loader.attachFixture(body, name, fd, 1f);
	    
	    Sprite sprite = createSprite("units/" + name + ".png");
	    sprite.setPosition(body.getPosition().x, body.getPosition().y );
		sprite.setSize(100, 100);

	    return new Asteroid( body, sprite );
	}
	
	public static Sprite createSprite(String textureName)
	{
		Texture texture = new Texture(Gdx.files.internal(textureName));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 512);
		
		Sprite sprite = new Sprite(region);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		
		return sprite;
	}
}
