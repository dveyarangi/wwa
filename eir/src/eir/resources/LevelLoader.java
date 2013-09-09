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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

/**
 * @author dveyarangi
 * 
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
	
	private static class LoadingContext
	{
		Map <String, Asteroid> asteroids = 
				new HashMap <String, Asteroid> ();

		/**
		 * @param asteroidName
		 * @return
		 */
		public Asteroid getAsteroid(String name)
		{
			return asteroids.get( name );
		}

		/**
		 * @param asteroid
		 */
		public void addAsteroid(Asteroid asteroid)
		{
			asteroids.put( asteroid.getName(), asteroid );
		}
		
	}
	
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
	public Collection <String> getLevelNames(String levelType) 
	{
		return Collections.unmodifiableCollection( levelTypes.get( levelType ) );
	}


	/**
	 * Provides listing of levels in {@link LEVEL_DATA_ROOT} dir.
	 * 
	 * @return
	 */
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
	 * Loads level descriptor of specified levelName.
	 * @param levelName
	 * @return
	 */
	public Level readLevel(final GameFactory factory, String levelId)
	{
		final LoadingContext context = new LoadingContext();
		
		final Gson rawGson = new GsonBuilder()
		.registerTypeAdapter( PolygonalModel.class, new JsonDeserializer<PolygonalModel>()
		{
			@Override
			public PolygonalModel deserialize(JsonElement elem, Type type, JsonDeserializationContext arg2) throws JsonParseException
			{
				JsonObject object = elem.getAsJsonObject();
				String modelId = object.get("modelId").getAsString();
				int size = object.get("size").getAsInt();
				
				return factory.loadModel(  modelId, size );
			}
		}).create();
		// attaching polygonal model loader
		Gson gson = new GsonBuilder()
			.registerTypeAdapter( Asteroid.class, new JsonDeserializer<Asteroid>()
			{
				@Override
				public Asteroid deserialize(JsonElement elem, Type type, JsonDeserializationContext ctx) throws JsonParseException
				{
					Asteroid asteroid;
					if(elem.isJsonObject())
					{
						asteroid = rawGson.fromJson( elem, type );
						context.addAsteroid(asteroid);
						return asteroid;
					}
					String asteroidName = elem.getAsString();
					
					asteroid = context.getAsteroid(asteroidName);
					if(asteroid == null)
						throw new IllegalArgumentException("Asteroid " + asteroidName + " not defined.");
					return asteroid;
					
				}
			})
			.registerTypeAdapter( Texture.class, new JsonDeserializer<Texture>()
			{
				@Override
				public Texture deserialize(JsonElement elem, Type type, JsonDeserializationContext arg2) throws JsonParseException
				{
					String textureFile = elem.getAsString();
					
					return factory.loadTexture( textureFile );
				}
			})
			.create();
		
		InputStream stream = openFileStream( levelId );

		Level level = null;

		try
		{
			level = gson.fromJson( new InputStreamReader( stream ), Level.class );
		} 
		catch ( JsonSyntaxException jse ) { Gdx.app.error( TAG,  "Level file is contains errors", jse ); } 
		catch ( JsonIOException jioe ) { Gdx.app.error( TAG,  "Level file not found or unreadible", jioe ); }

		return level;
	}

	/**
	 * Open stream to level name.
	 * @param levelName
	 * @return
	 * @throws IllegalArgumentException if no resource matching the Level name is found.
	 */
	private InputStream openFileStream(String levelId)
	{
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream( levelId );
		if(stream == null)
		{
			throw new IllegalArgumentException ("Cannot open resource " + levelId );
		}
		return stream;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// try {
		LevelLoader loader = new LevelLoader();

		String levelName = loader.getLevelNames( "exodus" ).iterator().next();
//		LevelDescriptor level = loader.readLevel( world, levelName );

//		System.out.println( "Loaded level descriptor " + level );

		// }
		// finally { System.exit( 0 ); }
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
	String[] getResourceListing(Class <?> clazz, String path)
	{
		URL dirURL = clazz.getClassLoader().getResource( path );
		
		try 
		{
			if ( dirURL != null && dirURL.getProtocol().equals( "file" ) )
			{
				/* A file path: easy enough */
				return new File( dirURL.toURI() ).list();
			}
	
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
}
