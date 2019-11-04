import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

public class Bird extends Rectangle{
	Image pic;
	private boolean alive;

	Random random=new Random();

	Bird(int a, int b, int w, int h, String s){
		alive=true;
		x=a;
		y=b;
		width=w;
		height=h;
		pic=Toolkit.getDefaultToolkit().getImage(s);
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