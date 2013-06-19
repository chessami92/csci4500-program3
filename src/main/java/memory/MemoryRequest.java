package memory;

/*
 * Author: Josh DeWitt
 * Written for Program 3 during CSCI4500 in 2013 Summer session.
 *
 * Formal request for memory allocation. The memory request is created
 * with an actual size needed, which is then rounded up to the nearest
 * power of two. If this next power of two is even smaller than the
 * minimum memory block size, the request is set for the minimum block
 * size.
 */

public class MemoryRequest {
    /* A unique ID for this request. */
    private int id;
    /* Size, stored as log2 of the actual size. */
    private int size;

    /* Create a new resource request given an id and a requested size. */
    public MemoryRequest(int id, int size) {
        this.id = id;

        /* Convert the request to the next largest power    */
        /* of two that is >= the minimum memory block size. */
        int actualPower = Memory.convertToPowerOfTwo(size);
        this.size = (actualPower >= Memory.minBlockSize) ? actualPower : Memory.minBlockSize;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
}
