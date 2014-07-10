package tests.circle;

public class Mask
{
	private int [] buffer;
	private int size;

	public Mask(final int size)
	{
		this.size = size;
		this.buffer = new int [size*size];
		for(int idx = 0; idx < buffer.length; idx ++)
		{
			buffer[idx] = 0;
		}
	}

	public int size() { return size; }

	public int at( final int mx, final int my ) { return buffer[index(mx, my)]; }

	public void put( final int mx, final int my, final int value, final IMaskingMethod method )
	{
		if(inBounds( mx, my ))
		buffer[index(mx,my)] = method.eval( value, buffer[index(mx,my)] );
	}
	public void put( final int mx, final int my, final int value)
	{
		buffer[index(mx,my)] = value;
	}

	private int index(final int mx, final int my) { return mx+size*my; }

	public int getMin()
	{
		int min = Integer.MAX_VALUE;
		for(int idx = 0; idx < buffer.length; idx ++)
		{
			if(buffer[idx] < min)
			{
				min = buffer[idx];
			}
		}
		return min;
	}
	public int getMax()
	{
		int max = Integer.MIN_VALUE;
		for(int idx = 0; idx < buffer.length; idx ++)
		{
			if(buffer[idx] > max)
			{
				max = buffer[idx];
			}
		}
		return max;
	}

	public int[] getBuffer() { return buffer; }

	public boolean inBounds( final int nx, final int ny )
	{
		return nx >= 0 && nx < size && ny >= 0 && ny < size;
	}
}
