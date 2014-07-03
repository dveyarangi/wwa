/**
 *
 */
package eir.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import eir.world.Asteroid;
import eir.world.Level;
import eir.world.controllers.IController;
import eir.world.environment.nav.SurfaceNavNode;
import eir.world.unit.Faction;
import eir.world.unit.Unit;

/**
 * TODO: vivisect this monstrosity
 *
 * @author dveyarangi
 */
public class LevelLoader
{

	private static final String TAG = LevelLoader.class.getSimpleName();
	/** put level files here */
	private static String LEVEL_DATA_ROOT = "data/levels/";

	/**
	 * Maps level types to level names. Level type represents a group of levelnames,
	 * that can be loaded using {@link #readLevel(String)}.
	 */
	private Multimap<String, String> levelTypes;


	/**
	 * Creates level loader and lists level folder ({@link #LEVEL_DATA_ROOT}
	 */
	public LevelLoader()
	{
	}

	/**
	 * Returns set of found level types.
	 * @return
	 */
	public Set <String> getLevelTypes()
	{
		return Collections.unmodifiableSet( levelTypes.keySet() );
	}

	/**
	 * Returns all level names for specified level type.
	 * @param levelType
	 * @return
	 */
	public Collection <String> getLevelNames(final String levelType)
	{
		return Collections.unmodifiableCollection( levelTypes.get( levelType ) );
	}


	/**
	 * Provides listing of levels in {@link LEVEL_DATA_ROOT} dir.
	 *
	 * @return
	 */
	@SuppressWarnings("unused")
	private Multimap<String, String> readLevelFiles()
	{
		Multimap<String, String> levelTypes = HashMultimap.create();

		// resource path scaner; TODO: comes with 2mb lib, consider something
		// lighter:
		String[] levelsFiles = getResourceListing( this.getClass(), LEVEL_DATA_ROOT );


		Gdx.app.debug( TAG, "Listing " + levelsFiles.length + " resource file(s)." );

		// for strings like data/levels/level_exodus_01.dat
		Pattern levelFilePattern = Pattern.compile( ".*level_(.*)_(\\d{2})\\.dat" );
		//              ^      ^
		//              ^      ^<- two digits
		//              ^<- anything as level type

		for ( String filename : levelsFiles )
		{

			Gdx.app.debug( TAG,  "Checking resource file " + filename );

			Matcher matcher = levelFilePattern.matcher( filename );

			if ( !matcher.matches() || matcher.groupCount() != 2 )
			{
				Gdx.app.error( TAG,  "Ignoring file " + filename );
				continue;
			}

			String levelType = matcher.group( 1 );
			String levelIdx = matcher.group( 2 );

			Gdx.app.debug( TAG,  "Level file found: " + levelType + " : " + levelIdx );

			levelTypes.put( levelType, LEVEL_DATA_ROOT+filename );

		}

		return levelTypes;
	}

