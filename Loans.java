import java.util.*;

public class Loans
{
	public static ArrayList<Loan> find_all() throws Exception
	{
		return Database.find_loans();
	}

	public static ArrayList<Loan> find_by_borrower_id(int id) throws Exception
	{
		return Database.find_loans(id);
	}	
}
