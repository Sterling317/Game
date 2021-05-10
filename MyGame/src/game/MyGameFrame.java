package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class MyGameFrame extends JFrame {
	Image rocketImg = GameUtil.getImage("images/rocket.jpg");
	Image bg = GameUtil.getImage("images/bg.jpg");
	
	Rocket rocket = new Rocket(rocketImg, 250, 250);
	
	Shell[] shells = new Shell[20];
	
	
	public void paint(Graphics g) {
		g.drawImage(bg, 0, 0, null);
		rocket.drawSelf(g);//画飞机
		Graphics g2;
		for(int i=0;i<shells.length-1;i++) {
			g2 = g;
			//System.out.println(g);
			shells[i].draw(g);//画炮弹
			
			boolean bomb = shells[i].getRect().intersects(rocket.getRect());
			if(bomb) {
				rocket.live = false;
			}
		}
	}
	
	class PaintThread extends Thread{
		@Override
		public void run() {
			while(true) {
				repaint();//重画
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			rocket.addDirection(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			rocket.minusDirection(e);
		}
		
	}

	public void launchFrame() {
		setTitle("一起打飞机");
		setVisible(true);
		setSize(500, 500);
		setLocation(300, 300);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		new PaintThread().start();//启动窗口重画的线程
		addKeyListener(new KeyMonitor());//给窗口增加键盘监听
		
		for(int i=0;i<shells.length-1;i++) {
			shells[i] = new Shell();
		}
	}

	public static void main(String[] args) {
		MyGameFrame f = new MyGameFrame();
		f.launchFrame();
	}

}
