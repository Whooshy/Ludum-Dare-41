package objects;

import java.awt.Rectangle;

import engine.Bitmap;
import utilities.Images;
import utilities.Rect;

public class Item {

	public Bitmap bm;
	public int x, y, id;
	
	public static final int WOOD = 0;
	public static final int APPLE = 1;
	public static final int FOILAGE = 2;
	public static final int STICKS = 3;
	public static final int ROPE = 4;
	public static final int WOODWALL = 5;
	public static final int WOODFLOOR = 6;
	public static final int CRAFTER = 7;
	public static final int PICKAXE = 8;
	public static final int SWORD = 9;
	public static final int BOW = 10;
	public static final int ARROW = 11;
	public static final int STONE = 12;
	public static final int SPIKES = 13;
	public static final int ARROWTRAP = 14;
	public static final int TURRET = 15;
	public static final int STONEWALL = 16;
	public static final int DOOR = 17;
	public static final int APPLEPIE = 18;
	public static final int CAMPFIRE = 19;
	public static final int ZOMBIEMEAT = 20;
	public static final int ROAST = 21;
	
	public boolean destroyed = false;
	
	public Item(Bitmap bm, int x, int y, int id)
	{
		this.bm = bm;
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	public void update()
	{
		bm.createTexture(Images.items[id], x, y);
	}
	
	public Rect bounds()
	{
		return new Rect(x, y, 3, 3);
	}
}
