package eir.input;

import eir.rendering.IRenderer;
import eir.world.environment.spatial.ISpatialObject;

/**
 * Control mode determines input handling some subset of game functionality
 *
 * @author Fima
 *
 */
public interface IControlMode
{
	/**
	 * Called before the mode is activated.
	 */
	void reset();

	/**
	 * Sensor for game entities picking
	 * @return
	 */
	PickingSensor getPickingSensor();

	/**
	 * Game entity mouse hover callback
	 * @param pickedObject
	 */
	void touchUnit( ISpatialObject pickedObject );

	/**
	 * Game entity mouse hover callback
	 * @param pickedObject
	 */
	void objectPicked( ISpatialObject pickedObject );
	/**
	 * Game entity mouse unhover callback
	 * @param pickedObject
	 */
	void objectUnpicked( ISpatialObject pickedObject );

	/**
	 * Renderer for mode-specific UI
	 * @param renderer
	 */
	void render( IRenderer renderer );

}
