/**
 * 
 */


import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

/**
 * Packs textures into TextureAtlas-readable format
 * 
 * @author dveyarangi
 *
 */
public class TexturePacker
{
	  public static void main (String[] args) throws Exception {
	         TexturePacker2.
	         process("drawing\\uiskin", // source dir
	        		 "assets\\skins",  // target dir
	        		 "uiskin" // atlas name
	        		 );
	 }
}
