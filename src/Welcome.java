// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/** A splash screen */
public class Welcome extends JPanel {

    private Font vectorFont;

    private Meteors parent;

    private InputHandler input;

    private boolean running = true;

    public Welcome(Meteors parent) {

        this.parent = parent;
        setSize(MeteorGame.windowWidth, MeteorGame.windowHeight);
        setLayout(null);
        setBackground(Color.BLACK);
        System.out.println("Welcome instantiated");

        initialize();

        setVisible(true);

        repaint();


    }

    private void initialize() {
        try {
            vectorFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("Vectorb.ttf")).deriveFont(24f);

            Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
            attributes.put(java.awt.font.TextAttribute.TRACKING, 0.2); // Adjust the tracking of the font
            vectorFont = vectorFont.deriveFont(attributes);
            vectorFont = vectorFont.deriveFont(40f);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            vectorFont = Font.getFont("Helvetica Nueue");
        }

        input = parent.getInputHandler();


    }

    // Used to start the game if the splash screen is not timed
    private void run(){
        //if (input.isKeyDown(KeyEvent.VK_ENTER)) parent.setHasStartedGame(true);
        running = false;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Mandatory call

        System.out.println("Hello");

        Graphics2D g2d = (Graphics2D) g;

        g2d.setFont(vectorFont);

        g2d.setColor(Color.WHITE);
        g2d.drawString("METEORS", Meteors.windowWidth / 2 - 140, Meteors.windowHeight / 2);
        g2d.setFont(vectorFont.deriveFont(12f));
        g2d.drawString("BY ESTEBAN VALLE", Meteors.windowWidth / 2 - 100, Meteors.windowHeight / 2 + 50);
        //g2d.setFont(vectorFont.deriveFont(25f));
        //g2d.drawString("1 COIN 1 PLAY", Meteors.windowWidth / 2 - 168, Meteors.windowHeight / 2 + 200);


    }

}
