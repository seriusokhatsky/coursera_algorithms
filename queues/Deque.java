import java.util.*;
import java.lang.NullPointerException;

public class Deque<Item> implements Iterable<Item> {

	private Node first = null;

	private Node last = null;

	private int size = 0;

	// construct an empty deque
	public Deque() {
		first = new Node();
		last = new Node();

		clean();

	}

	// add the item to the front
	public void addFirst(Item item) {

		if( item == null ) throw new java.lang.NullPointerException();

		size++;

		Node oldFirst = first;

		first = new Node();

		first.next = oldFirst;
		first.item = item;
		first.prev = null;

		oldFirst.prev = first;

		if( last.item == null ) {
			last = first;
		}

	}

	// add the item to the end
	public void addLast(Item item) {
		if( item == null ) throw new NullPointerException();
	
		size++;

		Node oldLast = last;

		last = new Node();

		last.prev = oldLast;
		last.item = item;
		last.next = null;

		oldLast.next = last;

		if( first.item == null ) {
			first = last;
		}
		
	}

	private void clean() {
		last.prev = null;
		last.item = null;
		last.next = null;

		first.prev = null;
		first.item = null;
		first.next = null;
	}

	// remove and return the item from the front
	public Item removeFirst() {
		if( isEmpty() ) throw new NoSuchElementException();

		Item removedItem = first.item;

		if( first.item == null ) return null;

		if( size() == 1 ) {
			clean();
		} else {
			first = first.next;
			first.prev = null;
		}

		size--;

		return removedItem;
	}

	// remove and return the item from the end
	public Item removeLast() {
		if( isEmpty() ) throw new NoSuchElementException();

		if( first.item == null ) return null;

		Item removedItem = last.item;

		if( size() == 1 ) {
			clean();
		} else {
			last = last.prev;
			last.next = null;
		}

		size--;

		return removedItem;
	}

	// return an iterator over items in order from front to end
	public Iterator<Item> iterator() { 
		return new ListIterator(); 
	}
	

	private class ListIterator implements Iterator<Item> {

		private Node current = first;

		public boolean hasNext() {
			return current.item != null;
		}

		public Item next() {
			if( ! hasNext() ) throw new NoSuchElementException();

			Item item = current.item;
			if( current.next == null ) {
				current.item = null;
			} else {
				current = current.next;
			}
			return item;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}	

	public static void main(String[] args) {

		// Create a Deque
		Deque<Integer> deque = new Deque<Integer>();
		deque.addLast(1);
		deque.addFirst(2);

		// Show all Deque
		for( Integer i : deque) {
			System.out.println(i);
			try {
			    Thread.sleep(100);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	}


	private class Node {
		public Node next;
		public Node prev;
		public Item item;
	}

	// is the deque empty?                       
	public boolean isEmpty() {
		return size == 0;
	}

	// return the number of items on the deque
	public int size() {
		return size;
	}
}