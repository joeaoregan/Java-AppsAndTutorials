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
import javax.swing.JFrame;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener,MouseListener,KeyListener{
	private static final long serialVersionUID = 1L;
	SoundEffect flapFX = new SoundEffect("/flap.wav");
	SoundEffect crashFX = new SoundEffect("/sadwah.wav");

	public static FlappyBird flappyBird;

	static int highScore = 0;

	public static final int WIDTH=1200,HEIGHT=800;
	public static final int GROUND_HEIGHT=120;
	public final int PIPE_SPEED=10;

	public Renderer renderer;

	public Bird bird;

	public String gameOverStr, startStr, scoreStr, highScoreStr;

	public int ticks, score, overTextWidth, startTextWidth, scoreTextWidth, highScoreTextWidth;

	public ArrayList<Pipe> pipes;

	public Random rand;

	public boolean gameOver, started;

	boolean playCrash=true;

	public FlappyBird(){
		started=false;
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

		pipes = new ArrayList<Pipe>();

		init();

		timer.start();
	}

	public void init() {
		bird = new Bird(WIDTH / 2 - Bird.WIDTH / 2, HEIGHT / 2 - Bird.HEIGHT / 2);

		gameOver = false;
		playCrash = true;
		score = 0;

		pipes.clear();
		addPipe(true);
		addPipe(true);
		addPipe(true);
		addPipe(true);
	}

	@Override
	public void actionPerformed(ActionEvent e){
		ticks++;

		if(started) {
			for(int i=0;i<pipes.size();i++){
				Pipe pipe = pipes.get(i);
				pipe.move();
			}

			if(ticks%2==0 && bird.yMotion<15){
				bird.yMotion+=2;
			}

			for(int i=0;i<pipes.size();i++){
				Pipe pipe = pipes.get(i);

				if(pipe.x + pipe.width < 0){
					pipes.remove(pipe);
					addPipe(false);
				}
			}

			bird.move();

			for(Pipe pipe:pipes){
				if(!gameOver && pipe.getBottomPipe() && bird.x+bird.width/2>pipe.x+pipe.width/2-10 && bird.x+bird.width/2<pipe.x+pipe.width/2+10){
					score++;
				}
				if(pipe.intersects(bird)){
					gameOver=true;

					if(bird.x<=pipe.x){
						bird.x=pipe.x-bird.width;
					}else{
						if(pipe.y!=0){
							bird.y=pipe.y-bird.height;
						}else if(bird.y<pipe.height){
							bird.y=pipe.height;
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
		}

		renderer.repaint();
	}

	public void addPipe(boolean start){
		int startY=100+rand.nextInt(300); //Y position for pipes
		int distance=(rand.nextInt(7)*25)+150; //Distance between pipes (after first 2)

		if(start){
			pipes.add(new Pipe(WIDTH+Pipe.WIDTH+pipes.size() * 300,HEIGHT-startY-GROUND_HEIGHT,true)); //bottom
			pipes.add(new Pipe(WIDTH+Pipe.WIDTH+(pipes.size()-1)*300,-startY,false)); //top
		} else{
			pipes.add(new Pipe(pipes.get(pipes.size()-1).x+300+distance,HEIGHT-startY-GROUND_HEIGHT,true)); //bottom
			pipes.add(new Pipe(pipes.get(pipes.size()-1).x,-startY,false)); // top
		}
	}

	public void jump(){
		//Restart Game
		if(gameOver){
			init();
		}

		if(!started){
			started=true;
		} else if(!gameOver){
			if(bird.yMotion>0){
				bird.yMotion=0;
			}

			bird.yMotion-=10;
			flapFX.play();
		}
	}

	public void repaint(Graphics g){
		//Sky
		g.setColor(Color.cyan);
		g.fillRect(0,0,WIDTH,HEIGHT);

		bird.draw(g, this);

		for(Pipe pipe:pipes){
			pipe.draw(g,this);
		}

		//Ground
		g.setColor(Color.orange);
		g.fillRect(0,HEIGHT-GROUND_HEIGHT,WIDTH,GROUND_HEIGHT);
		g.setColor(Color.green);
		g.fillRect(0,HEIGHT-GROUND_HEIGHT,WIDTH,20);

		// Text
		gameOverStr = "Game Over!";
		startStr = "Click To Start!";
		scoreStr = "Score: " + score;
		highScoreStr = "HighScore: " + highScore;

		if (!started) {
			drawText(g, startStr, 0.75, 0.5, 100,Color.white);
		}

		if (gameOver) {
			drawText(g, gameOverStr, 0.5, 0.25,100, Color.black);
			drawText(g, scoreStr, 0.5, 0.5, 50, Color.black);

			// Set the high score
			if (score > highScore) {
				highScore = score;
			}
			drawText(g, highScoreStr, 0.5, 0.75, 50, Color.black);
		}

		if (!gameOver && started) {
			drawText(g, scoreStr, 0.5, 0.933, 50, Color.white);
		}
	}

	// Draw text
	public void drawText(Graphics g, String textStr, double xPosition, double yPosition, int fontSize, Color color) {
		g.setColor(color);
		g.setFont(new Font(g.getFont().getFontName(), 1, fontSize));
		int textWidth = g.getFontMetrics().stringWidth(textStr);
		g.drawString(textStr, (int) ((WIDTH - textWidth) * xPosition), (int) ((HEIGHT * yPosition)));
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