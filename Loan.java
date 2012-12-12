import org.joda.time.DateTime;
import org.joda.time.Days;
import java.util.Date;

/**
 * Loan class to  handle creation of loan objects and methods manipulating a current instance of the loan class.
 */
public class Loan 
{
	/**
	 * The deweyID is the unique identifier of a copy.
	 */
    public String deweyID;
    
    
    /**
     * The date the loan was issued.
     */
    public Date issueDate;
    
    /**
     * The date the loan is due to be returned to the library.
     */
    public Date dueDate;
    
    /**
     * Whether or not the loan has been recalled.
     */
	public boolean recalled;

	/**
	 * The unique borrowers identification number.
	 */
	public int borrower_id;
	
	/**
	 * The isbn number uniquely identifies a book.
	 */
	public String book_isbn;
	
	/**
	 * The issn number uniquely identifies a periodical.
	 */
	public String periodical_issn;

	/**
	 * The item relating to the copy that is being loaned.
	 */
	public Item item;
	
	/**
	 * The copy that is being loaned.
	 */
	public Copy copy;

	/**
	 * Loan constructor to create a loan in which the copy being loaned is provided as a parameter.
	 * @param deweyID The deweyID is the unique identifier of a copy.
	 * @param issueDate The date the loan was issued.
	 * @param borrower_id The unique borrowers identification number.
	 * @param copy The copy that is being loaned.
	 * @param recalled Whether or not the loan has been recalled.
	 */
    public Loan(String deweyID, Date issueDate, int borrower_id, Copy copy, boolean recalled)
    {
        this.deweyID = deweyID;
        this.issueDate = issueDate;
		this.borrower_id = borrower_id;
		this.recalled = recalled;

		this.item = copy.item;
		this.copy = copy;

		if(item.getType().equals("Book"))
		{
			this.book_isbn = item.isbn;			
		}
        else if(item.getType().equals("Periodical"))
		{
			this.periodical_issn = item.issn;			
		}

		//long timestamp = issueDate.getTime()+(3*7*24*3600*1000);

		
        DateTime dueDate = new DateTime(issueDate.getTime());
        this.dueDate = new Date(dueDate.plusWeeks(3).getMillis());
    }

    /**
     * Loan constructor to create a new loan object in which the specific copy being loaned is provided.
     * @param deweyID The deweyID is the unique identifier of a copy.
     * @param issueDate The date the loan was issued.
     * @param borrower_id The unique borrowers identification number.
     * @param copy The copy that is being loaned.
     */
    public Loan(String deweyID, Date issueDate, int borrower_id, Copy copy)
	{
		this(deweyID, issueDate, borrower_id, copy, false);
	}

    /**
     * Loan constructor to create a new loan object in which the specific copy being loaned is not provided.
     * @param deweyID The deweyID is the unique identifier of a copy.
     * @param issueDate The date the loan was issued.
     * @param borrower_id  The unique borrowers identification number.
     * @throws DataNotFoundException Thrown if no deweyID is present in the database.
     * @throws InvalidArgumentException Thrown if arguments given to constructor are not valid.
     */
    public Loan(String deweyID, Date issueDate, int borrower_id) throws DataNotFoundException, InvalidArgumentException
    {
		this(deweyID, issueDate, borrower_id, Database.find_copy_by_dewey(deweyID));
    }

    /**
     * Loan constructor to create a new loan object in which the specific copy being loaned is not provided.
     * @param deweyID The deweyID is the unique identifier of a copy.
     * @param issueDate The date the loan was issued.
     * @param borrower_id The unique borrowers identification number.
     * @param recalled Whether or not the loan has been recalled.
     * @throws DataNotFoundException Thrown if no deweyID is present in the database.
     * @throws InvalidArgumentException Thrown if arguments given to constructor are not valid.
     */
    public Loan(String deweyID, Date issueDate, int borrower_id, boolean recalled) throws DataNotFoundException, InvalidArgumentException
    {
		this(deweyID, issueDate, borrower_id, Database.find_copy_by_dewey(deweyID), recalled);
    }

    /**
     * Loan constructor to create a new loan object.
     * @param deweyID The deweyID is the unique identifier of a copy.
     * @param issueDate The date the loan was issued.
     * @param dueDate The date the loan is due to be returned to the library.
     * @param borrowerID The unique borrowers identification number.
     * @param recalled Whether or not the loan has been recalled.
     * @throws DataNotFoundException Thrown if no deweyID is present in the database.
     * @throws InvalidArgumentException Thrown if arguments given to constructor are not valid.
     */
    public Loan(String deweyID, Date issueDate, Date dueDate, int borrowerID, boolean recalled) throws  DataNotFoundException, InvalidArgumentException
    {
        this(deweyID, issueDate, dueDate, borrowerID, Database.find_copy_by_dewey(deweyID), recalled);
    }

    /**
     * Loan constructor to create a new loan in which all possible parameters are provided.
     * @param deweyID The deweyID is the unique identifier of a copy.
     * @param issueDate The date the loan was issued.
     * @param dueDate The date the loan is due to be returned to the library.
     * @param borrowerID The unique borrowers identification number.
     * @param copy The copy that is being loaned.
     * @param recalled Whether or not the loan has been recalled.
     */
    public Loan(String deweyID, Date issueDate, Date dueDate, int borrowerID, Copy copy, boolean recalled)
    {
        this(deweyID, issueDate, borrowerID, copy, recalled);
        this.dueDate = dueDate;
    }

    /**
     * OverDue method to calculate if the current instance of copy is overdue. 
     * @return Boolean true if current loan instance is overdue and false if it is not overdue.
     */
    //This checks that the borrower has not held the item on laon longer than the loan period allows.
	public boolean overDue()
	{
		//This create the current date uisng the date constructor from the date class built into java.
		Date now = new Date();

		//If the duedate is after the current date the loan is overdue and returns true, else it returns false.
		if(now.after(dueDate))
		{
			return true;
		}
        else
        {
			return false;
		}
	}

	/**
	 * Method to calculate the fines that the borrower owes.
	 * @return int the amount in fines the borrower has to pay.
	 */
	public int getFine()
	{
		//If the current loan instance is overdue calculate the fine to charge the borrower based on how many days late the loan is.
		if(this.overDue()==true)
		{
			//return the amount the borrower owes in fines. (£1/day)
			Days d = Days.daysBetween(new DateTime(this.dueDate.getTime()), new DateTime());
			int days = d.getDays();
			return days;
		}else{
			//If current instance of loan is not overdue the borrower has no fines for this loan.
			return 0;
		}
	}

	/**
	 * Method to convert the current instance of loan into a string so it can be displayed suitably on screen.
	 */
	public String toString()
	{
		String out = "Loan:"+this.deweyID+"\t Issue Date: "+this.issueDate+"\t Due date: "+this.dueDate;

		if(this.overDue())
		{
			out += "\tOVERDUE, fines due: "+this.getFine();
		}

		return out;
	}
}
