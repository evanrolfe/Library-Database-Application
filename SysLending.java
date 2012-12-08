import java.util.*;

public class SysLending
{
	public static void main(String[] args) throws Exception, InvalidArgumentException
	{
		SysLending sys = new SysLending();
		sys.search_borrower();
		sys.search_item();
		sys.identify_copy();
		sys.issue_loan();
	}

	//1. "Search for any given Borrower on any field, or combinations of fields. Identify the Borrower by their membership ID and see their details. List all current Loans related to that Borrower and identify which Loans are overdue."
	public void search_borrower() throws Exception
	{
		//Find all the borrowers (optionally add paramenters to find_borrowers for searching)
		ArrayList<Borrower> borrowers = Database.find_borrowers();

		for(int i=0; i<borrowers.size(); i++)
		{
			System.out.println("Borrower: ID: "+borrowers.get(i).id+"\t Name:"+borrowers.get(i).forename+" "+borrowers.get(i).surname+"\t Email:"+borrowers.get(i).email);
			
			for(int j=0; j<borrowers.get(i).getLoans().size(); j++)
			{
				Loan loan = borrowers.get(i).getLoans().get(j);
				System.out.println("\t\t Loan:"+loan.deweyID+"\t Issue Date: "+loan.issueDate+"\t "+loan.dueDate+"\t Is loan overdue? "+loan.overDue());
			}			
			System.out.println("");
		}			
	}

	//2. "Search for any given Book or Periodical on any field, or combination of fields. Identify a book by its ISBN and a Periodical by ISSN and see its details. List all Copies related to that Book or Periodical and identify which are currently on loan."
	public void search_item() throws InvalidArgumentException
	{
		//Find all the books/periodicals (optionally add paramenters to find_items for searching)
		Hashtable params = new Hashtable();
		params.put("title","asdf");

		ArrayList<Item> items = Database.find_items(params);

		for(int i=0; i<items.size(); i++)
		{
			System.out.println(items.get(i));

			for(int j=0; j<items.get(i).getCopies().size(); j++)
			{
				Copy copy = items.get(i).getCopies().get(j);
				System.out.println("\t "+copy);
			}
		}		
	}

//3. "Identify a Copy by its Dewey index and determine whether it is on loan. Identify the related Book or Periodical and determine whether this has been resereved."
	public void identify_copy() throws Exception
	{
		ArrayList<Copy> copies = Database.find_copies();

		for(int i=0; i<copies.size(); i++)
		{
			System.out.println(copies.get(i));
		}			
	}

	public void list_reservations()
	{
		ArrayList<Reservation> reservations = Database.find_reservations();

		for(int i=0; i<reservations.size(); i++)
		{
			System.out.println("Reserved by: #"+reservations.get(i).borrower_id+"; Title: "+reservations.get(i).item.title);
		}	
	}

//4. "Issue a new Loan of a copy to a borrower, subject to the rules that:..."
	public void issue_loan() throws LoanException, Exception
	{ 
		ArrayList<Copy> copies = Database.find_copies();
		ArrayList<Borrower> borrowers = Database.find_borrowers();

		//As an example: this will return an exception
		Database.issue_loan(copies.get(6), borrowers.get(0));		
	}
}
