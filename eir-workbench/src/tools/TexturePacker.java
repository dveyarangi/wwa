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
	         process("images\\rocket\\", // source dir
	        		 "images\\rocket\\r\\",  // target dir
	        		 "rocket02" // atlas name
	        		 );
	 }
}
