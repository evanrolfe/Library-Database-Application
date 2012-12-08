import java.util.*;

public class Loan 
{
    public String deweyID;
    public Date issueDate;
    public Date dueDate;

	public int borrower_id;
	public String book_isbn;
	public String periodical_issn;

    public Loan(String deweyID, Date issueDate, int borrower_id, Item item)
    {
        this.deweyID = deweyID;
        this.issueDate = issueDate;
		this.borrower_id = borrower_id;

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
}
