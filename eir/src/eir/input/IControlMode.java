package eir.input;

import eir.world.IRenderer;
import eir.world.environment.spatial.ISpatialObject;

public interface IControlMode
{
	PickingSensor getPickingSensor();

	void touchUnit( ISpatialObject pickedObject );

	void render( IRenderer renderer );

	void objectPicked( ISpatialObject pickedObject );
	void objectUnpicked( ISpatialObject pickedObject );

	void reset();
}
