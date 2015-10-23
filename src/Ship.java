// Copyright © 2014 Esteban Valle. All rights reserved. Game based on Atari Asteroids © 1979 Atari Inc.
//        Meteors Ship.java
//        Created April 10th 2014 by Esteban Valle
//
//        Copyright © 2014 Esteban Valle.All rights reserved.
//
//        +1-775-351-4427
//        esteban@thevalledesign.com
//        http://facebook.com/SeniorShizzle


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The Ship that is displayed on the screen.
 */
public class Ship {
    /** The instantaneous x position */
    private double x;

    /** The instantaneous y position */
    private double y;

    /** The instantaneous x acceleration */
    private double xVelocity;

    /** The instantaneous y acceleration */
    private double yVelocity;

    /** The amount of rotation on the ship currently */
    private double rotation;

    /** The instantaneous rotational acceleration on the ship */
    private double rotationAccel;

    /** The ship's radius */
    private double radius;

    /** The current state of the ship's engines */
    private boolean isThrusting = false;

    /** The maximum forward velocity in any one axis, in pixels per frame.
     * The maximum actual velocity is equal to the square root of twice this
     * number squared (if the ship is accelerating in a diagonal direction). */
    private int maxSpeed = 15;

    /** TRUE if the ship has recently been hyperjumped */
    private boolean isHyperJumped = false;

    /** The counted number of warp hyper-jumps done so far. Collected for statistics later */
    private int warps = 0;

    /** The x value array to draw the ship. These must be accessed through the getter */
    private int[] xValues = new int[5];

    /** The y value array to draw the ship. These must be accessed through the getter */
    private int[] yValues = new int[5];

    /** Whether the ship updates its location, et cetera, for brief pauses */
    private boolean shipUpdates = true;

    /** TRUE if the ship crashed on re-entry */
    private boolean crashedOnReEntry = false;

    public static final double TWO_PI_THIRDS = 2.094395;

    /** The main ship object. Singleton for preservation between levels */
    private static Ship ship;

    public static Ship getInstance(){
        if (ship == null){
            ship = new Ship(10);
        }
        return ship;
    }


    /** Initializes a ship object with a default radius of 10 pixels. */
    private Ship(){
        this(10);
    }

    /**
     * Initializes a ship object with a radius of 'radius'
     * @param radius the radius of the ship in pixels.
     *
     */
    private Ship(int radius) {
        System.out.println("New Ship Instantiated");
        rotation = 0;
        rotationAccel = 0;
        xVelocity = 0;
        yVelocity = 0;
        this.radius = radius;

        x = Meteors.windowWidth  / 2;
        y = Meteors.windowHeight / 2;
    }

    /** Turns the ship left. Will not affect the ship's direction until a call to update() is made */
    public void left(){
        if (rotationAccel > -.5) rotationAccel -= .03;
    }

    /**
     * Turns the ship right. Will not affect the ship's direction until a call to update() is made
     */
    public void right(){
        if (rotationAccel < .5) rotationAccel += .03;
    }

    /**
     * This method is called in each frame where the ship's thrusters should be applied (i.e. the thrust key is depressed).
     * It accelerates the ship by incrementing the x and y velocities as a function of the rotation. Additionally, it tracks the
     * ship's maximum velocity.
     */
    public void thrust(){
        isThrusting = true;
        xVelocity +=  .1 * Math.cos(rotation);
        yVelocity +=  .1 * Math.sin(rotation);

        if (xVelocity > maxSpeed) xVelocity = maxSpeed;
        if (xVelocity < -1 * maxSpeed) xVelocity = -1 * maxSpeed;
        if (yVelocity > maxSpeed) yVelocity = maxSpeed;
        if (yVelocity < -1 * maxSpeed) yVelocity = -1 * maxSpeed;

    }

