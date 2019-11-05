
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

public class SoundEffect{
	Clip clip;

	public SoundEffect(String filename){
		try{
			InputStream audioSrc = getClass().getResourceAsStream(filename);
			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			AudioInputStream sound=AudioSystem.getAudioInputStream(bufferedIn);

			clip=AudioSystem.getClip();
			clip.open(sound);
		}catch(Exception e){

		}
	}

	public void setClip(String filename){
		try{
			File file = new File(filename);
			AudioInputStream sound=AudioSystem.getAudioInputStream(file);
			clip=AudioSystem.getClip();
			clip.open(sound);
		}catch(Exception e){

		}
	}

	public void play(){
		clip.setFramePosition(0);
		clip.start();
	}
}