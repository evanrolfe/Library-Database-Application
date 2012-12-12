import java.util.*;

public class Database
{
//==============================================================
// BORROWERS METHODS
//==============================================================

	/**
	 * Return all borrowers in the database
	 * @return All the borrowers in the database
     * @throws DataNotFoundException If there was a database issue
     * @throws InvalidArgumentException If there was a database issue
	 */
	public static ArrayList<Borrower> find_borrowers() throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getBorrowers();
	}

	/**
	 * Return a single borrower with the specified ID
	 *
	 * @param id The id of the borrower
	 * @return A borrower from the database
     * @throws DataNotFoundException If there was a database issue
     * @throws InvalidArgumentException If there was a database issue
	 */
	public static Borrower find_borrower(int id) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getBorrower(id);
	}

	/**
	 * Search the database for borrowers based on search input hashtable called params, assume that each key=>value pair
     * is of a correct column (i.e. one of id, forename, surname, email)
	 * @param params a hashtable with the search parameters where the key is the column and the value is the search value
	 * @return An array list of borrowers matching those options
     * @throws DataNotFoundException If there was a database issue
     * @throws InvalidArgumentException If there was a database issue
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
	 * Return all loans in the database
	 * @return All the loans in the database
     * @throws DataNotFoundException If there was a database issue
     * @throws InvalidArgumentException If there was a database issue
	 */
	public static ArrayList<Loan> find_loans() throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getLoans();
	}

	/**
	 * Return all loans in the database belonging to user with a certain id
	 * @param id The id of the borrower
	 * @return An array list of loans that relate to a borrower
     * @throws DataNotFoundException If there was a database issue
     * @throws InvalidArgumentException If there was a database issue
	 */
	public static ArrayList<Loan> find_loans(int id) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		Hashtable<String,Object> params = new Hashtable<String,Object>();
		params.put("borrowerID", id);
		return db.getLoans(params);	
	}

	/**
	 * Return all loans in the database with a dewey id
	 * @param deweyid The dewey id to search for
	 * @return An array list of loans that relate to a dewey id
     * @throws DataNotFoundException If there was a database issue
     * @throws InvalidArgumentException If there was a database issue
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
		}
        catch(IndexOutOfBoundsException e)
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
	 * @param copy The copy to issue
	 * @param borrower The borrower to issue a loan to
     * @throws Exception If there's some issue
	 */
	public static void issue_loan(Copy copy, Borrower borrower) throws LoanException, Exception
	{
		//1.
		if(copy.referenceOnly)
        {
			throw new LoanException("That copy  is marked for reference only!");
        }

		//2.
		if(copy.getLoan() != null)
        {
			throw new LoanException("That copy is already on loan!");		
        }

		//3.
		if(borrower.getLoans().size() > 5)
        {
			throw new LoanException("The borrower has reached their limit of six loans!");
        }

		//4.
		if(borrower.hasLoansOverDue() == true)
        {
            throw new LoanException("The borrower has over due loans!");
        }

		//5.
		//TODO: allow it if the borrower has reserved it and is first in the queue
		if(copy.item.isReserved())
        {
            throw new LoanException("That copy has already been reserved by another borrower!");
        }

		//MYSQL: insert the loan
	}

    /**
     * Renew an existing Loan
     * @param loan the loan to be renewed
     * @return True if the loan was renewed, false otherwise
     */
	public static void renew_loan(Loan loan) throws LoanException, Exception
	{
		//1. check copy has not been recalled
		//TODO: check that copy exists

		//2. check Borrower has no overdue loans
		Borrower b = Database.find_borrower(loan.borrower_id);
		if(b.hasLoansOverDue())
        {
            throw new LoanException("The borrower has over due loans!");
        }
	}

    /**
     * Discharge an existing loan
     * @param loan the loan to be deleted
     * @return true if the loan was delete, false otherwise
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
	 * @return All the items in the database
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
	 * @param params The search parameters i.e. Hashtable: {"author" => "J.K. Rowling", "title" => "Harry Potter"}
	 * @return An array list of loans
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
	 * @return All the copies relating to that issn
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
     * @param isbn The isbn to search or
     * @return All the copies with that isbn
     * @throws DataNotFoundException If that isbn wasn't found
     * @throws InvalidArgumentException If the search options were invalid
     */
	public static ArrayList<Copy> find_copies_by_isbn(String isbn) throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		Hashtable<String,Object> params = new Hashtable<String,Object>();
		params.put("isbn", isbn);
		return db.getCopies(params);
	}

    /**
     * Finds a copy with a specified dewey id
     * @param dewey The dewey id to search for
     * @return A single copy
     * @throws DataNotFoundException If that dewey wasn't found
     * @throws InvalidArgumentException If the search options were invalid
     */
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
     * @return An array list of reservations in the database
     * @throws DataNotFoundException If no reservations were found
     * @throws InvalidArgumentException If the search options were invalid
     */
	public static ArrayList<Reservation> find_reservations() throws DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();
		return db.getReservations();
	}

	/**
	 * Return all Reservations in the database belonging to a specified item
	 * @param item The item to find a reservation for
	 * @return The reservations that relates to that item
     * @throws DataNotFoundException If there are no reservations relating to that item
     * @throws InvalidArgumentException If the search options were invalid
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
	 * @param borrower_id the id of the borrower
     * @param item The item to cancel
	 * @throws ReservationException if there was an issue with reserving
     * @throws DataNotFoundException If there are no reservations in the database
     * @throws InvalidArgumentException If the search options were invalid
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
	 * Place a reservation on an item. If there are free copies then notify. Otherwise, the Copy that was loaned with the earliest issue
     * date is recalled and the Borrower is told to wait for a week
	 *
     * @param borrower_id The id of the borrower
	 * @param item The item to place the reservation on
     * @throws ReservationException If the reservation isn't valid
     * @throws DataNotFoundException If there was a database issue
     * @throws InvalidArgumentException If there was a database issue
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
