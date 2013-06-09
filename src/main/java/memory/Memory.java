package memory;

public class Memory {
    public static int minSize;
    private int address = 0;
    private int size;
    private int allocatedBy;

    /* Create the initial, unallocated memory block. */
    public Memory(int size) {
        this.size = size;
        allocatedBy = 0;
    }

    /* Create a new memory block with the given size and address. */
    public Memory(int address, int size) {
        this(size);
        this.address = address;
    }

    /* Creates and returns a new memory region split from this one. */
    /* Returns null if the memory cannot be split because it is     */
    /* allocated or is already at the minimum size.The size of this */
    /* memory and the new memory are half the original size of the  */
    /* current memory block.                                        */
    public Memory split() {
        if( allocatedBy != 0 || size == minSize ) {
            return null;
        }

        size = size / 2;
        return new Memory(address + size, size);
    }

    public int getAddress() {
        return address;
    }

    public int getSize() {
        return size;
    }
}
