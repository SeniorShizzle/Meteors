// Copyright © 2014 Esteban Valle. All rights reserved. Game based on Atari Asteroids © 1979 Atari Inc.
//        Meteors Projectile.java
//        Created April 10th 2014 by Esteban Valle
//
//        Copyright © 2014 Esteban Valle.All rights reserved.
//
//        +1-775-351-4427
//        esteban@thevalledesign.com
//        http://facebook.com/SeniorShizzle

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Projectile class produces wholly autonomous projectile objects which track their own positions and lifespans.
 * A Projectile object is designed to have a finite lifespan (default 5000ms) after which it flags itself to be destroyed
 * by assigning the isExpired boolean to TRUE. Projectile objects should be instantiated in some enumerable data structure
 * which is regularly parsed to remove expired projectile objects. Each individual projectile can have a unique damage level,
 * a unique lifespan, and a unique velocity factor, all of which are managed automatically once set.
 */
public class Projectile {

    public double x;
    public double y;

    private double xVelocity;
    private double yVelocity;


    public static final int DEFAULT_SPEED       = 10;       // pixels per frame (ppf)
    public static final int DEFAULT_LIFESPAN    = 5000;     // milliseconds
    public static final int DEFAULT_DAMAGE      = 100;      // unitless scalar

    /** This field is set to TRUE when the projectile has expired.
     *  It will automatically return TRUE after the projectile's lifespan has ended
     *  but can be set manually if desired with no ill effects. If the variable isExpired
     *  is set manually to FALSE after the lifespan of the projectile has been exhausted
     *  the designer assumes responsibility to manage the projectile independently
     *  as projectile objects in such a state will not self-terminate.
     */
    public boolean isExpired = false;

    /** The damage scalar of the projectile object. Default value is 100 */
    public final int damage;


    /** Creates a Projectile object with default damage, lifespan, velocities, et cetera.
     *
     * @param x the initial x position of the projectile.
     * @param y the initial y position of the projectile.
     * @param theta the direction in which the projectile will travel, in radians.
     */
    public Projectile(int x, int y, double theta){
        this(x, y, DEFAULT_SPEED, DEFAULT_SPEED, theta, DEFAULT_SPEED, DEFAULT_LIFESPAN);
    }

    /**
     * Creates a Projectile object with default damage of 100.
     * @param x the initial x position of the projectile.
     * @param y the initial y position of the projectile.
     * @param xVelocity the velocity at which the projectile is launched (to conserve momentum from a moving launch vehicle).
     * @param yVelocity the velocity at which the projectile is launched (to conserve momentum from a moving launch vehicle).
     * @param theta the direction in which the projectile will travel, in radians.
     * @param speedFactor a scalar speed multiplier for the projectile. This is added to the velocity linearly.
     * @param lifespan the lifespan of the Projectile in milliseconds before it is flagged isExpired for removal.
     */
    public Projectile(int x, int y, double xVelocity, double yVelocity, double theta, double speedFactor, int lifespan) {
        this(x, y, xVelocity, yVelocity, theta, speedFactor, lifespan, DEFAULT_DAMAGE);
    }

    /**
     * Creates a Projectile object with default damage of 100.
     * @param x the initial x position of the projectile.
     * @param y the initial y position of the projectile.
     * @param theta the direction in which the projectile will travel, in radians.
     * @param speedFactor a scalar speed multiplier for the projectile. This is added to the velocity linearly.
     * @param lifespan the lifespan of the Projectile in milliseconds before it is flagged isExpired for removal.
     */
    public Projectile(int x, int y, double theta, double speedFactor, int lifespan){
        this(x, y, DEFAULT_SPEED, DEFAULT_SPEED, theta, speedFactor, lifespan, DEFAULT_DAMAGE);
    }


    /**
     * Creates a Projectile object.
     *
     * @param x           the initial x position of the projectile.
     * @param y           the initial y position of the projectile.
     * @param xVelocity   the velocity at which the projectile is launched (to conserve momentum from a moving launch vehicle).
     * @param yVelocity   the velocity at which the projectile is launched (to conserve momentum from a moving launch vehicle).
     * @param theta       the direction in which the projectile will travel, in radians.
     * @param speedFactor a scalar speed multiplier for the projectile. This is added to the velocity linearly.
     * @param lifespan    the lifespan of the Projectile in milliseconds before it is flagged isExpired for removal.
     * @param damage      the scalar damage constant for the projectile. This should be called when calculating damage done.
     */
    public Projectile(int x, int y, double xVelocity, double yVelocity, double theta, double speedFactor, int lifespan, int damage) {
        // Initialize the bullet's velocity
        this.xVelocity = xVelocity + (speedFactor * Math.cos(theta)); // I never thought I would ever use trig functions...
        this.yVelocity = yVelocity + (speedFactor * Math.sin(theta)); // +1 Mrs. Oswald's Geometry class

        // Initialize the bullet at the front of the ship
        this.x = (x + MeteorGame.getShipRadius() * Math.cos(theta)); // Perhaps concerns if we are not launching from a ship
        this.y = (y + MeteorGame.getShipRadius() * Math.sin(theta));

        this.damage = damage;

        Timer lifeTimer = new Timer(lifespan, invalidate); // This timer flags the projectile as expired after the lifespan
        lifeTimer.setRepeats(false);
        lifeTimer.start();
    }


    /**
     * Updates the projectiles position x and y values. If the projectile travels off the screen, it wraps automatically.
     */
    public void update(){
        // Elegance at its finest
        x += xVelocity;
        y += yVelocity;

        // Wrap around the left and right screen edge
        if (x < -30) x = MeteorGame.windowWidth + 25;
        if (x > MeteorGame.windowWidth + 30) x = -25;

        // Wrap around the top and bottom screen edge
        if (y > MeteorGame.windowHeight + 30) y = -25;
        if (y < -30) y = MeteorGame.windowHeight + 25;
    }

    /** The ActionListener object that expires the projectile. Referenced solely from the invalidation timer */
    ActionListener invalidate = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
           isExpired = true; // Expire the bullet so it can be collected and removed
        }
    };

}