    /**
     * This method jumps the ship through hyperspace to a random screen location. It then starts a timer
     * to prevent the ship from warping sequentially within 10 seconds. It tracks the maximum number of warps, which
     * by default is 4
     */
    public void warp(){
        if (!isHyperJumped) {
            x = (Math.random() * MeteorGame.windowWidth);     // Places the ship randomly
            y = (Math.random() * MeteorGame.windowHeight);
            warps++;
            isHyperJumped = true;

            if ((int)(Math.random() * 10) == 3){
                System.out.println("Ship Crashed on Re-Entry");
                crashedOnReEntry = true;
            }
            Timer warpTimer = new Timer(1000, WarpTimer); // So that we can't warp too often
            warpTimer.setRepeats(false);
            warpTimer.start();
        }
    }

    /** This method decelerates the translational momentum of the ship when not thrusting. It is called once
     * per frame. This method does not affect rotational momentum. To decelerate rotational momentum, notTurning() should be used
     */
    public void coast(){
        xVelocity *= .99;
        yVelocity *= .99;
        isThrusting = false;

        if (Math.abs(xVelocity) < .01) xVelocity = 0; // I hate it when things keep dividing until the registry overflows
        if (Math.abs(yVelocity) < .01) yVelocity = 0;
    }

    /** This method decelerates the rotational momentum of the ship when not turning. Called once per frame */
    public void notTurning(){
        rotationAccel *= .7;
    }

