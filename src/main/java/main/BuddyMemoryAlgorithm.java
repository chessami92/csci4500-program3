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

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class BuddyMemoryAlgorithm {
    public static void main(String[] args) throws IOException {
        Scanner in = null;
        FileReader fin = null;

        /* If there is one argument, it is the file to be read from. */
        if (args.length == 0) {
            in = new Scanner(System.in);
        } else if (args.length == 1) {
            fin = new FileReader(args[0]);
            in = new Scanner(fin);
        } else {
            /* The program is being used incorrectly. Notify the user. */
            System.err.println("Usage: java main.BuddyMemoryAlgorithm [filename");
            System.exit(0);
        }

        /* Create a memory manager with the memory size and minimum block size. */
        MemoryManager manager = new MemoryManager(in.nextInt(), in.nextInt());

        /* Continue processing requests until the end of input. */
        while (in.hasNext()) {
            /* Get the allocation ID. */
            int requestId = in.nextInt();
            /* Get the operation type - allocation or deallocation. */
            String requestType = in.next();

            /* See if allocating or deallocating. */
            if (requestType.equals("+")) {
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
            } else if (requestType.equals("-")) {
                /* Deallocate the memory block identified by the request ID. */
                /* This will always be successful, so print as such.         */
                System.out.printf("Request ID %d: deallocate.\n   Success.\n", requestId);
                manager.deallocate(requestId);
            } else {
                /* There was a problem reading the request type, notify the user. */
                System.err.printf("ERROR: got request ID (%d) but unknown request type (%s)",
                        requestId, requestType);
                System.exit(1);
            }
        }

        if (fin != null) {
            fin.close();
        }
    }
}
