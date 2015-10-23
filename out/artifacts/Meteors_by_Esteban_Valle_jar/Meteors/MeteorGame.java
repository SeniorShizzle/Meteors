// Copyright © 2014 Esteban Valle. All rights reserved. Game based on Atari Asteroids © 1979 Atari Inc.
//        Meteors MeteorGame.java
//        Created April 10th 2014 by Esteban Valle
//
//        Copyright © 2014 Esteban Valle. All rights reserved.
//        Simple JAVA game engine based on tutorial from http://compsci.ca/v3/viewtopic.php?t=25991
//
//        +1-775-351-4427
//        esteban@thevalledesign.com
//        http://facebook.com/SeniorShizzle


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 * The main GUI class. Interprets I/O data and controls chief game components
 */
public class MeteorGame extends JPanel {

    /** The maximum speed of the ship and other objects */
    public static final int MAX_SPEED = 10;

    /** The gray color for the meteors and ship */
    private Color METEOR_GRAY = new Color(222,222,222);

    /** The delay in frames between background music tones */
    private int musicDelay = 60;

    public static final int windowWidth = Meteors.windowWidth;
    public static final int windowHeight = Meteors.windowHeight;

    private boolean hasLifted = true;

    /** TRUE if the ship can fire a projectile. FALSE if it cannot. Used primarily with the fireTimer*/
    private boolean canFire = true;

    /** Sets whether or not to display the main ship */
    private boolean shipDisplayed = true;

    /* The types of music for the game */
    enum MusicType{
        METEOR_TRADITIONAL_MUSIC, METEOR_A_THEME
    }

    /** The style of music that is played in the level */
    private MusicType musicType;

    /** The insets from the side of the window to the content portion */
    private Insets insets;

    /** TRUE for a burst of color. Assigned to value of parent "Meteors" object */
    public boolean colorMode;

    /** The main apparatus for detecting and reporting keyboard input */
    private InputHandler input;

    /** Tracks and resets every 3 frames for timing and processor efficiency. */
    private int threeFrameCounter = 0;

    /** Tracks the number of frames for timing. Reset every 1,000,000 frames */
    private int frameCounter = 0;

    private static final int shipRadius = 10;   // in pixels

    private int projectilePersistance = 1000;   // in milliseconds
    private double projectileVelocity = 15;     // in pixels per frame
    private int projectileRateDelay = 100;      // in milliseconds

    /** The number of meteors for this level */
    private final int numberOfMeteors;

    /** God mode */
    private boolean isGod = false;

    /** RED mode */
    private boolean redMode = false;

    /** The system time in milliseconds that the game was first initialized */
    private long initTime;

    /** The main vectorFont font */
    private Font vectorFont;

    /** The points threshold (increments of 10,000) for gaining ships with points */
    private int threshold; // Set to add a life every 10000 points

    /** A list of projectiles launched by the main ship */
    private LinkedList<Projectile> projectiles = new LinkedList<Projectile>();

    /** A list of explosions to draw on screen */
    private LinkedList<Explosion> explosions = new LinkedList<Explosion>();

    /** The object managing the Meteor objects */
    private MeteorManager meteorManager = new MeteorManager(this);

    /** Responsible for playing audio throughout the game */
    private MeteorAudioManager audioManager = MeteorAudioManager.getInstance();

    /** TRUE if the level is over */
    private boolean levelCompleted = false;

    /** The main ship */
    public Ship ship;

    /** The number of lives of the current MeteorGame */
    private int lives = 4;

    /** The score of the current MeteorGame */
    private int score = 0;

    /** TRUE if the object has already initialized */
    private boolean hasInitialized = false;

    /** The parent Meteors object that instantiated this class */
    Meteors parent;

    /** The main run timer */
    private Timer runTimer;

    /** The timer that controls the appearance of the saucer */
    private Timer saucerTimer;

    /** The saucer that comes out on the display */
    Saucer saucer;

