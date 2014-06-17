package eir.input;

import eir.world.IRenderer;
import eir.world.environment.spatial.ISpatialObject;
import eir.world.unit.Unit;

public class OrderingControlMode implements IControlMode
{
	PickingSensor sensor = new PickingSensor(new IPickingFilter () {

		@Override
		public boolean accept( final ISpatialObject entity )
		{
			return entity instanceof Unit;
		}

	});

	private Unit pickedUnit;

	@Override
	public PickingSensor getPickingSensor() { return sensor; }

	@Override
	public void render( final IRenderer renderer )
	{
	}

	@Override
	public void objectPicked( final ISpatialObject pickedObject )
	{
		pickedUnit = (Unit) pickedObject;
		pickedUnit.setIsHovered( true );
	}

	@Override
	public void objectUnpicked( final ISpatialObject pickedObject )
	{
//		assert pickedUnit == pickedObject;
		if(pickedUnit != null)
		{
			pickedUnit.setIsHovered( false );
			pickedUnit = null;
		}
	}
	@Override
	public void touchUnit( final ISpatialObject pickedObject )
	{
	}

	@Override
	public void reset()
	{
		pickedUnit = null;
	}


}
