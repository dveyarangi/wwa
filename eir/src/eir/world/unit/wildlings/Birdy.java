/**
 *
 */
package eir.world.unit.wildlings;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.world.IRenderer;
import eir.world.unit.Damage;
import eir.world.unit.Hull;
import eir.world.unit.IDamager;
import eir.world.unit.TaskedUnit;
import eir.world.unit.Unit;

/**
 * @author dveyarangi
 *
 */
public class Birdy extends TaskedUnit implements IDamager
{
	///////////////////////////////////////////

	private static Sprite sprite = GameFactory.createSprite( "anima//gears//birdy_02.png" );
//	private static Sprite sprite = GameFactory.createSprite( "anima//gears//birdy.png" );
	private static int animationId = GameFactory.registerAnimation("anima//glow//glow.atlas",	"glow");

	private  float maxSpeed = 40;

	private float size = 1;

	///////////////////////////////////////////

	public Damage damage = new Damage(1,1,1,1);

	public Vector2 impactImpulse;

	@Override
	protected void init()
	{
		super.init();
		this.hull = new Hull(100f, 0f, new float [] {0f,0f,0f,0f});
	}

	@Override
	public void draw( final IRenderer renderer)
	{
		final SpriteBatch batch = renderer.getSpriteBatch();
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
	public void update(final float delta)
	{
		super.update( delta );
	}
	@Override
	public float hit(final Damage source, final IDamager damager, final float damageCoef)
	{
		float damage = super.hit( source, damager, damageCoef );


		Unit unit = (Unit)damager;

		impactImpulse = this.getArea().getAnchor().tmp()
				.sub(
						unit.getArea().getAnchor() )
				.nor()
				.mul( damage );


		getVelocity().add( impactImpulse );

		return damage;
	}

	@Override
	public float getSize() { return size; }

	@Override
	public Damage getDamage() {	return damage; }

	@Override
	public Unit getSource()
	{
		return this; // TODO: maybe generalize to drone and make source the spawner?
	}

	@Override
	public float getMaxSpeed() { return maxSpeed; }

}
