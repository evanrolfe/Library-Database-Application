import java.util.*;

/**
 * Item class. This deals with construction of item object and methods related to the item object. *
 */
public class Item
{
	/**
	 * The isbn of a book instance of Item
	 */
    public String isbn;
    
    /**
     * The issn of a periodical instance of Item
     */
    public String issn;

	/**
	 * The title of the Item
	 */
    public String title;
    
    /**
     * The publisher of the Item
     */
    public String publisher;
    
    /**
     * The date the Item was published
     */
    public Date date;

	/**
	 * The author of the Item
	 */
    public String author;

	/**
	 * The volume of a periodical instance
	 */
    public int volume;
    
    /**
     * The number of a periodical instance
     */
    public int number;

	/**
	 * Item constructor to create an item which is specifically a book.
	 * @param isbn The isbn of the book
	 * @param title The title of the book
	 * @param author The author of the book
	 * @param publisher The publisher of the book
	 * @param date The date of the book
	 */
    public Item(String isbn, String title, String author, String publisher, Date date)
    {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.date = date;
    }

	/**
	 * Item Constructor to create a item which is specifically a periodical
	 * @param issn The issn of the periodical
	 * @param title The title of the periodical
	 * @param volume The volume of the periodical
	 * @param number The number of the periodical
	 * @param publisher The publisher of the periodical
	 * @param date The date of the periodical
	 */
    public Item(String issn, String title, int volume, int number, String publisher, Date date)
    {
        this.issn = issn;
        this.title = title;
        this.volume = volume;
        this.date = date;
        this.publisher = publisher;
        this.number = number;
    }

    /**
     * Method to check if the current instance of item is reserved by any borrower.
     * @return True if the item is reserved, false otherwise.
     * @throws DataNotFoundException Thrown if there are no reservations
     * @throws InvalidArgumentException Thrown if the search options are invalid
     */
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

	/**
	 * Method to get all copies associated with current instance of item.
	 * @return An ArraryList of Copy objects
	 * @throws DataNotFoundException Thrown if there are no Copy objects
	 * @throws InvalidArgumentException Thrown if the search options are invalid
	 */
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

	/**
	 * Method to retrieve the type of the current instance of item.
	 * @return the type of the item. Either "Book" or "periodical"
	 */
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

	/**
	 * Method to convert the current item instance into a string. This is done for displaying the item on screen
	 */
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
