package eir.world.unit.overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import eir.rendering.IRenderer;
import eir.world.environment.spatial.AABB;
import eir.world.unit.Hull;
import eir.world.unit.IOverlay;
import eir.world.unit.IUnit;

/**
 *
 * This is rendering method for unit health overlay
 * @author Fima
 *
 * @param <U>
 */
public class IntegrityOverlay <U extends IUnit> implements IOverlay <U>
{

	@Override
	public void draw( final U unit, final SpriteBatch batch )
	{
		// NOOP TODO: not supported yet
	}

	@Override
	public void draw( final U unit, final IRenderer renderer )
	{
		Hull hull = unit.getHull();
		if(hull == null)
			//			Debug.log( "Unit has no hull: " + unit );
			return;

		float maxHP = hull.getMaxHitPoints();
		float currHP = hull.getHitPoints();

		float percentage = currHP / maxHP;

		AABB area = unit.getArea();

		float barYOffset = 0.1f;
		float barHeight = 1;

		float width = area.rx()*2;
		float filledWidth = width * percentage;

		ShapeRenderer shape = renderer.getShapeRenderer();

		shape.setColor( 1 - percentage, 0, percentage, 1 );
		shape.begin( ShapeType.Rectangle );

		shape.rect( area.getMinX(), area.getMaxY()- barYOffset, width, barHeight-barYOffset );

		shape.end();

		shape.begin( ShapeType.FilledRectangle );
		shape.filledRect( area.getMinX(), area.getMaxY()- barYOffset, filledWidth, barHeight-barYOffset );
		shape.end();
	}

	@Override
	public boolean isProjected()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
