import java.util.*;

// The Borrower class handles all actions which relate to a particular borrower.
public class Borrower
{

    /**
     * The id of the borrower
     */
    public int id;

    /**
     * The first name of the borrower
     */
    public String forename;

    /**
     * The surname of the borrower
     */
    public String surname;

    /**
     * The email of the borrower
     */
    public String email;

    /**
     * Creates a new borrower object
     * @param id The identifier of the borrower
     * @param forename The forename of the borrower
     * @param surname The surname of the borrower
     * @param email The email of the borrower
     */
    public Borrower(int id, String forename, String surname, String email)
    {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
    }


    /**
     * Returns all the copies that this borrower has on loan, whether they are overdue or not.
     * @return Returns all copies that a borrower has on loan.
     * @throws Exception If there's a problem accessing the database
     */
    public ArrayList<Copy> getAllCopiesOnLoan() throws Exception
    {
        ArrayList<Copy> copiesOnLoan = getCopiesOnLoan();
        ArrayList<Copy> copiesOverDue = getCopiesOverDue();
        copiesOnLoan.addAll(copiesOverDue);
        return copiesOnLoan;
    }

    /**
     * Gets all the copies that the borrower has on loan and are <strong>not</strong> overdue.<br />
     * For the loans that are overdue, call getCopiesOverDue(). For all loans, call getAllCopiesOnLoan().
     * @return Returns all copies that a borrower has that are not overdue.
     * @throws Exception If there's a problem with accessing the database
     */
	public ArrayList<Copy> getCopiesOnLoan() throws Exception
	{
		//Initialise array list of Copy, to be returned which will contain the copies on loan.
		ArrayList<Copy> copies_on_loan = new ArrayList<Copy>();
		
		//This iterates through each of the loans associated to the borrower using getLoans method.
		for(Loan loan: getLoans())
		{
			//If the loan associated to the borrower is not overdue then it is added to the array list.
			if(!loan.overDue())
			{
				copies_on_loan.add(loan.copy);				
			}
		}

		//The array list of all the non overdue copies associated to a borrower are returned.
		return copies_on_loan;	
	}

	//This method retrives all of the copies associated to the borrower which are overdue

    /**
     * Gets all the copies that this borrower has which are overdue
     * @return An ArrayList of copies that are overdue
     * @throws Exception If there's a database error
     */
	public ArrayList<Copy> getCopiesOverDue() throws Exception
	{
		ArrayList<Copy> copies_overdue = new ArrayList<Copy>();
		//This iterates through each of the loans associated to the borrower using getLoans method.
		for(Loan loan:getLoans())
		{
			//If the loan associated to the borrower is overdue then it is added to the array list.
			if(loan.overDue())
			{
				copies_overdue.add(loan.copy);
			}
		}

		//The array list of all the overdue copies aa borrower has on loan are returned.
		return copies_overdue;	
	}

	//This gets all the loans associated to a borrowers membership id.

    /**
     * Gets all the loans that this borrower has
     * @return An array list of loans
     * @throws Exception If there's a database connection issue
     */
	public ArrayList<Loan> getLoans() throws Exception
	{
		//Use this instance of borrower to find the corresponding loans using static database class method.
		return Database.find_loans(this.id);
	}

	//Check if the borrower has any loans whcih are overdue.

    /**
     * Returns if the borrower has 1 or more loans
     * @return True if there is at least one loan, false otherwise
     * @throws Exception If there's some database connection issue
     */
	public boolean hasLoansOverDue() throws Exception
	{
		//Initialise array list containing all of the overdue loans a borrower holds.
		ArrayList<Loan> overDueLoans = this.getOverdueLoans();

		//If the array list is not empty the borrower has overdue loans otherwise this is false.
		return overDueLoans.size() > 0;
	}

    /**
     * Returns the how much of a fine the borrower owes
     * @return The amount owed by the borrower
     * @throws Exception If there's a database connection issue
     */
    public int fines() throws Exception
	{
		//TODO: calculate the borrowers fines
		return 0;
	}

	//This returns an array list of loans associate to the borrower which are overdue.

    /**
     * Gets any overdue loans of this borrower
     * @return The overdue loans of this borrower
     * @throws Exception If there's a database connection issue
     */
    private ArrayList<Loan> getOverdueLoans() throws Exception
	{
		//Initialise the loans array list which contains all loans a borrower holds.
		ArrayList<Loan> loans = this.getLoans();
		//Initialise overDueLoans array list which will contain all overdue loans.
		ArrayList<Loan> overDueLoans = new ArrayList<Loan>();

		//Iterate thorough the array list of loans the borrower holds.
        for (Loan loan : loans)
        {
            //If any loan is overdue it is added to the overDueLoans array list
            if (loan.overDue())
            {
                overDueLoans.add(loan);
            }
        }

		//The array list of overdue loans is returned.
		return overDueLoans;
	}

}

