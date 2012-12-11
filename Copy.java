public class Copy
{
	public String deweyIndex;
	public boolean referenceOnly;
	public String bookIsbn;
	public String periodicalIssn;
    private boolean onLoan;

	public Item item;

	public Copy(String deweyIndex, boolean referenceOnly, Item item, boolean onLoan)
	{
		this.deweyIndex = deweyIndex;
		this.referenceOnly = referenceOnly;
        this.onLoan = onLoan;

		if(item.getType().equals("Book"))
		{
			this.bookIsbn = item.isbn;
		}else if(item.getType().equals("Periodical"))
		{
			this.periodicalIssn = item.issn;
		}

		this.item = item;
	}

	//This constructor can be used if you have the issn/isbn of the item but you do not have the Item object
	public Copy(String deweyIndex, boolean referenceOnly, String identify, String type, boolean onLoan)
	{
		this.deweyIndex = deweyIndex;
		this.referenceOnly = referenceOnly;
        this.onLoan = onLoan;

		//TODO: item is not defined

		if(item.getType().equals("Book"))
		{
			this.bookIsbn = item.isbn;
		}else if(item.getType().equals("Periodical"))
		{
			this.periodicalIssn = item.issn;
		}

        this.item = item;
	}

	public Item getItem()
	{
		return item;
	}

	public boolean onLoan()
	{
        if(this.getLoan() == null)
		{
			return false;
		}else{
			return true;
		}
	}

	public Loan getLoan()
	{
		Loan loan = null;
		try
        {
			System.out.println("Finding loan with deweyID: "+this.deweyIndex);
			loan = Database.find_loans_by_deweyid(this.deweyIndex);
			return loan;
		}
		catch (DataNotFoundException e)
		{
			return loan;
		}
		catch (InvalidArgumentException e)
		{
			return loan;
		}
	}

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
