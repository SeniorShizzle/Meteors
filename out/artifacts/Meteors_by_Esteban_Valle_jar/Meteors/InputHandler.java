//  Game based on Atari Asteroids © 1979 Atari Inc.
//        Meteors InputHandler.java
//        Created April 10th 2014.
//
//        Design and naming based on tutorial at http://compsci.ca/v3/viewtopic.php?t=25991
//
//        +1-775-351-4427
//        esteban@thevalledesign.com
//        http://facebook.com/SeniorShizzle

import java.awt.Component;
import java.awt.event.*;


/**
 * The InputHandler class monitors the keyboard for the state of each key.
 * The up/down state of each key is tracked as a boolean and can be referenced by
 * passing the
 */
public final class InputHandler implements KeyListener {

    /** The singleton object */
    private static InputHandler instance;



    /** This array of booleans tracks the up/down state of each key
     *  TRUE = key pressed; FALSE = key not pressed.
     */
    private boolean[] keys = new boolean[256];

    public static InputHandler getInstance(Component c){
        if (instance == null){
            instance = new InputHandler(c);
        }
        return instance;
    }


    /**
     * Assigns the newly created InputHandler to a Component
     *
     * @param c Component to get input from. Typically called as InputHandler(this);
     */
    private InputHandler(Component c){

        System.out.println("InputHandler Instantiated");
        c.addKeyListener(this);
    }



    /**
     * Returns TRUE if the key at keyCode is currently pressed, FALSE if it is not currently pressed
     * Most effectively used with keyCode values as KeyEvent constants for each key (e.g. KeyEvent.VK_SHIFT).
     *
     * @param keyCode and integer 0 to 255; the value of the key to check
     * @return TRUE if the key is pressed currently. FALSE otherwise
     */
    public boolean isKeyDown(int keyCode) {
        if (keyCode > 0 && keyCode < 256) {
            return keys[keyCode];
        }
        return false;
    }

    /**
     * Called when a key is pressed while the component is focused
     *
     * @param e KeyEvent sent by the component
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() > 0 && e.getKeyCode() < 256) {
            keys[e.getKeyCode()] = true;
        }
    }

    /**
     * Called when a key is released while the component is focused
     *
     * @param e KeyEvent sent by the component
     */
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() > 0 && e.getKeyCode() < 256) {
            keys[e.getKeyCode()] = false;
        }
    }

    /**
     * Not implemented but required by interface
     */
    public void keyTyped(KeyEvent e) {

    }





}