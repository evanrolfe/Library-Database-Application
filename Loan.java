import java.util.*;

public class Loan 
{
    public String deweyID;
    public Date issueDate;
    public Date dueDate;

	public int borrower_id;
	public String book_isbn;
	public String periodical_issn;

	public Item item;
	public Copy copy;

    public Loan(String deweyID, Date issueDate, int borrower_id, Copy copy)
    {
        this.deweyID = deweyID;
        this.issueDate = issueDate;
		this.borrower_id = borrower_id;

		this.item = copy.item;
		this.copy = copy;

		if(item.getType() == "Book")
		{
			this.book_isbn = item.isbn;			
		}else if(item.getType() == "Periodical")
		{
			this.periodical_issn = item.issn;			
		}

		long timestamp = issueDate.getTime()+(3*7*24*3600*1000);
		this.dueDate = new Date(timestamp);
    }

    public Loan(String deweyID, Date issueDate, int borrower_id) throws DataNotFoundException
    {
		this(deweyID, issueDate, borrower_id, Database.find_copies_by_dewey(deweyID));
    }

	public boolean overDue()
	{
		Date now = new Date();

		if(this.dueDate.getTime() > now.getTime())
		{
			return true;
		}else{
			return false;
		}
	}

	public int getFine()
	{
		if(this.overDue()==true)
		{
			//TODO: workout the difference in days (£1/day) to calculate the fine
			return 0;
		}else{
			return 0;
		}
	}

	public String toString()
	{
		return "Loan:"+this.deweyID+"\t Issue Date: "+this.issueDate+"\t "+this.dueDate+"\t Is loan overdue? "+this.overDue();
	}
}
