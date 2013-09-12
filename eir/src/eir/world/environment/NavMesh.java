/**
 * 
 */
package eir.world.environment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.sun.org.apache.bcel.internal.generic.SWAP;

/**
 * @author dveyarangi
 *
 */
public class NavMesh
{
	private List <NavNode> nodes;
	
	private NavNode[][] routes; // predecessors
	
	public NavMesh()
	{
		nodes = new ArrayList<NavNode> ();
	}
	
	public NavNode insertNode(Vector2 point)
	{
		NavNode node = new NavNode(point);
		nodes.add( node );
		return node;
	}
	
	public void linkNodes(NavNode na, NavNode nb)
	{
		na.addNeighbour(nb);
		nb.addNeighbour(na);
	}
	
	public void init()
	{
		int n = nodes.size();
		
		float[][] dists = new float[n][n];
		float[][] lastdists = new float[n][n];
		float[][] tmpdists = null;
		
		NavNode[][] preds = new NavNode[n][n];
		NavNode[][] lastpreds = new NavNode[n][n];
		NavNode[][] tmppreds = null;
		
		for( int i=0 ; i<n ; i++ )
		{
			for( int j=0 ; j<n ; j++ )
			{
				dists[i][j] = Float.MAX_VALUE;
				lastdists[i][j] = Float.MAX_VALUE;
				preds[i][j] = null;
				lastpreds[i][j] = null;
			}
		}
		
		for( NavNode cur : nodes )
		{
			for( NavNode neighbour : cur.getNeighbors() )
			{
				lastdists[nodes.indexOf(cur)][nodes.indexOf(neighbour)] = cur.getPoint().dst(neighbour.getPoint());
				lastpreds[nodes.indexOf(cur)][nodes.indexOf(neighbour)] = neighbour;
			}
		}
		
		for( int k=0 ; k<n ; k++ )
		{
			for( int i=0 ; i<n ; i++ )
			{
				for( int j=0 ; j<n ; j++ )
				{
					float contendor = lastdists[i][k] + lastdists[k][j];
					
					if( lastdists[i][j]>contendor )
					{
						dists[i][j] = contendor;
						preds[i][j] = nodes.get(k);
					}
				}
			}
			
			// swap buffers
			tmppreds = lastpreds;
			lastpreds = preds;
			preds = tmppreds;
			
			tmpdists = lastdists;
			lastdists = dists;
			dists = tmpdists;
		}
		
		routes = lastpreds;
	}
	
	
	/**
	 * find the shortest route between node a and b
	 * @param a
	 * @param b
	 * @return ordered list starting at a and ending at b using shortest route / null if no route
	 */
	public List<NavNode> getShortestRoute( NavNode a, NavNode b )
	{
		int aindex = nodes.indexOf(a);
		int bindex = nodes.indexOf(b);
		
		if( routes[aindex][bindex]==null )
			return null;
		
		
		List<NavNode> route = new LinkedList<NavNode>();
		
		while( routes[aindex][bindex] != b )
		{
			route.add(routes[aindex][bindex]);
		}
		
		route.add(b);
		
		return route;
	}
	
	
	/**
	 * Debug rendering method
	 * @param shape
	 */
	public void draw(ShapeRenderer shape)
	{
		shape.setColor( 0, 1, 0, 0.5f );
		for(NavNode srcNode : nodes)
		{
			shape.begin(ShapeType.Circle);
				shape.circle( srcNode.getPoint().x, srcNode.getPoint().y, 1 );
			shape.end();
			
			for(NavNode tarNode : srcNode.getNeighbors())
			{

				shape.begin(ShapeType.Line);
					shape.line( srcNode.getPoint().x, srcNode.getPoint().y, tarNode.getPoint().x, tarNode.getPoint().y);
				shape.end();
			
			}
		}
		
	}
}
