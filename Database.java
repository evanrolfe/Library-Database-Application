import java.util.*;

public class Database
{
//==============================================================
// BORROWERS METHODS
//==============================================================

	/**
	 * Return all borrowers in the database
	 *
	 * @return ArrayList<Borrower>
	 */
	public static ArrayList<Borrower> find_borrowers()
	{
		ArrayList<Borrower> borrowers = new ArrayList<Borrower>();

		//EXAMPLE BY EVAN (ONLY FOR TESTING)
			borrowers.add(new Borrower(1, "Evan", "Rolfe", "evanrolfe@hotmail.com"));
			borrowers.add(new Borrower(2, "Patrick", "Rose", "prose@gmail.com"));
			borrowers.add(new Borrower(3, "Jake", "Grover", "jg@hotmail.com"));
			borrowers.add(new Borrower(4, "Nikhil", "Dodhia", "aca10nd@shef.ac.uk"));
			borrowers.add(new Borrower(5, "Jordan", "Millner", "aca10jm@shef.ac.uk"));
			borrowers.add(new Borrower(6, "Evan", "Millner", "aca10jm@shef.ac.uk"));

		return borrowers;
	}

	/**
	 * Return a single borrower with the specified ID
	 *
	 * @param id
	 * @return Borrower
	 */
	public static Borrower find_borrowers(int id)
	{
		//EXAMPLE BY EVAN (ONLY FOR TESTING)
		Borrower result = new Borrower(3, "Jake", "Grover", "jg@hotmail.com");

		return result;
	}

	/**
	 * Search the database for borrowers based on search input hashtable called params, assume that each key=>value pair is of a correct column (i.e. one of id, forename, surname, email)
	 *
	 * @param params a hashtable with the search parameters where the key is the column and the value is the search value
	 * @return ArrayList<Borrower>
	 */
	public static ArrayList<Borrower> find_borrowers(Hashtable params) throws InvalidArgumentException
	{
		ArrayList<Borrower> borrowers = new ArrayList<Borrower>();

		Set set = params.entrySet();
		Iterator it = set.iterator();

		//This is used to check that the right search fields have been inputted
		String[] columns = new String[] { "id", "forename", "surname", "email" };
		Arrays.sort(columns);

		//1. Instantiate result arraylist
		ArrayList<Borrower> result = new ArrayList<Borrower>();

		//2. Iteratate through each search field and filter out borrowers accordingly
		while(it.hasNext())
		{			
			Map.Entry row = (Map.Entry) it.next();

			//3. In case this method was called with a field that does not exist for Borrower
			if(Arrays.binarySearch(columns, row.getKey()) < 0)
				throw new InvalidArgumentException("The search field you have entered: '"+row.getKey()+"' is not a valid borrower field!");
		}

		//MYSQL: Should fetch borrowers from database, then create a new Borrower object for each one and push it to the borrowers ArrayList

		//EXAMPLE BY EVAN (ONLY FOR TESTING) (DELETE THIS WHEN MYSQL IS IMPLEMENTED)
			borrowers.add(new Borrower(3, "Jake", "Grover", "jg@hotmail.com"));
			borrowers.add(new Borrower(6, "Evan", "Millner", "aca10jm@shef.ac.uk"));

		return borrowers;
	}

//==============================================================
// LOANS METHODS
//==============================================================
	/**
	 * Return all LOANS in the database
	 *
	 * @return ArrayList<Loan>
	 */
	public static ArrayList<Loan> find_loans()
	{
		ArrayList<Loan> loans = new ArrayList<Loan>();
		ArrayList<Borrower> borrowers = Borrowers.find_all();
		ArrayList<Item> items = Items.find_all();

		Date today = new Date();

		loans.add(new Loan("1", new Date(), borrowers.get(0).id, items.get(0)));
		loans.add(new Loan("2", new Date(), borrowers.get(0).id, items.get(2)));
		loans.add(new Loan("3", new Date(), borrowers.get(4).id, items.get(1)));

		loans.add(new Loan("4", new Date(), borrowers.get(1).id, items.get(3)));
		loans.add(new Loan("5", new Date(), borrowers.get(1).id, items.get(4)));
		loans.add(new Loan("6", new Date(today.getTime()-11*7*24*3600*1000), borrowers.get(2).id, items.get(5)));

		return loans;
	}

