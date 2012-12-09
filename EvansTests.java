import java.util.*;

public class EvansTests
{
	public static void main(String[] args) throws Exception
	{
		EvansTests.test_borrowers();

		EvansTests.test_loan();

		EvansTests.test_items();

		//Part THREE
		EvansTests.test_copies();
	}


	//TODO: for some reason java's date class does not seem to be able to subtract > 1 month from the timestamp? it wraps around..
	public static void test_loan() throws Exception
	{
		ArrayList<Loan> loans = Database.find_loans();

		for(int i=0; i<loans.size(); i++)
		{
			System.out.println(loans.get(i).issueDate+"\t "+loans.get(i).dueDate+"\t "+loans.get(i).overDue());
		}		
	}

	//Test for Lending System Part 1
	public static void test_borrowers() throws Exception
	{
		ArrayList<Borrower> borrowers = Database.find_borrowers();

		for(int i=0; i<borrowers.size(); i++)
		{
			System.out.println("Borrower: ID: "+borrowers.get(i).id+"\t Name:"+borrowers.get(i).forename+" "+borrowers.get(i).surname+"\t Email:"+borrowers.get(i).email);
			
			for(int j=0; j<borrowers.get(i).getLoans().size(); j++)
			{
				Loan loan = borrowers.get(i).getLoans().get(j);
				System.out.println("\t"+loan.deweyID+"\t Issue Date: "+loan.issueDate+"\t "+loan.dueDate+"\t Is loan overdue? "+loan.overDue());				
			}			
		}		
	}


	//Test for Lending System Part 2
	public static void test_items() throws Exception
	{
		ArrayList<Item> items = Database.find_items();

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

	//Test for Lending System Part 3
	public static void test_copies() throws Exception
	{
		ArrayList<Copy> copies = Database.find_copies();

		for(int i=0; i<copies.size(); i++)
		{
			System.out.println(copies.get(i));
		}		
	}
}
