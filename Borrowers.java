import java.util.*;

public class Borrowers
{
	public static void main(String[] args) throws InvalidArgumentException, Exception
	{
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("id", 1);
		params.put("forename", "Evan");
		params.put("surname", "Rolfe");
		//params.put("email", "evanrolfe@gmail.com");
		ArrayList<Borrower> b = Borrowers.find(params);

		for(int i=0; i<b.size(); i++)
		{
			System.out.println(b.get(i).forename+"\t"+b.get(i).email+"\t\t"+b.get(i).getLoans());
		}
	}

	public static ArrayList<Borrower> find_all()
	{
		return Database.find_borrowers();
	}

	public static Borrower find_by_id(int id)
	{
		return Database.find_borrowers(id);
	}
}
