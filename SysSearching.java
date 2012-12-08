import java.util.*;

public class SysSearching
{
	public static void main(String[] args) throws Exception, InvalidArgumentException
	{
		SysSearching sys = new SysSearching();
		sys.search_borrower(3);
		sys.search_items();
		sys.place_reservation();
	}

	//1. "Identify their own membership record by their membership ID and see their personal details. Identify which Copies they currently have out on loan, and which of these are overdue"
	public void search_borrower(int id) throws Exception
	{
		Borrower b = Database.find_borrower(id);

		System.out.println("Copies Over Due");
		for(int i=0; i<b.getCopiesOverDue().size(); i++)
		{
			System.out.println(b.getCopiesOverDue().get(i));
		}

		for(int i=0; i<b.getCopiesOnLoan().size(); i++)
		{
			System.out.println(b.getCopiesOnLoan().get(i));
		}
	}

	//2. "Search for any given book/periodical on any field, or combination of fields. ... and see its details. List all copies related to that book/periodical and identify which ones are still in the library.

	public void search_items() throws InvalidArgumentException
	{
		//Find all the books/periodicals (optionally add paramenters to find_items for searching)
		Hashtable options = new Hashtable();
		options.put("title","asdf");

		ArrayList<Item> items = Database.find_items(options);

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

	//3. "Place a reservation on a book/periodical. If there are free copies then the borrower is notified of this. Otherwise, the Copy that was loaned with the earliest issue date is recalled and the Borrower is told to wait for a week."
	public void place_reservation() throws ReservationException
	{
		Borrower borrower = Database.find_borrower(1);
		ArrayList<Item> items = Database.find_items();

		Database.place_reservation(borrower.id, items.get(6));		//This item is available
		Database.place_reservation(borrower.id, items.get(1));		//This item is already on loan => should throw exception
	}

	//4. "Cancel a reservation on a Book or Periodical "
}
