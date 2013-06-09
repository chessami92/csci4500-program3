package main;

import memory.Memory;
import memory.MemoryManager;
import memory.MemoryRequest;

import java.util.Scanner;

public class BuddyMemoryAlgorithm {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        MemoryManager manager = new MemoryManager(in.nextInt(), in.nextInt());

        while (in.hasNextLine()) {
            int requestId = in.nextInt();
            char requestType = in.next().charAt(0);

            /* See if allocating or deallocating. */
            if (requestType == '+') {
                int size = in.nextInt();
                MemoryRequest request = new MemoryRequest(requestId, size);
                System.out.printf("Request ID %d: allocate %d bytes.\n", requestId, size);
                Memory allocated = manager.allocate(request);
                if (allocated == null) {
                    System.out.println("\tRequest deferred.");
                } else {
                    System.out.printf("\tSuccess; addr = 0x%08x.\n", allocated.getAddress());

                }
            } else if (requestType == '-') {
                System.out.printf("Request ID %d: deallocate.\n\tSuccess.\n", requestId);
                manager.deallocate(requestId);
            }
        }
    }
}
