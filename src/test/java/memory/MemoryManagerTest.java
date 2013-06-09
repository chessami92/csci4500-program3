package memory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MemoryManagerTest {

    private static final int MEMORY_SIZE = 8;
    private static final int MIN_BLOCK_SIZE = 2;
    private MemoryManager manager;

    @BeforeMethod
    public void setup() {
        manager = new MemoryManager(MEMORY_SIZE, MIN_BLOCK_SIZE);
    }

    @Test
    public void allocate() {
        MemoryRequest request = new MemoryRequest(1, 2);
        Memory allocatedMemory;
        for (int i = 0; i < MEMORY_SIZE / MIN_BLOCK_SIZE; ++i) {
            allocatedMemory = manager.allocate(request);
            assertNotNull(allocatedMemory);
            assertEquals(allocatedMemory.getAddress(), i * MIN_BLOCK_SIZE);
        }
        allocatedMemory = manager.allocate(request);
        assertNull(allocatedMemory);
    }
}
