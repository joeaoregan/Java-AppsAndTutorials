import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

public class Pipe extends Rectangle{
	final static int WIDTH=100, HEIGHT=500, SPEED=10;
	Image pic;
	private boolean bottomPipe;

	Random random=new Random();

	Pipe(int a, int b, boolean bottomPipe){
		this.bottomPipe=bottomPipe;
		x=a;
		y=b;
		width=WIDTH;
		height=HEIGHT;

		if(bottomPipe){
			pic=Toolkit.getDefaultToolkit().getImage("pipe_bottom.png");
		}else{
			pic=Toolkit.getDefaultToolkit().getImage("pipe_top.png");
		}
	}

	public void move(){
		x-=SPEED;
	}

	public boolean getBottomPipe(){
		return bottomPipe;
	}

	public void setBottomPipe(boolean bottomPipe){
		this.bottomPipe=bottomPipe;
	}

	public void draw(Graphics g, Component c){
		g.drawImage(pic,x,y,width,height,c);
	}
}