/**
 *
 */
package eir.world.unit.weapon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.Effect;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Damage;
import eir.world.unit.Hull;
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

	/**
	 * Bullet velocity
	 */
	Vector2 velocity;

//	public IWeapon weapon;

	float angle;

	TargetProvider targetProvider;

	boolean leaveTrace = false;

	Bullet()
	{
		super();

		velocity = Vector2.Zero.cpy();
	}

	@Override
	protected void init()
	{
		super.init();
		this.lifetime = 0;
		this.hull = new Hull(0.001f, 0f, new float [] {0f,0f,0f,0f});
		this.target = null;
		this.targetProvider = null;
		this.leaveTrace = false;
	}


	/**
	 * @param delta
	 */
	@Override
	public void update(final float delta)
	{
		super.update( delta );

		weapon.getBulletBehavior().update( delta, this );
	}

	@Override
	public void draw( final SpriteBatch batch )
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
					target.getArea().getAnchor().x-crossHairregion.getRegionWidth()/2, target.getArea().getAnchor().y-crossHairregion.getRegionHeight()/2,
					crossHairregion.getRegionWidth()/2,crossHairregion.getRegionHeight()/2,
					crossHairregion.getRegionWidth(), crossHairregion.getRegionHeight(),
					5f/crossHairregion.getRegionWidth(),
					5f/crossHairregion.getRegionWidth(), angle);
		}

		if(leaveTrace)
		{
			Effect effect = weapon.createTraceEffect(this);
			if(effect != null)
			{
				weapon.getLevel().addEffect( effect );
			}
		}

	}

	public Vector2 getVelocity() { return velocity; }

	@Override
	public ISpatialObject getTarget() { return targetProvider.getTarget(); }


	@Override
	public float getSize() {
		return size;
	}

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
	public float getMaxSpeed() {return weapon.getMaxSpeed(); }
}
