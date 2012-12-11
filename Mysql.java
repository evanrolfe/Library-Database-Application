import org.joda.time.DateTime;

import java.sql.*;
import java.util.*;

/*
 * Run on command line using:
javac -cp joda.jar:. *.java && java -cp connector.jar:joda.jar:. MysqlTest
 */

public class Mysql
{
    /**
     * The connection string to the database
     */
    private final String DATABASE_CONNECTION = "jdbc:mysql://stusql.dcs.shef.ac.uk/team007?user=team007&password=bf251b2e";

    /**
     * Adds a loan to the database
     * @param id The borrower's id
     * @param deweyID The dewey id for the copy they've just loaned
     * @param issueDate The issueDate of the loan
     * @param dueDate When the loan is due
     */
    public void addLoan(int id, String deweyID, java.util.Date issueDate, java.util.Date dueDate) throws SQLException, LibraryRulesException, DataNotFoundException
    {
        PreparedStatement stmt = null; //Prepared statements mean that it does most of the hard work for us
        //Generate queries
        String copiesQuery = "UPDATE copies SET onLoan = ? WHERE deweyID LIKE ?";
        String loansQuery = "INSERT INTO loans(borrowerID, deweyID, issueDate, dueDate) " +
                "VALUES (?, ?, ?, ?);";
        String errorMessage = null;

		try
		{
			Borrower borrower = this.getBorrower(id);

			if(borrower.getLoans().size() >= 6)
				throw new LibraryRulesException("The borrower id #"+id+" has reached their limit of 6 loans!");

			if(borrower.hasLoansOverDue()==true)
				throw new LibraryRulesException("The borrower id #"+id+" has some loans which are overdue!");

            // Check copy isn't a reference only
			Copy copy = Database.find_copy_by_dewey(deweyID);
			if(copy.referenceOnly==true)
				throw new LibraryRulesException("The copy with deweyID: "+deweyID+" is reference only!");

			//check it isn't already on loan
			if(copy.onLoan()==true)
				throw new LibraryRulesException("The copy with deweyID: "+deweyID+" is already on loan!");

            //Check isn't reserved by ANOTHER borrower
			//1. Identify the related book/periodical (Item)
			//2. Find the reservations
			ArrayList<Reservation> reservations = Database.find_reservations(copy.item);
		
			//3. Find the first reservation in the queue (one with earliest 
		}
        catch(InvalidArgumentException e1)
		{
			
		}

        try
        {
            Connection con = DriverManager.getConnection(DATABASE_CONNECTION);
            //Transaction start - since we need to update 2 tables at once.
            con.setAutoCommit(false);
            stmt = con.prepareStatement(loansQuery);
            //Assign the parameters for the first query
            stmt.setInt(1, id);
            stmt.setString(2, deweyID);
            stmt.setDate(3, new java.sql.Date(issueDate.getTime()));
            stmt.setDate(4, new java.sql.Date(dueDate.getTime()));
            //Execute the query
            stmt.executeUpdate();
            //Clear and close to be safe.
            stmt.clearParameters();
            stmt.close();
            stmt = con.prepareStatement(copiesQuery);
            //Assign for the second
            stmt.setBoolean(1, true);
            stmt.setString(2, deweyID);
            //Execute and commit
            stmt.executeUpdate();
            con.commit();
        }
        catch (SQLException e)
        {
            errorMessage = e.getMessage();
        }
        finally
        {
            //Close the connection if it's been left open.
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e2)
                {
                    e2.printStackTrace();
                }
            }
        }
        if (errorMessage != null)
        {
            throw new SQLException(errorMessage);
        }
    }

    /**
     * Delete a loan from a borrower
     * @param dewey The dewey id of the copy
     */
    public void deleteLoan(String dewey) throws SQLException
    {
        String loansQuery = "DELETE FROM loans WHERE deweyID=?";
        String copiesQuery = "UPDATE copies SET onLoan = ? AND deweyID = ?";
        PreparedStatement stmt = null;
        String errorMessage = null; //Used if we have an error
        //Check if borrower has overdue fines and throw an error if this is the case
        //Have to be a bit of douche
        try
        {
            Connection con = DriverManager.getConnection(DATABASE_CONNECTION);
            con.setAutoCommit(false); //Need to update 2 tables at once, so use transaction
            stmt = con.prepareStatement(loansQuery);
            //Assign parameters for query 1
            stmt.setString(1, dewey);
            stmt.executeUpdate();
            stmt.clearParameters(); //Just in case...
            stmt.close(); //Just in case...
            stmt = con.prepareStatement(copiesQuery);
            //Assign for query 2
            stmt.setBoolean(1, false);
            stmt.setString(2, dewey);
            stmt.executeUpdate();
            con.commit();
        }
        catch (SQLException e)
        {
            errorMessage = e.getMessage();
        }
        finally
        {
            //Close any open connections
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e2)
                {
                    e2.printStackTrace();
                }
            }
        }
        if (errorMessage != null)
        {
            throw new SQLException(errorMessage);
        }
    }

    /**
     * Updates a loan and sets the new dueDate
     * @param borrower The borrower's id
     * @param dewey The dewey id of the loaned copy
     * @param newDueDate The new dueDate for the loan
     */
    public void updateLoan(int borrower, String dewey, java.util.Date newDueDate) throws SQLException
    {
		//VALIDATIN

		//can only renew if the loan has not been recalled

		//and the borrower has not overdue loans
		Borrower borrower = this.getBorrower(borrower);

		if(borrower.getCopiesOverDue().size() > 0)
			throw new LibraryRulesException("That borrower already has overdue loans!");


        String query = "UPDATE loans SET dueDate = ? WHERE borrowerID=? AND deweyID=?";
        PreparedStatement stmt = null;
        String errorMessage = null;
        try
        {
            Connection con = DriverManager.getConnection(DATABASE_CONNECTION);
            stmt = con.prepareStatement(query);
            //Assign the parameters for the query
            stmt.setDate(1, new java.sql.Date(newDueDate.getTime()));
            stmt.setInt(2, borrower);
            stmt.setString(3, dewey);
            //Execute the query
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            errorMessage = e.getMessage();
        }
        finally
        {
            //Close any open connections
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e2)
                {
                    e2.printStackTrace();
                }
            }
        }
        if (errorMessage != null)
        {
            throw new SQLException(errorMessage);
        }
    }

    //==============================================================
