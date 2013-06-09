package main;

import memory.MemoryManager;

import java.util.Scanner;

public class BuddyMemoryAlgorithm {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        MemoryManager manager = new MemoryManager(in.nextInt(), in.nextInt());
    }
}
