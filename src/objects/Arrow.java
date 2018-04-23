package objects;

import engine.Bitmap;
import utilities.Images;
import utilities.Rect;

public class Arrow {
	
	public Bitmap bm;
	public float x, y, velX, velY;

	public Rect bounds = new Rect(x, y, 1, 1);
	
	public Arrow(Bitmap bm, float x, float y, float velX, float velY)
	{
		this.bm = bm;
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
	}
	
	public void update()
	{
		bm.createTexture(Images.arrow, (int) x, (int) y);
		
		x += velX;
		y += velY;
		
		bounds = new Rect(x, y, 3, 3);
	}
}
