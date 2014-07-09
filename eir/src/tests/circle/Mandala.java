package tests.circle;

import java.awt.image.BufferedImage;

import yarangi.numbers.RandomUtil;

public class Mandala
{

	int size = 1024;
	int radius = 400;

	public static final float SQRT2 = (float)Math.sqrt( 2 );


	BufferedImage generateImage()
	{
		Mask image = new Mask(size);

		for(int x = 0 ; x < size; x ++)
		{
			for(int y = 0 ; y < size; y ++)
			{
				int rx = x - size/2;
				int ry = y - size/2;
				if(rx*rx+ry*ry <= radius*radius)
				{
					image.put( x, y, ColorUtil.toARGB(0xFF, 0xFF, 0x00, 0x00) );
				} else
				{
					image.put( x, y, ColorUtil.toARGB(0xFF, 0x00, 0x00, 0x00) );
				}
			}
		}

//		Mask testMask = createXGradient(50);

		for(float r = 10; r < radius; r += 10)
		{
			float amod = RandomUtil.STD( 0, 10 );
			float rmod = RandomUtil.STD( 0, 30 );
			Mask baseMask = createRadialMask((int)RandomUtil.STD( 40, 5 ));
			for(double angle = 0; angle < Math.PI*2; angle += Math.PI/100f)
			{

				Mask testMask = rotateMask( baseMask, (float)(r*angle)  );

				applyMask(
						(int)(size/2 + (r+rmod*Math.cos(Math.PI*angle))*Math.cos(angle)),
						(int)(size/2 + (r+rmod*Math.cos(Math.PI*angle))*Math.sin(angle)), testMask, image, IMaskingMethod.MULTIPLY );
			}
		}
		return createImage(image);
	}

	private BufferedImage createImage(final Mask imageMask)
	{
		int width = imageMask.size();
		int [] imageBuffer = new int[width*width];
		for(int mx = 0; mx < imageMask.size(); mx ++)
		{
			for(int my = 0; my < imageMask.size(); my ++)
			{
				imageBuffer[mx+my*width] = imageMask.at( mx, my );
			}
		}


		BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
		for(int x = 0 ; x < width; x ++)
		{
			for(int y = 0 ; y < width; y ++)
			{
				int idx = x+size*y;
				int rgb = imageBuffer[idx];
				image.setRGB( x, y, rgb );
			}
		}

		return image;
	}

	private Mask rotateMask(final Mask mask, final float angle)
	{
		int width = (int)Math.ceil( Math.sqrt( 2 ) * mask.size() );
		int semiwidth = width/2;
		Mask rotatedMask = new Mask(width);
		for(int rx = 0; rx < rotatedMask.size(); rx ++)	for(int ry = 0; ry < rotatedMask.size(); ry ++)
		{
			float ax = rx - semiwidth;
			float ay = ry - semiwidth;
			// distance to pixel from the rotated mask center:
			float d = (float) Math.sqrt( ax*ax + ay*ay );
			float a = (float) Math.atan2( ay, ax );
			float nx = (float)( d * Math.cos( a-angle ) ) + semiwidth;
			float ny = (float)( d * Math.sin( a-angle ) ) + semiwidth;

			int tx = (int)(nx - (rotatedMask.size() - mask.size())/2f );
			int ty = (int)(ny - (rotatedMask.size() - mask.size())/2f );
			if(!mask.inBounds( tx, ty))
			{
				rotatedMask.put( rx, ry, 0 );
				continue;
			}

			rotatedMask.put( rx, ry, mask.at(tx, ty) );
		}

		return rotatedMask;
	}

	private void applyMask( final int px, final int py, final Mask mask, final Mask layer, final IMaskingMethod method)
	{
		for(int mx = 0; mx < mask.size(); mx ++) for(int my = 0; my < mask.size(); my ++)
		{
			layer.put(px+mx-mask.size()/2, py+my-mask.size()/2, mask.at(mx, my), method);
		}
	}

	private Mask createRadialMask(final int width)
	{
		float rmod = RandomUtil.R( 1f );// + 0.5f;
		float gmod = RandomUtil.R( 1f );// + 0.5f;
		float bmod = RandomUtil.R( 1f );// + 0.5f;
		Mask mask = new Mask(width);
		for(int mx = 0; mx < mask.size(); mx ++)
		{
			for(int my = 0; my < mask.size(); my ++)
			{
				float d = (float)Math.sqrt( (mx - width/2)*(mx - width/2) + (my - width/2)*(my - width/2) );

				int c = (int)((width/2f - d)/width * 255f);
				if(c < 0 ) c = 0;
				if(c > 255) c = 255;
				mask.put( mx, my, ColorUtil.toARGB( c, (int)(c*rmod), (int)(c*gmod), (int)(c*bmod) ) );
			}
		}

		return mask;
	}

	private Mask createRandomGradient(final int width)
	{
		float rmod = RandomUtil.R( 1f );// + 0.5f;
		float gmod = RandomUtil.R( 1f );// + 0.5f;
		float bmod = RandomUtil.R( 1f );// + 0.5f;
		Mask mask = new Mask(width);
		for(int mx = 0; mx < mask.size(); mx ++)
		{
			for(int my = 0; my < mask.size(); my ++)
			{
				int c = (int)((width/2f - mx)/width * 255f);
				if(c < 0 ) c = 0;
				if(c > 255) c = 255;
				mask.put( mx, my, ColorUtil.toARGB( c, (int)(c*rmod), (int)(c*gmod), (int)(c*bmod) ) );
			}
		}
		return mask;
	}

	private Mask createXGradient(final int width)
	{
		float rmod = RandomUtil.R( 1f );// + 0.5f;
		float gmod = RandomUtil.R( 1f );// + 0.5f;
		float bmod = RandomUtil.R( 1f );// + 0.5f;
		Mask mask = new Mask(width);
		for(int mx = 0; mx < mask.size(); mx ++)
		{
			for(int my = 0; my < mask.size(); my ++)
			{
				int c = (int)((width/2f - mx)/width * 255f);
				if(c < 0 ) c = 0;
				if(c > 255) c = 255;
				mask.put( mx, my, ColorUtil.toARGB( c, (int)(c*rmod), (int)(c*gmod), (int)(c*bmod) ) );
			}
		}

		return mask;
	}
}
