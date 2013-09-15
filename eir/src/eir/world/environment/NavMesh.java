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
	
	private ArrayList<NavNode>[][] routes;
	
	private int nextNodeIndex = 0;
	
	public static void main( String args[] )
	{
		NavMesh mesh = new NavMesh();
		
		Random r = new Random();
		r.setSeed(22222);
		
		for( int i=0 ; i<5 ; i++ )
		{
			mesh.insertNode(new Vector2(r.nextInt()%10, r.nextInt()%10));
		}
		
		for( int i=0 ; i<5 ; i++ )
		{
			int a=0, b=0;
			while( a==b )
			{
				a = Math.abs(r.nextInt()%5);
				b = Math.abs(r.nextInt()%5);
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
				a = Math.abs(r.nextInt()%5);
				b = Math.abs(r.nextInt()%5);
			}
			System.out.println("from "+a+" to "+b);
			
			String s = "";
			
			List<NavNode> l = mesh.getShortestRoute(mesh.nodes.get(a), mesh.nodes.get(b));
			if( l!=null )
			{
				for( NavNode n : l )
				{
					s+= n.index+",";
				}
				System.out.println(s);
			}
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
		
		@SuppressWarnings("unchecked")
		ArrayList<NavNode>[][] preds = new ArrayList[n][n];
		
		@SuppressWarnings("unchecked")
		ArrayList<NavNode>[][] lastpreds = new ArrayList[n][n];
		
		ArrayList<NavNode>[][] tmppreds = null;
		
		for( int i=0 ; i<n ; i++ )
		{
			for( int j=0 ; j<n ; j++ )
			{	
				if( i!=j )
				{
					dists[i][j] = Float.MAX_VALUE;
					lastdists[i][j] = Float.MAX_VALUE;
				}
				else
				{
					dists[i][j] = 0;
					preds[i][j] = new ArrayList<NavNode>();
					preds[i][j].add(nodes.get(i));
					lastdists[i][j] = 0;
					lastpreds[i][j] = new ArrayList<NavNode>();
					lastpreds[i][j].add(nodes.get(i));
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
				
				if( lastpreds[i][j]==null ) 
					lastpreds[i][j] = new ArrayList<NavNode>();
				
				lastpreds[i][j].add(cur);
				lastpreds[i][j].add(neighbour);
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
						
						if( preds[i][j]==null ) 
							preds[i][j] = new ArrayList<NavNode>();
						else
							preds[i][j].clear();
						
						preds[i][j].add( nodes.get(i) );
						preds[i][j].addAll(lastpreds[k][j]);
					}
					else
					{
						dists[i][j] = lastdists[i][j];
						preds[i][j] = lastpreds[i][j];
					}
				}
			}
			
			
//			System.out.println("-----------------");
//			p(dists, n);
//			System.out.println();
//			p2(preds, n);
			
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
	
//	private void p( float[][] dists, int n )
//	{
//		for( int i=0 ; i<n ; i++ )
//		{
//			for( int j=0 ; j<n ; j++ )
//			{
//				if( dists[i][j]==Float.MAX_VALUE )
//				{
//					System.out.print("inf\t");
//				}
//				else
//					System.out.printf( "%.2f\t",dists[i][j]);
//				
//			}
//			System.out.println();
//		}
//	}
	
	/**
	 * find the shortest route between node a and b
	 * @param a
	 * @param b
	 * @return ordered list starting at a and ending at b using shortest route / null if no route
	 */
	public List<NavNode> getShortestRoute( NavNode a, NavNode b )
	{		
		return routes[a.index][b.index];
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
