import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

public class Pipe extends Rectangle{
	final int PIPE_WIDTH=100, PIPE_HEIGHT=500;
	Image pic;
	private boolean alive;

	Random random=new Random();
/*
	Pipe(int a, int b, int h){
		alive=true;
		x=a;
		y=b;
		width=PIPE_WIDTH;
		height=PIPE_HEIGHT;
		pic=Toolkit.getDefaultToolkit().getImage("pipe_bottom.png");
		//pic2=Toolkit.getDefaultToolkit().getImage("pipe2.png");
	}

	Pipe(int a, int b, int h, boolean alive){
		this.alive=alive;
		x=a;
		y=b;
		width=PIPE_WIDTH;
		height=h;
		pic2=Toolkit.getDefaultToolkit().getImage("pipe_top.png");
	}
	*/
	Pipe(int a, int b){
		alive=true;
		x=a;
		y=b;
		width=PIPE_WIDTH;
		height=PIPE_HEIGHT;
		pic=Toolkit.getDefaultToolkit().getImage("pipe_bottom.png");
		//pic2=Toolkit.getDefaultToolkit().getImage("pipe2.png");
	}

	Pipe(int a, int b, boolean alive){
		this.alive=alive;
		x=a;
		y=b;
		width=PIPE_WIDTH;
		height=PIPE_HEIGHT;
		if(alive)
		pic=Toolkit.getDefaultToolkit().getImage("pipe_bottom.png");
		else
		pic=Toolkit.getDefaultToolkit().getImage("pipe_top.png");
	}

	public boolean getAlive(){
		return alive;
	}

	public void setAlive(boolean alive){
		this.alive=alive;
	}

	public void draw(Graphics g, Component c){
		//if(alive){
			g.drawImage(pic,x,y,width,height,c);
		//}
		/*
		else{
			//g.drawImage(pic,x, height, width, y, x, y, width, height, c);
			g.drawImage(pic2,x,y,width,height,c);
		}
*/

	}
}