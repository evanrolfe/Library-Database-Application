import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
/*
 * Run on command line using:
javac *.java && java -cp connector.jar:. MysqlTest
 */

public class MysqlTest
{

	public static void main(String[] args) throws SQLException, DataNotFoundException, InvalidArgumentException, LibraryRulesException
	{
		Mysql db = new Mysql();

//========================================
//	VALIDATION TESTS
//========================================
	java.util.Date today = new java.util.Date();
	System.out.println(today);
	//db.addLoan(1000006,"124.452.767.5", new java.util.Date(), new java.util.Date());

//COMPLETED: EVAN: Mysql.addLoan() (borrower cannot have more than 6 loans, and no laons overdue)
	
	//Test: borrower with ID 1000001 has 6 loans out so cannnot add a new loan for him
	try
	{
		db.addLoan(1000001, "524.124.125.7", new java.util.Date(), new java.util.Date());
	}catch(LibraryRulesException e)
	{
		System.out.println(e);
	}


//COMPLETED: EVAN: Mysql.addLoan() - copy must not be reference only

	//Test: copy with deweyID: 823.452.767.7 is marked as reference only in the database so this should throw a LibraryRulesException
	try
	{
		db.addLoan(1000002, "753.159.852.3", new java.util.Date(), new java.util.Date());
	}catch(LibraryRulesException e)
	{
		System.out.println(e);
	}

//EVAN: Mysql.addLoan() - no outstanding reservations unless they themselves have reserved it

	//Test: Perioical with issn=12598754 is already reserved by borrower id=1000006
/*
	try
	{
		//db.addLoan(1000002, "753.159.852.3", new java.util.Date(), new java.util.Date());
	}catch(LibraryRulesException e)
	{
		System.out.println(e);
	}
*/
/*
- PADDY: Mysql.addReservation() - if no free copies then recall 
*/
    try
    {
        Hashtable<String, Object> details = new Hashtable<String, Object>();
        details.put("issn", 52411241);
        details.put("borrowerID", 1000001);
        details.put("date", new DateTime());
        db.addReservation(details);
    }
    catch (LibraryRulesException e)
    {
        System.out.println(e);
    }
    catch (SQLException e)
    {
        System.out.println(e);
    }

/*
- EVAN: Renewing a loan: Mysql.updateLoan()

	-can only renew if the loan has not been recalled
	-and the borrower has not overdue loans
*/

/*
- PADDY: Discharging a loan: Mysql.deleteLoan()

	-copy must be on loan
	-if Loan.getFine() > 0 then display popup with fines, then still delete the loan
*/

        try
        {
            db.deleteLoan("823.452.767.5");
        }
        catch (LibraryRulesException e)
        {
            System.out.println(e);
        }

		//Borrower b = db.getBorrower(1000001);
		//System.out.println(b.forename);


		System.out.println("TESTING RESERVATIONS\n");

		ArrayList<Reservation> reservations = db.getReservations();


		for(int i=0; i<reservations.size(); i++)
		{
			System.out.println(reservations.get(i));
		}

		System.out.println(db.getPeriodical(12552144));
		System.out.println("=====================================");


		//TESTING PERIODICALS
		System.out.println("TESTING PERIODICALS\n");
		Hashtable<String,Object> params1 = new Hashtable<String,Object>();
		params1.put("issn", 12552144);

		ArrayList<Item> periodicals = db.getPeriodicals(params1);


		for(int i=0; i<periodicals.size(); i++)
		{
			System.out.println(periodicals.get(i));
		}

		System.out.println(db.getPeriodical(12552144));
		System.out.println("=====================================");


		//TESTING BOOKS
		System.out.println("TESTING BOOKS\n");
		Hashtable<String,Object> params2 = new Hashtable<String,Object>();
		params2.put("isbn", 1468584646);
		ArrayList<Item> books = db.getBooks(params2);

		for(int i=0; i<books.size(); i++)
		{
			System.out.println(books.get(i));
		}

		System.out.println(db.getBook(1524585639));
		System.out.println("=====================================");


		//TESTING COPIES
		System.out.println("TESTING COPIES\n");
		Hashtable<String,Object> params3 = new Hashtable<String,Object>();
		params3.put("deweyID", "124.452.767.6");
		ArrayList<Copy> copies = db.getCopies(params3);

		for(int i=0; i<copies.size(); i++)
		{
			System.out.println(copies.get(i));
		}

		System.out.println(db.getCopy("823.452.767.5"));
		System.out.println("=====================================");

		//TESTING BORROWERS
		System.out.println("TESTING BORROWERS\n");
		Hashtable<String,Object> params4 = new Hashtable<String,Object>();
		params4.put("forename", "James");

		ArrayList<Borrower> borrowers = db.getBorrowers(params4);


		for(int i=0; i<borrowers.size(); i++)
		{
			System.out.println(borrowers.get(i).id);
		}

		System.out.println(db.getBorrower(1000001));
		System.out.println("=====================================");


		//TESTING LOANS
		System.out.println("TESTING LOANS\n");
		Hashtable<String,Object> params5 = new Hashtable<String,Object>();

		ArrayList<Loan> loans = db.getLoans();


		for(int i=0; i<loans.size(); i++)
		{
			System.out.println(loans.get(i));
		}

		System.out.println(db.getCopy("524.124.125.5"));
	}	

}
