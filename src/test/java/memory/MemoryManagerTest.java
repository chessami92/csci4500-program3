package memory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MemoryManagerTest {

    private static final int MEMORY_SIZE = 16;
    private static final int MIN_BLOCK_SIZE = 4;
    private MemoryManager manager;

    @BeforeMethod
    public void setup() {
        manager = new MemoryManager(MEMORY_SIZE, MIN_BLOCK_SIZE);
    }

    @Test
    public void findAvailableMemory() {
        assertEquals(manager.findAvailableMemory(2).getSize(), 4, "Should have found the block of size 16.");
    }

    @Test
    public void splitToSize() {
        Memory testMemory = new Memory(Memory.convertToPowerOfTwo(MEMORY_SIZE));
        assertEquals(manager.splitToSize(testMemory, 2).getSize(), 2);
    }

    @Test
    public void allocate() {
        MemoryRequest request;
        Memory allocatedMemory;
        for (int i = 0; i < MEMORY_SIZE / MIN_BLOCK_SIZE; ++i) {
            request = new MemoryRequest(i, MIN_BLOCK_SIZE);
            allocatedMemory = manager.allocate(request);
            assertNotNull(allocatedMemory);
            assertEquals(allocatedMemory.getAddress(), i * MIN_BLOCK_SIZE);
            assertEquals(allocatedMemory.allocatedBy, i);
        }
        request = new MemoryRequest(MEMORY_SIZE / MIN_BLOCK_SIZE, MIN_BLOCK_SIZE);
        allocatedMemory = manager.allocate(request);
        assertNull(allocatedMemory);
    }

    @Test
    public void deallocate() {
        MemoryRequest request = new MemoryRequest(1, MIN_BLOCK_SIZE);
        manager.allocate(request);
        Memory releasedMemory = manager.deallocate(1);

        assertEquals(releasedMemory.allocatedBy, 0);
        assertEquals(releasedMemory.getAddress(), 0);
        assertEquals(releasedMemory.getSize(), Memory.convertToPowerOfTwo(MEMORY_SIZE));
    }

    @Test
    public void deallocateMany() {
        MemoryRequest request = new MemoryRequest(1, MIN_BLOCK_SIZE);
        manager.allocate(request);
        request = new MemoryRequest(2, MIN_BLOCK_SIZE);
        manager.allocate(request);

        Memory releasedMemory = manager.deallocate(1);
        assertEquals(releasedMemory.allocatedBy, 0);
        assertEquals(releasedMemory.getAddress(), 0);
        assertEquals(releasedMemory.getSize(), Memory.convertToPowerOfTwo(MIN_BLOCK_SIZE));

        releasedMemory = manager.deallocate(2);
        assertEquals(releasedMemory.allocatedBy, 0);
        assertEquals(releasedMemory.getAddress(), 0);
        assertEquals(releasedMemory.getSize(), Memory.convertToPowerOfTwo(MEMORY_SIZE));
    }

    @Test
    public void allocateDeferred() {
        MemoryRequest request = new MemoryRequest(1, MEMORY_SIZE);
        assertNotNull(manager.allocate(request));
        request = new MemoryRequest(2, MEMORY_SIZE / 2);
        assertNull(manager.allocate(request));
        request = new MemoryRequest(3, MEMORY_SIZE / 2);
        assertNull(manager.allocate(request));

        Memory releasedThanAllocated = manager.deallocate(1);
        assertEquals(releasedThanAllocated.allocatedBy, 2);
        assertEquals(releasedThanAllocated.getSize(), 2);

        request = new MemoryRequest(4, MEMORY_SIZE / 2);
        assertNull(manager.allocate(request));
    }
}
