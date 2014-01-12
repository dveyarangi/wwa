/**
 * 
 */
package eir.world.unit.wildlings;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class Birdy extends Unit
{
	///////////////////////////////////////////

	private static Sprite sprite = GameFactory.createSprite( "anima//gears//birdy_02.png" );
//	private static Sprite sprite = GameFactory.createSprite( "anima//gears//birdy.png" );
	private static int animationId = GameFactory.registerAnimation("anima//glow//glow.atlas",	"glow");

	float pulseLength = 1;

	float pulseStreght = 30;

	float pulseDecay = 0.98f;	
	
	private float size = 5;
	
	///////////////////////////////////////////
	
	float timeToPulse = 0;
	
	Vector2 velocity = new Vector2();

	public int quantum;

	
	public void init()
	{
		super.init();
		
		velocity.set(0,0);
		
		timeToPulse = 0;
		quantum = 0;
	}

	@Override
	public void draw(SpriteBatch batch)
	{
		Vector2 position = getBody().getAnchor();
		batch.draw( sprite, 
				position.x-sprite.getRegionWidth()/2, position.y-sprite.getRegionHeight()/2,
				sprite.getRegionWidth()/2,sprite.getRegionHeight()/2, 
				sprite.getRegionWidth(), sprite.getRegionHeight(), 
				getSize()/sprite.getRegionWidth(), 
				getSize()/sprite.getRegionWidth(), angle);
/*		TextureRegion region = GameFactory.getAnimation(animationId).getKeyFrame( lifetime, true );
		batch.draw( region, 
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2, 
				region.getRegionWidth(), region.getRegionHeight(), 
				size/region.getRegionWidth(), 
				size/region.getRegionWidth(), angle);*/
	}


	@Override
	public float getSize() { return size; }
}