	/**
	 * TODO: this goes to read-only code mode fast, do something about it
	 *
	 * Loads level descriptor of specified levelName.
	 * @param levelName
	 * @return
	 */
	Level readLevel(final String levelId, final LevelLoadingContext context)
	{

		/*		final Gson resourceCounter = new GsonBuilder()
		.registerTypeAdapter(Texture.class, new JsonDeserializer<Texture>()
			{
				@Override
				public Texture deserialize(JsonElement elem, Type type, JsonDeserializationContext arg2) throws JsonParseException
				{
					String textureFile = elem.getAsString();

					return GameFactory.loadTexture( textureFile );
				}
			})
			.create();*/

		// this is raw gson parser (without custom deserializatiors)
		// it is used for asteroid reference substitutions
		ControllerAdapter controllerAdapter = new ControllerAdapter();

		final Gson rawGson = new GsonBuilder()
		.registerTypeAdapter( SurfaceNavNode.class, new JsonDeserializer<SurfaceNavNode>()
				{
			@Override
			public SurfaceNavNode deserialize(final JsonElement elem, final Type type, final JsonDeserializationContext arg2) throws JsonParseException
			{
				JsonObject object = elem.getAsJsonObject();
				String asteriodName = object.get( "asteroid" ).getAsString();
				int navIdx = object.get( "navIdx" ).getAsInt();

				Asteroid asteroid = context.asteroids.get( asteriodName );
				return asteroid.getModel().getNavNode( navIdx );
			}
				})
				.registerTypeAdapter( Texture.class, new JsonDeserializer<Texture>()
						{
					@Override
					public Texture deserialize(final JsonElement elem, final Type type, final JsonDeserializationContext arg2) throws JsonParseException
					{
						String textureFile = elem.getAsString();

						return GameFactory.loadTexture( textureFile );
					}
						})

						.registerTypeAdapter( Animation.class, new JsonDeserializer<Animation>()
								{
							@Override
							public Animation deserialize(final JsonElement elem, final Type type, final JsonDeserializationContext arg2) throws JsonParseException
							{
								JsonObject object = elem.getAsJsonObject();
								String atlasFile = object.get( "atlasFile" ).getAsString();
								String atlasId = object.get( "atlasId" ).getAsString();
								String animationName = atlasFile + "/" + atlasId;

								Animation animation = context.animations.get( animationName );
								if(animation == null)
								{
									int animationId = GameFactory.registerAnimation( atlasFile, atlasId );
									animation = GameFactory.getAnimation( animationId );
									context.animations.put( animationName, animation );
								}

								return animation;

							}
								})
								.registerTypeAdapter( IController.class, controllerAdapter)
								.create();

		UnitAdapter unitAdapter = new UnitAdapter()
		{
			@Override
			public Unit deserialize(final JsonElement elem, final Type type, final JsonDeserializationContext arg2) throws JsonParseException
			{
				JsonObject object = elem.getAsJsonObject();
				String unitType = object.get( "type" ).getAsString().intern();
				Class <?> unitClass = context.getUnitsFactory().getUnitClass(unitType);

				Unit unit = (Unit) gson.fromJson( elem, unitClass );

				unit.init(unitType, unit.anchor, unit.getFaction());
				return unit;
			}

		};

		final Gson gson = new GsonBuilder()
		///////////////////////////////////////////////////////////////
		// game objects adapters:
		///////////////////////////////////////////////////////////////

		// loading asteroids
		.registerTypeAdapter( Asteroid.class, new JsonDeserializer<Asteroid>()
				{
			@Override
			public Asteroid deserialize(final JsonElement elem, final Type type, final JsonDeserializationContext ctx) throws JsonParseException
			{
				Asteroid asteroid;

				if(elem.isJsonObject()) // creating asteroid by definitions
				{
					asteroid = rawGson.fromJson( elem, type );
					context.addAsteroid(asteroid);

					asteroid.preinit( context.navMesh );

					return asteroid;
				}

				// getting asteroid by name reference:
				String asteroidName = elem.getAsString();

				asteroid = context.getAsteroid(asteroidName);

				return asteroid;

			}
		})


		.registerTypeAdapter( Faction.class, new JsonDeserializer<Faction>()
				{
			@Override
			public Faction deserialize(final JsonElement elem, final Type type, final JsonDeserializationContext arg2) throws JsonParseException
			{
				Faction faction;

				if(elem.isJsonObject()) // creating asteroid by definitions
				{
					faction = rawGson.fromJson( elem, type );
					context.factions.put(faction.getOwnerId(), faction);


					return faction;
				}

				// getting asteroid by name reference:
				int factionId = elem.getAsInt();

				faction = context.factions.get(factionId);

				return faction;
			}
		})

		.registerTypeAdapter( SurfaceNavNode.class, new JsonDeserializer<SurfaceNavNode>()
				{
			@Override
			public SurfaceNavNode deserialize(final JsonElement elem, final Type type, final JsonDeserializationContext arg2) throws JsonParseException
			{
				return rawGson.fromJson( elem, SurfaceNavNode.class );
			}
		})
		.registerTypeAdapter( Unit.class, unitAdapter)

		///////////////////////////////////////////////////////////////
		// graphic objects adapters:
		///////////////////////////////////////////////////////////////

		.registerTypeAdapter( Texture.class, new JsonDeserializer<Texture>()
				{
			@Override
			public Texture deserialize(final JsonElement elem, final Type type, final JsonDeserializationContext arg2) throws JsonParseException
			{
				return rawGson.fromJson( elem, Texture.class );
			}
		})

		.registerTypeAdapter( Animation.class, new JsonDeserializer<Animation>()
				{
			@Override
			public Animation deserialize(final JsonElement elem, final Type type, final JsonDeserializationContext arg2) throws JsonParseException
			{
				return rawGson.fromJson( elem, Animation.class );

			}
		})
		.create();

		unitAdapter.gson = gson;
		controllerAdapter.gson = gson;

		InputStream stream = openFileStream( levelId );

		Level level = null;

		try
		{
			level = gson.fromJson( new InputStreamReader( stream ), Level.class );
		}
		catch ( JsonSyntaxException jse ) {
			Gdx.app.error( TAG,  "Level file is contains errors", jse ); }
		catch ( JsonIOException jioe ) {
			Gdx.app.error( TAG,  "Level file not found or unreadible", jioe );
		}


		return level;
	}