    /** TRUE if the game play has started */
    private boolean gameHasStarted = false;


    /** The starting x positions for the first 11 meteors */
    private int[] defaultStartingX = new int[]{0, 100, 0, 500, windowWidth, windowWidth - 100, windowWidth, windowWidth / 2, windowWidth / 2, 300, 500};

    /** The starting y positions for the first 11 meteors */
    private int[] defaultStartingY = new int[]{windowHeight, 100, 0, windowHeight, windowHeight - 50, 0, 50, windowHeight, 10, 0, 0};

    public MeteorGame(int numberOfMeteors, Meteors parent){
         this(numberOfMeteors, parent, 0, 4, MusicType.METEOR_A_THEME);
    }

    /**
     * Instantiates a MeteorGame with the following conditions
     * @param numberOfMeteors the number of meteors to begin with
     * @param parent the parent, used for scoring and clearing the level
     * @param score the current score to begin with
     * @param musicType the background music type
     */
    public MeteorGame(int numberOfMeteors, Meteors parent, int score, int lives, MusicType musicType){
        this.parent = parent;
        this.numberOfMeteors = numberOfMeteors;

        ship = Ship.getInstance(); // Initialize the main ship
        ship.setRadius(shipRadius);

        this.lives = lives;

        this.score = score;
        this.musicType = musicType;

        this.input = parent.getInputHandler();
    }

    /**
     * The main run loop. Controls the timing of the game.
     */
    public void run() {
        if (!hasInitialized) initialize(); // We can call initialize from an outside method if we want

        runTimer = new Timer(25, runGame);
        runTimer.setRepeats(true);
        runTimer.start();
    }

    /** The main loop that runs the game. Called every 25 milliseconds to refresh the frame */
    ActionListener runGame = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            threeFrameCounter++;
            frameCounter++;
            update();
            repaint();

