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
	         process("anima\\blob\\white", // source dir
	        		 "anima\\blob\\white",  // target dir
	        		 "blob_white" // atlas name
	        		 );
	 }
}
