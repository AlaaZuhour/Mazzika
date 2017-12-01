package com.udacity.capstone.musicapp.model;

import java.util.ArrayList;
import java.util.Arrays;
public class PriorityQueue
{
    private Song[] heap;
    private int heapSize, capacity;
 
    /** Constructor **/
    public PriorityQueue(int capacity)
    {    
        this.capacity = capacity + 1;
        heap = new Song[this.capacity];
        heapSize = 0;
    }
    /** function to clear **/
    public void clear()
    {
        heap = new Song[capacity];
        heapSize = 0;
    }
    /** function to check if empty **/
    public boolean isEmpty()
    {
        return heapSize == 0;
    }
    /** function to check if full **/
    public boolean isFull()
    {
        return heapSize == capacity - 1;
    }
    /** function to get Size **/
    public int size()
    {
        return heapSize;
    }

    public Song get(int index) {
        return heap[index];
    }

    public ArrayList<Song> toArray() {
        return new ArrayList<>(Arrays.asList(heap));
    }

    /** function to insert task **/


    public void insert(Song newSong)
    {
 
        heap[++heapSize] = newSong;
        int pos = heapSize;
        while (pos != 1 && newSong.getId() > heap[pos/2].getId())
        {
            heap[pos] = heap[pos/2];
            pos /=2;
        }
        heap[pos] = newSong;
    }
    public String[] getUrls(){
        String[] urls = new String[heapSize];
        for(int i=0;i<heapSize;i++){
            urls[i]=heap[i].getStreamUrl();
        }
        return urls;
    }
    /** function to remove task **/
    public Song remove()
    {
        int parent, child;
        Song item, temp;
        if (isEmpty() )
        {
            System.out.println("Heap is empty");
            return null;
        }
 
        item = heap[1];
        temp = heap[heapSize--];
 
        parent = 1;
        child = 2;
        while (child <= heapSize)
        {
            if (child < heapSize && heap[child].getId() < heap[child + 1].getId())
                child++;
            if (temp.getId() == heap[child].getId()) {
               for(int i=parent;i<=heapSize;){
                   heap[i]=heap[i+1];
                   i++;
               }
                break;
            }
            if (temp.getId() > heap[child].getId()) {
                heap[parent] = temp;
                break;
            }
 
            heap[parent] = heap[child];
            parent = child;
            child *= 2;
        }

 
        return item;
    }
}