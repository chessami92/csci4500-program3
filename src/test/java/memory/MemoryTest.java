package memory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MemoryTest {
    private static final int ORIGINAL_SIZE = Memory.convertToPowerOfTwo(8);
    private Memory memory;

    @BeforeMethod
    public void setup() {
        memory = new Memory(ORIGINAL_SIZE);
        Memory.minBlockSize = Memory.convertToPowerOfTwo(2);
    }

    @Test
    public void convertToPowerOfTwo() {
        assertEquals(Memory.convertToPowerOfTwo(1), 0);
        assertEquals(Memory.convertToPowerOfTwo(2), 1);
        assertEquals(Memory.convertToPowerOfTwo(64), 6);
        assertEquals(Memory.convertToPowerOfTwo(60), 6);
        assertEquals(Memory.convertToPowerOfTwo(100), 7);
    }

    @Test
    public void split() {
        Memory newMemory = memory.split();

        assertEquals(memory.getSize(), ORIGINAL_SIZE - 1);
        assertEquals(newMemory.getSize(), ORIGINAL_SIZE - 1);
        assertEquals(memory.getAddress(), 0);
        assertEquals(newMemory.getAddress(), (1 << (ORIGINAL_SIZE - 1)));
    }

    @Test
    public void split_minSize() {
        assertNotNull(memory.split());
        assertNotNull(memory.split());
        assertNull(memory.split());
    }

    @Test
    public void merge_memoryFirst() {
        Memory newMemory = new Memory((1 << ORIGINAL_SIZE), ORIGINAL_SIZE);

        Memory merged = memory.merge(newMemory);
        assertMergeSuccess(merged);
    }

    @Test
    public void merge_newMemoryFirst() {
        Memory newMemory = new Memory((1 << ORIGINAL_SIZE), ORIGINAL_SIZE);

        Memory merged = newMemory.merge(memory);
        assertMergeSuccess(merged);
    }

    private void assertMergeSuccess(Memory merged) {
        assertEquals(merged.getSize(), ORIGINAL_SIZE + 1);
        assertEquals(merged.getAddress(), 0);
    }

    @Test
    public void failedMerge_wrongAddress() {
        assertNull(memory.merge(memory));
    }
}
