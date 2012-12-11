import org.joda.time.DateTime;
import java.util.Date;

//Loan class to  handle creation of loan objects and methods manipulating a current instance of the loan class.
public class Loan 
{
	//Initialise attributes associated with a loan.
    public String deweyID;
    public Date issueDate;
    public Date dueDate;

	public int borrower_id;
	public String book_isbn;
	public String periodical_issn;

	public Item item;
	public Copy copy;

	//Loan constructor to create a loan in which the copy being loaned is provided as a parameter.
    public Loan(String deweyID, Date issueDate, int borrower_id, Copy copy)
    {
        this.deweyID = deweyID;
        this.issueDate = issueDate;
		this.borrower_id = borrower_id;

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

    //Loan constructor to create a loan in which the specific copy being loaned is not provided.
    public Loan(String deweyID, Date issueDate, int borrower_id) throws DataNotFoundException, InvalidArgumentException
    {
		this(deweyID, issueDate, borrower_id, Database.find_copy_by_dewey(deweyID));
    }

    //OverDue method to calculate if the current instance of copy is overdue. 
    //This checks that the borrower has not held the item on laon longer than the loan period allows.
	public boolean overDue()
	{
		//This create the current date uisng the date constructor from the date class built into java.
		Date now = new Date();

		//If the duedate is after the current date the loan is overdue and returns true, else it returns false.
		if(this.dueDate.getTime() < now.getTime())
		{
			return true;
		}else{
			return false;
		}
	}

	//Method to calculate the fines that the borrower owes.
	public int getFine()
	{
		//If the current loan instance is overdue calculate the fine to charge the borrower based on how many days late the loan is.
		if(this.overDue()==true)
		{
			//return the amount the borrower owes in fines. (£1/day)
			return 1;
		}else{
			//If current instance of loan is not overdue the borrower has no fines for this loan.
			return 0;
		}
	}

	//Method to convert the current instance of loan into a string so it can be displayed suitably on screen.
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
