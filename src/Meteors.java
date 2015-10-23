// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Meteors extends JFrame implements MouseListener {

    // These are not global constants necessarily (although they are for now), so they are not capitalized
    public static final int windowWidth = 1024;
    public static final int windowHeight = 760;

    /** The main object singleton that handles mouse and keyboard I/O */
    public InputHandler input = InputHandler.getInstance(this);

    /** True if the mouse is in the window and we should draw a close button */
    public boolean isFocused = false;

    /** The cumulative score */
    private int score = 0;

    /** The current level */
    private int level = 1;

    /** The type of music to play */
    MeteorGame.MusicType musicType = MeteorGame.MusicType.METEOR_A_THEME;

    /** COLOR MODE -- "May trigger seizures in persons with known seizure disorders including epilepsy" */
    private boolean colorMode = false;

    /** The currently visible level/game */
    private MeteorGame currentGame;


    public static void main(String[] args){
         Meteors game = new Meteors();
    }

    /** Instantiates a Meteors game */
    public Meteors(){
        setUndecorated(true); // Get rid of that pesky top bar
        setLayout(null);
        setFocusable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setSize(windowWidth, windowHeight);
        setTitle("Meteors by Esteban Valle"); // I guess its kind of unnecessary because we don't have a title bar, but just in case

        setLocationRelativeTo(null); // Open our window in the center of the display

        addMouseListener(this); // We can receive mouse events like clicks

        getContentPane().removeAll();

        setVisible(true);

        init();

    }

    public void init(){
        Welcome hello = new Welcome(this);
        add(hello);
        hello.repaint();


        Timer splashTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loadLevel(1);
            }
        });
        splashTimer.setRepeats(false);
        splashTimer.start();
    }


    /** Called by the currentGame to switch levels*/
    public void nextLevel(){
        level++;
        loadLevel(level);
    }

    /** Loads the level specified onto the screen, including preserving the score, et cetera
     * and calculating the proper number of meteors to begin with.
     * @param level the level to load
     */
    public void loadLevel(int level){

        int lives;

        if (currentGame != null){
            System.out.println("Starting level "+ level);
            score = currentGame.getScore();
            lives = currentGame.getLives();
        }
        else {
            System.out.println("No game started");
            lives = 4;
            score = 0; // Set this to 10,000 to start loading small saucers right away
        }

        // Clear the screen for a moment
        getContentPane().removeAll();

        currentGame = null;

        // Populate the game with an appropriate number of asteroids, et cetera (inherited from original game)
        switch (level){
            case 0:
                currentGame = new MeteorGame(1, this, score, lives,  musicType);
                break;
            case 1:
                currentGame = new MeteorGame(4, this, score, lives,  musicType);
                break;
            case 2:
                currentGame = new MeteorGame(6, this, score, lives,  musicType);
                break;
            case 3:
                currentGame = new MeteorGame(8, this, score, lives,  musicType);
                break;
            case 4:
                currentGame = new MeteorGame(10, this, score, lives, musicType);
                break;
            case 5:
                currentGame = new MeteorGame(11, this, score, lives, musicType);
                break;
            default:
                currentGame = new MeteorGame(11, this, score, lives, musicType);
                break;
        }

        // Add the panel to the current JFrame
        add(currentGame);

        // Run the game
        currentGame.run();
    }

    /** Removes the game from the display and adds the "EndGame" slide */
    public void gameOver(){
        System.out.println("Game Over");
        currentGame = null;
        getContentPane().removeAll();
        getContentPane().repaint();
        EndGame endGame = new EndGame(this);
        getContentPane().add(endGame);
        endGame.setVisible(true);
        endGame.repaint();

        Timer replayTimer = new Timer(3500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("New game starting");
                restart();
            }
        });
        replayTimer.setRepeats(false);
        replayTimer.start();
    }

    public void restart(){
        level = 1;
        score = 0;

        init();
    }


    /** Returns the main game's ship's x coordinate */
    public int getShipX(){
        if (currentGame != null) return currentGame.shipX();
        return 0;
    }

    /**
     * Returns the main game's ship's y coordinate
     */
    public int getShipY(){
        if (currentGame != null) return currentGame.shipY();
        return 0;
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point mouseClick = mouseEvent.getPoint();

        if (mouseClick.x < 25 && mouseClick.y < 25) System.exit(0);

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        isFocused = true;
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        isFocused = false;

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        System.out.println("mouse down");

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        Point mouseClick = mouseEvent.getPoint();
        if (mouseClick.x > 25 && mouseClick.y > 25)
                setLocation(mouseEvent.getXOnScreen() - windowWidth / 2, mouseEvent.getYOnScreen() - windowHeight / 2);

    }


    public InputHandler getInputHandler() {
        return input;
    }

    public boolean isColorMode(){
        return colorMode;
    }







} // End of the class Meteors
