package hud;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import engine.Bitmap;
import engine.Engine;
import objects.Item;
import utilities.Images;

public class Inventory {

	public Bitmap bm;
	public static Slot[] slot = new Slot[12];
	
	public Rectangle[] rectSlots = new Rectangle[12];
	
	public int counter = 0;
	
	public Inventory(Bitmap bm)
	{
		int counter = -1;
		int counter2 = -1;
		
		int yOffset = 47;
		int xOffset = 42;
		
		int yOffset2 = 252;
		
		this.bm = bm;
		for(int i = 0; i < 12; i++)
		{
			counter += 1;
			
			xOffset = 42 + (counter * 13);
			
			if(counter >= 4)
			{
				xOffset = 42;
				yOffset += 11;
				counter = 0;
			}
			
			slot[i] = new Slot(bm, xOffset, yOffset, -1, 0);
		}
		
		for(int i = 0; i < 12; i++)
		{
			counter2 += 1;
			
			xOffset = 218 + (counter2 * 69);
			
			if(counter2 >= 4)
			{
				xOffset = 218;
				yOffset2 += 59;
				counter2 = 0;
			}
			
			rectSlots[i] = new Rectangle(xOffset, yOffset2, 48, 47);
		}
	}
	
	public void update(Graphics2D g)
	{
		Rectangle mouse = new Rectangle((int) Engine.mouseX, (int) Engine.mouseY, 1, 1);
		bm.createStaticTexture(Images.inventory, 35, 26);
		for(int i = 0; i < 12; i++)
		{
			slot[i].update();
			if(mouse.intersects(rectSlots[i]) && counter == 0)
			{
				if(slot[i].id == -1)
					continue;
				counter ++;
				if(Hotbar.slots[1].id != -1)
				{
					Hotbar.slots[2].id = Hotbar.slots[1].id;
					Hotbar.slots[2].count = Hotbar.slots[1].count;
				}
				if(Hotbar.slots[0].id != -1)
				{
					Hotbar.slots[1].id = Hotbar.slots[0].id;
					Hotbar.slots[1].count = Hotbar.slots[0].count;
				}
				Hotbar.slots[0].id = slot[i].id;
				Hotbar.slots[0].count = slot[i].count;
			}
			
		}
	}
	
	public void add(int id, int count)
	{
		for(int i = 0; i < 12; i++)
		{
			if(slot[i].id == id && slot[i].count < 9)
			{
				slot[i].count += count;
				if(slot[i].count >= 10)
				{
					for(int j = i; j < 12 - i; j++)
					{
						if(slot[j].id == id && slot[i].count <= 9 - count)
						{
							System.out.println(j + "A");
							slot[j].count = slot[i].count - 9;
							System.out.println("Hello!");
							break;
						}
						if(slot[j].id == -1)
						{
							System.out.println(j + "B");
							slot[j].id = id;
							slot[j].count = slot[i].count - 9;
							break;
						}
					}
					slot[i].count = 9;
				}
				break;
			}
			if(slot[i].id == -1)
			{
				slot[i].id = id;
				slot[i].count += count;
				break;
			}
		}
	}
	
	public void remove(int id, int count)
	{
		for(int i = 0; i < 12; i++)
		{
			if(slot[i].id == id)
			{
				slot[i].count -= count;
				break;
			}
		}
	}
	
	public boolean checkFor(int id, int count)
	{
		int count_ = 0;
		for(int i = 0; i < 12; i++)
		{
			count_ ++;
			if(slot[i].id == id)
			{
				if(slot[i].count >= count)
				{
					break;
				}
			}
		}
		if(count_ == 12) return false;
		else return true;
	}
}
