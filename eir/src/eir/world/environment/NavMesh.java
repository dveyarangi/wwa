/**
 * 
 */
package eir.world.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * @author dveyarangi
 *
 */
public class NavMesh
{
	private List <NavNode> nodes;
	
	protected NavNode[][] routes;
	
	private int nextNodeIndex = 0;
	
	public static void main( String args[] )
	{
		NavMesh mesh = new NavMesh();
		
		Random rand = new Random();
		rand.setSeed(22222);
		
		for( int i=0 ; i<5 ; i++ )
		{
			mesh.insertNode(new Vector2(rand.nextInt()%10, rand.nextInt()%10));
		}
		
		for( int i=0 ; i<5 ; i++ )
		{
			int a=0, b=0;
			while( a==b )
			{
				a = Math.abs(rand.nextInt()%5);
				b = Math.abs(rand.nextInt()%5);
			}
			
			mesh.linkNodes( mesh.nodes.get(a), mesh.nodes.get(b) );
		}
		
//		long start = System.currentTimeMillis();
		mesh.init();
//		
//		System.out.println(System.currentTimeMillis()-start);
//		
		for( int i=0 ; i<20 ; i++ )
		{
			int a=0, b=0;
			while( a==b )
			{
				a = Math.abs(rand.nextInt()%5);
				b = Math.abs(rand.nextInt()%5);
			}
			System.out.println("from "+a+" to "+b);
			
			String s = "";
			
			Route r = mesh.getShortestRoute(mesh.nodes.get(a), mesh.nodes.get(b));
			while( r.hasNext() )
			{
				NavNode n = r.next();
				s+= n.index+",";
			}
			System.out.println(s);
		}
	}
	
	public NavMesh()
	{
		nodes = new ArrayList<NavNode> ();
	}
	
	public NavNode insertNode(Vector2 point)
	{
		NavNode node = new NavNode(point, nextNodeIndex++);
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
				if( i!=j )
				{
					dists[i][j] = Float.MAX_VALUE;
					lastdists[i][j] = Float.MAX_VALUE;
					preds[i][j] = null;
					lastpreds[i][j] = null;
				}
				else
				{
					dists[i][j] = 0;
					preds[i][j] = nodes.get(i);
					lastdists[i][j] = 0;
					lastpreds[i][j] = nodes.get(i);
				}
			}
		}
		
		for( NavNode cur : nodes )
		{
			for( NavNode neighbour : cur.getNeighbors() )
			{
				int i = cur.index;
				int j = neighbour.index;
				
				lastdists[i][j] = cur.getPoint().dst(neighbour.getPoint());
				dists[i][j] = cur.getPoint().dst(neighbour.getPoint());
				
				lastpreds[i][j] = neighbour;
			}
		}
		
//		System.out.println("-----------------");
//		p(lastdists, n);
//		
//		System.out.println("-----------------");
//		p(dists, n);
//		
//		System.out.println("-----------------");
//		System.out.println("-----------------");
		
		for( int k=0 ; k<n ; k++ )
		{
			for( int i=0 ; i<n ; i++ )
			{
//				System.out.println();
				for( int j=0 ; j<n ; j++ )
				{
					float contendor = lastdists[i][k] + lastdists[k][j];
					
					if( lastdists[i][j]>contendor )
					{
						dists[i][j] = contendor;						
						preds[i][j] = lastpreds[i][k];
					}
					else
					{
						dists[i][j] = lastdists[i][j];
						preds[i][j] = lastpreds[i][j];
					}
				}
			}
			
			
//			System.out.println("-----------------");
			p(dists, n);
//			System.out.println();
			p2(preds, n);
			
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
	
	private void p( float[][] dists, int n )
	{
		for( int i=0 ; i<n ; i++ )
		{
			for( int j=0 ; j<n ; j++ )
			{
				if( dists[i][j]==Float.MAX_VALUE )
				{
					System.out.print("inf\t");
				}
				else
					System.out.printf( "%.2f\t",dists[i][j]);
				
			}
			System.out.println();
		}
	}
	
	private void p2( NavNode[][] dists, int n )
	{
		for( int i=0 ; i<n ; i++ )
		{
			for( int j=0 ; j<n ; j++ )
			{
				if( dists[i][j]==null )
				{
					System.out.print("n\t");
				}
				else
					System.out.print( dists[i][j].index+"\t");
				
			}
			System.out.println();
		}
	}
	
	/**
	 * find the shortest route between node a and b
	 * @param a
	 * @param b
	 * @return ordered list starting at a and ending at b using shortest route / null if no route
	 */
	public Route getShortestRoute( NavNode a, NavNode b )
	{
		return new Route(this, a, b);
//		return routes[a.index][b.index];
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
