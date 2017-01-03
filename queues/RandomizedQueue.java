import java.util.*;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item ts;

	private Item[] q;

	private Integer size = 0;

	// construct an empty randomized queue
	public RandomizedQueue() {
		q = (Item[]) new Object[1];
	}

	// is the queue empty?            
	public boolean isEmpty() {
		return size == 0;
	}	

	// return the number of items on the queue              
	public int size() {
		return size;
	}

	// add the item                 
	public void enqueue(Item item) {

		if( item == null ) throw new NullPointerException();

		// Double array
		if (size == q.length) resize(2 * q.length);

		q[size++] = item;
	}

	private void resize(int capacity) {
		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < size; i++)
			copy[i] = q[i];
		q = copy;
	}

	// remove and return a random item
	public Item dequeue() {

		if( isEmpty() ) throw new NoSuchElementException();

		int random = StdRandom.uniform(size);

		Item item = q[random];

		q[random] = q[--size];
		q[size] = null;

		if (size > 0 && size == q.length/4) resize(q.length/2);

		return item;
	}

	// return (but do not remove) a random item    
	public Item sample() {
		if( isEmpty() ) throw new NoSuchElementException();

		int random = StdRandom.uniform(size);

		Item item = q[random];

		return item;
	}	 

	// return an iterator over items in order from front to end
	public Iterator<Item> iterator() { 
		return new ListIterator(); 
	}
	

	private class ListIterator implements Iterator<Item> {

		private Integer k = 0;

		private Item[] temp;

		public ListIterator() {

			temp = (Item[]) new Object[size];

			for (int i = 0; i < size; i++ ) {
				temp[i] = q[i];
			}

			StdRandom.shuffle(temp);

		}

		public boolean hasNext() {
			return k < temp.length && temp[k] != null;
		}

		public Item next() {
			if( temp[k] == null) throw new NoSuchElementException();
			return temp[k++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}


	// unit testing     
	public static void main(String[] args) {

		RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();

		q.enqueue(1);
		q.enqueue(2);
		q.enqueue(3);

		System.out.println(q.size());

		for( Integer i : q) {
			System.out.println(i);
			try {
			    Thread.sleep(100);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	}
}