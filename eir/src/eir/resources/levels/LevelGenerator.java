package eir.resources.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import mandala.Mandala;
import yarangi.math.BitUtils;
import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import eir.resources.GameFactory;
import eir.resources.LevelInitialSettings;
import eir.resources.LevelParameters;
import eir.resources.PolygonalModelHandle;
import eir.resources.TextureHandle;
import eir.world.environment.parallax.Background;

public class LevelGenerator
{
	public LevelDef generate(final LevelParameters parameters, final GameFactory factory )
	{

		LevelDef levelDef = new LevelDef( );

		levelDef.setWidth( parameters.getWidth() );
		levelDef.setHeight( parameters.getHeight() );

		levelDef.setInitialSettings( new LevelInitialSettings(new Vector2(0,0)) );

		levelDef.setBackgroundDef( generateBackground( parameters, factory ) );

		List <AsteroidDef> asteroids = new ArrayList <AsteroidDef> ();

		for(int aidx = 0; aidx < 3;/*parameters.getAsteroids();*/ aidx ++)
		{
			String name = "asteroid-" + aidx;

			Vector2 position = new Vector2(
					(RandomUtil.R( parameters.getWidth() ) - parameters.getWidth()/2)/2,
					(RandomUtil.R( parameters.getHeight() ) - parameters.getHeight()/2)/2
					);

			System.out.println(position);
			float radius = RandomUtil.STD( 300, 10 );

			int segments = (int)(radius / 2f);
			//generateCircleModel( position, Vector2.Zero, radius, segments );

			int textureSize = BitUtils.po2Ceiling( (int)(2*radius) );
			TextureHandle textureHandle = new TextureHandle(Mandala.createParameterString( 205,
					textureSize, radius));
			factory.registerTexture( textureHandle );
			PolygonalModelHandle modelHandle = PolygonalModelHandle.createCircularHandle(radius, segments);
			factory.registerModelHandle( modelHandle );

			float rotation = RandomUtil.sign() * (RandomUtil.R( 5 ) + 3f);

			AsteroidDef asteroid = new AsteroidDef(
					name,
					position,
					0,
					radius,
					textureHandle,
					modelHandle,
					rotation);

			asteroids.add( asteroid );
		}
		levelDef.setAsteroidDefs( asteroids );

		/////////////////////////////////////////////////////
		// generating factions:

		List <FactionDef> factions = new ArrayList <FactionDef>();

		FactionDef faction1Def = new FactionDef(1, new Color(0.8f, 0.8f, 0.8f, 1.0f), "PlayerController");
		factions.add( faction1Def );
		FactionDef faction2Def = new FactionDef(2, new Color(0.2f, 0.2f, 0.2f, 1.0f), "DummyController");
		factions.add( faction2Def );
		FactionDef faction3Def = new FactionDef(3, new Color(0.5f, 0.0f, 0.5f, 1.0f), "WildlifeController");
		factions.add( faction3Def );

		levelDef.setFactionDefs( factions );

		return levelDef;
	}

	private Background generateBackground( final LevelParameters parameters, final GameFactory factory )
	{
		NavigableSet <Background.Layer> layers = new TreeSet <Background.Layer> ();

/*		TextureHandle layer1Handle =  new TextureHandle("levels/plasma_01.png");
		factory.registerTexture( layer1Handle );
		Background.Layer layer1 = new Background.Layer(
				5,
				layer1Handle,
				new Vector2(0,0), false );
		layers.add( layer1 );


		TextureHandle layer2Handle =  new TextureHandle("levels/fog_01.png");
		factory.registerTexture( layer2Handle );
		Background.Layer layer2 = new Background.Layer(
				0.9f, layer2Handle,
				new Vector2(500,0), true );

		layers.add( layer2 );*/

		Background background = new Background(layers);

		return background;
	}



}
