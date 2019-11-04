import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Font;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Random;

public class BlockBreakerPanel extends JPanel implements KeyListener{
	ArrayList<Block> blocks = new ArrayList<Block>();
	ArrayList<Block> balls = new ArrayList<Block>();
	ArrayList<Block> powerups = new ArrayList<Block>();
	Block paddle;
	Thread thread;
	Animate animate;
	int size=25;
	int score=0;
	Boolean gameStarted=false;

	BlockBreakerPanel(){
		paddle=new Block(175,480,150,25,"paddle.png");
		for(int i=0;i<8;i++){
			blocks.add(new Block(i*60+2,0,60,25,"blue.png"));
			blocks.add(new Block(i*60+2,25,60,25,"red.png"));
			blocks.add(new Block(i*60+2,50,60,25,"green.png"));
			blocks.add(new Block(i*60+2,75,60,25,"yellow.png"));
		}
		Random random=new Random();
		blocks.get(random.nextInt(32)).setPowerup(true);
		blocks.get(random.nextInt(32)).setPowerup(true);
		blocks.get(random.nextInt(32)).setPowerup(true);
		blocks.get(random.nextInt(32)).setPowerup(true);
		blocks.get(random.nextInt(32)).setPowerup(true);

		balls.add(new Block(237,437,25,25,"ball.png"));
		addKeyListener(this);
		setFocusable(true);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(Block block : blocks){
			block.draw(g,this);
		}
		for(Block ball : balls){
			ball.draw(g,this);
		}
		for(Block powerup : powerups){
			powerup.draw(g,this);
		}
		paddle.draw(g, this);

		g.drawString("Score: "+score, 10, getHeight()-10);	// Score

		if(!gameStarted){
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20));

			String pressEnter="Press Enter To Begin";
			int textWidth=g.getFontMetrics().stringWidth(pressEnter);

			g.drawString(pressEnter, (getWidth()/2)-(textWidth/2), getHeight()/2);	// Start Game
		}
	}

	public void update(){
		for(Block powerup:powerups){
			powerup.y+=1;	// drop powerup
			if(powerup.intersects(paddle)&& !powerup.getDestroyed()){
				powerup.setDestroyed(true);
				score+=25;
				balls.add(new Block(paddle.x+75,437,25,25,"ball.png"));
			}
		}

		for(Block ball:balls){
			ball.x+=ball.dx;
			if(ball.x>(getWidth()-size)&&ball.dx>0 || ball.x<0){
				ball.dx*=-1;
			}
			if(ball.y<0 || ball.intersects(paddle)){
				ball.dy*=-1;
			}
			ball.y+=ball.dy;

			for(Block block:blocks){
				if((block.left.intersects(ball)||block.right.intersects(ball)) && !block.getDestroyed()){
					ball.dx*=-1;
					block.setDestroyed(true);
					if(block.getPowerup()){
						powerups.add(new Block(block.x,block.y,25,19,"extra.png"));
					}
				}else if(ball.intersects(block) && !block.getDestroyed()){
					block.setDestroyed(true);
					score+=10;
					ball.dy*=-1;
					if(block.getPowerup()){
						powerups.add(new Block(block.x,block.y,25,19,"extra.png"));
					}
				}
			}
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e){

	}

	@Override
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			animate=new Animate(this);
			thread=new Thread(animate);
			thread.start();
			gameStarted=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_LEFT && paddle.x>0){
			paddle.x-=15;
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT && paddle.x<(getWidth()-paddle.width)){
			paddle.x+=15;
		}
	}

	@Override
	public void keyReleased(KeyEvent e){

	}
}