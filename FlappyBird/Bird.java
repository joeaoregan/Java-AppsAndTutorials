import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

public class Bird extends Rectangle{
	public static final int WIDTH=28;
	public static final int HEIGHT=20;
	Image pic;
	private boolean alive;
	public int yMotion;

	Random random=new Random();

	Bird(int a, int b, int w, int h, String s){
		yMotion=0;
		alive=true;
		x=a;
		y=b;
		width=w;
		height=h;
		pic=Toolkit.getDefaultToolkit().getImage(s);
	}

	public void move(){
		y+=yMotion;

		if(y+yMotion>=FlappyBird.HEIGHT-FlappyBird.GROUND_HEIGHT){
			y=FlappyBird.HEIGHT-FlappyBird.GROUND_HEIGHT-height;
		}
	}

	public boolean getAlive(){
		return alive;
	}

	public void setAlive(boolean alive){
		this.alive=alive;
	}

	public void draw(Graphics g, Component c){
		if(alive){
			g.drawImage(pic,x,y,width,height,c);
		}
	}
}