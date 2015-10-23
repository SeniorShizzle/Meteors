// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import java.awt.*;

/**
 * The generic meteor object. A meteor is differentiated from an asteroid in the sense that a meteor is
 * located in a planetary atmosphere whereas an asteroid is located in deep space. Additionally, a meteor
 * is much smaller than a proper Asteroid, just like my wallet in a lawsuit against Atari. There are several
 * shapes and sizes of meteor. Each meteor maintains its own position and velocity, as well as destruction
 * state. Meteors are managed by the MeteorManager class, where they are stored in a circular doubly linked list.
 */
public class Meteor {
    public double x;
    public double y;

    public double xVelocity;
    public double yVelocity;

    /**
     * The size of the meteors. Integer values of 1, 2, or 3
     */
    public final int size;

    /**
     * The shape (style) of the asteroid)
     */
    private final MeteorShape shape;

    public final int radius;

    /**
     * An enumeration of the shapes of meteor
     */
    public enum MeteorShape {
        STYLE_1, STYLE_2, STYLE_3, STYLE_4
    }

    /**
     * Returns a Meteor object with size of 'size', an integer value 1, 2, or 3.
     */
    public Meteor(double x, double y, double theta, int size) {
        if (size > 0 && size < 4) this.size = size;
        else this.size = 0; // smallest meteor
        this.x = x;
        this.y = y;

        this.shape = MeteorShape.values()[(int) (Math.random() * 4)]; // Randomly assign a meteor style

        int velocity;

        switch (this.size) { // There are three sizes of meteor: Big, Medium, and Small
            case 1:
                radius = 15;
                velocity = 4;
                break;
            case 2:
                radius = 30;
                velocity = 3;
                break;
            case 3:
                radius = 60;
                velocity = 2;
                break;
            default:
                radius = 20;
                velocity = 4;
                break;
        }

        this.xVelocity = velocity * Math.cos(theta);
        this.yVelocity = velocity * Math.sin(theta);
    }

    /**
     * Updates the x and y positions of the meteor
     */
    public void update() {
        x += xVelocity;
        y += yVelocity;

        // The ship wraps the edges
        if (x < -30) x = MeteorGame.windowWidth + 25;
        if (x > MeteorGame.windowWidth + 30) x = -25;

        if (y > MeteorGame.windowHeight + 30) y = -25;
        if (y < -30) y = MeteorGame.windowHeight + 25;

    }

