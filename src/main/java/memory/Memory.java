package memory;

public class Memory {
    /* The minimum block size available, stored as log2 of the actual size. */
    public static int minBlockSize;
    /* Which request ID is holding this memory block. */
    protected int allocatedBy;
    /* Address of this memory block. */
    private int address = 0;
    /* Size, stored as log2 of the actual size. */
    private int size;

    /* Create the initial, unallocated memory block. */
    public Memory(int size) {
        this.size = convertToPowerOfTwo(size);
        allocatedBy = 0;
    }

    /* Create a new memory block with the given size and address. */
    public Memory(int address, int size) {
        this(size);
        this.address = address;
    }

    /* Take in an integer value, and convert it to a power of two.      */
    /* Will return the next highest power of two if not an exact match. */
    public static int convertToPowerOfTwo(int num) {
        int power = 0;

        while((1 << power) < num) {
            power++;
        }

        return power;
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

        /* Size is stored as power of two, decrement to halve size. */
        size--;
        return new Memory(address + getSize(), getSize());
    }

    /* Takes a memory block and attempts to merge them. */
    /* Returns null if they cannot be merged, returns   */
    /* this resized merged memory block if successful.  */
    public Memory merge(Memory memory) {
        /* See if this is on odd or even side of split */
        /* to determine the buddy's address.           */
        int buddyAddress = (isEvenBuddy() ? address + getSize() : address - getSize());

        if (buddyAddress != memory.getAddress()) {
            return null;
        }

        /* Set the new address appropriately and double the size. */
        address = (isEvenBuddy() ? address : memory.getAddress());
        /* Size is stored as power of two, increment to double size. */
        size++;

        /* This memory block has been adjusted to be   */
        /* the new merged memory block. Return itself. */
        return this;
    }

    private boolean isEvenBuddy() {
        return (((address >> size) & 1) == 0);
    }

    public int getAddress() {
        return address;
    }

    public int getSize() {
        return 1 << size;
    }
}
