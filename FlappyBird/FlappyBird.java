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

public class FlappyBird extends JPanel implements ActionListener, MouseListener, KeyListener {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH=1200,HEIGHT=800,GROUND_HEIGHT=120;

	public static FlappyBird flappyBird;

	Cloud cloud1 = new Cloud(); // Same 3 clouds resized and repositioned after moving off screen
	Cloud cloud2 = new Cloud();
	Cloud cloud3 = new Cloud();

	static int highScoreEasy = 0, highScoreMedium = 0, highScoreHard = 0;

	Renderer renderer;

	Bird bird;

	String gameOverStr, startStr, scoreStr, highScoreStr;

	int ticks, lastTicks, score, overTextWidth, startTextWidth, scoreTextWidth, highScoreTextWidth, difficulty=1;

	ArrayList<Pipe> pipes;

	Random rand;

	boolean gameOver, started, playCrash;

	public FlappyBird(){
		JFrame jframe = new JFrame("Flappy Bird (Joe O'Regan)");
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

		started = false;

		pipes = new ArrayList<Pipe>();

		init();

		timer.start();
	}

	public void init() {
		lastTicks=0;
		SoundEffect.crashFX.stop();
		bird = new Bird(WIDTH / 2 - Bird.WIDTH / 2, HEIGHT / 2 - Bird.HEIGHT / 2);
		gameOver = false;
		playCrash = true;
		startPipes();
	}

 	public void startPipes(){
		ticks=0;
		lastTicks=0;
		score = 0;

		pipes.clear();

		addPipe(difficulty, true);
		addPipe(difficulty, true);
		addPipe(difficulty, true);
		addPipe(difficulty, true);
	}

	@Override
	public void actionPerformed(ActionEvent e){
		ticks++;

		cloud1.move();
		cloud2.move();
		cloud3.move();

		if(!started) {
			if((ticks%33==0||ticks<=1)) {
				bird.jump();
			} else if (ticks % 3 == 0) {
				bird.fall();
			}
			bird.move();
		}

		if(started) {
			for(int i=0;i<pipes.size();i++){
				Pipe pipe = pipes.get(i);
				pipe.move();
			}

			if(ticks%2==0){
				bird.fall();
			}

			for(int i=0;i<pipes.size();i++){
				Pipe pipe = pipes.get(i);

				if(pipe.x + pipe.width < 0){
					pipes.remove(pipe);
					addPipe(difficulty,false);
				}
			}

			bird.move();

			for(Pipe pipe:pipes){
				if(!gameOver && pipe.getBottomPipe() && bird.x+bird.width/2>pipe.x+pipe.width/2-10 && bird.x+bird.width/2<pipe.x+pipe.width/2+10){
					score++;//Increment score for 1 pipe only
				}

				if(pipe.intersects(bird)){
					gameOver=true;//Crash

					if(bird.x<=pipe.x){
						bird.x=pipe.x-bird.width;//Bird will not overlap with pipe
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
				SoundEffect.crashFX.play();
			}

			if(gameOver && lastTicks==0){
				lastTicks=ticks;
			}
		}

		renderer.repaint();
	}

	public void addPipe(int difficultyLevel,boolean start){
		int startY=100+rand.nextInt(300); //Y position for pipes
		int distance=(rand.nextInt(7)*25)+150; //Distance between pipes (after first 2)

		if(start){
			pipes.add(new Pipe(WIDTH+Pipe.WIDTH+pipes.size() * 300, HEIGHT -startY -GROUND_HEIGHT +((difficultyLevel==0)?60:(difficultyLevel==1)?0:-70),true)); //bottom
			pipes.add(new Pipe(WIDTH+Pipe.WIDTH+(pipes.size()-1)*300, -startY,false)); //top
		} else{
			pipes.add(new Pipe(pipes.get(pipes.size()-1).x+300+distance, HEIGHT-startY -GROUND_HEIGHT+((difficultyLevel==0)?60:(difficultyLevel==1)?0:-70),true)); //bottom
			pipes.add(new Pipe(pipes.get(pipes.size()-1).x, -startY,false)); // top
		}
	}

	public void jump(){
		if(!started){
			started=true;
		} else if(!gameOver){
			bird.jump();
		}
	}

	public void repaint(Graphics g){
		//Sky
		g.setColor(Color.cyan);
		g.fillRect(0,0,WIDTH,HEIGHT);

		cloud1.draw(g,this);
		cloud2.draw(g,this);
		cloud3.draw(g,this);

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
		scoreStr = "Your Score: " + score;
		highScoreStr = "HighScores: ";

		if (!started) {
			drawText(g, startStr, 0.5, 0.5, 100, Color.white);
		}

		if (!started || gameOver) {
			drawText(g, "Difficulty: 1. Easy 2. Medium 3. Hard", 0.9, 0.933, 50, Color.white);
		}

		bird.draw(g, this);

		if (gameOver) {
			drawText(g, gameOverStr, 0.5, 0.25, 100, Color.black);
			drawText(g, scoreStr, 0.5, 0.5, 50, Color.black);
			setHighScores();
			drawText(g, highScoreStr, 0.5, 0.75, 50, Color.black);

			drawText(g, "Easy: " + highScoreEasy, 0.5, 0.775, 25, Color.black);
			drawText(g, "Medium: " + highScoreMedium, 0.5, 0.8, 25, Color.black);
			drawText(g, "Hard:" + highScoreHard, 0.5, 0.825, 25, Color.black);
		}

		if (!gameOver && started) {
			drawText(g, scoreStr, 0.5, 0.933, 50, Color.white);
			drawText(g, "Pause: Esc/P", 0.966, 0.933, 50, Color.white);
		}

		drawText(g, (difficulty==0)?"Easy":(difficulty==1)?"Medium":"Hard", 0.025, 0.933, 50, Color.white);
	}

	public void setHighScores(){
		if(difficulty==0){
			highScoreEasy = (score>highScoreEasy)?score:highScoreEasy; // Set the high score for Easy difficulty
		}else if(difficulty==1){
			highScoreMedium = (score>highScoreMedium)?score:highScoreMedium; // Set the high score for Medium difficulty
		}else if(difficulty==2){
			highScoreHard = (score>highScoreHard)?score:highScoreHard; // Set the high score for Hard difficulty
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
		actionButton();
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
			actionButton();
		}

		if((e.getKeyCode()==KeyEvent.VK_ESCAPE || e.getKeyCode()==KeyEvent.VK_P) && !gameOver){
			started=false;
		}

		if((e.getKeyCode()==KeyEvent.VK_1 || e.getKeyCode()==KeyEvent.VK_NUMPAD1) && (gameOver || !started)){
			difficulty=0;
			startPipes();
		} else if((e.getKeyCode()==KeyEvent.VK_2 || e.getKeyCode()==KeyEvent.VK_NUMPAD2) && (gameOver || !started)){
			difficulty=1;
			startPipes();
		} else if((e.getKeyCode()==KeyEvent.VK_3 || e.getKeyCode()==KeyEvent.VK_NUMPAD3) && (gameOver || !started)){
			difficulty=2;
			startPipes();
		}
	}

	public void actionButton(){
		if(gameOver){
			if(ticks>(lastTicks+50)){ //Delay before restarting
				init();	//Start or Restart Game
			}
		}else{
			jump(); // Bird Jumps
		}
	}
}