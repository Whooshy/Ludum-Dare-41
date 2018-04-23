package hud;

import java.awt.Color;

import engine.Bitmap;
import engine.World;
import player.Player;

public class StatusBar {
	
	public Bitmap bm;
	
	public Hotbar hb;
	public static float timer = 100;
	
	public static boolean waveInProgress = false;
	
	public StatusBar(Bitmap bm)
	{
		this.bm = bm;
		hb = new Hotbar(bm);
	}
	
	public void update()
	{
		if(!waveInProgress) timer -= 0.01f;
		bm.fillRectangle(Color.BLACK, 0, 85, 150, 112 - 85);
		if(World.breaking)
		{
			String breaking = "BREAKING:" + String.valueOf(World.health + 1) + "/" + String.valueOf((int) World.maxHealth);
			bm.drawMessage(breaking, 1, 96);
		}
		
		String wave = "WAVE: " + String.valueOf(World.wave) + " / " + String.valueOf((int) timer); 
		bm.drawMessage(wave, 1, 88);
		
		bm.drawMessage("HP:" + String.valueOf(Player.health), 150 - (6 * 7), 88);
		bm.drawMessage("EP:" + String.valueOf((int) Player.energy), 150 - (6 * 7), 96);
		
		hb.update();
	}

}
