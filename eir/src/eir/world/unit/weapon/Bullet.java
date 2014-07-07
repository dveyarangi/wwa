/**
 *
 */
package eir.world.unit.weapon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.Level;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Damage;
import eir.world.unit.IDamager;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */

public class Bullet extends Unit implements IDamager
{

	//////////////////////////////////////////////////////////////////

	Weapon weapon;


//	public IWeapon weapon;

	float angle;

	boolean leaveTrace = false;
	boolean decaying = false;

	Bullet()
	{
		super();
	}

	@Override
	protected void reset( final GameFactory gameFactory, final Level level )
	{
		super.reset( gameFactory, level );
		//this.hull = new Hull(0.001f, 0f, new float [] {0f,0f,0f,0f});
		this.leaveTrace = false;

		this.decaying = false;
	}


	/**
	 * @param delta
	 */
	@Override
	public void update(final float delta)
	{
		super.update( delta );

		if(this.target == null || ! this.target.isAlive() )
		{
			this.setDecaying( true );
			this.target = null;
		}

		if( weapon.getDef().decayOnNoTarget() && isDecaying() )
		{
			this.lifetime += delta * (this.lifelen - this.lifetime) * 5f;
		}

		if(this.target != null && this.getArea().getAnchor().dst2( this.target.getArea().getAnchor() )  < 1)
		{
			this.setDead();
		}

		weapon.getDef().getBulletBehavior().update( delta, this );
	}

	@Override
	public void draw( final IRenderer renderer )
	{
		final SpriteBatch batch = renderer.getSpriteBatch();

		Vector2 position = getBody().getAnchor();
/*		TextureRegion region = weapon.getBulletAnimation().getKeyFrame( getLifetime(), true );
		batch.draw( region,
				position.x-region.getRegionWidth()/2, position.y-region.getRegionHeight()/2,
				region.getRegionWidth()/2,region.getRegionHeight()/2,
				region.getRegionWidth(), region.getRegionHeight(),
				size/region.getRegionWidth(),
				size/region.getRegionWidth(), angle);
*/
		Sprite sprite = getUnitSprite();
		batch.draw( sprite,
				position.x-sprite.getRegionWidth()/2, position.y-sprite.getRegionHeight()/2,
				sprite.getRegionWidth()/2,sprite.getRegionHeight()/2,
				sprite.getRegionWidth(), sprite.getRegionHeight(),
				getSize()/sprite.getRegionWidth(),
				getSize()/sprite.getRegionHeight(), angle);

		Animation crossHair = renderer.getAnimation( GameFactory.CROSSHAIR_ANIM );

		if(weapon.getDef().getBulletBehavior().requiresTarget() && getTarget() != null)
		{
			TextureRegion crossHairregion = crossHair.getKeyFrame( getLifetime(), true );
			batch.draw( crossHairregion,
					getTarget().getArea().getAnchor().x-crossHairregion.getRegionWidth()/2, getTarget().getArea().getAnchor().y-crossHairregion.getRegionHeight()/2,
					crossHairregion.getRegionWidth()/2,crossHairregion.getRegionHeight()/2,
					crossHairregion.getRegionWidth(), crossHairregion.getRegionHeight(),
					5f/crossHairregion.getRegionWidth(),
					5f/crossHairregion.getRegionWidth(), angle);
		}

		if(leaveTrace)
		{
//			if(RandomUtil.oneOf( 3 ))
			{
				Effect effect = weapon.getDef().createTraceEffect(this);
				if(effect != null)
				{
					renderer.addEffect( effect );
				}
			}
		}

	}

	@Override
	public ISpatialObject getTarget() { return target; }


	@Override
	public Weapon getWeapon() {	return weapon; }

	@Override
	public Effect getDeathEffect(  )
	{
		return weapon.getDef().createHitEffect( this, true );
	}

	@Override
	public Damage getDamage()
	{
		return weapon.getDef().getDamage();
	}
	@Override
	public Unit getSource()
	{
		return getWeapon();
	}

	@Override
	public float getMaxSpeed() {return weapon.getDef().getBulletSpeed(); }

	public void setDecaying( final boolean decaying ) { this.decaying = decaying; }

	public boolean isDecaying() { return decaying; }
}
