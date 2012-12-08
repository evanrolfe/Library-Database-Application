import java.util.*;

public class Item
{
    public String isbn;
    public String issn;

	//Common to both
    public String title;
    public String publisher;
    public Date date;

	//Book
    public String author;

	//Periodical
    public int volume;
    public int number;

	//Constructor to create a book
    public Item(String isbn, String title, String author, String publisher, Date date)
    {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.author = publisher;
        this.date = date;
    }

	//Constructor to create a periodical
    public Item(String issn, String title, int volume, int number, String publisher, Date date)
    {
        this.issn = issn;
        this.title = title;
        this.volume = volume;
        this.date = date;
        this.publisher = publisher;
        this.number = number;
    }

	public ArrayList<Copy> getCopies()
	{
		ArrayList<Copy> copies = new ArrayList<Copy>();

		if(this.getType().equals("Book"))
		{
			copies = Database.find_copies_by_isbn(this.isbn);
		}else{
			copies = Database.find_copies_by_issn(this.issn);
		}

		return copies;
	}

	public String getType()
	{
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

	public String toString()
	{
		if(this.getType().equals("Book"))
		{
			return "Book: \t"+this.isbn+"\t "+this.title+"\t by "+this.author;
		}else{
			return "Periodical: \t"+this.issn+"\t "+this.title+"\t by "+this.publisher;			
		}
	}
}
