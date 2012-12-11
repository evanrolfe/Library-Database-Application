/**
 * A copy class that handles all things relating to a copy of an item in the library.
 */
public class Copy
{
    /**
     * The dewey index of a copy. This should be unique for each copy
     */
	public String deweyIndex;

    /**
     * Whether a book is reference only or not
     */
	public boolean referenceOnly;

    /**
     * The ISBN of the book
     */
	public String bookISBN;

    /**
     * The ISSN of the periodical
     */
	public String periodicalISSN;

    /**
     * The item this copy relates to
     */
	public Item item;

    /**
     * Creates a new copy object
     * @param deweyIndex The dewey ID of the copy
     * @param referenceOnly Whether the copy is reference only
     * @param item The item this copy relates to
     */
	public Copy(String deweyIndex, boolean referenceOnly, Item item)
	{
		this.deweyIndex = deweyIndex;
		this.referenceOnly = referenceOnly;

		if(item.getType().equals("Book"))
		{
			this.bookISBN = item.isbn;
		}
        else if(item.getType().equals("Periodical"))
		{
			this.periodicalISSN = item.issn;
		}

		this.item = item;
	}

    //TODO: Check if we still need this?
	//This constructor can be used if you have the issn/isbn of the item but you do not have the Item object
	public Copy(String deweyIndex, boolean referenceOnly, String identify, String type)
	{
		this.deweyIndex = deweyIndex;
		this.referenceOnly = referenceOnly;

		//TODO: item is not defined

		if(item.getType().equals("Book"))
		{
			this.bookISBN = item.isbn;
		}else if(item.getType().equals("Periodical"))
		{
			this.periodicalISSN = item.issn;
		}

        this.item = item;
	}

    /**
     * Gets the item that this copy relates to
     * @return
     */
	public Item getItem()
	{
		return item;
	}

    /**
     * Checks whether this copy is on loan or not
     * @return
     */
	public boolean onLoan()
	{
        //Return false if we can't find a loan relating to this copy
        return getLoan() != null;
	}

    /**
     * Gets the loan relating to this copy
     * @return A loan object with the details of this loan
     */
	public Loan getLoan()
	{
		Loan loan = null;
		try
        {
			loan = Database.find_loans_by_deweyid(this.deweyIndex);
		}
		catch (Exception e)
		{
            //empty catch
        }
        return loan;
	}

    /**
     * Returns a String representation of this object
     * @return If the item is a book, then it returns B: [the ISBN] Is it on loan [onLoan]
     *         If the item is a periodical, then it returns P: [the ISSN] Is it on loan? [onLoan]
     */
	public String toString()
	{
        if(this.item.getType().equals("Book"))
        {
            return "B: \t ISBN: "+this.item.isbn+"\t is it on loan? "+this.onLoan();
        }
        else
        {
            return "P: \t ISSN: "+this.item.issn+"\t is it on loan? "+this.onLoan();
        }
    }
}
