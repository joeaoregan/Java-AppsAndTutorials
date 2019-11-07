import java.net.URL;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Bird extends Rectangle{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH=28;
	public static final int HEIGHT=20;
	Image pic;
	public int yMotion;

	Bird(int a, int b){
		yMotion=0;
		x=a;
		y=b;
		width=WIDTH;
		height=HEIGHT;

		URL url = getClass().getResource("/flappy.png");
		pic=Toolkit.getDefaultToolkit().getImage(url);
	}

	public void move(){
		// Stop at ground level
		if (y + yMotion >= FlappyBird.HEIGHT - FlappyBird.GROUND_HEIGHT - height) {
			y = FlappyBird.HEIGHT - FlappyBird.GROUND_HEIGHT - height;
		} else {
			y += yMotion;//jump
		}
	}

	public void jump(){
		if(yMotion>0){
			yMotion=0;
		}
		yMotion-=10;
		SoundEffect.flap();
	}

	public void fall(){
		if(yMotion<15){
			yMotion+=2;
		}
	}

	public void draw(Graphics g, Component c){
		g.drawImage(pic,x,y,width,height,c);
	}
}