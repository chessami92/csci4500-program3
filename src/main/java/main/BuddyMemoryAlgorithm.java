package main;

/*
 * Author: Josh DeWitt
 * Written for Program 3 during CSCI4500 in 2013 Summer session.
 *
 * Main entry point for the program. Performs input operations
 * for determining the next memory allocation/deallocation. Prints
 * out success messages for both operations and deferred messages for
 * allocations.
 *
 * Since the memory and minimum block sizes are guaranteed to be powers
 * of two, efficient bitwise operations are used instead of dividing
 * and multiplying.
 */

import memory.Memory;
import memory.MemoryManager;
import memory.MemoryRequest;

import java.util.Scanner;

public class BuddyMemoryAlgorithm {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        /* Create a memory manager with the memory size and minimum block size. */
        MemoryManager manager = new MemoryManager(in.nextInt(), in.nextInt());

        /* Continue processing requests until the end of input. */
        while (in.hasNextInt()) {
            /* Get the allocation ID. */
            int requestId = in.nextInt();
            /* Get the operation type - allocation or deallocation. */
            char requestType = in.next().charAt(0);

            /* See if allocating or deallocating. */
            if (requestType == '+') {
                /* See how much space is required. */
                int size = in.nextInt();
                System.out.printf("Request ID %d: allocate %d bytes.\n", requestId, size);
                /* Create a formal request to send to the memory manager. */
                MemoryRequest request = new MemoryRequest(requestId, size);
                /* Attempt to allocate the request. */
                Memory allocated = manager.allocate(request);
                if (allocated == null) {
                    /* The allocation returned null, could not be fulfilled. */
                    System.out.println("   Request deferred.");
                } else {
                    /* The allocation was successful. Notify the user */
                    /* of the address of the newly allocated memory.  */
                    System.out.printf("   Success; addr = 0x%08x.\n", allocated.getAddress());

                }
            } else if (requestType == '-') {
                /* Deallocate the memory block identified by the request ID. */
                /* This will always be successful, so print as such.         */
                System.out.printf("Request ID %d: deallocate.\n   Success.\n", requestId);
                manager.deallocate(requestId);
            }
        }
    }
}
