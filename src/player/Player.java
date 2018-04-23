package player;

import engine.Bitmap;
import engine.World;
import hud.Inventory;
import objects.Arrow;
import objects.Item;
import objects.Tile;
import utilities.Images;
import utilities.Rect;

public class Player {
	
	public static Bitmap bm;
	public Inventory inv;
	
	public static float x, y;
	
	public float velX, velY;
	
	public float frame = 0;
	
	public float speed = 0.5f;
	
	public static boolean isMovingForward, isMovingLeft, isMovingRight, isMovingBackward;
	public static boolean isCollidingForward, isCollidingLeft, isCollidingRight, isCollidingBackward;
	public static int faceDirection = 0;

	public static Rect hitbox = new Rect(x, y, 1, 1);
	
	public Rect topBox = new Rect(x, y, 8, 2);
	public Rect bottomBox = new Rect(x, y + 14, 8, 2);
	public Rect leftBox = new Rect(x, y, 2, 16);
	public Rect rightBox = new Rect(x + 6, y, 2, 16);
	
	public static int health = 100;
	public static float energy = 50;
	
	public static boolean usingSword = false;
	
	public static int useTime = 10;
	public static int cooldown = 100;
	
	public static Rect swordBox = new Rect(-5, -5, 1, 1);
	public static Arrow bowArrow = new Arrow(bm, -5, -5, 0, 0);
	
	public float velocity = 0;
	
	public Player(Bitmap bm, Inventory inv, float x, float y)
	{
		this.bm = bm;
		this.inv = inv;
		this.x = x;
		this.y = y;
		bowArrow = new Arrow(bm, -5, -5, 0, 0);
	}
	
	public void update()
	{
		if(isMovingForward) bm.createTexture(Images.player[(int) frame + 3], (int) x, (int) y);
		else if(isMovingBackward) bm.createTexture(Images.player[(int) frame], (int) x, (int) y);
		else if(isMovingLeft) bm.createTexture(Images.player[(int) frame + 8], (int) x, (int) y);
		else if(isMovingRight) bm.createTexture(Images.player[(int) frame + 6], (int) x, (int) y);
		else if(faceDirection == 0) bm.createTexture(Images.player[0], (int) x, (int) y);
		else if(faceDirection == 1) bm.createTexture(Images.player[3], (int) x, (int) y);
		else if(faceDirection == 2) bm.createTexture(Images.player[6], (int) x, (int) y);
		else if(faceDirection == 3) bm.createTexture(Images.player[11], (int) x, (int) y);
		
		x += velX;
		y += velY;
		
		hitbox = new Rect(x, y, 8, 16);
		
		topBox = new Rect(x, y + 12, 8, 2);
		bottomBox = new Rect(x, y + 14, 8, 2);
		leftBox = new Rect(x - 1, y + 13, 2, 3);
		rightBox = new Rect(x + 6, y + 13, 2, 3);
		
		velX = 0;
		velY = 0;
		
		swordBox = new Rect(-5, -5, 1, 1);
		
		bowArrow.update();
		
		if(usingSword && cooldown == 100 && useTime > 0 && energy >= 1)
		{
			if(isMovingForward || faceDirection == 0)
			{
				bm.createTexture(Images.swords[2], (int) x, (int) y + 16);
				swordBox = new Rect(x, y + velocity + 16, 16, 1);
				//bm.drawRect(swordBox);
			}else if(isMovingRight || faceDirection == 2) 
			{
				bm.createTexture(Images.swords[1], (int) x + 8, (int) y);
				swordBox = new Rect(x + velocity + 16, y, 1, 16);
				//bm.drawRect(swordBox);
			}else if(isMovingBackward || faceDirection == 1)
			{
				bm.createTexture(Images.swords[0], (int) x, (int) y - 16);
				swordBox = new Rect(x, y - velocity, 16, 1);
				//bm.drawRect(swordBox);
			}else if(isMovingLeft || faceDirection == 3)
			{
				bm.createTexture(Images.swords[3], (int) x - 8, (int) y);
				swordBox = new Rect(x - velocity, y, 1, 16);
				//bm.drawRect(swordBox);
			}
			
			velocity += 2;
			useTime --;
		}
		
		bm.drawRect(swordBox);
		
		if(energy >= 50)
		{
			energy = 50;
		}
		
		if(energy <= 0)
		{
			energy = 0;
		}
		
		energy += 0.005f;
		
		if(useTime <= 0)
		{
			usingSword = false;
			cooldown = 0;
			useTime = 10;
			energy -= 2;
			velocity = 0;
		}
		
		cooldown++;
		
		if(cooldown >= 30)
		{
			cooldown = 100;
		}
		
		if(isMovingForward && !isCollidingForward)
		{
			velY = -speed;
			frame += 0.08f;
			if(frame >= 3)
			{
				frame = 1;
			}
		}
		if(isMovingBackward && !isCollidingBackward)
		{
			velY = speed;
			frame += 0.08f;
			if(frame >= 3)
			{
				frame = 1;
			}
		}
		if(isMovingLeft && !isCollidingLeft)
		{
			velX = -speed;
			frame += 0.08f;
			if(frame >= 3)
			{
				frame = 1;
			}
		}
		if(isMovingRight && !isCollidingRight)
		{
			velX = speed;
			frame += 0.08f;
			if(frame >= 3)
			{
				frame = 1;
			}
		}
		
		collision();
	}
	
