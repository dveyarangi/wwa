/**
 *
 */
package eir.world.unit.ai;

import yarangi.numbers.RandomUtil;
import eir.resources.PolygonalModel;
import eir.world.environment.spatial.ISpatialObject;


/**
 * @author dveyarangi
 *
 */
public class PolygonGuardingOrder extends Order
{

	private PolygonalModel model;

	/**
	 * @param priority
	 * @param sourceNode
	 * @param targetNode
	 */
	public PolygonGuardingOrder(final float priority, final PolygonalModel model)
	{
		super( new TaskStage [] { TaskStage.GUARD }, false, priority,

			null, null );

		this.model = model;
	}


	@Override
	public ISpatialObject getTarget() {

		// picking random polygon vertex for guarding target
		int nodeIdx = RandomUtil.N( model.getSize() );
		return model.getNavNode( nodeIdx );

	}

}
