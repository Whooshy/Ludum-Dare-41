package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import utilities.Images;
import utilities.Rect;

public class Bitmap {
	
	public BufferedImage screen;
	
	public static float camX, camY;
	
	public String characterList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789:/?! ";
	
	public int tile0X, tile0Y;
	
	public Bitmap()
	{
		screen = new BufferedImage(150, 112, BufferedImage.TYPE_INT_RGB);
	}
	
	public void clear()
	{
		for(int x = 0; x < screen.getWidth(); x++)
		{
			for(int y = 0; y < screen.getHeight(); y++)
			{
				screen.setRGB(x, y, 0);
			}
		}
	}
	
	public void translate(float x, float y)
	{
		camX = x;
		camY = y;
	}
	
	public void draw(Graphics2D g, int width, int height)
	{
		g.drawImage(screen, 0, 0, width, height, null);
	}
	
	public void createTexture(BufferedImage img, int x, int y)
	{
		//System.out.println(x + camX);
		for(int xx = 0; xx < img.getWidth(); xx++)
		{
			for(int yy = 0; yy < img.getHeight(); yy++)
			{
				if(x + xx + camX <= -1) continue;
				if(x + xx + camX > screen.getWidth() - 2) continue;
				if(y + yy + camY <= -1) continue;
				if(y + yy + camY > screen.getHeight() - 2) continue;
				
				if(img.getRGB(xx, yy) == new Color(0, 0, 0).getRGB()) continue;
				screen.setRGB(x + xx + (int) camX, y + yy + (int) camY, img.getRGB(xx, yy));
			}
		}
	}
	
	public void createStaticTexture(BufferedImage img, int x, int y)
	{
		//System.out.println(x + camX);
		for(int xx = 0; xx < img.getWidth(); xx++)
		{
			for(int yy = 0; yy < img.getHeight(); yy++)
			{
				if(x + xx <= -1) continue;
				if(x + xx > screen.getWidth() - 2) continue;
				if(y + yy <= -1) continue;
				if(y + yy > screen.getHeight() - 2) continue;
				
				if(img.getRGB(xx, yy) == new Color(0, 0, 0).getRGB()) continue;
				screen.setRGB(x + xx, y + yy, img.getRGB(xx, yy));
			}
		}
	}
	
	public void fillRectangle(Color color, int x, int y, int width, int height)
	{
		for(int xx = 0; xx < width; xx++)
		{
			for(int yy = 0; yy < height; yy++)
			{
				if(x + xx < 0) continue;
				if(x + xx >= screen.getWidth()) continue;
				if(y + yy < 0) continue;
				if(y + yy >= screen.getHeight()) continue;

				screen.setRGB(x + xx, y + yy, color.getRGB());
			}
		}
	}
	
	public void drawMessage(String message, int x, int y)
	{
		for(int i = 0; i < message.length(); i++)
		{
			char letter = message.charAt(i);
			int index = characterList.indexOf(letter);
			int yOffset = 0;
			if(index >= 21) {
				yOffset = 7;
				index -= 21;
			}
			BufferedImage character = Images.font_regular.getSubimage(index * 6, yOffset, 6, 7);
			createStaticTexture(character, x + (i * 6), y);
		}
	}
	
	public void drawRect(Rect rect)
	{
		for(int x = (int) rect.x; x < rect.x + rect.width; x++)
		{
			for(int y = (int) rect.y; y < rect.y + rect.height; y++)
			{
				if(x + camX < 0) continue;
				if(x + camX >= screen.getWidth() - 2) continue;
				if(y + camY < 0) continue;
				if(y + camY >= screen.getHeight() - 2) continue;
				
				screen.setRGB(x + (int) camX, y + (int) camY, Color.WHITE.getRGB());
			}
		}
	}
	
	public boolean intersects(Rect bounds, Rect zone)
	{
		if(bounds.x > zone.x && bounds.x <= zone.x + zone.width && bounds.y > zone.y && bounds.y <= zone.y + zone.height)
			return true;
		else
			return false;
	}
}