// BORROWERS GETTER METHODS
//==============================================================
    public ArrayList<Borrower> getBorrowers(Hashtable<String,Object> params) throws DataNotFoundException, InvalidArgumentException
    {
        ArrayList<Object> objects = this.getObjects(params, "borrowers");
        ArrayList<Borrower> borrowers = new ArrayList<Borrower>();

        //Cast it as a borrower object
        for (Object object : objects)
        {
            borrowers.add((Borrower) object);
        }

        return borrowers;
    }

    public ArrayList<Borrower> getBorrowers() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getBorrowers(new Hashtable<String,Object>());
    }

    public Borrower getBorrower(int id) throws DataNotFoundException, InvalidArgumentException
    {
        Hashtable<String,Object> params = new Hashtable<String,Object>();
        params.put("borrowerID", id);
        Borrower borrower;
        try
        {
            borrower = this.getBorrowers(params).get(0);
        }catch(IndexOutOfBoundsException e)
        {
            throw new DataNotFoundException("No borrower was found with the id:"+id);
        }

        return borrower;
    }
//==============================================================
// BOOKS GETTER METHODS
//==============================================================

    public ArrayList<Item> getBooks(Hashtable<String, Object> params) throws DataNotFoundException, InvalidArgumentException
    {
        ArrayList<Object> objects = this.getObjects(params, "books");
        ArrayList<Item> books = new ArrayList<Item>();

        //Cast it as a Loan object
        for (Object object : objects)
        {
            books.add((Item) object);
        }

        return books;
    }

    public ArrayList<Item> getBooks() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getBooks(new Hashtable<String,Object>());
    }

    public Item getBook(int isbn) throws DataNotFoundException, InvalidArgumentException
    {
        Hashtable<String,Object> params = new Hashtable<String,Object>();
        params.put("isbn", isbn);


        Item item;
        try
        {
            item = this.getBooks(params).get(0);
        }catch(IndexOutOfBoundsException e)
        {
            throw new DataNotFoundException("No book was found with the isbn:"+isbn);
        }

        return item;
    }