            if (threeFrameCounter > 3) threeFrameCounter = 0;
            if (frameCounter > 1000000) frameCounter = 1; // reindex to 1 so we don't prematurely trip the sound
            if (frameCounter % musicDelay == 0 && musicType == MusicType.METEOR_TRADITIONAL_MUSIC) {
                audioManager.play(); // Plays the alternating low and high notes automatically
            }
        }
    };

    /**
     * This method will set up everything need for the game to run
     */
    void initialize(){

        hasInitialized = true;

        if (musicType == MusicType.METEOR_A_THEME) audioManager.aTheme();

        colorMode = parent.isColorMode(); // Not for the faint of heart

        initTime = System.currentTimeMillis(); // Used for calculating timing and other things
        setSize(windowWidth, windowHeight);
        setLayout(null);
        setBackground(Color.BLACK);

        threshold = (score - score % 10000) + 10000; // Sets the score to the next factor of 10000

        //setVisible(true);
        //setFocusable(true);
        //requestFocusInWindow();

        // Instantiating files must use a try block to catch any errors
        try {
            vectorFont = Font.createFont(Font.TRUETYPE_FONT, new File("Vectorb.ttf")).deriveFont(24f);

            // We set up the font and adjust the tracking so that it looks neater
            Map<java.awt.font.TextAttribute, Object> attributes = new HashMap<java.awt.font.TextAttribute, Object>();
            attributes.put(java.awt.font.TextAttribute.TRACKING, 0.2); // Adjust the tracking of the font
            vectorFont = vectorFont.deriveFont(attributes);

        } catch (Exception e){
            System.out.println(e.getMessage());
            vectorFont = Font.getFont("Helvetica Nueue");
        }




        insets = getInsets();
        setSize(insets.left + windowWidth + insets.right, insets.top + windowHeight + insets.bottom);

        //backBuffer = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
        //input = InputHandler.getInstance(this); // Manages the input from the keyboard and mouse. Singleton object.

        // The timer that instantiates the saucers
        saucerTimer = new Timer((int)(Math.random() * 5000 + 8000), createSaucerListener);
        saucerTimer.setRepeats(true);
        saucerTimer.start();


        // Brief delay while we add the meteors
        Timer addMeteorsOnDelay = new Timer(numberOfMeteors > 4 ? 2000 : 1, addMeteors);
        addMeteorsOnDelay.setRepeats(false);
        addMeteorsOnDelay.start();


    }

    /** Draws the meteors to the display. The delay is used to create a gap between levels */
    ActionListener addMeteors = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for (int i = 0; i < numberOfMeteors; i++) {
                if (i < 11) {
                    meteorManager.addMeteor(defaultStartingX[i], defaultStartingY[i], 6.28 * Math.random(), 3);
                } else {
                    meteorManager.addMeteor((int) (600 * Math.random()), (int) (600 * Math.random()), 6.28 * Math.random(), 3);
                }
            }
            gameHasStarted = true;
        }
    };

    /** Creates a new saucer. Delegate actionListener */
    ActionListener createSaucerListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            createSaucer(); // We must separate this method because we cannot call "this" from the ActionListener object
        }
    };

    /** Instantiates a new saucer on the screen, of the appropriate size */
    public void createSaucer(){
        if (saucer == null) {
            if (score >= 10000){
                saucer = new Saucer(Saucer.SaucerStyle.LITTLE_SAUCER, this);
            } else {
                if ((int)(Math.random() * 10) < 3){ // Before 10,000 points, we want a ~20% chance of getting a small saucer
                    saucer = new Saucer(Saucer.SaucerStyle.LITTLE_SAUCER, this);
                } else {
                    saucer = new Saucer(Saucer.SaucerStyle.BIG_SAUCER, this);
                }
            }
        }
    }

    /**
     * This method polls for input from InputHandler and updates the respective components
     * including the ship and projectiles
     */
    void update() {

        // Check for no-lives and end-level
        if (lives <= 0 && gameHasStarted){
            System.out.println("Out of Lives");
            Timer endGameTimer = new Timer(2500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (parent != null ) parent.gameOver();
                    endLevel();
                }
            });

            endGameTimer.setRepeats(false);
            endGameTimer.start();
        }

        if (isLevelCompleted() && gameHasStarted){
            System.out.println("Level Completed");
            parent.nextLevel();
            endLevel();
        }

        /* ------------
         * THE MAIN INPUT LOOP
         */
        if (input.isKeyDown(KeyEvent.VK_RIGHT) || input.isKeyDown(KeyEvent.VK_LEFT)) {
            if (input.isKeyDown(KeyEvent.VK_RIGHT)) ship.right();

            if (input.isKeyDown(KeyEvent.VK_LEFT)) ship.left();

        } else{
            ship.notTurning(); // This way we still decelerate turning even when moving forward.
                               // Although rotational acceleration and momentum are not part of
                               // the original design, I liked the way they handled better.
        }

        if (input.isKeyDown(KeyEvent.VK_UP)){
            audioManager.thrust();        // The thrust sound is causing a huge amount of overhead and lag with the current audio manager
            ship.thrust();

        } else {
            ship.coast(); // Controls the deceleration automatically
            audioManager.stopThrust();

        }

        if (input.isKeyDown(KeyEvent.VK_SHIFT)){
            if (!ship.isHyperJumped()) briefPause(1000);
            ship.warp();
        }

        if (input.isKeyDown(KeyEvent.VK_SPACE)){
            if ((canFire && hasLifted && shipDisplayed) || (isGod && shipDisplayed)) {
                if (projectiles.size() < 5 || isGod){ // We are only allowed to have 4 projectiles on the screen (inherited from original game)
                    audioManager.fire();
                    projectiles.add(new Projectile((int) ship.x(), (int) ship.y(), ship.xVelocity(), ship.yVelocity(), ship.rotation(), projectileVelocity, projectilePersistance));
                }
                hasLifted = false;
                canFire = false;

                Timer fireTimer = new Timer(projectileRateDelay, fireTimerTrigger);
                fireTimer.setRepeats(false);
                fireTimer.start();
            }

        } else {
            hasLifted = true;
        }
        /*
         * END MAIN INPUT LOOP
         */


        // Update the main ship and the saucer if on-screen
        ship.update();
        if (saucer != null) saucer.update();

        if (!isGod && meteorManager.areaIntersectsWithMeteorAndDestroy(ship.getShipPolygon())){
            isGod = true;
            addExplosion(shipX(), shipY());
            looseLife();

            Timer invulnerable = new Timer(4500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    isGod = false;
                }
            });
            invulnerable.setRepeats(false);
            invulnerable.start();

        }

        if (saucer != null) {
            // Test for impacts between the saucer and the meteors
            if (meteorManager.areaIntersectsWithMeteorAndDestroy(saucer.saucerPolygon())) {
                System.out.println("Saucer crashed into meteor");
                addExplosion(saucer.getX(), saucer.getY());
                saucer.destroy();
                saucer = null;
            }
        }

        if (saucer != null) {
            if (saucer.isExpired()) {
                saucer.destroy();
                saucer = null;
            }
        }

        // We have to have two if statements because the first condition could set the saucer to null
        if (saucer != null) {

            // Test for impacts between the ship and the saucer
            Area testArea = new Area(saucer.saucerPolygon());
            testArea.intersect(new Area(ship.getShipPolygon()));  // We intersect the area of the shape and the saucer

            if (!isGod && !testArea.isEmpty()) {
                System.out.println("Saucer crashed into ship");
                addExplosion(shipX(), shipY());
                looseLife();

                isGod = true;
                Timer invulnerable = new Timer(4500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        isGod = false;
                    }
                });
                invulnerable.setRepeats(false);
                invulnerable.start();
            }

        }

        if (threeFrameCounter == 1) overhead(); // Does the work not needed in every frame

    }

    /**
     * This method is triggered by the fire timer, which prevents the ship from rapid-firing
     */
    ActionListener fireTimerTrigger = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            canFire = true; // Allows the ship to fire again
        }
    };



    /**
     * This method will draw everything to the screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Mandatory call

        Graphics2D g2d = (Graphics2D)g;  // Cast our graphics context to a Graphics2D for better drawing
        g2d.setFont(vectorFont);         // Set the font to be the neat custom one


        // Draw the score and title on the display
        g2d.setColor(redMode ? Color.RED : METEOR_GRAY);
        if (colorMode) {
            //to get rainbow, pastel colors
            Random random = new Random();
            final float hue = random.nextFloat();
            final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
            final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
            g2d.setColor(Color.getHSBColor(hue, saturation, luminance));
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawString("" + score, 40, 50);
        g2d.setFont(vectorFont.deriveFont(10f));
        g2d.drawString("Meteors™ by Esteban Valle", windowWidth / 2 - 120, windowHeight - 50);


        if (redMode) g2d.setColor(Color.RED.darker());
        // Draw the ship on the display.
        if (shipDisplayed && lives > 0) g2d.drawPolygon(ship.getShipPolygon());

        // Draw the explosions on the screen
        g2d.setStroke(new BasicStroke(1.5f));
        Iterator<Explosion> eit = explosions.iterator();
        while (eit.hasNext()){
            Explosion explosion = eit.next();
            explosion.update();
            if (!explosion.isExpired()) {
                for (Point p : explosion.getParticles()) {
                    g2d.drawLine(p.x, p.y, p.x, p.y);
                }
            } else {
               eit.remove();
            }
        }

        g2d.setStroke(new BasicStroke(1));


        // Draw the saucer on the display.
        if (saucer != null) g2d.draw(saucer.saucerPolygon());

        if (ship.isThrusting() && shipDisplayed) { // Draw the frame 2 out of 3 frames to create a flicker effect
            if (threeFrameCounter < 3)
                g2d.drawPolyline(ship.getFlameXValues(), ship.getFlameYValues(), 3); // Draw the flame
        }

        for (int i = 0; i < lives - 1; i++) { // Draw the lives onto the screen.
            g2d.drawPolygon(ship.getShipAt(50 + i * 25, 80, -1.571));
        }

        // Draws each meteor
        if (!meteorManager.allMeteorsDestroyed()) {
            for (Polygon poly : meteorManager.getMeteorPolygons()) {
                if (colorMode){
                    //to get rainbow, pastel colors
                    Random random = new Random();
                    final float hue = random.nextFloat();
                    final float saturation = 0.9f; //1.0 for brilliant
                    final float luminance  = 1.0f;  //1.0 for brighter
                    g2d.setColor(Color.getHSBColor(hue, saturation, luminance));
                }


                g2d.drawPolygon(poly);
            }
        }

        /* ENABLE TO DRAW A GRID */
        if (false) {
            // Draw a grid on the display
            g2d.setStroke(new BasicStroke(.1f));
            for (int i = 50; i < windowWidth; i += 50) {
                g2d.drawLine(i, 0, i, windowHeight);
            }
            for (int i = 50; i < windowHeight; i += 50) {
                g2d.drawLine(0, i, windowWidth, i);
            }
        }



        g2d.setColor(Color.WHITE); // The projectiles are white to make them stand out more, like the original game
        g2d.setStroke(new BasicStroke(3));

        // Find and remove expired ship's projectiles
        // Hit test and update the ship's projectiles
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile bullet = it.next();
            bullet.update();                    // Although perhaps less proper to include the update code here in the draw method
                                                // it is done for efficiency, rather than having to iterate through each bullet three
                                                // times, I can iterate through them only once and do three things

            // Testing against each meteor
            if (!bullet.isExpired && meteorManager.isHitAndDestroy(bullet.x, bullet.y)) {
                bullet.isExpired = true;
            }

            // Hit test the ship's bullets with the saucer
            if (saucer != null) {
                if (!bullet.isExpired && saucer.saucerPolygon().contains(bullet.x, bullet.y)){
                    addScore(saucer.getStyle() == Saucer.SaucerStyle.LITTLE_SAUCER ? 1000 : 200);
                    addExplosion(saucer.getX(), saucer.getY());
                    saucer.destroy();
                    saucer = null;
                }
            }

            // Draws the bullet if it is not expired. If it is, remove it
            if (bullet.isExpired) {
                it.remove();
            } else {
                g2d.drawLine((int) bullet.x, (int) bullet.y, (int) bullet.x, (int) bullet.y);
            }

        }

        // Tests the saucer's projectiles
        if (saucer != null){
            Iterator<Projectile> bit = saucer.getProjectiles().iterator();
            while (bit.hasNext()){
                Projectile bullet = bit.next();
                if (!bullet.isExpired && meteorManager.isHitAndDestroy(bullet.x, bullet.y)) {
                    bullet.isExpired = true;
                }
                // Tests the saucer's bullets against the ship
                if (!isGod && !bullet.isExpired && ship.getShipPolygon().contains(bullet.x, bullet.y)){
                    System.out.println("Saucer destroyed ship");
                    addExplosion(shipX(), shipY());
                    looseLife();

                    isGod = true;
                    Timer invulnerable = new Timer(4500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            isGod = false;
                        }
                    });
                    invulnerable.setRepeats(false);
                    invulnerable.start();
                }
                if (bullet.isExpired){
                    bit.remove();
                } else {
                    bullet.update();
                    g2d.drawLine((int) bullet.x, (int) bullet.y, (int) bullet.x, (int) bullet.y);
                }

            }
        }

        g2d.setStroke(new BasicStroke(1));

        // Draws the close box "X" in the upper left-hand corner
        if (parent.isFocused){
            g2d.drawLine(10, 10, 20, 20);
            g2d.drawLine(20, 10, 10, 20);
            g2d.setColor(Color.RED.darker());
            g2d.drawRect(5,5,20,20);
        }


    } // END of paintComponent()


    /** This method contains items that should be invoked periodically, rather than in every frame */
    private void overhead(){

        if (score >= threshold){ // Add lives every 10,000 points
            System.out.println("1UP");
            threshold += 10000;
            lives++;
        }

        // rev209()
        if (ship.rotationAccel() > .49) {
            if (!isGod) {
                Timer testModeTimer = new Timer(2000, rev209);
                testModeTimer.setRepeats(false);
                testModeTimer.start();
            }
        }

        // Calculates the appropriate delay for the background music to speed up as the game progresses
        if (musicType == MusicType.METEOR_TRADITIONAL_MUSIC) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - initTime > 20000) {
                musicDelay = 40;
                if (currentTime - initTime > 40000) {
                    musicDelay = 30;
                    if (currentTime - initTime > 60000) {
                        musicDelay = 20;
                        if (currentTime - initTime > 70000) {
                            musicDelay = 15;
                        }

                    }

                }

            }
        }

    }   // End of overhead()


    /** Briefly removes the ship from the display for 'delayInMilliseconds' milliseconds, and sets it to be invulnerable */
    public void briefPause(int delayInMilliseconds){
        shipDisplayed = false;
        isGod = true;
        ship.setShipUpdates(false);
        Timer timeOut = new Timer(delayInMilliseconds, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ship.setShipUpdates(true);
                isGod = false;
                shipDisplayed = true;

                if (ship.isCrashedOnReEntry()){
                    addExplosion(shipX(), shipY());
                    looseLife();
                }
            }
        });

        timeOut.setRepeats(false);
        timeOut.start();

    }

    /** Removes a life credit, initiates a 3 second brief pause, and resets the ship's location to the middle */
    public void looseLife(){
            briefPause(3000);
            lives--;
            ship.setX(windowWidth / 2);
            ship.setY(windowHeight / 2);

    }

    /** Destructs the level and stops the main timer */
    public void endLevel(){
        if (saucer != null){
            saucer.destroy();
            saucer = null;
        }
        gameHasStarted = false;
        saucerTimer.stop();
        runTimer.stop();
        this.parent = null;

    }

    /**
     * Called by "children" and other methods in this class to add an explosion to the list
     */
    public void addExplosion(int x, int y) {
        explosions.add(new Explosion(x, y));
        audioManager.explosion();
    }


    /**
     * Places the game into a test mode. The ship will become invincible.
     */
    ActionListener rev209 = new ActionListener() { //
        public void actionPerformed(ActionEvent event) {
            if (input.isKeyDown(KeyEvent.VK_DOWN) && input.isKeyDown(KeyEvent.VK_RIGHT)) {
                isGod = true;
                redMode = true;
                projectileRateDelay = 10;
                System.out.println("i am a god");
                ship.setX(windowWidth  / 2);
                ship.setY(windowHeight / 2);
                ship.setxVelocity(0);
                ship.setyVelocity(0);
            }
        }
    };

    /** Returns TRUE if all the meteors and saucers have been destroyed */
    public boolean isLevelCompleted(){
        return meteorManager.allMeteorsDestroyed() && saucer == null;
    }

    /** Returns the main ship's X position */
    public int shipX() {
        return (int)ship.x();
    }

    /** Returns the main ship's Y position */
    public int shipY() {
        return (int)ship.y();
    }

    /** Returns TRUE if the ship is visible */
    public boolean isShipDisplayed(){
        return shipDisplayed;
    }

    /** Returns the main ship's rotation in radians */
    public double shipRotation(){
        return ship.rotation();
    }

    public int getScore() {
        return score;
    }

    public void addScore(int deltaScore) {
        this.score += deltaScore;
    }

    public int getLives(){
        return lives;
    }

    public static int getShipRadius() {
        return shipRadius;
    }

}

