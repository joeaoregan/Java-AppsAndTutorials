import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;

public class Pipe extends Rectangle{
	private static final long serialVersionUID = 1L;
	final static int WIDTH=100, HEIGHT=500, SPEED=10;
	Image pic;
	private boolean bottomPipe;
	URL url;

	Pipe(int a, int b, boolean bottomPipe){
		this.bottomPipe=bottomPipe;
		x=a;
		y=b;
		width=WIDTH;
		height=HEIGHT;

		if(bottomPipe){
			url = getClass().getResource("/pipe_bottom.png");
			pic=Toolkit.getDefaultToolkit().getImage(url);
		}else{
			url = getClass().getResource("/pipe_top.png");
			pic=Toolkit.getDefaultToolkit().getImage(url);
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