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

	public ArrayList<Loan> getLoans() throws Exception
	{
		return Loans.find_by_borrower_id(this.id);
	}

	public Object getValue(String key)
	{
		Object out = new Object();

		switch(key)
		{
			case "id":
				out = this.id;
			break;

			case "forename":
				out = this.forename;
			break;

			case "surname":
				out = this.surname;
			break;

			case "email":
				out = this.email;
			break;
		}

		return out;
	}
}
