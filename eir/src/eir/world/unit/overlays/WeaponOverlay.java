package eir.world.unit.overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.rendering.IRenderer;
import eir.world.unit.IOverlay;
import eir.world.unit.Unit;
import eir.world.unit.cannons.Cannon;
import eir.world.unit.weapon.IWeapon;

public class WeaponOverlay implements IOverlay <Unit>
{

	@Override
	public void draw( final Unit unit, final SpriteBatch batch )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void draw( final Unit unit, final IRenderer renderer )
	{
		if( unit.getWeapon() == null )
		{
			Debug.log( "Unit has no weapon:" + unit );
			return;
		}

		IWeapon weapon = unit.getWeapon();

		ShapeRenderer shape = renderer.getShapeRenderer();


		shape.setColor( 0,1,0,0.5f );

		shape.begin(ShapeType.Circle);
		shape.circle(unit.cx(), unit.cy(), Cannon.SENSOR_RADIUS);
		shape.end();
		Vector2 direction = weapon.getDirection();
		shape.begin(ShapeType.Line);
		shape.line(unit.cx(), unit.cy(), unit.cx() + direction.x * Cannon.SENSOR_RADIUS, unit.cy() + direction.y * Cannon.SENSOR_RADIUS);
		shape.end();


		Vector2 leftAngularMargin = direction.tmp().rotate( weapon.getMaxFireAngle() );
		shape.begin(ShapeType.Line);
		shape.line(unit.cx(), unit.cy(), unit.cx() + leftAngularMargin.x * Cannon.SENSOR_RADIUS, unit.cy() + leftAngularMargin.y * Cannon.SENSOR_RADIUS);
		shape.end();
		Vector2 rightAngularMargin = direction.tmp().rotate( -weapon.getMaxFireAngle() );
		shape.begin(ShapeType.Line);
		shape.line(unit.cx(), unit.cy(), unit.cx() + rightAngularMargin.x * Cannon.SENSOR_RADIUS, unit.cy() + rightAngularMargin.y * Cannon.SENSOR_RADIUS);
		shape.end();

	}

	@Override
	public boolean isProjected()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