	public void collision()
	{
		for(int x = 0; x < World.tiles.length; x++)
		{
			for(int y = 0; y < World.tiles[0].length; y++)
			{
				//Forward
				if(bm.intersects(topBox, World.tiles[x][y].bounds) && World.tiles[x][y].isSolid)
				{
					isCollidingForward = true;
				}
				if(bm.intersects(topBox, World.tiles[x][y].bounds) && !World.tiles[x][y].isSolid)
				{
					isCollidingForward = false;
				}
				
				//Backward
				if(bm.intersects(bottomBox, World.tiles[x][y].bounds) && World.tiles[x][y].isSolid)
				{
					isCollidingBackward = true;
				}
				if(bm.intersects(bottomBox, World.tiles[x][y].bounds) && !World.tiles[x][y].isSolid)
				{
					isCollidingBackward = false;
				}
				
				//Left
				if(bm.intersects(leftBox, World.tiles[x][y].bounds) && World.tiles[x][y].isSolid)
				{
					isCollidingLeft = true;
				}
				if(bm.intersects(leftBox, World.tiles[x][y].bounds) && !World.tiles[x][y].isSolid)
				{
					isCollidingLeft = false;
				}
				
				//Right
				if(bm.intersects(rightBox, World.tiles[x][y].bounds) && World.tiles[x][y].isSolid)
				{
					isCollidingRight = true;
				}
				if(bm.intersects(rightBox, World.tiles[x][y].bounds) && !World.tiles[x][y].isSolid)
				{
					isCollidingRight = false;
				}
			}
		}
		
		for(int x = 0; x < World.tiles.length; x++)
		{
			for(int y = 0; y < World.tiles[0].length; y++)
			{
				//Entrance Portals
				if(bm.intersects(hitbox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.RED_PORTAL && World.tiles[x][y].function == Tile.ENTRANCE)
				{
					this.x = World.xExt[0] * 16 + 32;
					this.y = World.yExt[0] * 16;
				}
				if(bm.intersects(hitbox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.BLUE_PORTAL && World.tiles[x][y].function == Tile.ENTRANCE)
				{
					this.x = World.xExt[1] * 16 + 32;
					this.y = World.yExt[1] * 16;
				}
				if(bm.intersects(hitbox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.GREEN_PORTAL && World.tiles[x][y].function == Tile.ENTRANCE)
				{
					this.x = World.xExt[2] * 16 + 32;
					this.y = World.yExt[2] * 16;
				}
				if(bm.intersects(hitbox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.CYAN_PORTAL && World.tiles[x][y].function == Tile.ENTRANCE)
				{
					this.x = World.xExt[3] * 16 + 32;
					this.y = World.yExt[3] * 16;
				}
				if(bm.intersects(hitbox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.ORANGE_PORTAL && World.tiles[x][y].function == Tile.ENTRANCE)
				{
					this.x = World.xExt[4] * 16 + 32;
					this.y = World.yExt[4] * 16;
				}
				if(bm.intersects(hitbox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.YELLOW_PORTAL && World.tiles[x][y].function == Tile.ENTRANCE)
				{
					this.x = World.xExt[5] * 16 + 32;
					this.y = World.yExt[5] * 16;
				}
				if(bm.intersects(hitbox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.PURPLE_PORTAL && World.tiles[x][y].function == Tile.ENTRANCE)
				{
					this.x = World.xExt[6] * 16 + 32;
					this.y = World.yExt[6] * 16;
				}
				if(bm.intersects(hitbox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.PINK_PORTAL && World.tiles[x][y].function == Tile.ENTRANCE)
				{
					this.x = 105 * 16;
					this.y = 100 * 16;
				}
				
				//Exit Portals
				if(bm.intersects(bottomBox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.RED_PORTAL && World.tiles[x][y].function == Tile.EXIT)
				{
					this.x = World.xEnt[0] * 16 + 32;
					this.y = World.yEnt[0] * 16;
					System.out.println(this.y / 16);
				}
				if(bm.intersects(bottomBox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.BLUE_PORTAL && World.tiles[x][y].function == Tile.EXIT)
				{
					this.x = World.xEnt[1] * 16 + 32;
					this.y = World.yEnt[1] * 16;
					System.out.println(this.y / 16);
				}
				if(bm.intersects(bottomBox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.GREEN_PORTAL && World.tiles[x][y].function == Tile.EXIT)
				{
					this.x = World.xEnt[2] * 16 + 32;
					this.y = World.yEnt[2] * 16;
				}
				if(bm.intersects(bottomBox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.CYAN_PORTAL && World.tiles[x][y].function == Tile.EXIT)
				{
					this.x = World.xEnt[3] * 16 + 32;
					this.y = World.yEnt[3] * 16;
				}
				if(bm.intersects(bottomBox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.ORANGE_PORTAL && World.tiles[x][y].function == Tile.EXIT)
				{
					this.x = World.xEnt[4] * 16 + 32;
					this.y = World.yEnt[4] * 16;
				}
				if(bm.intersects(bottomBox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.YELLOW_PORTAL && World.tiles[x][y].function == Tile.EXIT)
				{
					this.x = World.xEnt[5] * 16 + 32;
					this.y = World.yEnt[5] * 16;
				}
				if(bm.intersects(bottomBox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.PURPLE_PORTAL && World.tiles[x][y].function == Tile.EXIT)
				{
					this.x = World.xEnt[6] * 16 + 32;
					this.y = World.yEnt[6] * 16;
				}
				if(bm.intersects(bottomBox, World.tiles[x][y].bounds) && World.tiles[x][y].id == Tile.PINK_PORTAL && World.tiles[x][y].function == Tile.EXIT)
				{
					this.x = World.xEnt[7] * 16 + 32;
					this.y = World.yEnt[7] * 16;
				}
			}
		}
	}
	
	public void useBow(int x, int y)
	{
		float dX = (float) x / (float) 40;
		float dY = (float) y / (float) 40;
		
		if(inv.checkFor(Item.ARROW, 1)) 
		{
			bowArrow = new Arrow(bm, this.x, this.y, dX, dY);
			inv.remove(Item.ARROW, 1);
		}
	}

}
