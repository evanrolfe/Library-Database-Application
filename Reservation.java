import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: paddy
 * Date: 28/11/12
 * Time: 13:49
*/
//Reservation class which handles creation of reservation objects and methods manipulating current instances of reservation.
public class Reservation 
{
	//Initialise the reservation attributes.
    public Date reserveDate;
	public int borrower_id;
	public String book_isbn;
	public String periodical_issn;

	public Item item;

	//Reservation constructer which creates a reservation object given the specified parameters.
    public Reservation(Date reserveDate, int borrower_id, Item item)
    {
    	//Match the parameters given to the reservation attributes.
		this.borrower_id = borrower_id;
        this.reserveDate = reserveDate;

		this.item = item;

		//Check if the item given is a book or periodical initialises the isbn or issn of the reservation accordingly.
		if(item.getType() == "Book")
		{
			this.book_isbn = item.isbn;			
		}else{
			this.periodical_issn = item.issn;			
		}
    }

}
