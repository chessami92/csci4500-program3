package memory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class MemoryTest {
    private static final int ORIGINAL_SIZE = 8;
    private Memory memory;

    @BeforeMethod
    public void setup() {
        memory = new Memory(ORIGINAL_SIZE);
    }

    @Test
    public void split() {
        Memory newMemory = memory.split();

        assertEquals(memory.getSize(), ORIGINAL_SIZE / 2);
        assertEquals(newMemory.getSize(), ORIGINAL_SIZE / 2);
        assertEquals(memory.getAddress(), 0);
        assertEquals(newMemory.getAddress(), ORIGINAL_SIZE / 2);
    }
}
