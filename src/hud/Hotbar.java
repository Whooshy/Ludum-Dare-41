package hud;

import engine.Bitmap;
import utilities.Images;

public class Hotbar {
	
	public Bitmap bm;
	public static Slot[] slots = new Slot[3];
	
	public static int selection = 0;
	
	public int x = 74;
	
	public Hotbar(Bitmap bm)
	{
		this.bm = bm;
		for(int i = 0; i < slots.length; i++)
		{
			int xOffset = 75 + (i * 11);
			slots[i] = new Slot(bm, xOffset, 90, -1, 0);
		}
	}
	
	public void update()
	{
		bm.createStaticTexture(Images.hotbar, 74, 90);
		bm.createStaticTexture(Images.highlight, x, 90);
		for(int i = 0; i < slots.length; i++) slots[i].update();
	}

}
