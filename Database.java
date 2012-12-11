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
	public static ArrayList<Borrower> find_borrowers() throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getBorrowers();
	}

	/**
	 * Return a single borrower with the specified ID
	 *
	 * @param id
	 * @return Borrower
	 */
	public static Borrower find_borrower(int id) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getBorrower(id);
	}

	/**
	 * Search the database for borrowers based on search input hashtable called params, assume that each key=>value pair is of a correct column (i.e. one of id, forename, surname, email)
	 *
	 * @param params a hashtable with the search parameters where the key is the column and the value is the search value
	 * @return ArrayList<Borrower>
	 */
	public static ArrayList<Borrower> find_borrowers(Hashtable params) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getBorrowers(params);
	}

//==============================================================
// LOANS METHODS
//==============================================================
	/**
	 * Return all LOANS in the database
	 *
	 * @return ArrayList<Loan>
	 */
	public static ArrayList<Loan> find_loans() throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getLoans();
	}

	/**
	 * Return all LOANS in the database belonging to user with id
	 *
	 * @param id	the id of the borrower 
	 * @return ArrayList<Loan>
	 */
	public static ArrayList<Loan> find_loans(int id) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		Hashtable<String,Object> params = new Hashtable<String,Object>();
		params.put("borrowerID", id);
		return db.getLoans(params);	
	}

	/**
	 * Return all LOANS in the database with a dewey id
	 *
	 * @param String deweyid
	 * @return ArrayList<Loan>
	 */
	public static Loan find_loans_by_deweyid(String deweyid) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		Hashtable<String,Object> params = new Hashtable<String,Object>();
		params.put("deweyID", deweyid);

		Loan loan;
		try
		{
			loan = db.getLoans(params).get(0);		
		}catch(IndexOutOfBoundsException e)
		{
			throw new DataNotFoundException("There is no loan with the dewey ID: "+deweyid+"!");
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
	public static ArrayList<Item> find_items() throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		ArrayList<Item> items = new ArrayList<Item>();	
	
		//Books
		items.addAll(db.getBooks());

		//Periodicals
		items.addAll(db.getPeriodicals());

		return items;
	}

	/**
	 * Return all items in the database which match the search criteria
	 *
	 * @param Hashtable	the search parameters i.e. Hashtable: {"author" => "J.K. Rowling", "title" => "Harry Potter"}
	 * @return ArrayList<Loan>
	 */
	public static ArrayList<Item> find_items(Hashtable params) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		ArrayList<Item> items = new ArrayList<Item>();	
	
// TODO:
		//Books
		items.addAll(db.getBooks(params));

		//Periodicals
		items.addAll(db.getPeriodicals(params));

		return items;
	}


//==============================================================
// COPIES METHODS
//==============================================================
	/**
	 * Return all copies in the database
	 *
	 * @return ArrayList<Copy>
	 */
	public static ArrayList<Copy> find_copies() throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getCopies();
	}

	/**
	 * Return all copies with the specified issn in the database (for use with periodicals only)
	 *
	 * @return ArrayList<Copy>
	 */
	public static ArrayList<Copy> find_copies_by_issn(String issn) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		Hashtable<String,Object> params = new Hashtable<String,Object>();
		params.put("issn", issn);
		return db.getCopies(params);
	}

	/**
	 * Return all copies with the specified isbn in the database (for use with books only)
	 *
	 * @return ArrayList<Copy>
	 */
	public static ArrayList<Copy> find_copies_by_isbn(String isbn) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		Hashtable<String,Object> params = new Hashtable<String,Object>();
		params.put("isbn", isbn);
		return db.getCopies(params);
	}
	
	public static Copy find_copy_by_dewey(String dewey) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getCopy(dewey);
	}

//==============================================================
// RESERVATIONS METHODS
//==============================================================
	/**
	 * Return all Reservations in the database
	 *
	 * @return ArrayList<Reservation>
	 */
	public static ArrayList<Reservation> find_reservations() throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getReservations();
	}

	/**
	 * Return all Reservations in the database belonging to a specified item
	 *
	 * @param id	the id of the borrower 
	 * @return ArrayList<Loan>
	 */
	public static ArrayList<Reservation> find_reservations(Item item) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		ArrayList<Reservation> all_reservations = Database.find_reservations();
		ArrayList<Reservation> result = new ArrayList<Reservation>();
		Hashtable<String,Object> params = new Hashtable<String,Object>();

		if(item.getType() == "Book")
		{
			params.put("isbn", item.isbn);			
		}else{
			params.put("issn", item.issn);		
		}

		return db.getReservations(params);
	}

	/**
	 * Cancel a reservation
	 *
	 * @param id	the id of the borrower 
	 * @return ArrayList<Loan>
	 */
	public static void cancel_reservation(int borrower_id, Item item) throws ReservationException, DataNotFoundException, InvalidArgumentException
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
	public static void place_reservation(int borrower_id, Item item) throws ReservationException, DataNotFoundException, InvalidArgumentException
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
