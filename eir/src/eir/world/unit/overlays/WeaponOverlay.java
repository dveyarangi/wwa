package eir.world.unit.overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import eir.debug.Debug;
import eir.rendering.IRenderer;
import eir.world.unit.IOverlay;
import eir.world.unit.Unit;
import eir.world.unit.weapon.Weapon;

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

		Weapon weapon = unit.getWeapon();

		ShapeRenderer shape = renderer.getShapeRenderer();


		float sensorRadius = weapon.getDef().getSensorRadius();

		shape.setColor( 0,1,0,0.5f );

		shape.begin(ShapeType.Circle);
		shape.circle(unit.cx(), unit.cy(), sensorRadius, 100);
		shape.end();
		Vector2 direction = weapon.getDirection();
		float angle = direction.angle();
		shape.begin(ShapeType.Line);
		shape.line(unit.cx(), unit.cy(),
				(float)(unit.cx() + sensorRadius * Math.cos( angle )),
				(float)(unit.cy() + sensorRadius * Math.sin( angle )));
		shape.end();

		Vector2 leftAngularMargin = direction.tmp().rotate( weapon.getDef().getMaxFireAngle() );
		Vector2 rightAngularMargin = direction.tmp().rotate( -weapon.getDef().getMaxFireAngle() );

		shape.begin(ShapeType.FilledTriangle);
		for(angle = rightAngularMargin.angle(); angle < leftAngularMargin.angle()-10; angle += 10)
		{
			shape.triangle( unit.cx(), unit.cy(),
					(float)(unit.cx() + sensorRadius * Math.cos( angle )),
					(float)(unit.cy() + sensorRadius * Math.sin( angle )),
					(float)(unit.cx() + sensorRadius * Math.cos( angle+10 )),
					(float)(unit.cy() + sensorRadius * Math.sin( angle+10 )));
		}
		shape.end();
	}

	@Override
	public boolean isProjected()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
