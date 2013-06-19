package memory;

/*
 * Author: Josh DeWitt
 * Written for Program 3 during CSCI4500 in 2013 Summer session.
 *
 * A memory chunk in the allocation scheme. The size is represented as a
 * power of two for efficiency. The memory can be split, provided that it
 * is not allocated and it's size is greater than the minimum block size.
 * Merging must only be performed with it's buddy, which is identified by
 * calling getBuddyAddress() and checking that the sizes are equal.
 */

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
        this.size = size;
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
        return new Memory(address + (1 << size), size);
    }

    /* Takes a memory block and merges it with itself, setting the  */
    /* appropriate address. Returns itself, the grown memory block. */
    public Memory merge(Memory memory) {
        /* Set the new address appropriately and double the size. */
        address = (isEvenBuddy() ? address : memory.getAddress());
        /* Size is stored as power of two, increment to double size. */
        size++;

        /* This memory block has been adjusted to be   */
        /* the new merged memory block. Return itself. */
        return this;
    }

    /* Get the address of this memory block's buddy. */
    public int getBuddyAddress() {
        return  (isEvenBuddy() ? address + (1 << size) : address - (1 << size));
    }

    /* See if this memory chuck is an even or odd chunk. */
    private boolean isEvenBuddy() {
        return (((address >> size) & 1) == 0);
    }

    public int getAddress() {
        return address;
    }

    public int getSize() {
        return size;
    }
}
