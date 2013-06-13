package memory;

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
        this.size = actualPower >= Memory.minBlockSize ? actualPower : Memory.minBlockSize;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
}
