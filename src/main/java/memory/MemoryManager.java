package memory;

import java.util.LinkedList;
import java.util.List;

public class MemoryManager {
    List<Memory> unallocated;
    List<Memory> allocated;
    List<ResourceRequest> deferredRequests;

    public MemoryManager(int memorySize, int minBlockSize) {
        unallocated = new LinkedList<Memory>();
        allocated = new LinkedList<Memory>();
        deferredRequests = new LinkedList<ResourceRequest>();

        /* Create the starting memory with the given */
        /* size and the minimum block size.          */
        unallocated.add(new Memory(memorySize));
        /* Set the minimum size for a memory block. */
        Memory.minBlockSize = minBlockSize;
    }
}
