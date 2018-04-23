package engine;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import hud.Hotbar;
import hud.Inventory;
import hud.StatusBar;
import objects.Item;
import objects.Tile;
import player.Player;
import player.Zombie;
import utilities.Rect;

public class World {
	
	public static Tile[][] tiles = new Tile[200][200];
	
	public Random random = new Random();
	public static Rectangle mouseBox;
	
	public ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<Item> removeItems = new ArrayList<Item>();
	
	public static ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	public ArrayList<Zombie> removeZombies = new ArrayList<Zombie>();
	
	public static int wave = 0;
	
	public Rect[] islands = new Rect[8];
	
	public Line2D line;
	
	public static int[] xEnt = new int[8];
	public static int[] xExt = new int[8];
	
	public static int[] yEnt = new int[8];
	public static int[] yExt = new int[8];
	
	public Bitmap bm;
	public Inventory inv;
	public Zombie zombie;
	
	public boolean waveBegun = false;
	
	public World(Bitmap bm, Inventory inv)
	{
		this.bm = bm;
		this.inv = inv;
		for(int x = 0; x < tiles.length; x++)
		{
			for(int y = 0; y < tiles[0].length; y++)
			{
				tiles[x][y] = new Tile(bm, x * 16, y * 16, Tile.WATER);
			}
		}
		
		islands[0] = new Rect(90 * 16, 90 * 16, 20 * 16, 20 * 16);
		
		for(int i = 1; i < 8; i++)
		{
			islands[i] = new Rect((random.nextInt(160) + 20) * 16, (random.nextInt(160) + 20) * 16, (random.nextInt(15) + 5) * 16, (random.nextInt(15) + 5) * 16);
		}
		
		for(int x = 0; x < tiles.length; x++)
		{
			for(int y = 0; y < tiles[0].length; y++)
			{
				for(int i = 0; i < 8; i++)
				{
					if(bm.intersects(tiles[x][y].bounds, islands[i])) tiles[x][y].id = Tile.GRASS;
				}
				
				if(random.nextInt(10) == 0 && tiles[x][y].id == Tile.GRASS)
				{
					tiles[x][y].id = Tile.TREE;
				}
				
				if(random.nextInt(7) == 0 && tiles[x][y].id == Tile.GRASS)
				{
					tiles[x][y].id = Tile.FLOWERS;
				}
				
				if(random.nextInt(5) == 0 && tiles[x][y].id == Tile.GRASS)
				{
					tiles[x][y].id = Tile.BUSH;
				}
				
				if(random.nextInt(8) == 0 && tiles[x][y].id == Tile.GRASS)
				{
					tiles[x][y].id = Tile.STONE;
				}
				
			}
		}
		
		tiles[100][100].id = Tile.BEACON;
		
		xEnt[0] = random.nextInt(20) + 90;
		yEnt[0] = random.nextInt(20) + 90;
		
		tiles[xEnt[0]][yEnt[0]].id = Tile.RED_PORTAL;
		tiles[xEnt[0]][yEnt[0]].function = Tile.ENTRANCE;
		
		for(int i = 0; i < 7; i++)
		{
			xExt[i] = (int) (random.nextInt((int) (islands[i + 1].width)) + islands[i + 1].x) / 16;
			yExt[i] = (int) (random.nextInt((int) (islands[i + 1].height)) + islands[i + 1].y) / 16;
			
			if(i == 0) tiles[xExt[i]][yExt[i]].id = Tile.RED_PORTAL;
			if(i == 1) tiles[xExt[i]][yExt[i]].id = Tile.BLUE_PORTAL;
			if(i == 2) tiles[xExt[i]][yExt[i]].id = Tile.GREEN_PORTAL;
			if(i == 3) tiles[xExt[i]][yExt[i]].id = Tile.CYAN_PORTAL;
			if(i == 4) tiles[xExt[i]][yExt[i]].id = Tile.ORANGE_PORTAL;
			if(i == 5) tiles[xExt[i]][yExt[i]].id = Tile.YELLOW_PORTAL;
			if(i == 6) tiles[xExt[i]][yExt[i]].id = Tile.PURPLE_PORTAL;
			if(i == 7) tiles[xExt[i]][yExt[i]].id = Tile.PINK_PORTAL;
			
			tiles[xExt[i]][yExt[i]].function = Tile.EXIT;
			
			xEnt[i + 1] = (int) (random.nextInt((int) (islands[i + 1].width)) + islands[i + 1].x) / 16;
			yEnt[i + 1] = (int) (random.nextInt((int) (islands[i + 1].height)) + islands[i + 1].y) / 16;
			
			if(i == 7) continue;
			
			if(i == 0) tiles[xEnt[i + 1]][yEnt[i + 1]].id = Tile.BLUE_PORTAL;
			if(i == 1) tiles[xEnt[i + 1]][yEnt[i + 1]].id = Tile.GREEN_PORTAL;
			if(i == 2) tiles[xEnt[i + 1]][yEnt[i + 1]].id = Tile.CYAN_PORTAL;
			if(i == 3) tiles[xEnt[i + 1]][yEnt[i + 1]].id = Tile.ORANGE_PORTAL;
			if(i == 4) tiles[xEnt[i + 1]][yEnt[i + 1]].id = Tile.YELLOW_PORTAL;
			if(i == 5) tiles[xEnt[i + 1]][yEnt[i + 1]].id = Tile.PURPLE_PORTAL;
			if(i == 6) tiles[xEnt[i + 1]][yEnt[i + 1]].id = Tile.PINK_PORTAL;
			
			tiles[xEnt[i + 1]][yEnt[i + 1]].function = Tile.ENTRANCE;
		}
		
		zombie = new Zombie(bm, 90 * 16, 90 * 16);
	}
	
