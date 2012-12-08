import java.util.*;

public class Copy
{
	public String deweyIndex;
	public boolean referenceOnly;
	public String book_isbn;
	public String periodical_issn;

	public Item item;

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

	public boolean onLoan()
	{
		ArrayList<Loan> loans = this.getLoans();

		if(loans.size() > 0)
		{
			return true;
		}else{
			return false;
		}
	}

	public Loan getLoans()
	{
		Loan loan = null;
		try {
			loan = Database.find_loans_by_deweyid(this.deweyIndex);
			return loan;
		} 
		catch (DataNotFoundException e) 
		{
			// TODO 
		}
		return loan;
	}

	public String toString()
	{
		if(this.item.getType() == "Book")
		{
			return "B: "+this.deweyIndex+"\t ISBN: "+this.item.isbn+"\t is it on loan? "+this.onLoan();
		}else{
			return "P: "+this.deweyIndex+"\t ISSN: "+this.item.issn+"\t is it on loan? "+this.onLoan();
		}
	}
}