    /**
     * This timer action method is called after the ship's warp delay.
     */
    ActionListener WarpTimer = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            isHyperJumped = false;
            crashedOnReEntry = false;
            System.out.println("Ship can warp");
        }
    };

    /**
     * Updates the ship's position. Called once per frame. This method automatically wraps the ship around
     * the edges of the screen by referencing MeteorGame.windowWidth & MeteorGame.windowHeight
     */
    public void update(){
        if (shipUpdates) {
            x += xVelocity;
            y += yVelocity;
            rotation += rotationAccel;

            // The ship wraps the edges
            if (x < -30) x = MeteorGame.windowWidth + 25;
            if (x > MeteorGame.windowWidth + 30) x = -25;

            if (y > MeteorGame.windowHeight + 30) y = -25;
            if (y < -30) y = MeteorGame.windowHeight + 25;
        }


    }

    /**
     * Returns the array of 3 integers that represent the y values of the vertices of the flame {left, peak, right}
     *
     * @return an array of 3 integers
     */
    public int[] getFlameXValues(){
        int[] xValues = new int[3];
                xValues[0] = (int) (x + .4 * radius * Math.cos(rotation + 2));
                xValues[1] = (int) (x + radius * Math.cos(rotation + 3.141592));
                xValues[2] = (int) (x + .4 * radius * Math.cos(rotation - 2));

        return xValues;
    }

    /**
     * Returns the array of 3 integers that represent the y values of the vertices of the flame {left, peak, right}
     *
     * @return an array of 3 integers
     */
    public int[] getFlameYValues() {
        int[] yValues = new int[3];
        yValues[0] = (int) (y + .4 * radius * Math.sin(rotation + 2));
        yValues[1] = (int) (y + radius * Math.sin(rotation + 3.141592));
        yValues[2] = (int) (y + .4 * radius * Math.sin(rotation - 2));

        return yValues;
    }

    /**
     * Returns a 5 integer array representing the x values of the ship polygon. The points ordered 0 through 4
     * represent the vertices of the ship starting at the lower left point and proceeding clockwise through the point first.
     *
     * @return an array of integers, length 5
     */
    public int[] getXValues(){
        xValues[0] = (int) (x + radius * Math.cos(rotation + TWO_PI_THIRDS));
        xValues[1] = (int) (x + 2 * radius * Math.cos(rotation));
        xValues[2] = (int) (x + radius * Math.cos(rotation - TWO_PI_THIRDS));
        xValues[3] = (int) (x + (radius / 2) * Math.cos(rotation - 2));
        xValues[4] = (int) (x + (radius / 2) * Math.cos(rotation + 2));
        //for (int i = 0; i < 5; i++) xValues[i] += 10;

        return xValues;
    }

    /**
     * Returns a 5 integer array representing the y values of the ship polygon. The points ordered 0 through 4
     * represent the vertices of the ship starting at the lower left point and proceeding clockwise through the point first.
     *
     * @return an array of integers, length 5
     */
    public int[] getYValues() {
        yValues[0] = (int) (y + radius * Math.sin(rotation + TWO_PI_THIRDS));
        yValues[1] = (int) (y + 2 * radius * Math.sin(rotation));
        yValues[2] = (int) (y + radius * Math.sin(rotation - TWO_PI_THIRDS));
        yValues[3] = (int) (y + (radius / 2) * Math.sin(rotation - 2));
        yValues[4] = (int) (y + (radius / 2) * Math.sin(rotation + 2));

        //for (int i = 0; i < 5; i++) yValues[i] += 10;
        return yValues;
    }

    /** Returns a Polygon of the ship's coordinates */
    public Polygon getShipPolygon(){
        return new Polygon(
                new int[]{
                        (int) (x + radius * Math.cos(rotation + TWO_PI_THIRDS)),
                        (int) (x + 2 * radius * Math.cos(rotation)),
                        (int) (x + radius * Math.cos(rotation - TWO_PI_THIRDS)),
                        (int) (x + (radius / 2) * Math.cos(rotation - 2)),
                        (int) (x + (radius / 2) * Math.cos(rotation + 2))
                                                                    },

                new int[]{
                        (int) (y + radius * Math.sin(rotation + TWO_PI_THIRDS)),
                        (int) (y + 2 * radius * Math.sin(rotation)),
                        (int) (y + radius * Math.sin(rotation - TWO_PI_THIRDS)),
                        (int) (y + (radius / 2) * Math.sin(rotation - 2)),
                        (int) (y + (radius / 2) * Math.sin(rotation + 2))
                }, 5);

    }

    public Polygon getShipAt(int x, int y, double rotation){

        int[] xValUpright = new int[]{
                (int) (x + radius * Math.cos(rotation + TWO_PI_THIRDS)),
                (int) (x + 2 * radius * Math.cos(rotation)),
                (int) (x + radius * Math.cos(rotation - TWO_PI_THIRDS)),
                (int) (x + (radius / 2) * Math.cos(rotation - 2)),
                (int) (x + (radius / 2) * Math.cos(rotation + 2))
        };

        int[] yValUpright = new int[]{
                (int) (y + radius * Math.sin(rotation + TWO_PI_THIRDS)),
                (int) (y + 2 * radius * Math.sin(rotation)),
                (int) (y + radius * Math.sin(rotation - TWO_PI_THIRDS)),
                (int) (y + (radius / 2) * Math.sin(rotation - 2)),
                (int) (y + (radius / 2) * Math.sin(rotation + 2))
        };

        return new Polygon(xValUpright, yValUpright, 5);
    }

    public boolean isShipUpdates() {
        return shipUpdates;
    }

    public void setShipUpdates(boolean shipUpdates) {
        this.shipUpdates = shipUpdates;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(int shipRadius){
        radius = shipRadius;
    }

    public double y() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double x() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double xVelocity() {
        return xVelocity;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double yVelocity() {
        return yVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public double rotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double rotationAccel() {
        return rotationAccel;
    }

    public void setRotationAccel(double rotationAccel) {
        this.rotationAccel = rotationAccel;
    }

    public int maxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public boolean isThrusting() {
        return isThrusting;
    }

    public void setThrusting(boolean isThrusting) {
        this.isThrusting = isThrusting;
    }

    public boolean isHyperJumped(){
        return isHyperJumped;
    }

    public boolean isCrashedOnReEntry(){
        return crashedOnReEntry;
    }

    // UGH 80 lines of mutators & accessors
}

