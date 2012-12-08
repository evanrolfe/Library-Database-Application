import java.util.*;
import java.sql.*;

public class SQL
{
    private String databaseConnection = null;

    /**
     * Creates a new Database object and initialises the Database.
     */
    public SQL()
    {
        databaseConnection = "jdbc:mysql://stusql.dcs.shef.ac.uk/team007?user=team007&password=bf251b2e";
    }

    public Borrower getBorrower(int id) throws IndexOutOfBoundsException
    {
        Hashtable<String, Object> options = new Hashtable<String, Object>();
        options.put("id", id);
        ArrayList<Borrower> borrowers = getBorrowers(options);
        return borrowers.get(0);
    }

    public ArrayList<Borrower> getBorrowers()
    {
        return getBorrowers(null);
    }

    /**
     *
     * @param id The id of the borrower
     * @return An ArrayList of the loans a given borrower has
     */
    public ArrayList<Loan> getLoans(int id) throws DataNotFoundException
    {
        Hashtable<String, Object> options = new Hashtable<String, Object>();
        options.put("id", id);
        return getLoans(options);
    }

    private ArrayList<Loan> getLoans(Hashtable<String, Object> options) throws DataNotFoundException
    {
        ArrayList<Loan> toReturn = new ArrayList<Loan>();
        String query = "SELECT * FROM LOANS ";
        if (options.size() > 0)
        {
            Set keySet = options.keySet();
            query += "WHERE ";
            if (keySet.contains("issueDate"))
            {
                query += "issueDate = " + options.get("issueDate");
                keySet.remove("issueDate");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("dueDate"))
            {
                query += "dueDate LIKE '" + options.get("dueDate") + "'";
                keySet.remove("dueDate");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("deweyID"))
            {
                query += "deweyID LIKE '" + options.get("deweyID") + "'";
                keySet.remove("deweyID");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("publisher"))
            {
                query += "publisher LIKE '" + options.get("publisher") + "'";
                keySet.remove("publisher");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("date"))
            {
                query += "date = " + options.get("date") + ";";
            }
        }
        ResultSet result = runQuery(query);
        try
        {
            while (result.next())
            {
                java.util.Date issueDate = result.getDate("issueDate");
                java.util.Date dueDate = result.getDate("dueDate");		//EVAN: unnecessary to use the dueDate field as it is already calculated by the Loan object
                String deweyID = result.getString("deweyID");
                int id = result.getInt("id");							//EVAN: Is this the borrower's id?
                Loan toAdd = new Loan(deweyID, issueDate, id);
                toReturn.add(toAdd);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace(); //TODO: Sort out error handling
        }
        return toReturn;
    }

    public ArrayList<Item> getBooks(Hashtable<String, Object> options)
    {
        ArrayList<Item> toReturn = new ArrayList<Item>();
        String query = "SELECT * FROM books";
        if (options.size() > 0)
        {
            Set keySet = options.keySet();
            query += "WHERE ";
            if (keySet.contains("isbn"))
            {
                query += "isbn = " + options.get("isbn");
                keySet.remove("isbn");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("title"))
            {
                query += "title LIKE '" + options.get("title") + "'";
                keySet.remove("title");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("author"))
            {
                query += "author LIKE '" + options.get("author") + "'";
                keySet.remove("author");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("publisher"))
            {
                query += "publisher LIKE '" + options.get("publisher") + "'";
                keySet.remove("publisher");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("date"))
            {
                query += "date = " + options.get("date") + ";";
            }
        }
        ResultSet result = runQuery(query);
        try
        {
            while(result.next())
            {
                int id = result.getInt("isbn");
                String title = result.getString("title");
                String author = result.getString("author");
                String publisher = result.getString("publisher");
                java.util.Date date = result.getDate("date");
                Item toAdd = new Item(""+id, title, author, publisher, date);
                toReturn.add(toAdd);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace(); //TODO: Sort out error handling
        }
        return toReturn;
    }
    /**
     * Returns a list of borrowers with some search parameters
     * @param options An arraylist with keys which may include "id", "forename",
     *                "surname", "email"
     * @return An array list of borrows
     */
    public ArrayList<Borrower> getBorrowers(Hashtable<String, Object>  options)
    {
        ArrayList<Borrower> toReturn = new ArrayList<Borrower>();
        String query = "SELECT * FROM borrowers ";
        if (options.size() > 0)
        {
            Set keySet = options.keySet();
            query += "WHERE ";
            if (keySet.contains("id"))
            {
                query += "id = " + options.get("id");
                keySet.remove("id");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("forename"))
            {
                query += "forename LIKE '" + options.get("forename") + "'";
                keySet.remove("forename");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("surname"))
            {
                query += "surname LIKE '" + options.get("surname") + "'";
                keySet.remove("surname");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("email"))
            {
                query += "email LIKE '" + options.get("email") + "';";
            }
        }
        ResultSet result = runQuery(query);
        try
        {
            while(result.next())
            {
                int id = result.getInt("id");
                String forename = result.getString("forename");
                String surname = result.getString("surname");
                String email = result.getString("email");
                Borrower toAdd = new Borrower(id, forename, surname, email);
                toReturn.add(toAdd);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace(); //TODO: Sort out error handling
        }
        return toReturn;
    }

    public Item getBook(int ISBN)
    {
        return new Item("0-00000-9", "The glory of boilerplate functions", "Patrick Rose", "COM3002", new java.util.Date());
    }

    public Copy getCopy(String deweyID) throws IndexOutOfBoundsException
    {
        Hashtable<String, Object> options = new Hashtable<String, Object>();
        options.put("deweyID", deweyID);
        ArrayList<Copy> copies = getCopies(options);
        return copies.get(0);
    }

    public ArrayList<Copy> getCopies(Hashtable<String, Object> options)
    {
        ArrayList<Copy> toReturn = new ArrayList<Copy>();
        String query = "SELECT * FROM copies WHERE ";
        if (options.size() > 0)
        {
            Set keySet = options.keySet();
            if (keySet.contains("deweyID"))
            {
                query += "deweyID LIKE " + options.get("deweyID");
                keySet.remove("deweyID");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("onLoan"))
            {
                int onLoan;
                if ((Boolean)options.get("onLoan"))
                {
                    onLoan = 1;
                }
                else
                {
                    onLoan = 0;
                }
                query += "onLoan = " + onLoan;
                keySet.remove("onLoan");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("reference"))
            {
                int reference;
                if ((Boolean)options.get("reference"))
                {
                    reference = 1;
                }
                else
                {
                    reference = 0;
                }
                query += "reference = '" + reference + "'";
                keySet.remove("reference");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("isbn"))
            {
                query += "isbn LIKE '" + options.get("isbn") + "';";
            }
            else if (keySet.contains("issn"))
            {
                query += "issn LIKE '" + options.get("issn") + "';";
            }
        }
        ResultSet result = runQuery(query);
        try
        {
            while(result.next())
            {
                String dewey = result.getString("deweyID");
                Boolean reference = result.getBoolean("reference");
                Boolean onLoan = result.getBoolean("onLoan");
                String isbn = result.getString("isbn");
                String issn = result.getString("issn");

                Copy toAdd;	
				if(isbn == null)
				{
                	toAdd = new Copy(dewey, reference, issn, "Periodical");
				}else{
	                toAdd = new Copy(dewey, reference, isbn, "Book");					
				}

                toReturn.add(toAdd);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace(); //TODO: Sort out error handling
        }
        return toReturn;
    }

    public ResultSet runQuery (String query)
    {
        ResultSet result = null;
        Statement stmt = null;
        try
        {
            Connection con = DriverManager.getConnection(databaseConnection);
            stmt = con.createStatement();
            result = stmt.executeQuery(query);
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    //This bit makes absolutely no sense...
                }
            }
        }
        return result;
    }

    public ArrayList<Reservation> getReservations(
            Hashtable<String, Object> options)
    {
        ArrayList<Reservation> toReturn = new ArrayList<Reservation>();
        String query = "SELECT * FROM reservations WHERE ";
        if (options.size() > 0)
        {
            Set keySet = options.keySet();
            if (keySet.contains("id"))
            {
                query += "id = " + options.get("id");
                keySet.remove("id");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("date"))
            {
                query += "date= " + options.get("date");
                keySet.remove("date");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("issn"))
            {
                query += "issn LIKE '" + options.get("issn") + "';";
            }
            else if (keySet.contains("isbn"))
            {
                query += "isbn LIKE '" + options.get("isbn") + "';";
            }
        }
        ResultSet result = runQuery(query);
        try
        {
            while(result.next())
            {
                java.util.Date reserveDate = result.getDate("date");
                int memberId = result.getInt("id");
                int isbn = result.getInt("isbn");
                int issn = result.getInt("issn");
                Item item = getPeriodical(issn);

                Reservation toAdd = new Reservation(reserveDate, memberId, item);
                toReturn.add(toAdd);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace(); //TODO: Sort out error handling
        }
        return toReturn;
    }

    public void updateLoan(Hashtable<String, Object> details, java.util.Date newDate)
    {
        String query = "UPDATE loans SET date=" + newDate + " WHERE id=" +
                details.get("id") + " AND deweyID=" + details.get("deweyID");
        runQuery(query);
    }

    public void createReservation(Hashtable<String, Object> details)
    {
        String query = "INSERT INTO reservations(id, isbn, issn, date) VALUES ("
                + details.get("id") + ", ";
        if (details.containsKey("isbn"))
        {
            query += details.get("isbn") + ", null, ";
        }
        else
        {
            query += "null, " + details.get("issn") + ", ";
        }
        query += details.get("date") + ");";
        runQuery(query);
    }

    public void deleteReservation(Hashtable<String, Object> details)
    {
        String query = "DELETE FROM reservations WHERE id=" + details.get("id") +
                "AND ";
        if (details.containsKey("isbn"))
        {
            query += "isbn = " + details.get("isbn");
        }
        else
        {
            query += "isbn = " + details.get("issn");
        }
        runQuery(query);
    }

    public Item getPeriodical(int issn) throws IndexOutOfBoundsException
    {
        Hashtable<String, Object> options = new Hashtable<String, Object>();
        options.put("issn", issn);
        ArrayList<Item> periodicals = getPeriodicals(options);
        return periodicals.get(0);
    }

    public ArrayList<Item> getPeriodicals(Hashtable<String, Object> options)
    {
        ArrayList<Item> toReturn = new ArrayList<Item>();
        String query = "SELECT * FROM periodicals";
        if (options.size() > 0)
        {
            Set keySet = options.keySet();
            query += "WHERE ";
            if (keySet.contains("issn"))
            {
                query += "issn = " + options.get("issn");
                keySet.remove("issn");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("title"))
            {
                query += "title LIKE '" + options.get("title") + "'";
                keySet.remove("title");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("volume"))
            {
                query += "title LIKE '" + options.get("volume") + "'";
                keySet.remove("volume");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("number"))
            {
                query += "title LIKE '" + options.get("number") + "'";
                keySet.remove("number");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("author"))
            {
                query += "author LIKE '" + options.get("author") + "'";
                keySet.remove("author");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("publisher"))
            {
                query += "publisher LIKE '" + options.get("publisher") + "'";
                keySet.remove("publisher");
                if (keySet.isEmpty())
                {
                    query += ";";
                }
                else
                {
                    query += " AND ";
                }
            }
            if (keySet.contains("date"))
            {
                query += "date = " + options.get("date") + ";";
            }
        }

        ResultSet result = runQuery(query);
        try
        {
            while(result.next())
            {
                int id = result.getInt("issn");
                String title = result.getString("title");
                int volume = result.getInt("volume");
                int number = result.getInt("number");
                String author = result.getString("author");
                String publisher = result.getString("publisher");
                java.util.Date date = result.getDate("date");

                Item toAdd = new Item(Integer.toString(id), title, volume, number, publisher, date);
                toReturn.add(toAdd);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace(); //TODO: Sort out error handling
        }
        return toReturn;
    }
}