	/**
	 * Open stream to level name.
	 * @param levelName
	 * @return
	 * @throws IllegalArgumentException if no resource matching the Level name is found.
	 */
	private InputStream openFileStream(final String levelId)
	{
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream( levelId );
		if(stream == null)
			throw new IllegalArgumentException ("Cannot open resource " + levelId );
		return stream;

	}

	/**
	 * @param args
	 */
	public static void main(final String[] args)
	{
		try {
			LevelLoader loader = new LevelLoader();

			String levelName = loader.getLevelNames( "exodus" ).iterator().next();
			Level level = loader.readLevel( levelName, new LevelLoadingContext() );

			System.out.println( "Loaded level descriptor " + level );

		}
		finally { System.exit( 0 ); }
	}

	/**
	 * List directory contents for a resource folder. Not recursive. This is
	 * basically a brute-force implementation. Works for regular files and also
	 * JARs.
	 *
	 * @author Greg Briggs
	 * @param clazz
	 *            Any java class that lives in the same place as the resources
	 *            you want.
	 * @param path
	 *            Should end with "/", but not start with one.
	 * @return Just the name of each member item, not the full paths.
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	String[] getResourceListing(final Class <?> clazz, final String path)
	{
		URL dirURL = clazz.getClassLoader().getResource( path );

		try
		{
			if ( dirURL != null && dirURL.getProtocol().equals( "file" ) )
				/* A file path: easy enough */
				return new File( dirURL.toURI() ).list();

			if ( dirURL == null )
			{
				/*
				 * In case of a jar file, we can't actually find a directory. Have
				 * to assume the same jar as clazz.
				 */
				String me = clazz.getName().replace( ".", "/" ) + ".class";
				dirURL = clazz.getClassLoader().getResource( me );
			}

			if ( dirURL.getProtocol().equals( "jar" ) )
			{
				/* A JAR path */
				String jarPath = dirURL.getPath().substring( 5, dirURL.getPath().indexOf( "!" ) ); // strip
				// out
				// only
				// the
				// JAR
				// file
				JarFile jar = new JarFile( URLDecoder.decode( jarPath, "UTF-8" ) );
				Enumeration<JarEntry> entries = jar.entries(); // gives ALL entries
				// in jar
				Set<String> result = new HashSet<String>(); // avoid duplicates in
				// case it is a
				// subdirectory
				while ( entries.hasMoreElements() )
				{
					String name = entries.nextElement().getName();
					if ( name.startsWith( path ) )
					{ // filter according to the path
						String entry = name.substring( path.length() );
						int checkSubdir = entry.indexOf( "/" );
						if ( checkSubdir >= 0 )
						{
							// if it is a subdirectory, we just return the directory
							// name
							entry = entry.substring( 0, checkSubdir );
						}
						result.add( entry );
					}
				}
				return result.toArray( new String[result.size()] );
			}
		}
		catch ( URISyntaxException e ) { Gdx.app.error( TAG, "Cannot list files for URL " + dirURL,  e ); }
		catch ( IOException e ) { Gdx.app.error( TAG, "Cannot list files for URL " + dirURL, e ); }

		throw new UnsupportedOperationException( "Cannot list files for URL " + dirURL );
	}

	private abstract static class UnitAdapter implements  JsonDeserializer<Unit>
	{
		Gson gson;
	}

	private static class ControllerAdapter implements  JsonDeserializer<IController>
	{
		Gson gson;

		@Override
		public IController deserialize(final JsonElement elem, final Type type, final JsonDeserializationContext arg2) throws JsonParseException
		{
			JsonObject object = elem.getAsJsonObject();
			String ctrlType = object.get( "type" ).getAsString().intern();
			String className = "eir.world.controllers." + ctrlType;
			Class<?> ctrlClass;
			try {
				ctrlClass = Class.forName( className );
			} catch (ClassNotFoundException e) {
				throw new JsonParseException("Faction controller of type " + className + " not found.");
			}

			IController ctrl = (IController) gson.fromJson( elem, ctrlClass );


			return ctrl;
		}
	}
}
