import edu.princeton.cs.algs4.StdIn;

public class Permutation {
	public static void main(String[] args) {
		RandomizedQueue<String> q = new RandomizedQueue<String>();

		Integer k = Integer.parseInt( args[0] );

		String[] s = StdIn.readAllStrings();

		for (int i = 0; i < s.length; i++ ) {
			q.enqueue(s[i]);
		}

		int limit = 0;

		for( String string : q ) {
			if( limit++ < k )
				System.out.println(string);
		}

	}
}