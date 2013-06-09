package memory;

public class MemoryRequest {
    private int id;
    private int size;

    /* Create a new resource request given an id and a requested size. */
    public MemoryRequest(int id, int size) {
        this.id = id;
        this.size = 1;

        /* Convert the request to the next largest power    */
        /* of two that is >= the minimum memory block size. */
        while (this.size < size || this.size < Memory.minBlockSize) {
            this.size = this.size * 2;
        }
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
}
