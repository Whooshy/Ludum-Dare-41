package utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {
	
	public static BufferedImage font_regular;
	
	public static BufferedImage playersheet;
	public static BufferedImage[] player = new BufferedImage[12];
	
	public static BufferedImage zombiesheet;
	public static BufferedImage[] zombie = new BufferedImage[12];
	
	public static BufferedImage tilesheet;
	public static BufferedImage[] tiles = new BufferedImage[28];
	
	public static BufferedImage itemsheet;
	public static BufferedImage[] items = new BufferedImage[21];
	
	public static BufferedImage watersheet;
	public static BufferedImage[] water = new BufferedImage[8];
	
	public static BufferedImage swordsheet;
	public static BufferedImage[] swords = new BufferedImage[4];
	
	public static BufferedImage inventory;
	public static BufferedImage crafting;
	public static BufferedImage hotbar;
	public static BufferedImage highlight;
	public static BufferedImage arrow;

	public Images()
	{
		try {
			font_regular = ImageIO.read(getClass().getResource("/fonts/regular.png"));
			playersheet = ImageIO.read(getClass().getResource("/player/player.png"));
			zombiesheet = ImageIO.read(getClass().getResource("/player/zombie.png"));
			tilesheet = ImageIO.read(getClass().getResource("/tiles/tilesheet.png"));
			itemsheet = ImageIO.read(getClass().getResource("/items/itemsheet.png"));
			watersheet = ImageIO.read(getClass().getResource("/tiles/water.png"));
			inventory = ImageIO.read(getClass().getResource("/hud/inventory.png"));
			crafting = ImageIO.read(getClass().getResource("/hud/crafting.png"));
			hotbar = ImageIO.read(getClass().getResource("/hud/hotbar.png"));
			highlight = ImageIO.read(getClass().getResource("/hud/highlight.png"));
			swordsheet = ImageIO.read(getClass().getResource("/particles/sword.png"));
			arrow = ImageIO.read(getClass().getResource("/particles/arrow.png"));
			
			int counter = -1;
			
			int yOffset = 0;
			int yOffset2 = 0;
			
			for(int i = 0; i < 12; i++)
			{
				player[i] = playersheet.getSubimage(i * 7, 0, 7, 16);
			}
			for(int i = 0; i < 12; i++)
			{
				zombie[i] = zombiesheet.getSubimage(i * 7, 0, 7, 16);
			}
			counter = -1;
			for(int i = 0; i < tiles.length; i++)
			{	
				counter++;
				
				if(counter >= 8)
				{
					counter = 0;
					yOffset += 16;
				}
				
				int xOffset = counter * 16;
								
				tiles[i] = tilesheet.getSubimage(xOffset, yOffset, 16, 16);
			}
			counter = -1;
			for(int i = 0; i < items.length; i++)
			{
				counter++;
				
				if(counter >= 9)
				{
					counter = 0;
					yOffset2 += 3;
				}
				
				int xOffset = counter * 3;
								
				items[i] = itemsheet.getSubimage(xOffset, yOffset2, 3, 3);
			}
			counter = -1;
			for(int i = 0; i < water.length; i++)
			{
				int xOffset = i * 16;
				water[i] = watersheet.getSubimage(xOffset, 0, 16, 16);
			}
			for(int i = 0; i < swords.length; i++)
			{
				int xOffset = i * 16;
				swords[i] = swordsheet.getSubimage(xOffset, 0, 16, 16);
			}
			counter = -1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
