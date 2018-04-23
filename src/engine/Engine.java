package engine;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import hud.Crafting;
import hud.Inventory;
import hud.StatusBar;
import objects.Item;
import player.Player;
import utilities.Images;

public class Engine extends Canvas implements Runnable, KeyListener, MouseListener, MouseWheelListener 
{
	
	public int width = 800;
	public int height = 600;
	
	private Thread thread;
	
	public boolean isRunning = false;
	
	public int frameCount, fps;
	public float counter = 0;
	
	public static float mouseX, mouseY;
	public static float altMouseX, altMouseY;
	public static boolean LMB = false;
	public static boolean RMB = false;
	
	public static int invOpen = 0;
	public static int cftOpen = 0;
	
	public static float delta = 0;
	
	public Images imgs;
	public Bitmap bm;
	public Player player;
	public World world;
	public StatusBar statusbar;
	public Inventory inventory;
	public Crafting crafting;
	
	public static int STATE = 1;
	
	public Engine()
	{
		JFrame frame = new JFrame("Wrathful Islands");
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		frame.add(this, BorderLayout.CENTER);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		
		imgs = new Images();
		bm = new Bitmap();
		inventory = new Inventory(bm);
		world = new World(bm, inventory);
		statusbar = new StatusBar(bm);
		crafting = new Crafting(bm, inventory);
		player = new Player(bm, inventory, 105 * 16, 100 * 16);
		
		start();
	}

	public static void main(String[] args)
	{
		System.out.println("Hello World!");
		new Engine();
	}
	
	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		
		isRunning = true;
	}
	
	public void run()
	{
		long startTime = System.currentTimeMillis();
		while(isRunning)
		{
			try {
				thread.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			frameCount++;
			
			delta = 1 / frameCount;
			
			update();
			
			if(System.currentTimeMillis() - startTime > 1000)
			{
				fps = frameCount;
				System.out.println("FPS: " + fps);
				frameCount = 0;
				startTime += 1000;
			}
		}
		stop();
	}
	
	public synchronized void stop()
	{
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		Graphics graphics = bs.getDrawGraphics();
		Graphics2D g = (Graphics2D) graphics;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		if(STATE == 0)
		{
			bm.clear();
			
			counter += 0.05f;
			bm.drawMessage("HELLO!", 10, 50);
			
			bm.tile0X = world.tiles[0][0].x;
			bm.tile0Y = world.tiles[0][0].y;
			
			world.update(g);
			player.update();
			
			statusbar.update();
			
			if(invOpen == 1) inventory.update(g);
			if(cftOpen == 1) crafting.update();
			
			if(player.health <= 0)
			{
				STATE = 1;
				player.health = 100;
				statusbar.timer = 100;
				player.energy = 50;
			}
			
			bm.translate(-player.x + (bm.screen.getWidth() / 2) - 4, -player.y + (bm.screen.getHeight() / 2) - 16);
			bm.draw(g, width, height);
		}
		
		if(STATE == 1)
		{
			bm.clear();
			
			bm.drawMessage("WRATHFUL ISLANDS", 10, 10);
			bm.drawMessage("PRESS ENTER TO PLAY", 10, 60);
			
			bm.draw(g, width, height);
		}
			
		graphics.dispose();
		bs.show();
	}

	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		
		if(k == e.VK_W)
		{
			player.isMovingForward = true;
		}
		if(k == e.VK_A)
		{
			player.isMovingLeft = true;
		}
		if(k == e.VK_S)
		{
			player.isMovingBackward = true;
		}
		if(k == e.VK_D)
		{
			player.isMovingRight = true;
		}
		if(k == e.VK_E)
		{
			invOpen += 1;
			if(invOpen >= 2)
				invOpen = 0;
		}
		if(k == e.VK_C)
		{
			cftOpen += 1;
			if(cftOpen >= 2)
				cftOpen = 0;
		}
		if(k == e.VK_ENTER && STATE == 1)
		{
			inventory = new Inventory(bm);
			world = new World(bm, inventory);
			statusbar = new StatusBar(bm);
			crafting = new Crafting(bm, inventory);
			player = new Player(bm, inventory, 105 * 16, 100 * 16);
			
			STATE = 0;
		}
	}

	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		
		if(k == e.VK_W)
		{
			player.isMovingForward = false;
			player.faceDirection = 1;
		}
		if(k == e.VK_A)
		{
			player.isMovingLeft = false;
			player.faceDirection = 3;
		}
		if(k == e.VK_S)
		{
			player.isMovingBackward = false;
			player.faceDirection = 0;
		}
		if(k == e.VK_D)
		{
			player.isMovingRight = false;
			player.faceDirection = 2;
		}
	}

	public void keyTyped(KeyEvent e) {}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		if(SwingUtilities.isLeftMouseButton(e)) LMB = true;
		if(SwingUtilities.isRightMouseButton(e)) RMB = true;
		
		if(statusbar.hb.slots[statusbar.hb.selection].id == Item.SWORD && statusbar.hb.slots[statusbar.hb.selection].count > 0)
		{
			player.usingSword = true;
		}
		if(statusbar.hb.slots[statusbar.hb.selection].id == Item.BOW && statusbar.hb.slots[statusbar.hb.selection].count > 0)
		{
			player.useBow((int) mouseX - 400, (int) mouseY - 300);
		}
	}

	public void mouseReleased(MouseEvent e) {
		mouseX = -20000 + bm.camX;
		mouseY = -20000 + bm.camY;
		
		if(SwingUtilities.isLeftMouseButton(e)) LMB = false;
		if(SwingUtilities.isRightMouseButton(e)) RMB = false;
		
		crafting.selected = false;
		inventory.counter = 0;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int r = e.getWheelRotation();
		
		if(r < 0)
		{
			statusbar.hb.x += 11;
			statusbar.hb.selection ++;
			if(statusbar.hb.x >= 74 + 33)
			{
				statusbar.hb.x = 74;
			}
			if(statusbar.hb.selection >= 3)
			{
				statusbar.hb.selection = 0;
			}
		}
		
		if(r > 0)
		{
			statusbar.hb.x -= 11;
			statusbar.hb.selection --;
			if(statusbar.hb.x <= 73)
			{
				statusbar.hb.x = 74 + 22;
			}
			if(statusbar.hb.selection <= -1)
			{
				statusbar.hb.selection = 2;
			}
		}
	}
	
	
}