//==============================================================
// PERIODICALS GETTER METHODS
//==============================================================

    public ArrayList<Item> getPeriodicals(Hashtable<String, Object> params) throws DataNotFoundException, InvalidArgumentException
    {
        ArrayList<Object> objects = this.getObjects(params, "periodicals");
        ArrayList<Item> periodicals = new ArrayList<Item>();

        //Cast it as a Loan object
        for (Object object : objects)
        {
            periodicals.add((Item) object);
        }

        return periodicals;
    }

    public ArrayList<Item> getPeriodicals() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getPeriodicals(new Hashtable<String,Object>());
    }

    public Item getPeriodical(int issn) throws DataNotFoundException, InvalidArgumentException
    {
        Hashtable<String,Object> params = new Hashtable<String,Object>();
        params.put("issn", issn);

        Item item;
        try
        {
            item = this.getPeriodicals(params).get(0);
        }catch(IndexOutOfBoundsException e)
        {
            throw new DataNotFoundException("No periodical was found with the issn:"+issn);
        }

        return item;
    }

//==============================================================
// COPIES GETTER METHODS
//==============================================================

    public ArrayList<Copy> getCopies(Hashtable<String, Object> params) throws DataNotFoundException, InvalidArgumentException
    {
        ArrayList<Object> objects = this.getObjects(params, "copies");
        ArrayList<Copy> copies = new ArrayList<Copy>();

        //Cast it as a Loan object
        for (Object object : objects)
        {
            copies.add((Copy) object);
        }

        return copies;
    }

    public ArrayList<Copy> getCopies() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getCopies(new Hashtable<String,Object>());
    }

    public Copy getCopy(String deweyID) throws DataNotFoundException, InvalidArgumentException
    {
        Hashtable<String,Object> params = new Hashtable<String,Object>();
        params.put("deweyID", deweyID);
        Copy copy;
        try
        {
            copy = this.getCopies(params).get(0);
        }catch(IndexOutOfBoundsException e)
        {
            throw new DataNotFoundException("No copy was found with the dewey id:"+deweyID);
        }

        return copy;
    }

    //==============================================================
// LOANS GETTER METHODS
//==============================================================
    public ArrayList<Loan> getLoans(Hashtable<String, Object> params) throws DataNotFoundException, InvalidArgumentException
    {
        ArrayList<Object> objects = this.getObjects(params, "loans");
        ArrayList<Loan> loans = new ArrayList<Loan>();

        //Cast it as a Loan object
        for (Object object : objects)
        {
            loans.add((Loan) object);
        }

        return loans;
    }

    public ArrayList<Loan> getLoans() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getLoans(new Hashtable<String,Object>());
    }

    public Loan getLoan(int id) throws DataNotFoundException, InvalidArgumentException
    {
        Hashtable<String,Object> params = new Hashtable<String,Object>();
        params.put("borrowerID", id);

        Loan loan;
        try
        {
            loan = this.getLoans(params).get(0);
        }catch(IndexOutOfBoundsException e)
        {
            throw new DataNotFoundException("No loan was found with the id:"+id);
        }
        return loan;
    }

//==============================================================
// RESERVATIONS GETTER METHODS
//==============================================================
    public ArrayList<Reservation> getReservations(Hashtable<String, Object> params) throws DataNotFoundException, InvalidArgumentException
    {
        ArrayList<Object> objects = this.getObjects(params, "reservations");
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();

        //Cast it as a Loan object
        for (Object object : objects)
        {
            reservations.add((Reservation) object);
        }

        return reservations;
    }

    public ArrayList<Reservation> getReservations() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getReservations(new Hashtable<String,Object>());
    }

    public Reservation getReservation(int id) throws DataNotFoundException, InvalidArgumentException
    {
        Hashtable<String,Object> params = new Hashtable<String,Object>();
        params.put("borrowerID", id);

        Reservation reservation;
        try
        {
            reservation = this.getReservations(params).get(0);
        }catch(IndexOutOfBoundsException e)
        {
            throw new DataNotFoundException("No reservation was found with the id:"+id);
        }

        return reservation;
    }

