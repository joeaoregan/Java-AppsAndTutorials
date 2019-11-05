import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener,MouseListener,KeyListener{
	final int COLUMN_SPEED=10;
	final int GROUND_HEIGHT=120;
	final int BIRD_WIDTH=28;
	final int BIRD_HEIGHT=20;

	SoundEffect flapFX = new SoundEffect("flap.wav");
	SoundEffect crashFX = new SoundEffect("sadwah.wav");

	public static FlappyBird flappyBird;

	public final int WIDTH=1200,HEIGHT=800;

	public Renderer renderer;

	public Bird bird;

	public int ticks, yMotion, score;

	public ArrayList<Pipe> columns;

	public Random rand;

	public boolean gameOver, started;

	boolean playCrash=true;

	public FlappyBird(){
		started=false;
		score=0;
		JFrame jframe = new JFrame("Flappy Bird");
		Timer timer = new Timer(20, this);

		renderer=new Renderer();
		rand=new Random();

		jframe.add(renderer);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH,HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		bird=new Bird(WIDTH/2-BIRD_WIDTH/2,HEIGHT/2-BIRD_HEIGHT/2,BIRD_WIDTH,BIRD_HEIGHT,"flappy.png");
		columns=new ArrayList<Pipe>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e){
		ticks++;

		if(started) {
			for(int i=0;i<columns.size();i++){
				Pipe column = columns.get(i);
				column.x-=COLUMN_SPEED;
			}

			if(ticks%2==0 && yMotion<15){
				yMotion+=2;
			}

			for(int i=0;i<columns.size();i++){
				Pipe column = columns.get(i);

				if(column.x + column.width < 0){
					columns.remove(column);

					//if(column.y == 0){
						addColumn(false);
					//}
				}
			}

			bird.y+=yMotion;

			for(Pipe column:columns){
				//if(column.y==0 && bird.x+bird.width/2>column.x+column.width/2-10 && bird.x+bird.width/2<column.x+column.width/2+10){
				if(column.getAlive() && bird.x+bird.width/2>column.x+column.width/2-10 && bird.x+bird.width/2<column.x+column.width/2+10){
					score++;
				}
				if(column.intersects(bird)){
					gameOver=true;

					if(bird.x<=column.x){
						bird.x=column.x-bird.width;
					}else{
						if(column.y!=0){
							bird.y=column.y-bird.height;
						}else if(bird.y<column.height){
							bird.y=column.height;
						}
					}
				}
			}

			if(bird.y>=HEIGHT-GROUND_HEIGHT-bird.height||bird.y<0){
				gameOver=true;
			}

			if(gameOver&&playCrash){
				playCrash=false;
				crashFX.play();
			}

			//if(gameOver){
			if(bird.y+yMotion>=HEIGHT-GROUND_HEIGHT){
				bird.y=HEIGHT-GROUND_HEIGHT-bird.height;
			}
		}

		renderer.repaint();
	}

	public void addColumn(boolean start){
		int space=300;
		int width=100;
		//int height=50+rand.nextInt(300);
		int startY=100+rand.nextInt(300);

		int distance=(rand.nextInt(7)*25)+150;



		if(start){
			//columns.add(new Pipe(WIDTH+width+columns.size() * 300,		HEIGHT-height-GROUND_HEIGHT,	height));
			//columns.add(new Pipe(WIDTH+width+(columns.size()-1)*300,	0,								HEIGHT-height-space,false));
			columns.add(new Pipe(WIDTH+width+columns.size() * 300,-startY+HEIGHT-GROUND_HEIGHT));
			columns.add(new Pipe(WIDTH+width+(columns.size()-1)*300,-startY,false));
		} else{
			//columns.add(new Pipe(columns.get(columns.size()-1).x+600, HEIGHT-height-GROUND_HEIGHT,height));
			//columns.add(new Pipe(columns.get(columns.size()-1).x,0,HEIGHT-height-space,false));
			columns.add(new Pipe(columns.get(columns.size()-1).x+300+distance, -startY+HEIGHT-GROUND_HEIGHT));
			columns.add(new Pipe(columns.get(columns.size()-1).x, -startY,false)); // top
		}
	}

	public void paintColumn(Graphics g, Rectangle column){
		g.setColor(Color.green.darker());
		g.fillRect(column.x,column.y,column.width,column.height);
	}

	public void jump(){
		if(gameOver){
			bird=new Bird(WIDTH/2-BIRD_WIDTH/2,HEIGHT/2-BIRD_HEIGHT/2,BIRD_WIDTH,BIRD_HEIGHT,"flappy.png");
			playCrash=true;
			columns.clear();
			yMotion=0;
			score=0;

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			gameOver=false;
		}

		if(!started){
			started=true;
		} else if(!gameOver){
			if(yMotion>0){
				yMotion=0;
			}

			yMotion-=10;
			flapFX.play();
		}
	}

	public void repaint(Graphics g){
		g.setColor(Color.cyan);
		g.fillRect(0,0,WIDTH,HEIGHT);

		//g.setColor(Color.red);
		//g.fillRect(bird.x,bird.y,bird.width,bird.height);
		bird.draw(g, this);

		for(Pipe column:columns){
			//paintColumn(g,column);
			column.draw(g,this);
		}

		g.setColor(Color.orange);
		g.fillRect(0,HEIGHT-GROUND_HEIGHT,WIDTH,GROUND_HEIGHT);

		g.setColor(Color.green);
		g.fillRect(0,HEIGHT-GROUND_HEIGHT,WIDTH,20);

		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));
		String gameOverStr="Game Over!";
		String startStr="Click To Start!";
		int overTextWidth=g.getFontMetrics().stringWidth(gameOverStr);
		int startTextWidth=g.getFontMetrics().stringWidth(startStr);

		if(!started){
			g.drawString(startStr,WIDTH/2-startTextWidth/2,HEIGHT/2-50);
		}

		if(gameOver){
			g.drawString(gameOverStr,WIDTH/2-overTextWidth/2,HEIGHT/2-50);
		}

		g.setFont(new Font(g.getFont().getFontName(), 1, 50));
		String scoreStr="Score: "+score;
		int scoreTextWidth=g.getFontMetrics().stringWidth(scoreStr);
		if(!gameOver&&started){
			g.drawString(scoreStr,WIDTH/2-scoreTextWidth/2,100);
		}
	}

	public static void main(String[] args){
		flappyBird=new FlappyBird();
	}

	@Override
	public void mouseClicked(MouseEvent e){
		jump();
	}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e){}

	@Override
	public void mouseEntered(MouseEvent e){}

	@Override
	public void mouseExited(MouseEvent e){}

	@Override
	public void keyTyped(KeyEvent e){}

	@Override
	public void keyPressed(KeyEvent e){}

	@Override
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_SPACE){
			jump();
		}
	}
}