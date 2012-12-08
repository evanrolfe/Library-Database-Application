import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: paddy
 * Date: 28/11/12
 * Time: 13:49
*/
public class Reservation 
{
    public Date reserveDate;
	public int borrower_id;
	public String book_isbn;
	public String periodical_issn;

	public Item item;

    public Reservation(Date reserveDate, int borrower_id, Item item)
    {
		this.borrower_id = borrower_id;
        this.reserveDate = reserveDate;

		this.item = item;

		if(item.getType() == "Book")
		{
			this.book_isbn = item.isbn;			
		}else{
			this.periodical_issn = item.issn;			
		}
    }

}
