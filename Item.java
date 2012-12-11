import java.util.*;

//Item class. This deals with construction of item object and methods related to the item object.
public class Item
{
	//Initialise the item attributes specific to books and periodicals respectively.
    public String isbn;
    public String issn;

	//initialise item attributes which are present in both books and periodicals.
    public String title;
    public String publisher;
    public Date date;

	//Initialise author attribute for book items.
    public String author;

	//Initialise volume and number attributes for periodical items.
    public int volume;
    public int number;

	//Item constructor to create an item which is specifically a book.
    public Item(String isbn, String title, String author, String publisher, Date date)
    {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.date = date;
    }

	//Item Constructor to create a item which is specifically a periodical
    public Item(String issn, String title, int volume, int number, String publisher, Date date)
    {
        this.issn = issn;
        this.title = title;
        this.volume = volume;
        this.date = date;
        this.publisher = publisher;
        this.number = number;
    }

    //Method to check if the current instance of item is reserved by any borrower.
	public boolean isReserved() throws DataNotFoundException, InvalidArgumentException
	{
		//Finds all reservations associtated to the current instance of item.
		ArrayList reservations = Database.find_reservations(this);

		//If there are reservations in array list then this is true and the item is currently reserved, else false.
		if(reservations.size() > 0)
		{
			return true;
		}else{
			return false;
		}
	}

	//Method to get all copies associated with current instance of item.
	public ArrayList<Copy> getCopies() throws DataNotFoundException, InvalidArgumentException
	{
		//Array list of copies which will be returned from the method containing all copies of the current instance of item.
		ArrayList<Copy> copies = new ArrayList<Copy>();

		//Checks if the item is a book or periodical and finds all copies related to the item by either isbn or issn to then store in copies array.
		if(this.getType().equals("Book"))
		{
			copies = Database.find_copies_by_isbn(this.isbn);
		}else{
			copies = Database.find_copies_by_issn(this.issn);
		}

		//The copies associated with current instance of item are returned.
		return copies;
	}

	//Method to retirve the type of the current instance of item, which is either book or periodical.
	public String getType()
	{
		//Check if the item contains an issn or an isbn and classifies the item as a periodical or book respectively.
		if(this.issn != null && this.isbn == null)
		{
			return "Periodical";
		}else if(this.issn == null && this.isbn != null)
		{
			return "Book";
		}else{
			return "";
		}
	}

	//Method to convert the current item instance into a string. This is done for displaying the item on screen
	public String toString()
	{
			//Checks if the item is a book or periodical to correctly convert it to a string.
			if(this.getType().equals("Book"))
			{
				return "Book: \t"+this.isbn+"\t "+this.title+"\t by "+this.author;
			}else{
				return "Periodical: \t"+this.issn+"\t "+this.title+"\t by "+this.publisher;			
			}
	}
}