	/**
	 * Return all LOANS in the database belonging to user with id
	 *
	 * @param id	the id of the borrower 
	 * @return ArrayList<Loan>
	 */
	public static ArrayList<Loan> find_loans(int id)
	{
		ArrayList<Loan> all_loans = Database.find_loans();
		
		ArrayList<Loan> result = new ArrayList<Loan>();

		for(int i=0; i<all_loans.size(); i++)
		{
			if(all_loans.get(i).borrower_id == id)
			{
				result.add(all_loans.get(i));
			}
		}

		return result;		
	}

	/**
	 * Return all LOANS in the database belonging to user with id
	 *
	 * @param id	the id of the borrower 
	 * @return ArrayList<Loan>
	 */
	public static ArrayList<Loan> find_loans_by_deweyid(String id)
	{
		ArrayList<Loan> all_loans = Database.find_loans();
		
		ArrayList<Loan> result = new ArrayList<Loan>();

		for(int i=0; i<all_loans.size(); i++)
		{
			if(all_loans.get(i).deweyID == id)
			{
				result.add(all_loans.get(i));
			}
		}

		return result;		
	}
//==============================================================
// ITEMS METHODS
//==============================================================

	/**
	 * Return all items in the database
	 *
	 * @return ArrayList<Item>
	 */
	public static ArrayList<Item> find_items()
	{
		ArrayList<Item> items = new ArrayList<Item>();	
	
		//Books
		items.add(new Item("0-000000-00-1", "Fifty Shades of Gray", "E. L. James", "Vintage Books", new Date()));
		items.add(new Item("0-000000-00-2", "Crime and Punishment", "Fyoder Dostoyevsky", "Penguin", new Date()));
		items.add(new Item("0-000000-00-3", "Wuthering Heights", "Emily Bronte", "Random House", new Date()));
		items.add(new Item("0-000000-00-4", "Beyond Good and Evil", "Friedrich Nietzsche", "Penguin", new Date()));

		//Periodicals
		items.add(new Item("0-000000-00-1", "Harvard Medical Review", 29, 1, "Harvard Press", new Date()));
		items.add(new Item("0-000000-00-2", "Oxford Mathematics", 69, 2, "Oxford Press", new Date()));
		items.add(new Item("0-000000-00-3", "Modern Physics", 1, 1, "Oxford Press", new Date()));

		return items;
	}

	/**
	 * Return all items in the database which match the search criteria
	 *
	 * @param id	the id of the borrower 
	 * @return ArrayList<Loan>
	 */
	public static ArrayList<Item> find(Hashtable params) throws InvalidArgumentException
	{
		ArrayList<Item> result = Database.find_items();

		return result;
	}


//==============================================================
// COPIES METHODS
//==============================================================
	/**
	 * Return all copies in the database
	 *
	 * @return ArrayList<Copy>
	 */
	public static ArrayList<Copy> find_copies()
	{
		ArrayList<Item> items = Database.find_items();
		ArrayList<Copy> copies = new ArrayList<Copy>();

		//Add one copies of each book/periodical
		for(int i=0; i<items.size(); i++)
		{
			copies.add(new Copy(""+i, false, items.get(i)));		
		}

		return copies;
	}

	/**
	 * Return all copies with the specified issn in the database (for use with periodicals only)
	 *
	 * @return ArrayList<Copy>
	 */
	public static ArrayList<Copy> find_copies_by_issn(String issn)
	{
		ArrayList<Copy> copies = Database.find_copies();
		ArrayList<Copy> result = new ArrayList<Copy>();

		for(int i=0; i<copies.size(); i++)
		{	
			if(copies.get(i).periodical_issn != null && copies.get(i).periodical_issn.equals(issn))
			{
				result.add(copies.get(i));
			}
		}

		return result;
	}

	/**
	 * Return all copies with the specified isbn in the database (for use with books only)
	 *
	 * @return ArrayList<Copy>
	 */
	public static ArrayList<Copy> find_copies_by_isbn(String isbn)
	{
		ArrayList<Copy> copies = Database.find_copies();
		ArrayList<Copy> result = new ArrayList<Copy>();

		for(int i=0; i<copies.size(); i++)
		{
			if(copies.get(i).book_isbn != null && copies.get(i).book_isbn.equals(isbn))
			{
				result.add(copies.get(i));
			}
		}

		return result;
	}
}
