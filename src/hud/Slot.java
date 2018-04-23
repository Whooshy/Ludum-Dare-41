package hud;

import engine.Bitmap;
import utilities.Images;

public class Slot {
	
	public Bitmap bm;
	public int x, y, id, count;
	
	public Slot(Bitmap bm, int x, int y, int id, int count)
	{
		this.bm = bm;
		this.x = x;
		this.y = y;
		this.id = id;
		this.count = count;
	}
	
	public void update()
	{
		if(id >= 0) bm.createStaticTexture(Images.items[id], x, y);
		if(count <= 0) id = -1;
		bm.drawMessage(String.valueOf(count), x + 2, y + 1);
	}

}
