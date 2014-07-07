package eir.world.unit.cannons;

import java.util.List;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;

import eir.rendering.IRenderer;
import eir.rendering.LevelRenderer;
import eir.resources.GameFactory;
import eir.world.Level;
import eir.world.environment.Anchor;
import eir.world.environment.RelativeAnchor;
import eir.world.environment.sensors.ISensor;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Damage;
import eir.world.unit.Hull;
import eir.world.unit.IDamager;
import eir.world.unit.Unit;
import eir.world.unit.weapon.Weapon;
import eir.world.unit.weapon.WeaponDef;

public class Cannon extends Unit implements IDamager
{

	public static final int SENSOR_RADIUS = 100;

	private ISensor sensor;

	private Weapon weapon;

	private Damage impactDamage = new Damage( 10, 1, 0, 0 );

	private TargetProvider targetProvider;
	private TargetingModule targetingModule;

	private WeaponDef weaponDef;

	private Anchor weaponMount;

	private float wanderAngle;


	public Cannon( )
	{
		super();

	}

	@Override
	protected void reset( final GameFactory gameFactory, final Level level )
	{
		super.reset( gameFactory, level );

		weaponMount = new RelativeAnchor( this );

		weaponDef = ((CannonDef)this.def).getWeaponDef();


		this.hull = new Hull(500f, 0f, new float [] {0f,0f,0f,0f});

		weapon = level.getUnitsFactory().getUnit( gameFactory, level, weaponDef, weaponMount );
		weapon.getDirection().setAngle( this.angle+90 );
		weapon.getTargetOrientation().setAngle( this.angle+90 );
		wanderAngle = this.angle+90;

		this.sensor = level.getEnvironment().createSensor( this, weaponDef.getSensorRadius() );

		this.targetProvider = weaponDef.createTargetProvider( this );

		this.targetingModule = new LinearTargetingModule();
	}

	@Override
	public void update(final float delta)
	{

		super.update( delta );

		List <ISpatialObject> units = sensor.sense( faction.getEnemyFilter() );


		if(weapon.getBulletsInMagazine() == 0)
		{
			weapon.reload();
		}

//		if(target == null || !target.isAlive() )
//		{
//		}



		target = targetProvider.pickTarget( units );
		weapon.target = target;
		Vector2 targetDirection = targetingModule.getShootingDirection( target, this );
		if(targetDirection != null)
		{
			weapon.getTargetOrientation().set( targetDirection ).nor();
		}
		else
		{	// no target, some meaningless behavior:
			if(RandomUtil.oneOf( 200 ))
			{
				wanderAngle = RandomUtil.STD( this.getAngle(), 30 );
				weapon.getTargetOrientation().setAngle( wanderAngle );
			}
		}

		weapon.update( delta );

		if(! weapon.isOriented() )
			return;

		if( target != null)
		{

			if(weapon.getTimeToReload() > 0)
				return;
			weapon.fire( target );
		}
	}

	@Override
	public void draw( final IRenderer renderer )
	{
		super.draw( renderer );
		weapon.draw( renderer );
	}

	@Override
	public float getSize()
	{
		return 4;
	}

	@Override
	public Weapon getWeapon() { return weapon; }

	@Override
	public Damage getDamage() { return impactDamage; }

	@Override
	public Unit getSource() { return this; }

	@Override
	public float getMaxSpeed() { return 0; }

	@Override
	protected void registerOverlays()
	{
		super.registerOverlays();
		addHoverOverlay( LevelRenderer.WEAPON_OID);
	}

	@Override
	public void setDead()
	{
		super.setDead();
		weapon.setDead();
	}
}
