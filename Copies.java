import java.util.*;

public class Copies
{
	private ArrayList<Copy> copies;
/*
	public static void main(String[] args)
	{
		ArrayList<Copy> c = Copies.find_all();
		System.out.println("# number of copies: "+c.size());
	}

	private static ArrayList<Copy> generate()
	{
		ArrayList<Book> books = Books.find_all();
		ArrayList<Periodical> periodicals = Periodicals.find_all();
		ArrayList<Copy> copies = new ArrayList<Copy>();

		//Add two copies of each book
		int i;
		int count = 1; 
		for(i = 0; i<books.size(); i++)
		{
			copies.add(new Copy(""+count, false, books.get(i).isbn, null));
 			count++;
			copies.add(new Copy(""+count, false, books.get(i).isbn, null));
 			count++;		
		}

		//Add two copies of each periodical
		System.out.println(count);
		for(int j=0; j<periodicals.size(); j++)
		{
			copies.add(new Copy(""+count, false, null, periodicals.get(j).issn));
 			count++;
			copies.add(new Copy(""+count, false, null, periodicals.get(j).issn));
 			count++;		
		}

		return copies;
	}

	public static ArrayList<Copy> find_all()
	{
		return Copies.generate();
	}
*/
}
