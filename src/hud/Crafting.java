package hud;

import java.awt.Rectangle;

import engine.Bitmap;
import engine.Engine;
import objects.Item;
import utilities.Images;

public class Crafting {
	
	public Bitmap bm;
	public Inventory inv;
	
	public int tab = 0;
	public int selection = 0;
	
	public static final int BASIC = 0;
	public static final int WEAPONS = 1;
	public static final int STRUCTS = 2;
	public static final int FOOD = 3;
	
	public Rectangle[] tabs = new Rectangle[4];
	public Rectangle[] selections = new Rectangle[8];
	
	public int isMakeable = 0;
	public boolean selected = false;
	
	public Crafting(Bitmap bm, Inventory inv)
	{
		this.bm = bm;
		this.inv = inv;
		for(int i = 0; i < tabs.length; i++)
		{
			tabs[i] = new Rectangle(425, 40 + (i * 45), 250, 40);
		}
		for(int i = 0; i < selections.length; i++)
		{
			selections[i] = new Rectangle(90, 40 + (i * 45), 250, 40);
		}
	}
	
	public void update()
	{
		Rectangle mouse = new Rectangle((int) Engine.mouseX, (int) Engine.mouseY, 1, 1);
		
		bm.createStaticTexture(Images.crafting, 15, 4);
		bm.drawMessage("BASIC", 80, 8);
		bm.drawMessage("WEAPONS", 80, 16);
		bm.drawMessage("TURRETS", 80, 24);
		bm.drawMessage("FOOD", 80, 32);
		
		if(tab == BASIC)
		{
			bm.drawMessage("STICKS", 17, 8);
			bm.drawMessage("ROPE", 17, 16);
			bm.drawMessage("LOG WALL", 17, 24);
			bm.drawMessage("LOG FLOOR", 17, 32);
			bm.drawMessage("CRAFTER", 17, 40);
		}
		if(tab == WEAPONS)
		{
			bm.drawMessage("PICKAXE", 17, 8);
			bm.drawMessage("SWORD", 17, 16);
			bm.drawMessage("BOW", 17, 24);
			bm.drawMessage("ARROW", 17, 32);
		}
		if(tab == STRUCTS)
		{
			bm.drawMessage("SPIKES", 17, 8);
			bm.drawMessage("ARROW TRAP", 17, 16);
			bm.drawMessage("STONE WALL", 17, 24);
		}
		if(tab == FOOD)
		{
			bm.drawMessage("APPLE PIE", 17, 8);
			bm.drawMessage("CAMPFIRE", 17, 16);
		}
		
		if(mouse.intersects(tabs[BASIC]))
			tab = BASIC;
		if(mouse.intersects(tabs[WEAPONS]))
			tab = WEAPONS;
		if(mouse.intersects(tabs[STRUCTS]))
			tab = STRUCTS;
		if(mouse.intersects(tabs[FOOD]))
			tab = FOOD;
		
		for(int i = 0; i < 8; i++)
		{
			if(mouse.intersects(selections[i]) && !selected)
				selection = i + 1;
		}
		
		if(tab == BASIC && !selected)
		{
			//Sticks
			if(selection == 1 && inv.checkFor(Item.WOOD, 1))
			{
				System.out.println("hello");
				inv.remove(Item.WOOD, 1);
				inv.add(Item.STICKS, 2);
				selected = true;
			}
			//Rope
			if(selection == 2 && inv.checkFor(Item.FOILAGE, 2))
			{
				inv.remove(Item.FOILAGE, 2);
				inv.add(Item.ROPE, 1);
				selected = true;
			}
			//Wood Wall
			if(selection == 3 && inv.checkFor(Item.WOOD, 4) && inv.checkFor(Item.ROPE, 2))
			{
				inv.remove(Item.WOOD, 4);
				inv.remove(Item.ROPE, 2);
				inv.add(Item.WOODWALL, 1);
				selected = true;
			}
			//Wood Floor
			if(selection == 4 && inv.checkFor(Item.WOOD, 3))
			{
				inv.remove(Item.WOOD, 3);
				inv.add(Item.WOODFLOOR, 1);
				selected = true;
			}
			//Crafter
			if(selection == 5 && inv.checkFor(Item.WOOD, 2) && inv.checkFor(Item.STICKS, 4) && inv.checkFor(Item.ROPE, 1))
			{
				inv.remove(Item.STICKS, 4);
				inv.remove(Item.ROPE, 1);
				inv.remove(Item.WOOD, 2);
				inv.add(Item.CRAFTER, 1);
				selected = true;
			}
		}
		if(tab == WEAPONS && !selected)
		{
			//Pickaxe
			if(selection == 1 && inv.checkFor(Item.STICKS, 2) && inv.checkFor(Item.ROPE, 1) && inv.checkFor(Item.WOOD, 1))
			{
				inv.remove(Item.STICKS, 2);
				inv.remove(Item.ROPE, 1);
				inv.remove(Item.WOOD, 1);
				inv.add(Item.PICKAXE, 1);
				selected = true;
			}
			//Sword
			if(selection == 2 && inv.checkFor(Item.STICKS, 1) && inv.checkFor(Item.ROPE, 1) && inv.checkFor(Item.STONE, 2))
			{
				inv.remove(Item.STICKS, 1);
				inv.remove(Item.ROPE, 1);
				inv.remove(Item.STONE, 2);
				inv.add(Item.SWORD, 1);
				selected = true;
			}
			//Bow
			if(selection == 3 && inv.checkFor(Item.STICKS, 3) && inv.checkFor(Item.ROPE, 2))
			{
				inv.remove(Item.STICKS, 3);
				inv.remove(Item.ROPE, 2);
				inv.add(Item.BOW, 1);
				selected = true;
			}
			//Arrow
			if(selection == 4 && inv.checkFor(Item.STICKS, 2) && inv.checkFor(Item.ROPE, 1) && inv.checkFor(Item.STONE, 1))
			{
				inv.remove(Item.STICKS, 2);
				inv.remove(Item.ROPE, 1);
				inv.remove(Item.STONE, 1);
				inv.add(Item.ARROW, 4);
				selected = true;
			}
		}
		if(tab == STRUCTS && !selected)
		{
			//Spikes
			if(selection == 1 && inv.checkFor(Item.WOODFLOOR, 1) && inv.checkFor(Item.STONE, 3))
			{
				inv.remove(Item.WOODFLOOR, 1);
				inv.remove(Item.STONE, 1);
				inv.add(Item.SPIKES, 1);
				selected = true;
			}
			//Arrow Trap
			if(selection == 2 && inv.checkFor(Item.ARROW, 5) && inv.checkFor(Item.WOODWALL, 1))
			{
				inv.remove(Item.ARROW, 5);
				inv.remove(Item.WOODWALL, 1);
				inv.add(Item.ARROWTRAP, 1);
				selected = true;
			}
			//Stone Wall
			if(selection == 3 && inv.checkFor(Item.STONE, 5))
			{
				inv.remove(Item.STONE, 5);
				inv.add(Item.STONEWALL, 1);
				selected = true;
			}
		}
		if(tab == FOOD && !selected)
		{
			//Apple Pie
			if(selection == 1 && inv.checkFor(Item.APPLE, 3) && inv.checkFor(Item.FOILAGE, 1))
			{
				inv.remove(Item.APPLE, 3);
				inv.remove(Item.FOILAGE, 1);
				inv.add(Item.APPLEPIE, 1);
				selected = true;
			}
			//Campfire
			if(selection == 2 && inv.checkFor(Item.WOOD, 3) && inv.checkFor(Item.FOILAGE, 1))
			{
				inv.remove(Item.WOOD, 3);
				inv.remove(Item.FOILAGE, 1);
				inv.add(Item.CAMPFIRE, 1);
				selected = true;
			}
		}
	}
}
