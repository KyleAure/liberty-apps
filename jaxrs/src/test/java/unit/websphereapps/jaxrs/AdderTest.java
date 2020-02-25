package unit.websphereapps.jaxrs;

import io.websphereapps.jaxrs.Adder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdderTest {

    @Test
    public void testAdd() {
        int expected = 4 + 6;
        assertEquals(expected, Adder.add(4, 6));
    }

    @Test
    public void testAddAgain() {
        int expected = 5 + 8;
        assertEquals(expected, Adder.add(5, 8));
    }
}