	public static float counter = 0;
	public static int health = 0;
	
	public static float maxHealth = 0;
	
	public static boolean breaking = false;
	
	public void update(Graphics2D g)
	{
		mouseBox = new Rectangle((int) Engine.mouseX, (int) Engine.mouseY, 1, 3);
		Rect loadBox = new Rect(-bm.camX - 20, -bm.camY - 20, 180, 112);
		for(int x = 0; x < tiles.length; x++)
		{
			for(int y = 0; y < tiles[0].length; y++)
			{
				if(mouseBox.intersects(tiles[x][y].worldBounds()) && Engine.LMB)
				{
					breaking = true;
					counter += 0.1f;
					if(tiles[x][y].id == Tile.TREE)
					{
						if(counter > 10)
						{
							health += 1;
							counter = 0;
						}
						if(health >= 3)
						{
							tiles[x][y].id = Tile.GRASS;
							health = 0;
							counter = 0;
							breaking = false;
							for(int i = 0; i < random.nextInt(2) + 1; i++)
							{
								items.add(new Item(bm, tiles[x][y].x + random.nextInt(16), tiles[x][y].y + random.nextInt(16), Item.WOOD));
							}
							for(int i = 0; i < random.nextInt(2); i++)
							{
								items.add(new Item(bm, tiles[x][y].x + random.nextInt(16), tiles[x][y].y + random.nextInt(16), Item.APPLE));
							}
							Player.energy -= 5;
						}
						maxHealth = 3;
					}
					if(tiles[x][y].id == Tile.BUSH)
					{
						if(counter > 5)
						{
							health += 1;
							counter = 0;
						}
						if(health >= 3)
						{
							tiles[x][y].id = Tile.GRASS;
							health = 0;
							counter = 0;
							breaking = false;
							for(int i = 0; i < random.nextInt(3) + 1; i++)
							{
								items.add(new Item(bm, tiles[x][y].x + random.nextInt(16), tiles[x][y].y + random.nextInt(16), Item.FOILAGE));
							}
							Player.energy -= 3;
						}
						maxHealth = 3;
					}
					if(tiles[x][y].id == Tile.STONE && Hotbar.slots[Hotbar.selection].id == Item.PICKAXE)
					{
						if(counter > 4)
						{
							health += 1;
							counter = 0;
						}
						if(health >= 5)
						{
							tiles[x][y].id = Tile.GRASS;
							health = 0;
							counter = 0;
							breaking = false;
							for(int i = 0; i < random.nextInt(2) + 2; i++)
							{
								items.add(new Item(bm, tiles[x][y].x + random.nextInt(16), tiles[x][y].y + random.nextInt(16), Item.STONE));
							}
							Player.energy -= 5;
						}
						maxHealth = 5;
					}
				}
				
				if(mouseBox.intersects(tiles[x][y].worldBounds()) && Engine.RMB)
				{
					breaking = false;
					if(Hotbar.slots[Hotbar.selection].id == Item.WOODWALL && Hotbar.slots[Hotbar.selection].count > 0 && (tiles[x][y].id == Tile.GRASS || tiles[x][y].id == Tile.FLOWERS))
					{
						tiles[x][y].id = Tile.WOODWALL_FRONT;
						Hotbar.slots[Hotbar.selection].count -= 1;
						if(tiles[x][y - 1].id == Tile.WOODWALL_FRONT) tiles[x][y - 1].id = Tile.WOODWALL_MAIN;
						inv.remove(Item.WOODWALL, 1);
					}
					if(Hotbar.slots[Hotbar.selection].id == Item.WOODFLOOR && Hotbar.slots[Hotbar.selection].count > 0 && (tiles[x][y].id == Tile.GRASS || tiles[x][y].id == Tile.FLOWERS))
					{
						tiles[x][y].id = Tile.WOODFLOOR;
						Hotbar.slots[Hotbar.selection].count -= 1;
						inv.remove(Item.WOODFLOOR, 1);
					}
					if(Hotbar.slots[Hotbar.selection].id == Item.CRAFTER && Hotbar.slots[Hotbar.selection].count > 0 && (tiles[x][y].id == Tile.GRASS || tiles[x][y].id == Tile.FLOWERS))
					{
						tiles[x][y].id = Tile.CRAFTER;
						Hotbar.slots[Hotbar.selection].count -= 1;
						inv.remove(Item.CRAFTER, 1);
					}
					if(Hotbar.slots[Hotbar.selection].id == Item.SPIKES && Hotbar.slots[Hotbar.selection].count > 0 && (tiles[x][y].id == Tile.GRASS || tiles[x][y].id == Tile.FLOWERS))
					{
						tiles[x][y].id = Tile.SPIKES;
						Hotbar.slots[Hotbar.selection].count -= 1;
						inv.remove(Item.SPIKES, 1);
					}
					if(Hotbar.slots[Hotbar.selection].id == Item.ARROWTRAP && Hotbar.slots[Hotbar.selection].count > 0 && (tiles[x][y].id == Tile.GRASS || tiles[x][y].id == Tile.FLOWERS))
					{
						tiles[x][y].id = Tile.ARROWTRAP_UP;
						Hotbar.slots[Hotbar.selection].count -= 1;
						if(tiles[x][y - 1].id == Tile.WOODWALL_FRONT) {
							tiles[x][y - 1].id = Tile.WOODWALL_MAIN;
							tiles[x][y].id = Tile.ARROWTRAP_DOWN;
						}
						inv.remove(Item.ARROWTRAP, 1);
					}
					if(Hotbar.slots[Hotbar.selection].id == Item.TURRET && Hotbar.slots[Hotbar.selection].count > 0 && (tiles[x][y].id == Tile.GRASS || tiles[x][y].id == Tile.FLOWERS || tiles[x][y].id == Tile.WOODFLOOR))
					{
						tiles[x][y].id = Tile.TURRET;
						Hotbar.slots[Hotbar.selection].count -= 1;
						inv.remove(Item.TURRET, 1);
					}
					if(Hotbar.slots[Hotbar.selection].id == Item.STONEWALL && Hotbar.slots[Hotbar.selection].count > 0 && (tiles[x][y].id == Tile.GRASS || tiles[x][y].id == Tile.FLOWERS))
					{
						tiles[x][y].id = Tile.STONEWALL;
						Hotbar.slots[Hotbar.selection].count -= 1;
						inv.remove(Item.STONEWALL, 1);
					}
					if(Hotbar.slots[Hotbar.selection].id == Item.DOOR && Hotbar.slots[Hotbar.selection].count > 0 && (tiles[x][y].id == Tile.GRASS || tiles[x][y].id == Tile.FLOWERS || tiles[x][y].id == Tile.WOODFLOOR))
					{
						tiles[x][y].id = Tile.DOOR_CLOSED;
						Hotbar.slots[Hotbar.selection].count -= 1;
						inv.remove(Item.DOOR, 1);
					}
					if(Hotbar.slots[Hotbar.selection].id == Item.CAMPFIRE && Hotbar.slots[Hotbar.selection].count > 0 && (tiles[x][y].id == Tile.GRASS || tiles[x][y].id == Tile.FLOWERS))
					{
						tiles[x][y].id = Tile.CAMPFIRE;
						Hotbar.slots[Hotbar.selection].count -= 1;
						inv.remove(Item.CAMPFIRE, 1);
					}
					if(Hotbar.slots[Hotbar.selection].id == Item.APPLEPIE && Hotbar.slots[Hotbar.selection].count > 0 && (tiles[x][y].id == Tile.GRASS || tiles[x][y].id == Tile.FLOWERS))
					{
						Player.energy += 20;
						inv.remove(Item.APPLEPIE, 1);
					}
				}
				
				if(bm.intersects(tiles[x][y].bounds, loadBox)) tiles[x][y].update();
			}
		}
	
		if(StatusBar.timer < 0 && !waveBegun) {
			StatusBar.timer = 100;
			StatusBar.waveInProgress = true;
			wave += 1;
			if(!waveBegun) startWave();
		}
		
		for(Item i : items)
		{
			i.update();
			if(bm.intersects(i.bounds(), Player.hitbox)) {
				for(int s = 0; s < 12; s++)
				{
					if(Inventory.slot[s].id == i.id && Inventory.slot[s].count < 9)
					{
						Inventory.slot[s].count += 1;
						removeItems.add(i);
						break;
					}
					if(Inventory.slot[s].id == -1)
					{
						Inventory.slot[s].id = i.id;
						Inventory.slot[s].count += 1;
						removeItems.add(i);
						break;
					}
				}
			}
		}
		
		for(Zombie z : zombies)
		{
			z.move();
			if(bm.intersects(z.hitbox, loadBox)) z.update();
			if(z.health <= 0)
			{
				removeZombies.add(z);
			}
		}
		
		if(zombies.isEmpty())
		{
			StatusBar.waveInProgress = false;
		}
		
		//zombie.move();
		//zombie.update();
		
		zombies.removeAll(removeZombies);
		items.removeAll(removeItems);
	}
	
	public void startWave()
	{
		for(int i = 0; i < wave * 5; i++)
		zombies.add(new Zombie(bm, random.nextInt(200 * 16), random.nextInt(200 * 16)));
		waveBegun = true;
	}
	
	public void remove(Item i)
	{
		items.remove(i);
	}

}
