import java.util.*;

// The Borrower class handles all actions which relate to a particular borrower.
public class Borrower
{
	
	//Attributes belonging to the borrower.
    public int id;
    public String forename;
    public String surname;
    public String email;

    //Borrower constructor, initialises the borrowers attributes.
    public Borrower(int id, String forename, String surname, String email)
    {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
    }

    
    //This will return all the copies a borrower has on loan if he has any.
	public ArrayList<Copy> getCopiesOnLoan() throws Exception
	{
		//Initialise array list of Copy, to be returned which will contain the copies on loan.
		ArrayList<Copy> copies_on_loan = new ArrayList<Copy>();

		Loan loan;
		
		//This iterates through each of the loans associated to the borrower using getLoans method.
		for(int i=0; i<this.getLoans().size(); i++)
		{
			loan = this.getLoans().get(i);
			
			//If the loan associated to the borrower is not overdue then it is added to the array list.
			if(loan.overDue()==false)
			{
				copies_on_loan.add(loan.copy);				
			}
		}

		//The array list of all the non overdue copies associated to a borrower are returned.
		return copies_on_loan;	
	}

	//This method retrives all of the copies associated to the borrower which are overdue.
	public ArrayList<Copy> getCopiesOverDue() throws Exception
	{
		//Initialises a borrower from the database and the overdue copies array list which will be returned.
		Borrower b = Database.find_borrower(id);
		ArrayList<Copy> copies_overdue = new ArrayList<Copy>();

		Loan loan;
		//This iterates through each of the loans associated to the borrower using getLoans method.
		for(int i=0; i<this.getLoans().size(); i++)
		{
			loan = this.getLoans().get(i);

			//If the loan associated to the borrower is overdue then it is added to the array list.
			if(loan.overDue()==true)
			{
				copies_overdue.add(loan.copy);
			}
		}

		//The array list of all the overdue copies aa borrower has on loan are returned.
		return copies_overdue;	
	}

	//This gets all the loans associated to a borrowers membership id.
	public ArrayList<Loan> getLoans() throws Exception
	{
		//Use this instance of borrower to find the corresponding loans using static database class method.
		return Database.find_loans(this.id);
	}

	//Check if the borrower has any loans whcih are overdue.
	public boolean hasLoansOverDue() throws Exception
	{
		//Initialise array list containing all of the overdue loans a borrower holds.
		ArrayList<Loan> overDueLoans = this.getOverdueLoans();

		//If the array list is not empty the borrower has overdue loans otherwise this is false.
		if(overDueLoans.size() > 0)
		{
			return true;
		}else{
			return false;
		}
	}

	//Calculates any fines the borrower is required to pay.
	public int fines() throws Exception
	{
		//TODO: calculate the borrowers fines
		return 0;
	}

	//This returns an array list of loans associate to the borrower which are overdue.
	private ArrayList<Loan> getOverdueLoans() throws Exception
	{
		//Initialise the loans array list which contains all loans a borrower holds.
		ArrayList<Loan> loans = this.getLoans();
		//Initialise overDueLoans array list which will contain all overdue loans.
		ArrayList<Loan> overDueLoans = new ArrayList<Loan>();

		//Iterate thorough the array list of loans the borrower holds.
		for(int i=0; i<loans.size(); i++)
		{
			//If any loan is overdue it is added to the overDueLoans array list
			if(loans.get(i).overDue() == true)
				overDueLoans.add(loans.get(i));
		}

		//The array list of overdue loans is returned.
		return overDueLoans;
	}

	//This uses a given Key parameter to establish what field is being used in other search queries.
	public Object getValue(String key)
	{
		//Initialise the object that will be returned corresponding to a particular search key.
		Object out = new Object();

		//Establish which filed the given key is in regards to the fields a borrower can be searched on.
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
		//Returns the object key which is the field that a search is being carried out for on the borrower.
		return out;
	}
}

