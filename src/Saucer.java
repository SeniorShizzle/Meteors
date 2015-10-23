// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**The saucer class will represent a flying saucer. A saucer object contains its own weak AI
 * which tracks and fires Projectile objects at the ship, operating autonomously from the MeteorGame object.
 * Saucer objects are instantiated by MeteorGame at preset intervals. The object’s update() method is called
 * by MeteorGame whenever an instantiated Saucer is present on the screen. The Saucer fires at the ship’s position every second. A Saucer
 * object manages its own structure of Projectiles and reports its polygon Shape.
 *
 */
public class Saucer {
    private double x;
    private double y;
    private double theta;

    private double radius;

    private double xVelocity;
    private double yVelocity;

    /** The switch delay before switching direction */
    private int directionSwitchDelay;

    /** TRUE if the saucer has reached end-of-life */
    private boolean expired = false;

    /** TRUE if the ship should expire itself when it goes off screen */
    private boolean expirable = false;

    /** The speed factor, different for different sizes of saucer */
    private int speedFactor;

    /** The singleton audio manager */
    private MeteorAudioManager audioManager = MeteorAudioManager.getInstance();

    /** The types of saucer */
    public enum SaucerStyle{
        BIG_SAUCER, LITTLE_SAUCER
    }

    /** The style of the saucer: Big or Little */
    private final SaucerStyle style;

    /** The parent of the saucer */
    private MeteorGame parent;

    /** The Linked List of Projectiles */
    private LinkedList<Projectile> projectiles = new LinkedList<Projectile>();


    // The timers are fields so that the can be stopped in the destroy() method
    private Timer switchDirectionTimer;
    private Timer fireProjectileTimer;
    private Timer expirationTimer;

    public Saucer(SaucerStyle style, MeteorGame parent){

        audioManager.saucer();

        this.style = style;
        this.parent = parent;

        x = Math.random() > .5 ? -25 : Meteors.windowWidth + 25; // Assigns the ship to start off screen on the left or right edge
        y = Math.random() * Meteors.windowHeight;

        switch (this.style){
            case BIG_SAUCER:
                radius = MeteorGame.getShipRadius() * 2.2;
                speedFactor = 4;
                directionSwitchDelay = 4000;
                break;
            case LITTLE_SAUCER:
                radius = MeteorGame.getShipRadius() * 1.5;
                speedFactor = 5;
                directionSwitchDelay = 2500;
                break;
            default:
                radius = 10;
                speedFactor = 10;
                break;
        }

        theta = Math.random() * 6.28;

        xVelocity = Math.cos(theta) * speedFactor;
        yVelocity = Math.sin(theta) * speedFactor;

        // The timer to expire the ship
        expirationTimer = new Timer(15000, expire);
        expirationTimer.setRepeats(false);
        expirationTimer.start();

        // The timer to switch direction every two seconds
        switchDirectionTimer = new Timer(directionSwitchDelay, switchDirection);
        switchDirectionTimer.setRepeats(true);
        switchDirectionTimer.start();

        // The timer to fire projectiles
        fireProjectileTimer = new Timer(1200, fireProjectile);
        fireProjectileTimer.setRepeats(true);
        fireProjectileTimer.start();
    }

    /** Sets the ship to be expired so that it can be destroyed */
    ActionListener expire = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            expirable = true;
        }
    };

    // Switches the direction of the saucer
    ActionListener switchDirection = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            theta *= Math.random() * 2; // The idea here is to eliminate dramatic direction changes MOST of the time

            xVelocity = Math.cos(theta) * speedFactor;
            yVelocity = Math.sin(theta) * speedFactor;

        }
    };

    /** Fires projectiles either randomly or at the ship */
    ActionListener fireProjectile = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            double projectileTheta;

            switch (style) {

                case BIG_SAUCER:

                    audioManager.fire();
                    projectileTheta = Math.random() * 6.28;
                    projectiles.add(new Projectile(
                            (int) (x + radius * Math.cos(projectileTheta)),
                            (int) (y + radius * Math.sin(projectileTheta)),
                            0, 0,
                            projectileTheta,
                            10, 2000));
                    break;
                case LITTLE_SAUCER:

                    audioManager.fire();
                    if (parent.isShipDisplayed()) { // Don't fire at a downed ship
                        double deltaX = parent.shipX() - (x);// + Math.random() > .5 ? Math.random() * -10 : Math.random() * 10) - parent.shipX();
                        double deltaY = parent.shipY() - (y);// + Math.random() > .5 ? Math.random() * -10 : Math.random() * 10) - parent.shipY();

                        projectileTheta = Math.atan2(deltaY, deltaX);

                    } else {
                        projectileTheta = Math.random() * 6.28;
                    }


                    if (Math.random() > .3){ // Most of the time we will randomize shots
                        projectileTheta *= Math.random() > .5 ? .92 : 1.08; // We need to lower the accuracy of the saucer
                    }

                    projectiles.add(new Projectile(
                            (int) (x + radius * Math.cos(projectileTheta)),
                            (int) (y + radius * Math.sin(projectileTheta)),
                            0, 0,
                            projectileTheta,
                            10, 1500));
                    break;


            }
        }
    };

    /** Updates the ship's position and tests for hits */
    public void update(){
        x += xVelocity;
        y += yVelocity;

        // Expire the ship only after it leaves the screen
        if (expirable && ( (x < 0 || x > MeteorGame.windowWidth) || (y < 0 || y > MeteorGame.windowHeight) )) expired = true;

        // The ship wraps the edges
        if (x < -30) x = MeteorGame.windowWidth + 25;
        if (x > MeteorGame.windowWidth + 30) x = -25;

        if (y > MeteorGame.windowHeight + 30) y = -25;
        if (y < -30) y = MeteorGame.windowHeight + 25;

    }

    public Polygon saucerPolygon() {
        int[] xValues = new int[]{
                (int) (x + radius / 2),
                (int) (x + radius * 1.2),
                (int) (x - radius * 1.2),
                (int) (x + radius * 1.2),
                (int) (x + radius / 2),
                (int) (x - radius / 2),
                (int) (x + radius / 2),
                (int) (x + radius / 4),
                (int) (x - radius / 4),
                (int) (x - radius / 2),
                (int) (x - radius * 1.2),
                (int) (x - radius / 2)

        };

        int[] yValues = new int[]{
                (int) (y + radius / 2),
                (int) (y),
                (int) (y),
                (int) (y),
                (int) (y - radius / 2),
                (int) (y - radius / 2),
                (int) (y - radius / 2),
                (int) (y - radius),
                (int) (y - radius),
                (int) (y - radius / 2),
                (int) (y),
                (int) (y + radius / 2)

        };

        return new Polygon(xValues, yValues, 12);
    }

    public void destroy(){
        expirationTimer.stop();
        fireProjectileTimer.stop();
        switchDirectionTimer.stop();

        audioManager.stopSaucer();

        projectiles = null;

        this.parent = null;

    }


    public int getX() {
        return (int)x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public boolean isExpired(){
        return expired;
    }

    public void setExpired(boolean expired){
        this.expired = expired;
    }

    public int getY() {
        return (int)y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public int getSpeedFactor() {
        return speedFactor;
    }

    public void setSpeedFactor(int speedFactor) {
        this.speedFactor = speedFactor;
    }

    public SaucerStyle getStyle() {
        return style;
    }

    public LinkedList<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(LinkedList<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    // More to come

}
