package ru.nsu.fit.chernikov.Task_2_2_1;

import javafx.scene.paint.Color;
import org.junit.Test;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Point;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Walls;

import static org.junit.Assert.*;

public class WallsTest {
    @Test
    public void addWall(){
        Walls walls = new Walls(10,10,new Color(1,1,1,1));
        assertEquals(0,walls.getPoints().size());
        walls.addWall(new Point(1,1));
        assertEquals(1,walls.getPoints().size());
        walls.addWall(new Point(-1,-1));
        walls.addWall(new Point(1000,1000));
        assertEquals(1,walls.getPoints().size());
    }
}
