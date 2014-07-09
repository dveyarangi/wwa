package tests.circle;

public class ColorUtil
{

	public static int toARGB(final int a, final int r, final int g, final int b)
	{
		return (a << 24) + (r << 16) + (g << 8) + b;
	}


}
