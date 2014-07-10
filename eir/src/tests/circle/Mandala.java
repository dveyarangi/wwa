package tests.circle;

import java.awt.Color;
import java.awt.image.BufferedImage;

import yarangi.math.Angles;
import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.math.Vector2;

import eir.util.BezierCubicCurve;
import eir.util.ParametricCurve;

public class Mandala
{

	int size = 1024;
	int radius = 400;

	public static final float SQRT2 = (float)Math.sqrt( 2 );


	BufferedImage generateImage()
	{
		Mask image = new Mask(size);

		for(int x = 0 ; x < size; x ++)	for(int y = 0 ; y < size; y ++)
		{
			int rx = x - size/2;
			int ry = y - size/2;
//			if(rx*rx+ry*ry <= radius*radius)
				image.put( x, y, ColorUtil.toARGB(0xFF, 0x00, 0x00, 0x00) );
		}


/*		Mask baseMask = createTriangleGradient( 50 );
			applyMask(
					50, 50, baseMask, image, IMaskingMethod.MULTIPLY );*/

//		Mask testMask = createXGradient(50);

/*		for(float r = 10; r < radius; r += RandomUtil.STD( 10, 10 ))
		{
			float amod = RandomUtil.N( 100 )+3;
			float rmod = RandomUtil.STD( 10, 10f );
			Mask baseMask = createTriangleGradient( (int)RandomUtil.STD( 40, 20 ));
			for(double angle = Math.PI/100; angle < Math.PI*2; angle += Math.PI/100)
			{
				if(angle == 0)
					continue;
				Mask testMask = rotateMask( baseMask, (float)(r*angle)  );

				applyMask(
						(int)(size/2 + (r+rmod*Math.cos(amod*angle))*Math.cos(angle)),
						(int)(size/2 + (r+rmod*Math.cos(amod*angle))*Math.sin(angle)),
						testMask, image, IMaskingMethod.MULTIPLY );
			}
		}*/

		float colorTheme = RandomUtil.N(360);
		int angularBase = RandomUtil.N(5)+1;
		System.out.println(colorTheme);
		Vector2 curvePoint = new Vector2();
		for(float r = radius; r > 0; r -= 10)
		{
			colorTheme += 180;
			colorTheme %= 360;
//			float flavor = colorTheme;
			float hue = RandomUtil.STD(colorTheme, 5);
			if(hue < 0) hue += 360;
			Color color = hsv2rgb(hue, RandomUtil.R(1f), 0.9f/* RandomUtil.R(0.5f)+0.5f*/);

			int angularStepsNum = angularBase*(RandomUtil.N( 20 )+1);
			float angularStep = (float)(2f * Math.PI /
					angularStepsNum);
			float spirality = RandomUtil.N(20)-10;
			if(spirality == 0)
				spirality = 1;
			float minR = r+RandomUtil.STD( 0, 5 );
			float maxR = r - Math.abs( RandomUtil.STD( 0, 2*radius/3f ) );

			ParametricCurve curve = createCurve( angularStep, minR, maxR );
			int maskSize = (int)RandomUtil.STD( r/10, r/25 )+3;
			Mask baseMask = createTriangleGradient( color, maskSize );
//			Mask baseMask = createRadialMask( color, maskSize );
			for(double angle = Math.PI/100; angle < Math.PI*2; angle += angularStep)
			{
				for(float t = 0; t <= 1; t += 0.02f)
				{
					curve.at( curvePoint, t );

					float dr = curvePoint.len();
					float da = (float)(curvePoint.angle()*Angles.TO_RAD);
					Mask testMask = rotateMask( baseMask, (float)(r*(da+angle))  );
					applyMask(
							(int)(size/2 + dr*Math.cos(da+spirality*angle)),
							(int)(size/2 + dr*Math.sin(da+spirality*angle)),
							testMask, image, IMaskingMethod.MULTIPLY );
				}
			}
		}

		//maskToCircle(image);

		return createImage(image);
	}

