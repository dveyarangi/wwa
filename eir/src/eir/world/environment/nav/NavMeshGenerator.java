package eir.world.environment.nav;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import eir.world.Asteroid;
import eir.world.environment.nav.NavEdge.Type;

import  yarangi.math.triangulation.ITriangulator;
import  yarangi.math.triangulation.Delaunay2D;
import  yarangi.math.triangulation.TriangleStore;
import yarangi.math.Geometry;
import yarangi.math.IVector2D;
import yarangi.math.Vector2D;
import yarangi.math.Edge;

public class NavMeshGenerator 
{
	
	private ITriangulator triangulator;
	
	public NavMeshGenerator()
	{
		triangulator = new Delaunay2D();
	}
	
	
	public NavMesh generateMesh(List <Asteroid> asteroids, Vector2 minCorner, Vector2 maxCorner)
	{
		NavMesh mesh = new DummyNavMesh();
		
		List <IVector2D> points = new LinkedList <IVector2D> ();
		
		List <Polygon> polygons = new LinkedList <Polygon> ();
		
		for(Asteroid asteroid : asteroids)
		{
			float [] polyv = new float[2*asteroid.getModel().getVertices().length];
			int idx = 0;
			for(Vector2 vertex : asteroid.getModel().getVertices())
			{
				polyv[idx++] = vertex.x;
				polyv[idx++] = vertex.y;
				points.add(Vector2D.R(vertex.x, vertex.y));
			}
			polygons.add(new Polygon(polyv));
		}
		
		points.add(Vector2D.R(minCorner.x, minCorner.y));
		points.add(Vector2D.R(minCorner.x, maxCorner.y));
		points.add(Vector2D.R(maxCorner.x, maxCorner.y));
		points.add(Vector2D.R(maxCorner.x, minCorner.y));
		
		TriangleStore store = triangulator.triangulate(points);

		Set <Edge> traversedEdges = new HashSet <Edge> ();
		
		// TODO: get rid of IVector2D or Vector2
		Map <Vector2, NavNode> nodes = new HashMap <Vector2, NavNode> ();
		edges: for(Edge edge1 : store.getEdges())
		{
			if( traversedEdges.contains(edge1) )
				continue;
			
			traversedEdges.add( edge1 );
			
			Edge edge2 = new Edge( edge1.second(), edge1.first() );
			
			IVector2D p1 = store.getCCWPoint( edge1 );
			IVector2D p2 = store.getCCWPoint( edge2 );
			
			if(p2 == null)
				continue;
			
			traversedEdges.add( edge2 );
			
			IVector2D o1 = Geometry.cirrcumCircleCenter(p1, edge1.first(), edge1.second());
			IVector2D o2 = Geometry.cirrcumCircleCenter(p2, edge2.first(), edge2.second());
			
			Vector2 c1 = new Vector2( (float)o1.x(), (float)o1.y());
			Vector2 c2 = new Vector2( (float)o2.x(), (float)o2.y());
			

			
			if(c1.x < minCorner.x || c1.y < minCorner.y || c1.x > maxCorner.x || c1.y > maxCorner.y)
				continue edges;
			if(c2.x < minCorner.x || c2.y < minCorner.y || c2.x > maxCorner.x || c2.y > maxCorner.y)
				continue edges;
			
			for(Polygon polygon : polygons)
			{
				if(polygon.contains(c1.x, c1.y) || polygon.contains(c2.x, c2.y))
				{
					continue edges;
				}
			}		
			
			NavNode node1;
			if(nodes.containsKey(c1))
				node1 = nodes.get(c1);
			else
				nodes.put(c1, node1 = mesh.insertNode( null, c1 )) ;
			NavNode node2;
			if(nodes.containsKey(c2))
				node2 = nodes.get(c2);
			else
				nodes.put(c2, node2 = mesh.insertNode( null, c2 )) ;
			
			mesh.linkNodes(node1, node2, Type.AIR);
			
		}
		
		return mesh;
	}
}
