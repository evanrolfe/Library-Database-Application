import java.util.*;

public class Copy
{
	public String deweyIndex;
	public boolean referenceOnly;
	public String book_isbn;
	public String periodical_issn;

	private Item item;

	public Copy(String deweyIndex, boolean referenceOnly, Item item)
	{
		this.deweyIndex = deweyIndex;
		this.referenceOnly = referenceOnly;

		if(item.getType() == "Book")
		{
			this.book_isbn = item.isbn;			
		}else if(item.getType() == "Periodical")
		{
			this.periodical_issn = item.issn;			
		}

		this.item = item;
	}

	public Item getItem()
	{
		return item;
	}

	public ArrayList<Loan> getLoans()
	{
		ArrayList<Loan> loans = Database.find_loans_by_deweyid(this.deweyIndex);
		return loans;
	}

	public String toString()
	{
		if(this.item.getType() == "Book")
		{
			return "B: "+this.deweyIndex+"\t #Loans: "+this.getLoans().size();
		}else{
			return "P: "+this.deweyIndex+"\t #Loans: "+this.getLoans().size();
		}
	}
}
