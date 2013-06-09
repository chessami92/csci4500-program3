package memory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MemoryManagerTest {

    private static final int MEMORY_SIZE = 8;
    private static final int MIN_BLOCK_SIZE = 2;
    private MemoryManager manager;

    @BeforeMethod
    public void setup() {
        manager = new MemoryManager(MEMORY_SIZE, MIN_BLOCK_SIZE);
    }

    @Test
    public void allocateTwo() {

    }
}
