package eir.resources;


public class PolygonalModelHandle
{

	private static final String MODELS_PATH = "models/";

	private String path;

	public PolygonalModelHandle(final String modelId)
	{
		this.path = createBodyPath( modelId );
	}


	private static final String createBodyPath(final String modelId)
	{
		return new StringBuilder()
		.append( MODELS_PATH ).append( modelId ).append(".bog")
		.toString();
	}


	public String getPath() { return path; }

}
