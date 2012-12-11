import java.util.*;
import java.sql.*;
/*
 * Run on command line using:
javac *.java && java -cp connector.jar:. MysqlTest
 */

public class MysqlTest
{

	public static void main(String[] args) throws SQLException, DataNotFoundException, InvalidArgumentException
	{
		Mysql db = new Mysql();


		//Borrower b = db.getBorrower(1000001);
		//System.out.println(b.forename);

/*
		System.out.println("TESTING RESERVATIONS\n");
		Hashtable<String,Object> params = new Hashtable<String,Object>();
		params.put("issn", 52411241);

		ArrayList<Reservation> reservations = db.getReservations(params);


		for(int i=0; i<reservations.size(); i++)
		{
			System.out.println(reservations.get(i));
		}

		System.out.println(db.getPeriodical(12552144));
		System.out.println("=====================================");


		//TESTING PERIODICALS
		System.out.println("TESTING PERIODICALS\n");
		Hashtable<String,Object> params1 = new Hashtable<String,Object>();
		params.put("issn", 12552144);

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
*/

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
