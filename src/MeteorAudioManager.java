// Copyright © 2014 Esteban Valle. All rights reserved. Game based on Atari Asteroids © 1979 Atari Inc.
//        Meteors MeteorAudioManager.java
//        Created April 10th 2014 by Esteban Valle
//
//        Copyright © 2014 Esteban Valle.All rights reserved.
//
//        +1-775-351-4427
//        esteban@thevalledesign.com
//        http://facebook.com/SeniorShizzle

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Responsible for playback of all in-game sounds.
 */
public class MeteorAudioManager {

    // All sounds used in game are original works and are Copyright © 2014 Esteban Valle. All rights reserved.
    private AudioClip low;
    private AudioClip high;
    private AudioClip saucer;
    private AudioClip fire;
    private AudioClip explosionLow;
    private AudioClip explosionMedium;
    private AudioClip explosionHigh;
    private AudioClip gameOver;
    private AudioClip thrust;
    private AudioClip aTheme;
    private AudioClip aTheme240;
    // End of copyright sound definitions.

    private boolean thrustIsPlaying = false;

    private boolean aThemeIsPlaying = false;

    // This variable alternates between true and false in order
    // to switch between the low and high tones for background music
    private boolean lowHasPlayed = true;

    private static MeteorAudioManager audioManager;

    /**
     * The MeteorAudioManager class
     */
    private MeteorAudioManager() {
        // Later on I intend to apply a much more sophisticated sound stream

        String filename = "pew.wav"; // Default name while I have not created each sound
        try {
            low =               Applet.newAudioClip(getClass().getResource("low.wav"));
            high =              Applet.newAudioClip(getClass().getResource("high.wav"));
            saucer =            Applet.newAudioClip(getClass().getResource("saucer_woo.wav"));
            fire =              Applet.newAudioClip(getClass().getResource("pew.wav"));
            explosionLow =      Applet.newAudioClip(getClass().getResource("explosion.wav"));
            explosionHigh =     Applet.newAudioClip(getClass().getResource("explosion_high.wav"));
            explosionMedium =   Applet.newAudioClip(getClass().getResource("explosion_medium.wav"));
            gameOver =          Applet.newAudioClip(getClass().getResource("" + filename));
            thrust =            Applet.newAudioClip(getClass().getResource("engines.wav"));
            aTheme =            Applet.newAudioClip(getClass().getResource("meteor_a_theme.wav"));
            aTheme240 =         Applet.newAudioClip(getClass().getResource("meteor_a_theme_240.wav"));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public static MeteorAudioManager getInstance(){
        if (audioManager == null){
            audioManager = new MeteorAudioManager();
        }
        return audioManager;
    }

    /** This method plays a file of the given name, provided that the file is contained within the package.
     *
     * @param filename the name of the file to play, including extension; must exist in the package
     *                 or the method will cause an
     */
    public void playWAV(String filename) {
        try {
            AudioClip clip = Applet.newAudioClip(new URL("file:" + filename));
            clip.play();
        } catch (MalformedURLException exception) {
            System.out.println(exception);
        }
    }

    /**
     * This method controls playing the alternating low and high tones automatically.
     * Call play(); at every instance that a tone should be heard, and it will play the appropriate tone.
     */
    public void play(){
        if (lowHasPlayed){
            low.play();
         } else {
            high.play();
        }
     lowHasPlayed = !lowHasPlayed;
    }

    /**
     * This method starts playing the thrust noise, iff it is not already playing.
     */
    public void thrust(){
        if (!thrustIsPlaying) thrust.play();
        thrustIsPlaying = true;
    }

    /**
     * This method stops playing the thrust noise, iff it is already playing.
     */
    public void stopThrust(){
        if (thrustIsPlaying) thrust.stop();
        thrustIsPlaying = false;
    }


    /** Plays the low tone */
    public void low()      { low.play();     }

    /** Plays the high tone */
    public void high()     { high.play();    }

    /** Plays the saucer sound */
    public void saucer()   { saucer.loop();  }

    /** Stops the saucer music*/
    public void stopSaucer(){
        saucer.stop();
    }

    /** Plays the low explosion */
    public void explosionLow(){
        explosionLow.play();
    }

    /** Plays the high explosion */
    public void explosionHigh(){
        explosionHigh.play();
    }

    /** Plays the medium explosion */
    public void explosionMedium(){
        explosionMedium.play();
    }


    /** Plays the pew-pew laser sound */
    public void fire()     { fire.play();    }

    /** Plays the sound when the ship crashes into an asteroid */
    public void explosion()    {
        switch ((int)(Math.random() * 3)){
            case 0: explosionHigh.play();
                break;
            case 1: explosionLow.play();
                break;
            case 2: explosionMedium.play();
                break;
            default: explosion();
                break;
        }
    }

    /** Plays the game over noise */
    public void gameOver() { gameOver.play();}

    /** Loops the Meteor's 'A' Theme. */
    public void aTheme()   {
        if (!aThemeIsPlaying) {
            aTheme.loop();
            aThemeIsPlaying = true;
        }
     }

}
