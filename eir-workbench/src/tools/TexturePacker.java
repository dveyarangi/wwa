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
	         process("anima\\smoke\\", // source dir
	        		 "anima\\smoke\\smoke01\\",  // target dir
	        		 "smoke" // atlas name
	        		 );
	 }
}
