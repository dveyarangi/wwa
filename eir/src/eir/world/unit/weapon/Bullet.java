/**
 *
 */
package eir.world.unit.weapon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.IRenderer;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Damage;
import eir.world.unit.IDamager;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
@SuppressWarnings("unused")
public class Bullet extends Unit implements IDamager
{

	//////////////////////////////////////////////////////////////////

	private static int crosshairAnimationId = GameFactory.registerAnimation("anima//ui//crosshair02.atlas", "crosshair");
	private static Animation crosshair = GameFactory.getAnimation( crosshairAnimationId );

	IWeapon weapon;

	float size = 1;


//	public IWeapon weapon;

	float angle;

	boolean leaveTrace = false;
	boolean decaying = false;

	Bullet()
	{
		super();
	}

	@Override
	protected void init()
	{
		super.init();
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

		if( weapon.decayOnNoTarget() && isDecaying() )
		{
			this.lifetime += delta * (this.lifelen - this.lifetime) * 5f;
		}

		if(this.target != null && this.getArea().getAnchor().dst2( this.target.getArea().getAnchor() )  < 1)
		{
			this.setDead();
		}

		weapon.getBulletBehavior().update( delta, this );
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
		Sprite sprite = weapon.getBulletSprite();
		batch.draw( sprite,
				position.x-sprite.getRegionWidth()/2, position.y-sprite.getRegionHeight()/2,
				sprite.getRegionWidth()/2,sprite.getRegionHeight()/2,
				sprite.getRegionWidth(), sprite.getRegionHeight(),
				getSize()/sprite.getRegionWidth(),
				getSize()/sprite.getRegionHeight(), angle);

		if(weapon.getBulletBehavior().requiresTarget() && getTarget() != null)
		{
			TextureRegion crossHairregion = crosshair.getKeyFrame( getLifetime(), true );
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
				Effect effect = weapon.createTraceEffect(this);
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
	public float getSize() {
		return size;
	}

	@Override
	public IWeapon getWeapon() {
		return weapon;
	}

	@Override
	public Effect getDeathEffect()
	{
		return weapon.createHitEffect( this );
	}

	@Override
	public Damage getDamage()
	{
		return weapon.getDamage();
	}
	@Override
	public Unit getSource()
	{
		return getWeapon().getOwner();
	}

	@Override
	public float getMaxSpeed() {return weapon.getBulletSpeed(); }

	public void setDecaying( final boolean decaying ) { this.decaying = decaying; }

	public boolean isDecaying() { return decaying; }
}
