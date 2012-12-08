import java.util.*;

public class Borrower
{
    public int id;
    public String forename;
    public String surname;
    public String email;

    public Borrower(int id, String forename, String surname, String email)
    {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
    }

	public ArrayList<Copy> getCopiesOnLoan() throws Exception
	{
		ArrayList<Copy> copies_on_loan = new ArrayList<Copy>();
	
		Loan loan;
		for(int i=0; i<this.getLoans().size(); i++)
		{
			loan = this.getLoans().get(i);
			
			if(loan.overDue()==false)
			{
				copies_on_loan.add(loan.copy);				
			}
		}

		return copies_on_loan;	
	}

	public ArrayList<Copy> getCopiesOverDue() throws Exception
	{
		Borrower b = Database.find_borrower(id);
		ArrayList<Copy> copies_overdue = new ArrayList<Copy>();
	
		Loan loan;
		for(int i=0; i<this.getLoans().size(); i++)
		{
			loan = this.getLoans().get(i);
			
			if(loan.overDue()==true)
			{
				copies_overdue.add(loan.copy);
			}
		}

		return copies_overdue;	
	}

	public ArrayList<Loan> getLoans() throws Exception
	{
		return Database.find_loans(this.id);
	}

	public boolean hasLoansOverDue() throws Exception
	{
		ArrayList<Loan> overDueLoans = this.getOverdueLoans();

		if(overDueLoans.size() > 0)
		{
			return true;
		}else{
			return false;
		}
	}

	public int fines() throws Exception
	{
		//TODO: calculate the borrowers £fines
		return 0;
	}

	private ArrayList<Loan> getOverdueLoans() throws Exception
	{
		ArrayList<Loan> loans = this.getLoans();
		ArrayList<Loan> result = new ArrayList<Loan>();

		for(int i=0; i<loans.size(); i++)
		{
			if(loans.get(i).overDue() == true)
				result.add(loans.get(i));
		}

		return result;
	}

	public Object getValue(String key)
	{
		Object out = new Object();

		if(key.equals(this.id))
		{
			out = this.id;
		}
		else if(key.equals(this.forename))
		{
			out = this.forename;
		}
		else if(key.equals(this.surname))
		{
			out = this.surname;
		}
		else if(key.equals(this.email))
		{
			out = this.email;
		}

		return out;
	}
}
