package player;

import engine.Bitmap;
import engine.World;
import objects.Tile;
import utilities.Images;
import utilities.Rect;

public class Zombie {
	
	public float x, y;
	public float velX, velY;
	
	public float health = 2;
	public Bitmap bm;
	
	public int frame = 0;
	
	public static Rect hitbox;
	
	public Rect topBox = new Rect(x, y, 8, 2);
	public Rect bottomBox = new Rect(x, y + 14, 8, 2);
	public Rect leftBox = new Rect(x, y, 2, 16);
	public Rect rightBox = new Rect(x + 6, y, 2, 16);
	
	public static boolean isMovingForward, isMovingLeft, isMovingRight, isMovingBackward;
	public static boolean isCollidingForward, isCollidingLeft, isCollidingRight, isCollidingBackward;
	
	public float speed = 0.25f;
	public int invincibilityTime = 30;
	
	public Zombie(Bitmap bm, float x, float y)
	{
		this.x = x;
		this.y = y;
		this.bm = bm;
	}
	
	public void update()
	{
		if(isMovingForward) bm.createTexture(Images.zombie[(int) frame + 3], (int) x, (int) y);
		else if(isMovingBackward) bm.createTexture(Images.zombie[(int) frame], (int) x, (int) y);
		else if(isMovingLeft) bm.createTexture(Images.zombie[(int) frame + 8], (int) x, (int) y);
		else if(isMovingRight) bm.createTexture(Images.zombie[(int) frame + 6], (int) x, (int) y);
		else if(!isMovingForward) bm.createTexture(Images.zombie[0], (int) x, (int) y);
		else if(!isMovingBackward) bm.createTexture(Images.zombie[3], (int) x, (int) y);
		else if(!isMovingLeft) bm.createTexture(Images.zombie[6], (int) x, (int) y);
		else if(!isMovingRight) bm.createTexture(Images.zombie[11], (int) x, (int) y);
	}
	
	public void move()
	{
		hitbox = new Rect(x, y, 8, 16);
		
		topBox = new Rect(x, y + 12, 8, 2);
		bottomBox = new Rect(x, y + 14, 8, 2);
		leftBox = new Rect(x - 1, y + 13, 2, 3);
		rightBox = new Rect(x + 6, y + 13, 2, 3);
		
		isMovingLeft = false;
		isMovingRight = false;
		isMovingForward = false;
		isMovingBackward = false;
		
		velX = 0;
		velY = 0;
		
		//bm.drawRect(hitbox);
		
		if(bm.intersects(Player.swordBox, hitbox) && Player.usingSword && invincibilityTime >= 30)
		{
			health -= 1;
			System.out.println("Ouch: " + health);
			if(isMovingLeft) x -= 16;
			if(isMovingRight) x += 16;
			if(isMovingForward) y += 16;
			if(isMovingBackward) y -= 16;
			invincibilityTime = 0;
		}
		
		if(bm.intersects(Player.bowArrow.bounds, hitbox) && invincibilityTime >= 30)
		{
			health -= 1;
			System.out.println("Ouch!: " + health);
			invincibilityTime = 0;
		}
		
		for(int x = 0; x < World.tiles.length; x++)
		{
			for(int y = 0; y < World.tiles[0].length; y++)
			{
				if((bm.intersects(World.tiles[x][y].bounds, hitbox) || bm.intersects(hitbox, World.tiles[x][y].bounds)) && World.tiles[x][y].id == Tile.SPIKES && invincibilityTime >= 30)
				{
					health -= 0.25;
					System.out.println("Ouch!: " + health);
					invincibilityTime = 0;
				}
			}
		}
		
		for(int x = 0; x < World.tiles.length; x++)
		{
			for(int y = 0; y < World.tiles[0].length; y++)
			{
				if((bm.intersects(World.tiles[x][y].arrowTrap.bounds, hitbox) || bm.intersects(hitbox, World.tiles[x][y].arrowTrap.bounds)) && invincibilityTime >= 30)
				{
					health -= 1;
					System.out.println("Ouch!: " + health);
					invincibilityTime = 0;
				}
			}
		}
		
		if(x >= 1580 && x <= 1620 && y >= 1580 && y <= 1620)
		{
			Player.health--;
		}
		
		invincibilityTime ++;
		
		if(invincibilityTime >= 30)
		{
			invincibilityTime = 30;
		}
		
		if(x < 100 * 16 && !isCollidingRight)
		{
			isMovingRight = true;
		}
		if(x > 100 * 16 && !isCollidingLeft)
		{
			isMovingLeft = true;
		}
		if(y < 100 * 16 && !isCollidingBackward)
		{
			isMovingBackward = true;
		}
		if(y > 100 * 16 && !isCollidingForward)
		{
			isMovingForward = true;
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
		
		x += velX;
		y += velY;
		
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
	}

}
