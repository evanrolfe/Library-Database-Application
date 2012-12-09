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

	//This constructor can be used if you have the issn/isbn of the item but you do not have the Item object
	public Copy(String deweyIndex, boolean referenceOnly, String identify, String type)
	{
		this.deweyIndex = deweyIndex;
		this.referenceOnly = referenceOnly;

		//TODO: item is not defined

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

	public boolean onLoan() throws DataNotFoundException, InvalidArgumentException
	{
		Loan loan = this.getLoan();

		if(loan == null)
		{
			return true;
		}else{
			return false;
		}
	}

	public Loan getLoan() throws DataNotFoundException, InvalidArgumentException
	{
		Loan loan = null;
		try {
			loan = Database.find_loans_by_deweyid(this.deweyIndex);
			return loan;
		}
		catch (DataNotFoundException e)
		{
			return null;
		}
	}

	public String toString()
	{
		try
		{
			if(this.item.getType() == "Book")
			{
				return "B: \t ISBN: "+this.item.isbn+"\t is it on loan? "+this.onLoan();
			}else{
				return "P: \t ISSN: "+this.item.issn+"\t is it on loan? "+this.onLoan();
			}
		}catch(DataNotFoundException e)
		{
			return "ERROR";
		}catch(InvalidArgumentException e)
		{
			return "ERROR";
		}
	}
}
