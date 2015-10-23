// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EndGame extends JPanel {

    private Font vectorFont;

    private Meteors parent;

    public EndGame(Meteors parent){

        this.parent = parent;
        setSize(MeteorGame.windowWidth, MeteorGame.windowHeight);
        setLayout(null);
        setBackground(Color.BLACK);
        System.out.println("EndGame instantiated");

        initialize();

        setVisible(true);

        Timer repaintTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                repaint();
            }
        });
        repaintTimer.setRepeats(false);
        repaintTimer.start();
    }

    private void initialize(){
        try {
            vectorFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("Vectorb.ttf")).deriveFont(24f);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
        attributes.put(java.awt.font.TextAttribute.TRACKING, 0.2); // Adjust the tracking of the font
        vectorFont = vectorFont.deriveFont(attributes);
        vectorFont = vectorFont.deriveFont(50f);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Mandatory call

        System.out.println("drawing");
        Graphics2D g2d = (Graphics2D)g;

        g2d.setFont(vectorFont);

        g2d.setColor(Color.WHITE);
        g2d.drawString("GAME OVER", Meteors.windowWidth / 2 - 200, Meteors.windowHeight / 2);

        // Draws the close box "X" in the upper left-hand corner
            g2d.drawLine(10, 10, 20, 20);
            g2d.drawLine(20, 10, 10, 20);
            g2d.setColor(Color.RED.darker());
            g2d.drawRect(5, 5, 20, 20);



    }




}