	private void maskToCircle( final Mask image )
	{
		for(int x = 0 ; x < size; x ++)	for(int y = 0 ; y < size; y ++)
		{
			int rx = x - size/2;
			int ry = y - size/2;
			if(rx*rx+ry*ry > radius*radius)
//			{
//				image.put( x, y, ColorUtil.toARGB(0xFF, 0xFF, 0x00, 0x00) );
//			} else
			{
				image.put( x, y, ColorUtil.toARGB(0xFF, 0x00, 0x00, 0x00) );
			}
		}
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

	private Mask createRadialMask(final Color color, final int width)
	{
		Mask mask = new Mask(width);
		for(int mx = 0; mx < mask.size(); mx ++)
		{
			for(int my = 0; my < mask.size(); my ++)
			{
				float d = (float)Math.sqrt( (mx - width/2)*(mx - width/2) + (my - width/2)*(my - width/2) );

				float c = (width/2f - d)/width;
				if(c < 0 ) c = 0;
				if(c > 1) c = 1;

				mask.put( mx, my, ColorUtil.toARGB( (int)(255*c),
						   (int)(c*color.getRed()),
						   (int)(c*color.getGreen()),
						   (int)(c*color.getBlue()) )  );
			}
		}

		return mask;
	}

	private Mask createTriangleGradient(final Color color, final int width)
	{
		System.out.println(color);

		float v1x = (float)(width/2 * Math.cos(0));
		float v1y = (float)(width/2 * Math.sin(0));
		float v2x = (float)(width/2 * Math.cos(Math.PI * 2f / 3f));
		float v2y = (float)(width/2 * Math.sin(Math.PI * 2f / 3f));
		float v3x = (float)(width/2 * Math.cos(-Math.PI * 2f / 3f));
		float v3y = (float)(width/2 * Math.sin(-Math.PI * 2f / 3f));

		Mask mask = new Mask(width);
		for(int mx = 0; mx < mask.size(); mx ++)
		{
			for(int my = 0; my < mask.size(); my ++)
			{
				if(!pointInTriangle( mx-width/2, my-width/2, v1x, v1y, v2x, v2y, v3x, v3y ))
					continue;
				float d = (float)Math.sqrt( (mx - width/2)*(mx - width/2) + (my - width/2)*(my - width/2) );

				float c = (width - d)/(2*width);
				if(c < 0 ) c = 0;
				if(c > 1) c = 1;



				mask.put( mx, my, ColorUtil.toARGB( (int)(255f*c/2f),
						(int)(c*color.getRed()),
						(int)(c*color.getGreen()),
						(int)(c*color.getBlue()) ) );
			}
		}
		return mask;
	}

	private Mask createXGradient(final int width)
	{
		float rmod = RandomUtil.R( 0.5f ) + 0.5f;
		float gmod = RandomUtil.R( 0.5f ) + 0.5f;
		float bmod = RandomUtil.R( 0.5f ) + 0.5f;
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

	float sign(final float p1x, final float p1y, final float p2x, final float p2y, final float p3x, final float p3y)
	{
	  return (p1x - p3x) * (p2y - p3y) - (p2x - p3x) * (p1y - p3y);
	}

	boolean pointInTriangle(final float t1x, final float t1y, final float p1x, final float p1y, final float p2x, final float p2y, final float p3x, final float p3y)
	{
	  boolean b1, b2, b3;

	  b1 = sign(t1x, t1y, p1x, p1y, p2x, p2y) < 0.0f;
	  b2 = sign(t1x, t1y, p2x, p2y, p3x, p3y) < 0.0f;
	  b3 = sign(t1x, t1y, p3x, p3y, p1x, p1y) < 0.0f;

	  return b1 == b2 && b2 == b3;
	}

	ParametricCurve createCurve(final float angularSpread, final float minR, final float maxR)
	{
		float r1 = RandomUtil.R( maxR-minR )+ minR;
		float r2 = RandomUtil.R( maxR-minR )+ minR;
		float r3 = RandomUtil.R( maxR-minR )+ minR;
		float r4 = RandomUtil.R( maxR-minR )+ minR;

		float a1 = 0;
//		float a2 = RandomUtil.R( 2*angularSpread )-angularSpread/2;
		float a2 = RandomUtil.STD( angularSpread/2, angularSpread/2 );
		float a3 = RandomUtil.STD( angularSpread/2, angularSpread/2 );
		float a4 = angularSpread;

		Vector2 p1 = new Vector2( (float)(r1 * Math.cos( a1 )), (float)(r1 * Math.sin( a1 )) );
		Vector2 p2 = new Vector2( (float)(r2 * Math.cos( a2 )), (float)(r2 * Math.sin( a2 )) );
		Vector2 p3 = new Vector2( (float)(r3 * Math.cos( a3 )), (float)(r3 * Math.sin( a3 )) );
		Vector2 p4 = new Vector2( (float)(r4 * Math.cos( a4 )), (float)(r4 * Math.sin( a4 )) );

		return new BezierCubicCurve( p1, p2, p3, p4 );
	}

	Color hsv2rgb(final float h, final float s, final float v)
	{
	    float      hh, p, q, t, ff;
	    int        i;
	    float r, g, b;

	    if(s <= 0.0) {       // < is bogus, just shuts up warnings
	        r = v;
	        g = v;
	        b = v;
	        return new Color(r, g, b);
	    }
	    hh = h;

	    if(hh >= 360.0) hh = 0.0f;
	    hh /= 60.0;
	    i = (int)hh;
	    ff = hh - i;
	    p = v * (1.0f - s);
	    q = v * (1.0f - s * ff);
	    t = v * (1.0f - s * (1.0f - ff));

	    switch(i) {
	    case 0:
	        r = v;
	        g = t;
	        b = p;
	        break;
	    case 1:
	        r = q;
	        g = v;
	        b = p;
	        break;
	    case 2:
	        r = p;
	        g = v;
	        b = t;
	        break;

	    case 3:
	        r = p;
	        g = q;
	        b = v;
	        break;
	    case 4:
	        r = t;
	        g = p;
	        b = v;
	        break;
	    case 5:
	    default:
	        r = v;
	        g = p;
	        b = q;
	        break;
	    }
	    return new Color(r,g,b);
	}
}
