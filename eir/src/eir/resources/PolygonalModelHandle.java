package eir.resources;



public class PolygonalModelHandle
{

	private static final String MODELS_PATH = "models/";

	private String path;

	public PolygonalModelHandle(final String modelId, final boolean isCircular)
	{
		this.path = isCircular ? modelId : createBodyPath( modelId );
	}


	private static final String createBodyPath(final String modelId)
	{
		return new StringBuilder()
		.append( MODELS_PATH ).append( modelId ).append(".bog")
		.toString();
	}


	public String getPath() { return path; }


	public static PolygonalModelHandle createCircularHandle( final float radius, final int segments )
	{
		return new PolygonalModelHandle( PolygonShape.toCircularName( radius, segments ), true );
	}

}
