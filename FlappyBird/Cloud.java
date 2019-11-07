import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Random;

public class Cloud extends Rectangle{
	private static final long serialVersionUID = 1L;
	final static int WIDTH=300, HEIGHT=180;
	Image pic;

	Random random=new Random();
	URL url;

	int speed;

	Cloud(){
		speed=(int) (Math.random()*5)+1;

		x=random.nextInt(12) * 100;
		y=random.nextInt(25) * 20;

		width=(int)((WIDTH/2) + ((WIDTH/2)*(random.nextInt(7))/5));
		height=(int)((HEIGHT/2) + ((HEIGHT/2)*(random.nextInt(7))/5));


		url = getClass().getResource("/cloud.png");
		pic=Toolkit.getDefaultToolkit().getImage(url);
	}

	public void move(){
		x-=speed;

		if(x<=-width){
			x=FlappyBird.WIDTH + (random.nextInt(5) * 50);
			y=random.nextInt(25) * 20;

			width=(int)((WIDTH/2) + ((WIDTH/2)*(random.nextInt(7))/5));
			height=(int)((HEIGHT/2) + ((HEIGHT/2)*(random.nextInt(7))/5));
		}
	}

	public void draw(Graphics g, Component c){
		g.drawImage(pic,x,y,width,height,c);
	}
}