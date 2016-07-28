package soundtest;

import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Igor Molodkin
 */
public class SoundTest {

    private Vector numbers;
    private Thread updater;
    private volatile boolean end_flag;
    private volatile boolean order;
    private int correct;
    private int incorrect;

    SoundTest() {
        numbers = new Vector();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException {
        SoundTest t = new SoundTest();
        Frame f = new MainFrame(t);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (InstantiationException ex) {
            Logger.getLogger(SoundTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SoundTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SoundTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(f);
        f.setVisible(true);
    }

    public void init() {
        Random rnd = new Random();
        numbers.clear();
        for (int i = 0; i < rnd.nextInt(5) + 2; i++) {  //От 2 до 7 цифр
            numbers.add(rnd.nextInt(9) + 1);
        }
        order = rnd.nextBoolean();
    }

    public Clip playMediaFile(int num) {
        Clip clip = null;
        try {
            InputStream is = ClassLoader.getSystemResourceAsStream(Integer.toString(num) + ".wav");
            System.out.println(Integer.toString(num) + ".wav");
            InputStream bufferedIn = new BufferedInputStream(is);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
            clip = (Clip) AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        return clip;
    }

    public synchronized void sayAll() {
        updater = new Thread() {
            @Override
            public void run() {
                end_flag = false;
                for (int i = 0; i < numbers.size(); i++) {
                    Clip clip = playMediaFile((int) numbers.elementAt(i));
                    try {
                        Thread.sleep(clip.getMicrosecondLength() / 1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SoundTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                end_flag = true;
            }
        };
        updater.start();
    }

    /**
     * @return the numbers
     */
    public Vector getNumbers() {
        return numbers;
    }

    /**
     * @param numbers the numbers to set
     */
    public void setNumbers(Vector numbers) {
        this.numbers = numbers;
    }

    /**
     * @return the end_flag
     */
    public boolean isEnd_flag() {
        return end_flag;
    }

    /**
     * @param end_flag the end_flag to set
     */
    public void setEnd_flag(boolean end_flag) {
        this.end_flag = end_flag;
    }

    /**
     * @return the order
     */
    public boolean isOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(boolean order) {
        this.order = order;
    }

    /**
     * @return the correct
     */
    public int getCorrect() {
        return correct;
    }

    /**
     * @param correct the correct to set
     */
    public void setCorrect(int correct) {
        this.correct = correct;
    }

    /**
     * @return the incorrect
     */
    public int getIncorrect() {
        return incorrect;
    }

    /**
     * @param incorrect the incorrect to set
     */
    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

}
