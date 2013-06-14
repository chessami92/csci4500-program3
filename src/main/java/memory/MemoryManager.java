package memory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MemoryManager {
    /* List of lists of memory. Element zero is the list of memory blocks */
    /* of the minimum size, element one is list of next bigger blocks.    */
    private List<List<Memory>> unallocated;
    /* A list of the already allocated memory locations. */
    private List<Memory> allocated;
    /* A queue of waiting deferred requests. */
    private Queue<MemoryRequest> deferredRequests;

    public MemoryManager(int memorySize, int minBlockSize) {
        int memorySizeLog2 = Memory.convertToPowerOfTwo(memorySize);
        int minBlockSizeLog2 = Memory.convertToPowerOfTwo(minBlockSize);

        unallocated = new ArrayList<List<Memory>>();
        for (int i = 0; i < memorySizeLog2 - minBlockSizeLog2 + 1; ++i) {
            unallocated.add(i, new LinkedList<Memory>());
        }
        allocated = new LinkedList<Memory>();
        deferredRequests = new LinkedList<MemoryRequest>();

        /* Create the starting memory with the given */
        /* size and the minimum block size.          */
        unallocated.get(memorySizeLog2 - minBlockSizeLog2).add(new Memory(memorySizeLog2));
        /* Set the minimum size for a memory block. */
        Memory.minBlockSize = minBlockSizeLog2;
    }

    /* Allocate memory given a request. Returns the memory  */
    /* block allocated if successful, null if unsuccessful. */
    public Memory allocate(MemoryRequest request) {
        /* Attempt to find a large enough memory location. */
        Memory allocatedMemory = findAvailableMemory(request.getSize());

        /* Check if a free memory block of enough size was found. */
        if (allocatedMemory != null) {
            allocatedMemory = splitToSize(allocatedMemory, request.getSize());

            allocatedMemory.allocatedBy = request.getId();
            allocated.add(allocatedMemory);
            return allocatedMemory;
        } else {
            /* Request could not be filled right now. Put it on hold. */
            deferredRequests.add(request);
            return null;
        }
    }

    /* Find an available memory location at least the size passed */
    /* (in log2). The memory chunk returned may be larger.        */
    /* The memory block is removed from the unused list.          */
    public Memory findAvailableMemory(int desiredSize) {
        Memory availableMemory = null;

        /* See which list of memory elements to begin looking at. */
        int smallestPossible = desiredSize - Memory.minBlockSize;

        for (int i = smallestPossible; i < unallocated.size(); ++i) {
            /* List of memory chunks of size i. */
            List<Memory> iSizeMemories = unallocated.get(i);
            if (iSizeMemories.size() > 0) {
                availableMemory = iSizeMemories.remove(0);
                break;
            }
        }

        return availableMemory;
    }

    /* Halve the size of the memory block until it is the desired size. */
    /* Adds the split off memory blocks back to the unallocated lists.  */
    public Memory splitToSize(Memory foundMemory, int desiredSize) {
        while (foundMemory.getSize() > desiredSize) {
            Memory newMemory = foundMemory.split();
            unallocated.get(newMemory.getSize() - Memory.minBlockSize).add(newMemory);
        }

        return foundMemory;
    }

    /* Takes in a request number, finds it in the allocated    */
    /* memory list and deallocates that memory block. Returns  */
    /* the resulting memory block after merging if successful. */
    public Memory deallocate(int requestNumber) {
        /* Search through the allocated memory to find */
        /* the memory block to be released.            */
        for (Memory allocatedMemory : allocated) {
            if (allocatedMemory.allocatedBy == requestNumber) {
                /* Remove memory block from allocated status. */
                allocated.remove(allocatedMemory);
                allocatedMemory.allocatedBy = 0;
                /* Merge it with all of it's buddies. */
                Memory merged = merge(allocatedMemory);
                /* Add the merged block to the unallocated list. */
                unallocated.get(merged.getSize() - Memory.minBlockSize).add(merged);
                /* See if any deferred requests can now be fulfilled. */
                attemptAllocationOfDeferred();
                return merged;
            }
        }

        /* Cannot be deallocated because it had not been allocated. */
        return new Memory(0);
    }

    /* Takes in a memory block and attempts to merge it with  */
    /* the other memory blocks in the unallocated lists.      */
    /* Returns the original memory if could not be merged, or */
    /* returns a merged memory block.                         */
    private Memory merge(Memory memory) {
        /* Obtain the list of memory blocks of the same size. */
        int sameSizeIndex = memory.getSize() - Memory.minBlockSize;
        List<Memory> sameSizeMemories = unallocated.get(sameSizeIndex);

        int buddyAddress = memory.getBuddyAddress();

        /* Search through all other memory cells of the */
        /* same size to see if they can be merged.      */
        for (Memory mergeCandidate : sameSizeMemories) {
            if (mergeCandidate.getAddress() == buddyAddress) {
                /* Remove the merge candidate from the unallocated list. */
                sameSizeMemories.remove(mergeCandidate);
                /* Recursively attempt to merge again. */
                return merge(memory.merge(mergeCandidate));
            }
        }

        /* Return the memory block that has been merged if possible. */
        return memory;
    }

    /* Go through all deferred requests and attempt to allocate them. */
    private void attemptAllocationOfDeferred() {
        int numRequests = deferredRequests.size();

        for (int i = 0; i < numRequests; ++i) {
            /* Attempt to allocate given the request. */
            /* If not possible, it is added back to   */
            /* the deferred requests list.            */
            Memory allocated = allocate(deferredRequests.poll());
            if (allocated != null) {
                System.out.printf("\tDeferred request %d allocated; addr = 0x%08x.\n",
                        allocated.allocatedBy, allocated.getAddress());
            }
        }
    }
}
