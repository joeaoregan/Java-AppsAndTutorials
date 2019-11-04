import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

public class Block extends Rectangle{
	final int DIFFERENCE=3;

	Image pic;
	int dx=DIFFERENCE;
	int dy=-DIFFERENCE;
	Rectangle left, right;
	private boolean destroyed=false;
	private boolean powerup=false;

	Random random=new Random();

	Block(int a, int b, int w, int h, String s){
		if(random.nextInt(10000)%2==0){
			dx*=-1;	// Set random start direction for Ball
		}
		x=a;
		y=b;
		width=w;
		height=h;
		left=new Rectangle(a-1,b,1,h);
		right=new Rectangle(a+w+1,b,1,h);
		pic=Toolkit.getDefaultToolkit().getImage(s);
	}

	public void draw(Graphics g, Component c){
		if(!destroyed){
			g.drawImage(pic,x,y,width,height,c);
		}
	}

	public boolean getDestroyed(){
		return destroyed;
	}

	public void setDestroyed(boolean destroyed){
		this.destroyed=destroyed;
	}

	public boolean getPowerup(){
		return powerup;
	}

	public void setPowerup(boolean powerup){
		this.powerup=powerup;
	}
}