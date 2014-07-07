package eir.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PolygonLoader extends AsynchronousAssetLoader<PolygonShape, AssetLoaderParameters<PolygonShape>>
{
	PolygonShape shape;

	public PolygonLoader(final FileHandleResolver resolver)
	{
		super( resolver );
	}


	@Override
	public Array<AssetDescriptor> getDependencies( final String fileName,
			final AssetLoaderParameters<PolygonShape> parameter )
	{
		return null;
	}

	@Override
	public void loadAsync( final AssetManager manager, final String fileName,
			final AssetLoaderParameters<PolygonShape> parameter )
	{

		ShapeLoader.RigidBodyModel bodyModel = ShapeLoader.readShape( Gdx.files.internal( fileName ).readString() ).rigidBodies.get( 0 );
		Vector2 [] vertices = bodyModel.shapes.get( 0 ).vertices;


		shape = new PolygonShape(vertices, bodyModel.origin);
	}

	@Override
	public PolygonShape loadSync( final AssetManager manager, final String fileName,
			final AssetLoaderParameters<PolygonShape> parameter )
	{
		return shape;
	}

}
