// A Java program to print all subsets of a set
import java.io.IOException;

class test
{
	// Print all subsets of given set[]
	static void printSubsets(char set[])
	{
		int n = set.length;

		// Run a loop for printing all 2^n
		// subsets one by obe
		System.out.println(1<<n);
	}

	// Driver code
	public static void main(String[] args)
	{
		char set[] = {'a', 'b', 'c'};
		printSubsets(set);
	}
}
