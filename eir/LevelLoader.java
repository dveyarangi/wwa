/**
 * 
 */
package eir.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.cedarsoftware.util.io.JsonObject;
import com.cedarsoftware.util.io.JsonReader;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


import eir.world.Asteroid;
import eir.world.Level;
import eir.world.controllers.IController;
import eir.world.environment.nav.FloydWarshal;
import eir.world.environment.nav.NavMesh;
import eir.world.environment.nav.NavNode;
import eir.world.unit.Faction;
import eir.world.unit.Unit;
import eir.world.unit.UnitsFactory;

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
	
	public static class LoadingContext
	{
		public NavMesh navMesh = new FloydWarshal();
		
		public Map <String, Asteroid> asteroids = 
				new HashMap <String, Asteroid> ();
		
		public Map <String, Animation> animations = 
				new HashMap <String, Animation> ();

		public Map <Integer, Faction> factions = new HashMap <Integer, Faction> ();

		/**
		 * @param asteroidName
		 * @return
		 */
		public Asteroid getAsteroid(String name)
		{
			if(!asteroids.containsKey( name ))
				throw new IllegalArgumentException("Asteroid " + name + " not defined.");

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
	Level readLevel(String levelId, final LoadingContext context)
	{
		
		JsonReader.addReader( Texture.class, new JsonReader.JsonClassReader() {
				@Override
				public Object read(Object jOb, LinkedList<JsonObject<String, Object>> stack)	
				{
					JsonObject object = (JsonObject) jOb;
					String textureFile = (String)object.getPrimitiveValue();
					
					return GameFactory.loadTexture( textureFile );
				}
		});
			
		JsonReader.addReader( Texture.class, new JsonReader.JsonClassReader() {
				@Override
				public Object read(Object jOb, LinkedList<JsonObject<String, Object>> stack)	
				{
					JsonObject object = (JsonObject) jOb;
					String atlasFile = object.get( "atlasFile" ).toString();
					String atlasId = object.get( "atlasId" ).toString();
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
		});	


//		UnitAdapter unitAdapter = new UnitAdapter();
		
		JsonReader.addReader( Texture.class, new JsonReader.JsonClassReader() {
			@Override
			public Object read(Object jOb, LinkedList<JsonObject<String, Object>> stack)	
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
		});

		

		.registerTypeAdapter( NavNode.class, new JsonDeserializer<NavNode>()
			{
				@Override
				public NavNode deserialize(JsonElement elem, Type type, JsonDeserializationContext arg2) throws JsonParseException
				{
					JsonObject object = elem.getAsJsonObject();
					String asteriodName = object.get( "asteroid" ).getAsString();
					int navIdx = object.get( "navIdx" ).getAsInt();
					
					Asteroid asteroid = context.asteroids.get( asteriodName );
					return asteroid.getModel().getNavNode( navIdx );
				}
			}) 			.create();
		
		JsonReader reader = new JsonReader();
		Level level = (Level)reader.jsonToJava( readLevelConfig ( levelId) );

		
		return level;
	}

	/**
	 * Open stream to level name.
	 * @param levelName
	 * @return
	 * @throws IllegalArgumentException if no resource matching the Level name is found.
	 */
	private String readLevelConfig(String levelId)
	{
		StringBuilder fileData = new StringBuilder();
		try
		{
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream( levelId );
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line;
			{
				while((line = reader.readLine()) != null)
					fileData.append( line );
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();
			return null;
		}
		
		return fileData.toString();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try {
		LevelLoader loader = new LevelLoader();

		String levelName = loader.getLevelNames( "exodus" ).iterator().next();
		Level level = loader.readLevel( levelName, new LoadingContext() );

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
	
/*	private static class UnitAdapter implements  JsonDeserializer<Unit>
	{
		Gson gson;
		
		@Override
		public Unit deserialize(JsonElement elem, Type type, JsonDeserializationContext arg2) throws JsonParseException
		{
			JsonObject object = elem.getAsJsonObject();
			String unitType = object.get( "type" ).getAsString().intern();
			Class <?> unitClass = UnitsFactory.getUnitClass(unitType);
			Unit unit = (Unit) gson.fromJson( elem, unitClass );

			unit.init(unitType, unit.anchor, unit.getFaction());
			return unit;
		}
	}
	
	private static class ControllerAdapter implements  JsonDeserializer<IController>
	{
		Gson gson;
		
		@Override
		public IController deserialize(JsonElement elem, Type type, JsonDeserializationContext arg2) throws JsonParseException
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
	}*/
}
