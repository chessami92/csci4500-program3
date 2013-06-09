package memory;

public class ResourceRequest {
    private int id;
    private int size;

    /* Create a new resource request given an id and a requested size. */
    public ResourceRequest(int id, int size) {
        this.id = id;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
}
