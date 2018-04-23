package objects;

import java.awt.Rectangle;

import engine.Bitmap;
import engine.World;
import player.Zombie;
import utilities.Images;
import utilities.Rect;

public class Tile {

	public Bitmap bm;
	public int x, y, id;
	
	public static final int GRASS = 0;
	public static final int FLOWERS = 1;
	public static final int TREE = 2;
	public static final int WATER = 3;
	public static final int BUSH = 4;
	public static final int WOODWALL_FRONT = 5;
	public static final int WOODWALL_MAIN = 6;
	public static final int BEACON = 7;
	public static final int STONE = 8;
	public static final int STONE_FLOOR = 9;
	public static final int RED_PORTAL = 10;
	public static final int BLUE_PORTAL = 11;
	public static final int GREEN_PORTAL = 12;
	public static final int CYAN_PORTAL = 13;
	public static final int ORANGE_PORTAL = 14;
	public static final int YELLOW_PORTAL = 15;
	public static final int PURPLE_PORTAL = 16;
	public static final int PINK_PORTAL = 17;
	public static final int WOODFLOOR = 18;
	public static final int CRAFTER = 19;
	public static final int SPIKES = 20;
	public static final int ARROWTRAP_UP = 21;
	public static final int ARROWTRAP_DOWN = 22;
	public static final int TURRET = 23;
	public static final int STONEWALL = 24;
	public static final int DOOR_OPEN = 25;
	public static final int DOOR_CLOSED = 26;
	public static final int CAMPFIRE = 27;
	
	public int function = 0;
	
	public static final int ENTRANCE = 0;
	public static final int EXIT = 1;
	
	public boolean isSolid = false;
	
	public static float width = (800 / 150) * 16;
	public static float height = (600 / 112) * 16;
	
	public static float worldCamX, worldCamY;
	
	public Rect bounds;
	public Rect turretSensors;
	
	public Arrow arrowTrap = new Arrow(bm, 0, 0, 0, 0);
	
	public float frame = 0;
	
	public Tile(Bitmap bm, int x, int y, int id)
	{
		this.bm = bm;
		this.x = x;
		this.y = y;
		this.id = id;
		
		bounds = new Rect(x, y, 16, 16);
		arrowTrap = new Arrow(bm, 0, 0, 0, 0);
		turretSensors = new Rect(x - 48, y - 48, 114, 114);
	}
	
	public void update()
	{
		frame += 0.05f;
		
		if(frame >= 8)
		{
			frame = 0;
		}
		
		if(id != WATER) bm.createTexture(Images.tiles[id], x, y);
		if(id == WATER)
		{
			bm.createTexture(Images.water[(int) frame], x, y);
		}
		
		switch(id)
		{
		case TREE:
			isSolid = true;
			break;
		case BUSH:
			isSolid = true;
			break;
		case WOODWALL_FRONT:
			isSolid = true;
			break;
		case WOODWALL_MAIN:
			isSolid = true;
			break;
		case WATER:
			isSolid = true;
			break;
		case BEACON:
			isSolid = true;
			break;
		case STONE:
			isSolid = true;
			break;
		case ARROWTRAP_UP:
			isSolid = true;
			break;
		case ARROWTRAP_DOWN:
			isSolid = true;
			break;
		case STONEWALL:
			isSolid = true;
			break;
		case CAMPFIRE:
			isSolid = true;
			break;
		default:
			isSolid = false;
			break;
		}
		
		if(id == ARROWTRAP_UP)
		{
			if(frame > 2)
			{
				arrowTrap = new Arrow(bm, x + 7, y, 0, -2);
				frame = 0;
			}
			arrowTrap.update();
		}
		
		if(id == ARROWTRAP_DOWN)
		{
			if(frame > 2)
			{
				arrowTrap = new Arrow(bm, x + 7, y + 16, 0, 2);
				frame = 0;
			}
			arrowTrap.update();
		}
		
		if(id == TURRET)
		{
			//bm.drawRect(turretSensors);
			for(Zombie z : World.zombies)
			{
				if(frame > 2 && bm.intersects(bounds, z.hitbox))
				{
					System.out.println("X: " + z.x);
					arrowTrap = new Arrow(bm, x + 7, y + 7, (z.x - x) / 20, (z.y - y) / 20);
				}
			}
			arrowTrap.update();
		}
	}
	
	public Rectangle worldBounds()
	{
		worldCamX = Bitmap.camX * (800 / 150);
		worldCamY = Bitmap.camY * (600 / 112);
		return new Rectangle((int) ((800 / 150) * x + worldCamX), (int) ((600 / 112) * y + worldCamY), (int) width, (int) height);
	}
}
