// Copyright © 2014 Esteban Valle. All rights reserved.
//
// +1-775-351-4427
// esteban@thevalledesign.com
// http://facebook.com/SeniorShizzle

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class MeteorGameTest {
    @Before
    public void setUp() throws Exception {

    }

    // Test the singleton ship and the initialization of the game
    @Test
    public void testModel(){
        MeteorGame game = new MeteorGame(4, new Meteors());
        Assert.assertEquals(0, game.getScore());
        game.initialize();
        Assert.assertEquals(game.ship, Ship.getInstance());
    }

}
