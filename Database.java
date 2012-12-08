import java.util.*;

public class Database
{
	MysqlConnection db = new MysqlConnection();

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
	public static Borrower find_borrower(int id)
	{
		ArrayList<Borrower> borrowers = Database.find_borrowers();

		for(int i=0; i<borrowers.size(); i++)
		{
			if(borrowers.get(i).id == id)
			{
				return borrowers.get(i);
			}
		}

		return borrowers.get(0);
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
		ArrayList<Borrower> borrowers = Database.find_borrowers();
		ArrayList<Copy> items = Database.find_copies();

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
	 * Return all LOANS in the database with a dewey id
	 *
	 * @param id	the dewey id 
	 * @return ArrayList<Loan>
	 */
	public static Loan find_loans_by_deweyid(String id) throws DataNotFoundException
	{
		ArrayList<Loan> all_loans = Database.find_loans();
		Loan loan = null;

		for(int i=0; i<all_loans.size(); i++)
		{
			if(all_loans.get(i).deweyID.equals(id))
			{
				loan = all_loans.get(i);
			}
		}
		
		if (loan == null)
		{
			throw new DataNotFoundException("The copy with this dewey number is not on loan.");
		}

		return loan;		
	}

	/**
	 * Creates a new loan of a Copy to a Borrower, subject to rules that:
	 *	1. Copy is not reference only
	 * 	2. Copy is not already on loan
	 * 	3. Borrower has less than six copies currently on loan
	 * 	4. Borrower has no overdue loans
	 *	5. Copy is not reserved by another borrower
	 * ALSO: If the borrower is first in the queue of reservations then the loan will be issued and the reservation deleted
	 *
	 * @param Copy 
	 * @param Borrower 
	 */
	public static void issue_loan(Copy copy, Borrower borrower) throws LoanException, Exception
	{
		//1.
		if(copy.referenceOnly == true)
			throw new LoanException("That copy  is marked for reference only!");

		//2.
		if(copy.getLoan() != null)
			throw new LoanException("That copy is already on loan!");		

		//3.
		if(borrower.getLoans().size() > 5)
			throw new LoanException("The borrower has reached their limit of six loans!");
		
		//4.
		if(borrower.hasLoansOverDue() == true)
			throw new LoanException("The borrower has over due loans!");

		//5.
		//TODO: allow it if the borrower has reserved it and is first in the queue
		if(copy.item.isReserved() == true)
			throw new LoanException("That copy has already been reserved by another borrower!");

		//MYSQL: insert the loan
	}

/**
 * Renew an existing Loan
 * @param Loan the loan to be renewed
 * @return Boolean was the loan renewed?
 */
	public static void renew_loan(Loan loan) throws LoanException, Exception
	{
		//1. check copy has not been recalled
		//TODO: check that copy exists

		//2. check Borrower has no overdue loans
		Borrower b = Database.find_borrower(loan.borrower_id);
		if(b.hasLoansOverDue()==true)
			throw new LoanException("The borrower has over due loans!");
	}

/**
 * Discharge an existing loan
 * @param Loan the loan to be deleted
 * @return Boolean was the loan deleted?
 */
	public static void delete_loan(Loan loan)
	{
		//If the Loan is overdue then there must be no fines due to be paid for the Loan

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
		items.add(new Item("0-330000-00-1", "Harvard Medical Review", 29, 1, "Harvard Press", new Date()));
		items.add(new Item("0-777000-00-2", "Oxford Mathematics", 69, 2, "Oxford Press", new Date()));
		items.add(new Item("0-666000-00-3", "Modern Physics", 1, 1, "Oxford Press", new Date()));

		return items;
	}

	/**
	 * Return all items in the database which match the search criteria
	 *
	 * @param Hashtable	the search parameters i.e. Hashtable: {"author" => "J.K. Rowling", "title" => "Harry Potter"}
	 * @return ArrayList<Loan>
	 */
	public static ArrayList<Item> find_items(Hashtable params) throws InvalidArgumentException
	{
		//1. Instantiate result arraylist
		ArrayList<Item> result = Database.find_items();

		Set set = params.entrySet();
		Iterator it = set.iterator();

		//1. Check that the right input has been given i.e. it is either a valid Book field or a valid Periodical field
		String[] book_fields = new String[] { "isbn", "title", "author", "publisher", "date" };
		Arrays.sort(book_fields);
		String[] periodical_fields = new String[] { "issn", "title", "volumne","number", "publisher", "date" };
		Arrays.sort(periodical_fields);

		while(it.hasNext())
		{			
			Map.Entry row = (Map.Entry) it.next();

			//If the search field is valid for neither books nor periodicals then throw exception
			if(Arrays.binarySearch(book_fields, row.getKey()) < 0 && Arrays.binarySearch(periodical_fields, row.getKey()) < 0)
				throw new InvalidArgumentException("The search field you have entered: '"+row.getKey()+"' is not a valid borrower field!");

			//TODO: Identify if this is a book or a periodical (based on which search fields appear)
		}

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
			copies.add(new Copy(""+(i+1), false, items.get(i)));		
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
	
	public static Copy find_copies_by_dewey(String dewey) throws DataNotFoundException
	{
		ArrayList<Copy> copies = Database.find_copies();
		Copy copy = null;

		for(int i=0; i<copies.size(); i++)
		{
			if(copies.get(i).deweyIndex != null && copies.get(i).deweyIndex.equals(dewey))
			{
				copy = copies.get(i);
			}
		}
		
		if (copy == null)
			throw new DataNotFoundException("No copy with this dewey number");

		return copy;
	}

//==============================================================
// RESERVATIONS METHODS
//==============================================================
	/**
	 * Return all Reservations in the database
	 *
	 * @return ArrayList<Reservation>
	 */
	public static ArrayList<Reservation> find_reservations()
	{
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		ArrayList<Borrower> borrowers = Database.find_borrowers();
		ArrayList<Item> items = Database.find_items();

		Date today = new Date();

		reservations.add(new Reservation(new Date(), 1, items.get(0)));			
		reservations.add(new Reservation(new Date(), 1, items.get(1)));
		reservations.add(new Reservation(new Date(), 3, items.get(1)));
		reservations.add(new Reservation(new Date(), 3, items.get(4)));
		reservations.add(new Reservation(new Date(), 4, items.get(4)));

		return reservations;
	}

	/**
	 * Return all Reservations in the database belonging to a specified item
	 *
	 * @param id	the id of the borrower 
	 * @return ArrayList<Loan>
	 */
	public static ArrayList<Reservation> find_reservations(Item item)
	{
		ArrayList<Reservation> all_reservations = Database.find_reservations();
		ArrayList<Reservation> result = new ArrayList<Reservation>();

		for(int i=0; i<all_reservations.size(); i++)
		{
			//If it is a book then compare isbn's 
			if(all_reservations.get(i).item.getType() == "Book")
			{
			
				if(all_reservations.get(i).item.isbn.equals(item.isbn))
					result.add(all_reservations.get(i));
	
			//Else if it is a periodical then compare issn's
			}else{

				if(all_reservations.get(i).item.issn.equals(item.issn))
					result.add(all_reservations.get(i));
			}
		}

		return result;		
	}

	/**
	 * Cancel a reservation
	 *
	 * @param id	the id of the borrower 
	 * @return ArrayList<Loan>
	 */
	public static void cancel_reservation(int borrower_id, Item item) throws ReservationException
	{
		if(item.getType() == "Book")
		{
			//Delete by borrower_id and isbn
		}else{
			//Delete by borrower_id and issn			
		}
	}

	/**
	 * Place a reservation on an item. If there are free copies then notify. Otherwise, the Copy that was loaned with the earliest issue 		 * date is recalled and the Borrower is told to wait for a week
	 *
	 * @param id	the id of the borrower 
	 * @return ArrayList<Loan>
	 */
	public static void place_reservation(int borrower_id, Item item) throws ReservationException
	{
		ArrayList<Copy> copies = item.getCopies();
		
		//1. Find the first free copy
		Copy free_copy = null; 
		for(int i=0; i<copies.size(); i++)
		{
			if(copies.get(i).onLoan() == false)
			{
				free_copy = copies.get(i);
				break;
			}
		}

		//If there are free copies then notify
		if(free_copy != null)
		{
			throw new ReservationException("There are already copies of this item available!");

		//Otherwise the Copy that was loaned with the earliest issue is recalled and Borrower is notified
		}else{
			//MYSQL: code goes here to add a new reservation
		}
	}
}
