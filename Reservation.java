import java.util.Date;

/**
 * Reservation class which handles creation of reservation objects and methods manipulating current instances of reservation.
 */
public class Reservation 
{
	/**
    * The date the item was reserved.
    */
    public Date reserveDate;
    
    /**
    * The id of the borrower
    */
	public int borrower_id;
	
	/**
	 * The isbn belonging to a book.
	 */
	public String book_isbn;
	
	/**
	 *  The issn number for a periodical
	 */
	public String periodical_issn;

	
	/**
	 * The item which is being reserved.
	 */
	public Item item;

	/**
	 * Reservation constructor which creates a reservation object given the specified parameters.
	 * @param reserveDate The data the item was reserved.
	 * @param borrower_id The identification number of the borrower.
	 * @param item The item which is being reserved.
	 */
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

    
	public String toString()
	{
		return "Reservation: borrower_id: "+this.borrower_id+"\t date:"+reserveDate;
	}
}
