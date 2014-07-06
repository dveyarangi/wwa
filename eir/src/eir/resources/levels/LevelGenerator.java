package eir.resources.levels;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;

import eir.rendering.EntityRenderer;
import eir.rendering.MandalaRenderer;
import eir.resources.LevelParameters;
import eir.resources.PolygonalModel;
import eir.world.Asteroid;
import eir.world.Level;
import eir.world.unit.UnitsFactory;

public class LevelGenerator
{
	public Level generate(final LevelParameters parameters, final UnitsFactory unitsFactory)
	{

		Level level = new Level( unitsFactory );

		for(int aidx = 0; aidx < parameters.getAsteroids(); aidx ++)
		{

			Vector2 position = new Vector2(
					RandomUtil.R( parameters.getWidth() ) - parameters.getWidth()/2,
					RandomUtil.R( parameters.getHeight() ) - parameters.getHeight()/2
					);

			float radius = RandomUtil.STD( 100, 50 );

			int segments = (int)(radius / 2f);

			PolygonalModel model = generateCircleModel( position, Vector2.Zero, radius, segments );

			EntityRenderer <Asteroid> renderer = new MandalaRenderer();

			Asteroid asteroid = new Asteroid();

		}

		return level;
	}


	public PolygonalModel generateCircleModel(final Vector2 position, final Vector2 origin, final float radius, final int segments)
	{
		 final Vector2 [] rawVertices = new Vector2[segments];

		 float angularStep = (float)(Math.PI * 2f / segments);

		 for(int idx = 0; idx < segments; idx ++)
		 {
			 rawVertices[idx] = new Vector2(
					 (float)( radius * Math.cos( angularStep * idx ) ),
					 (float)( radius * Math.sin( angularStep * idx ) )
					 );
		 }

		 PolygonalModel model = new PolygonalModel(rawVertices, origin, position, radius, 0);

		 return model;
	}
}