//==============================================================
// SETTER METHODS
//==============================================================

    /**
     * Adds a new reservation to the database.
     * @param details A hashtable of all the details. It expects the following keys:
     *                <ul>
     *                <li>"borrowerID"</li>
     *                <li>"isbn"</li>
     *                <li>"issn"</li>
     *                <li>"date"</li></ul>
     *                <strong>NOTE:</strong> If both isbn and issn are keys, isbn will take priority. Do not pass null
     *                as a value to the "isbn" key.
     */
    public void addReservation(Hashtable<String, Object> details) throws SQLException, LibraryRulesException, DataNotFoundException
    {
        String query = "INSERT INTO reservations(borrowerID, isbn, issn, date) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = null;
        String errorMessage = null; //Used if we have an error
        ArrayList<Copy> copies = new ArrayList<Copy>();
        try
        {
            if (details.containsKey("isbn"))
            {
                copies = Database.find_copies_by_isbn((String) details.get("isbn"));
            }
            else
            {
                copies = Database.find_copies_by_issn((String) details.get("issn"));
            }
        }
        catch (InvalidArgumentException e)
        {
            throw new SQLDataException(e.getMessage());
        }
        boolean canReserve = false;
        ArrayList<Loan> loans = new ArrayList<Loan>();
        try
        {
            for (Copy copy: copies)
            {
                if (!canReserve)
                {
                    try
                    {
                        loans.add(Database.find_loans_by_deweyid(copy.deweyIndex));
                    }
                    catch (DataNotFoundException e)
                    {
                        canReserve = true;
                    }
                }
            }
        }
        catch (InvalidArgumentException e)
        {
            throw new SQLDataException(e.getMessage());
        }
        //If there are no free copies, recall earliest loan (dueDate becomes week today)
        if (!canReserve)
        {
            //Find the earliest loan
            Loan earliestLoan = new Loan("2", new java.util.Date(), 0, copies.get(0));
            for (Loan loan: loans)
            {
                if (loan.issueDate.before(earliestLoan.issueDate))
                {
                    earliestLoan = loan;
                }
            }
            DateTime newDueDate = new DateTime();
            newDueDate = newDueDate.plusWeeks(1);
            updateLoan(earliestLoan.borrower_id, earliestLoan.deweyID, new java.util.Date(newDueDate.getMillis()));
        }
        try
        {
            Connection con = DriverManager.getConnection(DATABASE_CONNECTION);
            stmt = con.prepareStatement(query);
            //Give isbn and issn the right values in the database
            if (details.containsKey("isbn"))
            {
                stmt.setInt(2, Integer.parseInt((String) details.get("isbn")));
                stmt.setInt(3, 0);
            }
            else
            {
                stmt.setInt(3, Integer.parseInt((String)details.get("issn")));
                stmt.setInt(2, 0);
            }
            stmt.setInt(1,Integer.parseInt((String)details.get("borrowerID")));
            java.util.Date date = (java.util.Date) details.get("date");
            stmt.setDate(4, new java.sql.Date(date.getTime()));
            //Add to database
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            errorMessage = e.getMessage();
        }
        finally
        {
            //Close any open connections
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();  //AUTOGEN
                }
            }
        }
        if (errorMessage != null)
        {
            throw new SQLException(errorMessage);
        }
        if (!canReserve)
        {
            throw new LibraryRulesException("Your copy has been reserved, but all copies are on loan. Please wait a week");
        }
    }

    /**
     * Deletes a reservation from the database
     * @param details A hashtable of all the details of the reservation. It expects the following keys:
     *                <ul>
     *                <li>"borrowerID"</li>
     *                <li>"isbn"</li>
     *                <li>"issn"</li>
     *                <strong>NOTE:</strong> If both isbn and issn are keys, isbn will take priority. Do not pass null
     *                as a value to the "isbn" key.
     */
    public void deleteReservation(Hashtable<String, Object> details) throws SQLException
    {
        String query = "DELETE FROM reservations WHERE id= ? AND ";
        int firstParam = (Integer) details.get("borrowerID");
        int secondParam;
        PreparedStatement stmt = null;
        String errorMessage = null; //Used if there's an error
        //Sort out the case of isbn/issn keys
        if (details.containsKey("isbn"))
        {
            query += "isbn = ?;";
            secondParam = (Integer) details.get("isbn");
        }
        else
        {
            query += "isbn = ?;";
            secondParam = (Integer) details.get("isbn");
        }
        try
        {
            Connection con = DriverManager.getConnection(DATABASE_CONNECTION);
            stmt = con.prepareStatement(query);
            //assign parameters
            stmt.setInt(1, firstParam);
            stmt.setInt(2, secondParam);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            errorMessage = e.getMessage();
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
                }
            }
        }
        if (errorMessage != null)
        {
            throw new SQLException(errorMessage);
        }
    }