    /** Returns the polygon of the meteor object */
    public Polygon polygon() {

        int[] xPts;
        int[] yPts;

        switch (shape) {
            case STYLE_1:
                xPts = new int[]{
                        //(int) (x + radius),
                        (int) (x + radius * 1.1 * Math.cos(.2)),
                        (int) (x + radius * .5 * Math.cos(.8)),
                        (int) (x + radius * .8 * Math.cos(1.3)),
                        (int) (x + radius * Math.cos(1.8)),
                        (int) (x + radius * .9 * Math.cos(2.2)),
                        (int) (x + radius * .8 * Math.cos(2.8)),
                        (int) (x + radius * 1.1 * Math.cos(3.6)),
                        (int) (x + radius * .6 * Math.cos(4.5)),
                        (int) (x + radius * .9 * Math.cos(5)),
                        (int) (x + radius * Math.cos(5.7)),
                        (int) (x + radius * Math.cos(6))
                };

                yPts = new int[]{
                        //(int) (y + radius),
                        (int) (y + radius * 1.1 * Math.sin(.2)),
                        (int) (y + radius * .5 * Math.sin(.8)),
                        (int) (y + radius * .8 * Math.sin(1.3)),
                        (int) (y + radius * Math.sin(1.8)),
                        (int) (y + radius * .9 * Math.sin(2.2)),
                        (int) (y + radius * .8 * Math.sin(2.8)),
                        (int) (y + radius * 1.1 * Math.sin(3.6)),
                        (int) (y + radius * .6 * Math.sin(4.5)),
                        (int) (y + radius * .9 * Math.sin(5)),
                        (int) (y + radius * Math.sin(5.7)),
                        (int) (y + radius * Math.sin(6))

                };
                break;

            case STYLE_2:
                xPts = new int[]{
                        //(int) (x + radius),
                        (int) (x + radius * .5 * Math.cos(.2)),
                        (int) (x + radius * .9 * Math.cos(.8)),
                        (int) (x + radius * .9 * Math.cos(1.3)),
                        (int) (x + radius * .5 * Math.cos(2.0)),
                        (int) (x + radius * .3 * Math.cos(2.5)),
                        (int) (x + radius * .8 * Math.cos(2.8)),
                        (int) (x + radius * 1.1 * Math.cos(3.9)),
                        (int) (x + radius * .9 * Math.cos(4.5)),
                        (int) (x + radius * .6 * Math.cos(5)),
                        (int) (x + radius * Math.cos(5.7)),
                        (int) (x + radius * .9 * Math.cos(6))
                };

                yPts = new int[]{
                        //(int) (y + radius),
                        (int) (y + radius * .5 * Math.sin(.2)),
                        (int) (y + radius * .9 * Math.sin(.8)),
                        (int) (y + radius * .9 * Math.sin(1.3)),
                        (int) (y + radius * .5 * Math.sin(2.0)),
                        (int) (y + radius * .3 * Math.sin(2.5)),
                        (int) (y + radius * .8 * Math.sin(2.8)),
                        (int) (y + radius * 1.1 * Math.sin(3.6)),
                        (int) (y + radius * .9 * Math.sin(4.5)),
                        (int) (y + radius * .6 * Math.sin(5)),
                        (int) (y + radius * Math.sin(5.7)),
                        (int) (y + radius * .9 * Math.sin(6))

                };
                break;

            case STYLE_3:
                xPts = new int[]{
                        //(int) (x + radius),
                        (int) (x + radius * 1.1 * Math.cos(.2)),
                        (int) (x + radius * .6 * Math.cos(.8)),
                        (int) (x + radius * .9 * Math.cos(1.3)),
                        (int) (x + radius * Math.cos(2.0)),
                        (int) (x + radius * .7 * Math.cos(2.6)),
                        (int) (x + radius * .8 * Math.cos(3.1)),
                        (int) (x + radius * .7 * Math.cos(3.6)),
                        (int) (x + radius * .8 * Math.cos(4.2)),
                        (int) (x + radius * .7 * Math.cos(5)),
                        (int) (x + radius * 1 * Math.cos(5.2)),
                        (int) (x + radius * Math.cos(6))
                };

                yPts = new int[]{
                        //(int) (y + radius),
                        (int) (y + radius * 1.1 * Math.sin(.2)),
                        (int) (y + radius * .6 * Math.sin(.8)),
                        (int) (y + radius * .9 * Math.sin(1.3)),
                        (int) (y + radius * Math.sin(2.0)),
                        (int) (y + radius * .7 * Math.sin(2.6)),
                        (int) (y + radius * .8 * Math.sin(3.1)),
                        (int) (y + radius * .7 * Math.sin(3.6)),
                        (int) (y + radius * .8 * Math.sin(4.2)),
                        (int) (y + radius * .7 * Math.sin(5)),
                        (int) (y + radius * 1 * Math.sin(5.2)),
                        (int) (y + radius * Math.sin(6))

                };
                break;

            case STYLE_4:
                xPts = new int[]{
                        //(int) (x + radius),
                        (int) (x + radius * 1 * Math.cos(.2)),
                        (int) (x + radius * 1 * Math.cos(.8)),
                        (int) (x + radius * .2 * Math.cos(1.6)),
                        (int) (x + radius * 1 * Math.cos(1.6)),
                        (int) (x + radius * 1 * Math.cos(2.0)),
                        (int) (x + radius * 1 * Math.cos(2.7)),
                        (int) (x + radius * .5 * Math.cos(3.1)),
                        (int) (x + radius * 1 * Math.cos(3.3)),
                        (int) (x + radius * .9 * Math.cos(4.2)),
                        (int) (x + radius * 1 * Math.cos(5.2)),
                        (int) (x + radius * 1 * Math.cos(6))
                };

                yPts = new int[]{
                        //(int) (y + radius),
                        (int) (y + radius * 1 * Math.sin(.2)),
                        (int) (y + radius * 1 * Math.sin(.8)),
                        (int) (y + radius * 1 * Math.sin(1.6)),
                        (int) (y + radius * .2 * Math.sin(1.6)),
                        (int) (y + radius * 1 * Math.sin(2.0)),
                        (int) (y + radius * 1 * Math.sin(2.7)),
                        (int) (y + radius * .5 * Math.sin(3.1)),
                        (int) (y + radius * 1 * Math.sin(3.3)),
                        (int) (y + radius * .9 * Math.sin(4.2)),
                        (int) (y + radius * 1 * Math.sin(5.2)),
                        (int) (y + radius * 1 * Math.sin(6))

                };
                break;

            default:
                xPts = new int[]{
                        //(int) (x + radius),
                        (int) (x + radius * 1.1 * Math.cos(.2)),
                        (int) (x + radius * .5 * Math.cos(.8)),
                        (int) (x + radius * .8 * Math.cos(1.3)),
                        (int) (x + radius * Math.cos(1.8)),
                        (int) (x + radius * .9 * Math.cos(2.2)),
                        (int) (x + radius * .8 * Math.cos(2.8)),
                        (int) (x + radius * 1.1 * Math.cos(3.6)),
                        (int) (x + radius * .6 * Math.cos(4.5)),
                        (int) (x + radius * .9 * Math.cos(5)),
                        (int) (x + radius * Math.cos(5.7)),
                        (int) (x + radius * Math.cos(6))
                };

                yPts = new int[]{
                        //(int) (y + radius),
                        (int) (y + radius * 1.1 * Math.sin(.2)),
                        (int) (y + radius * .5 * Math.sin(.8)),
                        (int) (y + radius * .8 * Math.sin(1.3)),
                        (int) (y + radius * Math.sin(1.8)),
                        (int) (y + radius * .9 * Math.sin(2.2)),
                        (int) (y + radius * .8 * Math.sin(2.8)),
                        (int) (y + radius * 1.1 * Math.sin(3.6)),
                        (int) (y + radius * .6 * Math.sin(4.5)),
                        (int) (y + radius * .9 * Math.sin(5)),
                        (int) (y + radius * Math.sin(5.7)),
                        (int) (y + radius * Math.sin(6))

                };
                break;

        }

        return new Polygon(xPts, yPts, 11);
    }
}
