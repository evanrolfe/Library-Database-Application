import java.util.*;

public class EvansTests
{
	public static void main(String[] args) throws Exception
	{
		//MYSQL tests
		//EvansTests.test_db();

		//Part ONE
		EvansTests.test_borrowers();
		//EvansTests.test_loan();

		//Part TWO
		//EvansTests.test_items();

		//Part THREE
		//EvansTests.test_copies();
	}
	public static void test_db()
	{
		ArrayList<Borrower> b = Database.find_borrowers();
	}

	//TODO: for some reason java's date class does not seem to be able to subtract > 1 month from the timestamp? it wraps around..
	public static void test_loan() throws Exception
	{
		ArrayList<Loan> loans = new ArrayList<Loan>();
		ArrayList<Borrower> borrowers = Database.find_borrowers();
		ArrayList<Copy> items = Database.find_copies();

		Date today = new Date();
		Date month_ago = new Date(today.getTime() - 3*7*24*3600*1000);

		System.out.println(month_ago);
	
		loans.add(new Loan("1", new Date(), borrowers.get(0).id, items.get(0)));
		loans.add(new Loan("2", new Date(), borrowers.get(0).id, items.get(2)));
		loans.add(new Loan("3", new Date(), borrowers.get(4).id, items.get(1)));

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
