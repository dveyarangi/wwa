/**
 * 
 */
package eir.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.apache.log4j.Logger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * @author dveyarangi
 * 
 */
public class LevelLoader
{

	private Logger log = Logger.getLogger( this.getClass() );

	// me bad regexper, couldn't figure this - it should be "data/levels"
	private static String LEVEL_DATA_ROOT = "data/levels/";

	private Multimap<String, String> levelFiles;
	
	public LevelLoader()
	{
		levelFiles = readLevelFiles();
	}
	
	/**
	 * Returns set of found level types.
	 * @return
	 */
	public Set <String> getLevelTypes()
	{
		return Collections.unmodifiableSet( levelFiles.keySet() );
	}
	
	/**
	 * Returns all level names for specified level type.
	 * @param levelType
	 * @return
	 */
	public Collection <String> getLevelNames(String levelType) 
	{
		return Collections.unmodifiableCollection( levelFiles.get( levelType ) );
	}


	/**
	 * Provides listing of levels in {@link LEVEL_DATA_ROOT} dir.
	 * 
	 * @return
	 */
	private Multimap<String, String> readLevelFiles()
	{
		Multimap<String, String> levelFiles = HashMultimap.create();

		// resource path scaner; TODO: comes with 2mb lib, consider something
		// lighter:
		String[] levelsFiles = getResourceListing( this.getClass(), LEVEL_DATA_ROOT );


		log.trace( "Listing " + levelsFiles.length + " resource file(s)." );
		
		// for strings like data/levels/level_exodus_01.dat
		Pattern levelFilePattern = Pattern.compile( ".*level_(.*)_(\\d{2})\\.dat" );
        									  //              ^      ^
		                                      //              ^      ^<- two digits
		                                      //              ^<- anything as level type

		for ( String filename : levelsFiles )
		{

			log.trace( "Checking resource file " + filename );
			
			Matcher matcher = levelFilePattern.matcher( filename );

			if ( !matcher.matches() || matcher.groupCount() != 2 )
			{
				log.error( "Ignoring file " + filename );
				continue;
			}

			String levelName = matcher.group( 1 );
			String levelIdx = matcher.group( 2 );

			log.trace( "Level file found: " + levelName + " : " + levelIdx );

			levelFiles.put( levelName, LEVEL_DATA_ROOT+filename );
		}

		return levelFiles;
	}
	
	/**
	 * Loads level descriptor of specified levelName.
	 * @param levelName
	 * @return
	 */
	public LevelDescriptor readLevel(String levelName)
	{
		Gson gson = new Gson();
		
		InputStream stream = openFileStream( levelName );

		LevelDescriptor level = null;

		try
		{
			level = gson.fromJson( new InputStreamReader( stream ), LevelDescriptor.class );
		} 
		catch ( JsonSyntaxException jse ) { log.error( "Level file is contains errors", jse ); } 
		catch ( JsonIOException jioe ) { log.error( "Level file not found or unreadible", jioe ); }

		return level;
	}

	private InputStream openFileStream(String levelName)
	{
		String filename = new StringBuilder().append("").append("").append( levelName ).toString();
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream( filename );
		if(stream == null)
		{
			throw new IllegalArgumentException("Cannot open file " + filename);
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

		LevelDescriptor level = loader.readLevel( levelName );

		System.out.println( "Loaded level descriptor " + level );

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
	String[] getResourceListing(Class clazz, String path)
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
		catch ( URISyntaxException e ) { log.error( e ); } 
		catch ( IOException e ) { log.error( e ); }
		
		throw new UnsupportedOperationException( "Cannot list files for URL " + dirURL );
	}
}
