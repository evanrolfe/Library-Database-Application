import java.util.*;

public class Items
{
	public static ArrayList<Item> find_all()
	{
		return Database.find_items();
	}

	public static ArrayList<Item> find(Hashtable params) throws InvalidArgumentException
	{
		Set set = params.entrySet();
		Iterator it = set.iterator();

		//This is used to check that the right search fields have been inputted
		String[] columns = new String[] { "id", "forename", "surname", "email" };
		Arrays.sort(columns);

		//1. Get all the books/periodicals
		ArrayList<Item> items = Items.find_all();

		return items;
	}
}
