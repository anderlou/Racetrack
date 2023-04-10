package ch.zhaw.pm2.racetrack.model;

import ch.zhaw.pm2.racetrack.given.PositionVector;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PositionVectorTest {

    @Test
    void testEquals() {
        PositionVector a = new PositionVector(3, 5);
        PositionVector b = new PositionVector(3, 5);
        assertEquals(a, b);
    }

    @Test
    void testEqualsWithHashMap() {
        Map<PositionVector, Integer> map = new HashMap<>();
        PositionVector a = new PositionVector(3, 5);
        map.put(a, 1);
        PositionVector b = new PositionVector(3, 5);
        assertTrue(map.containsKey(a), "Test with same object");
        assertTrue(map.containsKey(b), "Test with equal object");
    }
}