//==============================================================
// DATABASE CORE 
//==============================================================
    private ArrayList<Object> getObjects(Hashtable<String,Object> params, String table_name) throws DataNotFoundException, InvalidArgumentException
    {
        ResultSet result = null;
        Statement stmt = null;
        String query = "SELECT * FROM `"+table_name+"` WHERE ";

        ArrayList<Object> objects = new ArrayList<Object>();
        try
        {
            Connection con = DriverManager.getConnection(DATABASE_CONNECTION);
            stmt = con.createStatement();
            result = stmt.executeQuery("SELECT * FROM `"+table_name+"`");

            //If search parameters have been specified
            if(params.size() > 0)
            {
                //1. Retreive number of columns, the column names and their data types from the ResultSet
                ResultSetMetaData metaData = result.getMetaData();
                int numberOfColumns = metaData.getColumnCount();

                //2. Create an array of the db table's column names
                //This is used to check that the right search fields have been inputted
                Hashtable<String, String> columns = new Hashtable<String, String>();
                ArrayList<String> columns_names = new ArrayList<String>();

                for(int i=1; i<=numberOfColumns; i++)
                {
                    columns.put(metaData.getColumnName(i), metaData.getColumnTypeName(i));
                    columns_names.add(metaData.getColumnName(i));
                }

                //3. BUILD THE SQL QUERY
                // check that no invalid columns names have been set in the params Hashtable
                // if it is valid then concatenate the relevant SQL code it to the query String
                Set set = params.entrySet();
                Iterator it = set.iterator();

                while(it.hasNext())
                {
                    Map.Entry search_col = (Map.Entry) it.next();

                    //If there is a search field in the inputted params that is not valid then throw an exception
                    if(!columns_names.contains(search_col.getKey()))
                        throw new InvalidArgumentException("The search field you have entered: '"+search_col.getKey()+"' is not a valid field in table: "+table_name+" !");

                    //Otherwise add it to the search query
                    if(columns.get(search_col.getKey()).equals("VARCHAR"))
                    {
                        query += search_col.getKey()+" LIKE '"+search_col.getValue()+"'";
                    }else if(columns.get(search_col.getKey()).equals("DATE"))
                    {
                        query += search_col.getKey()+"="+search_col.getValue();
                    }else if(columns.get(search_col.getKey()).equals("INT"))
                    {
                        query += search_col.getKey()+"="+search_col.getValue();
                    }else if(columns.get(search_col.getKey()).equals("CHAR")){
                        query += search_col.getKey()+"='"+search_col.getValue()+"'";
                    }

                    if(it.hasNext())
                        query += " AND ";
                }

                stmt.close();

                //If no search parameters have been specified then return all rows
            }
            else
            {
                query = "SELECT * FROM `"+table_name+"`";
            }
            //4. Execute the query and instantiate the relevant objects from the results data
            stmt = con.createStatement();
            result = stmt.executeQuery(query);
            while(result.next())
            {
                //Instantiate the objects depending on which table has been selected
                if(table_name.equals("loans"))
                {
                    objects.add(new Loan(result.getString("deweyID"), (java.util.Date) result.getDate("issueDate"), result.getInt("borrowerID")));				//Using java.util.Date to avoid conflict with java.sql.Date
                }else if(table_name.equals("borrowers"))
                {
                    objects.add(new Borrower(result.getInt("borrowerID"), result.getString("forename"), result.getString("surname"), result.getString("email")));
                }else if(table_name.equals("copies"))
                {
                    String dewey = result.getString("deweyID");
                    Boolean reference = result.getBoolean("reference");
                    Boolean onLoan = result.getBoolean("onLoan");
                    int isbn = result.getInt("isbn");
                    int issn = result.getInt("issn");

                    Item item;

                    if(isbn == 0)
                    {
                        item = this.getPeriodical(issn);
                    }else{
                        item = this.getBook(isbn);
                    }
                    objects.add(new Copy(dewey, reference, item));
                }else if(table_name.equals("books"))
                {
                    objects.add(new Item(""+result.getInt("isbn"), result.getString("title"), result.getString("author"), result.getString("publisher"), (java.util.Date) result.getDate("date")));
                }else if(table_name.equals("periodicals"))
                {
                    objects.add(new Item(Integer.toString(result.getInt("issn")), result.getString("title"), result.getInt("volume"), result.getInt("number"), result.getString("publisher"), (java.util.Date) result.getDate("date")));
                }else if(table_name.equals("reservations"))
                {
                    int isbn = result.getInt("isbn");
                    int issn = result.getInt("issn");
                    Item item;

                    if(isbn == 0)
                    {
                        item = this.getPeriodical(issn);
                    }else{
                        item = this.getBook(isbn);
                    }

                    objects.add(new Reservation((java.util.Date) result.getDate("date"), result.getInt("borrowerID"), item));
                }

            }

            stmt.close();
            con.close();

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
                }
            }


        }
        return objects;
    }
}
