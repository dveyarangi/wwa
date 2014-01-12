/**
 * 
 */
package eir.world.unit.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.Level;
import eir.world.environment.spatial.AABB;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Damage;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
@SuppressWarnings("unused")
public class Bullet extends Unit
{
	
	//////////////////////////////////////////////////////////////////
	
	private static int crosshairAnimationId = GameFactory.registerAnimation("anima//ui//crosshair02.atlas", "crosshair");
	private static Animation crosshair = GameFactory.getAnimation( crosshairAnimationId );

	IWeapon weapon;
	
	float size = 1;

	/**
	 * Bullet velocity
	 */
	Vector2 velocity;
	
	Vector2 target;
	
//	public IWeapon weapon;
	
	float angle;

	
	Bullet()
	{
		velocity = Vector2.Zero.cpy();
		
		target = Vector2.Zero.cpy();
	}
	
	public void init()
	{
		super.init();
		this.lifetime = 0;
	}


	/**
	 * @param delta
	 */
	@Override
	public void update(float delta)
	{
		lifetime += delta;
		if(lifetime > weapon.getLifeDuration())
		{
			setDead();
			return;
		}
		weapon.getBulletBehavior().update( delta, this );
	}
	
	@Override
	public void draw( SpriteBatch batch )
	{
		Vector2 position = getBody().getAnchor();
		TextureRegion region = weapon.getBulletAnimation().getKeyFrame( lifetime, true );
		batch.draw( region, 
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2, 
				region.getRegionWidth(), region.getRegionHeight(), 
				size/region.getRegionWidth(), 
				size/region.getRegionWidth(), angle);
		
		if(weapon.getBulletBehavior().requiresTarget() && target != null)
		{
			TextureRegion crossHairregion = crosshair.getKeyFrame( lifetime, true );
			batch.draw( crossHairregion, 
					target.x-crossHairregion.getRegionWidth()/2, target.y-crossHairregion.getRegionHeight()/2,
					crossHairregion.getRegionWidth()/2,crossHairregion.getRegionHeight()/2, 
					crossHairregion.getRegionWidth(), crossHairregion.getRegionHeight(), 
					5f/crossHairregion.getRegionWidth(), 
					5f/crossHairregion.getRegionWidth(), angle);
		}
		
		Effect effect = weapon.createTraceEffect(this);
		if(effect != null)
			weapon.getLevel().addEffect( effect );

	}

	public Vector2 getVelocity() { return velocity; }

	public Vector2 getTarget() { return target; }


	@Override
	public float getSize() {
		return size;
	}

	public IWeapon getWeapon() {
		return weapon;
	}

	public Effect getDeathEffect()
	{
		return weapon.createHitEffect( this );
	}
}
