package memory;

import java.util.LinkedList;
import java.util.List;

public class MemoryManager {
    private List<Memory> unallocated;
    private List<Memory> allocated;
    private List<ResourceRequest> deferredRequests;

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

    /* Allocate memory given a request. Returns the memory  */
    /* block allocated if successful, null if unsuccessful. */
    public Memory allocate(ResourceRequest request) {
        Memory allocatedMemory = null;

        /* Check all unallocated cells and try to find the best fit. */
        for (Memory memory : unallocated) {
            /* Check if this memory cell could fulfill the request. */
            if (memory.getSize() >= request.getSize()) {
                /* If this is the first block that can fulfill the request. */
                if (allocatedMemory == null) {
                    allocatedMemory = memory;
                } else if (memory.getSize() < allocatedMemory.getSize()) {
                    /* A better fit was found. */
                    allocatedMemory = memory;
                }
            }
        }

        /* Check if a free memory block of enough size was found. */
        if (allocatedMemory != null) {
            unallocated.remove(allocatedMemory);
            /* Split memory in half until the correct size is made.  */
            /* Add the halved memory pieces to the unallocated list. */
            while(allocatedMemory.getSize() > request.getSize()) {
                unallocated.add(allocatedMemory.split());
            }
            allocatedMemory.allocatedBy = request.getId();
            allocated.add(allocatedMemory);
            return allocatedMemory;
        } else {
            deferredRequests.add(request);
            return null;
        }
    }
}
