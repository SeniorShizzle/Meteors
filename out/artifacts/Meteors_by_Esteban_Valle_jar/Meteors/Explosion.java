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

/** An Explosion is a class which manages its own particle array
 * the particle array is instantiated at an x and y coordinate
 * and then updated for animation by calling the update() method.
 * The getParticles() method returns the linked list of particles
 * which can be used to draw the explosion.
 */
public class Explosion {

    /** The list of Points representing every particle */
    private LinkedList<Point> particles = new LinkedList<Point>();

    /** The angles of departure for each particle */
    private double[] thetas;

    /** The number of particles for a given explosion */
    private final int NUMBER_OF_PARTICLES = 20;

    /** TRUE if the object should be collected */
    private boolean expired = false;

    public Explosion(int x, int y){

        thetas = new double[NUMBER_OF_PARTICLES];

        // Populate the linked list with particles and theta values
        for (int i = 0; i < NUMBER_OF_PARTICLES; i++){
            thetas[i] = Math.random() * 6.28;
            int xi = (int)(x + Math.random() * Math.cos(thetas[i]));
            int yi = (int)(y + Math.random() * Math.sin(thetas[i]));
            particles.add(new Point(xi, yi));
        }


        Timer expirationTimer = new Timer(1300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                expired = true;
            }
        });
        expirationTimer.setRepeats(false);
        expirationTimer.start();

    }

    /** Updates the positions of each particle */
    public void update(){
       int i = 0;
       for (Point point : particles){
           point.x += Math.random() * 1.5 * Math.cos(thetas[i]);
           point.y += Math.random() * 1.5 * Math.sin(thetas[i]);
           i++;
       }
    }

    /** Returns the linked list of points to draw */
    public LinkedList<Point> getParticles(){
        return particles;
    }

    /** Returns TRUE if the explosion is expired */
    public boolean isExpired(){
        return expired;
    }

} // End of class Explosion
