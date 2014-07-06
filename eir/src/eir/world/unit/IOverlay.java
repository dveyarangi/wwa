package eir.world.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eir.rendering.IRenderer;

/**
 * Unit decorator
 * @author Fima
 *
 */
public interface IOverlay <U extends IUnit>
{

	public abstract void draw( U unit, final SpriteBatch batch );


	public void draw( U unit, final IRenderer renderer );

	/***
	 * If false, overlay will be rendered in absolute screen coordinates system
	 * @return
	 */
	public boolean isProjected();
}
