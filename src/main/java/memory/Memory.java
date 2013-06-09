package memory;

public class Memory {
    public static int minBlockSize;
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
        if (allocatedBy != 0 || size == minBlockSize) {
            return null;
        }

        size = size / 2;
        return new Memory(address + size, size);
    }

    /* Takes a memory block and attempts to merge them. */
    /* Returns null if they cannot be merged, returns   */
    /* a new merged memory block if successful.         */
    public Memory merge(Memory memory) {
        /* If they aren't the same size, they aren't buddies. */
        if (memory.getSize() != size) {
            return null;
        }

        /* See if this is on odd or even side of split */
        /* to determine the buddy's address.           */
        int buddyAddress = (isEvenBuddy() ? address + size : address - size);

        if (buddyAddress != memory.getAddress()) {
            return null;
        }

        /* Set the new address appropriately and double the size. */
        address = (isEvenBuddy() ? address : memory.getAddress());
        size = size * 2;

        /* This memory block has been adjusted to be   */
        /* the new merged memory block. Return itself. */
        return this;
    }

    private boolean isEvenBuddy() {
        return (address / size % 2 == 0);
    }

    public int getAddress() {
        return address;
    }

    public int getSize() {
        return size;
    }
}
