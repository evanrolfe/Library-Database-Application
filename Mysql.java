import org.joda.time.DateTime;

import java.sql.*;
import java.util.*;
import java.util.Date;

/*
 * Run on command line using:
javac -cp joda.jar:. *.java && java -cp connector.jar:joda.jar:. MysqlTest
 */

/**
 * This handles all the mySQL interactions.
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
        //Generate query
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
		
			//3. Find the first reservation in the queue (one with earliest date)
			if(reservations.size() > 0)
			{
				Reservation earliestReservation=null;

				for(Reservation reservation : reservations)
				{
					if (earliestReservation==null || earliestReservation.reserveDate.getTime() < reservation.reserveDate.getTime())
						earliestReservation=reservation;
				}

				if (earliestReservation.borrower_id!=id)
					throw new LibraryRulesException("That has already been reserved by another borrower!");
			}
		}
        catch(InvalidArgumentException e1)
		{
            throw new SQLException(e1.getMessage());
		}

        try
        {
            Connection con = DriverManager.getConnection(DATABASE_CONNECTION);
            stmt = con.prepareStatement(loansQuery);
            //Assign the parameters for the first query
            stmt.setInt(1, id);
            stmt.setString(2, deweyID);
            stmt.setDate(3, new java.sql.Date(issueDate.getTime()));
            stmt.setDate(4, new java.sql.Date(dueDate.getTime()));
            //Execute the query
            stmt.executeUpdate();
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
    public void deleteLoan(String dewey) throws SQLException, LibraryRulesException, DataNotFoundException
    {
        String loansQuery = "DELETE FROM loans WHERE deweyID=?";
        PreparedStatement stmt = null;
        String errorMessage = null; //Used if we have an error
        //Check if borrower has overdue fines and throw an error if this is the case
        int totalFine;
        try
        {
            Loan loan = Database.find_loans_by_deweyid(dewey);
            totalFine = loan.getFine();
        }
        catch (InvalidArgumentException e)
        {
            throw new SQLDataException(e.getMessage());
        }
        //Throw an error if it has a fine
        if (totalFine>0)
        {
            throw new LibraryRulesException("You owe a fine of £" + totalFine + ".");
        }
        try
        {
            Connection con = DriverManager.getConnection(DATABASE_CONNECTION);
            stmt = con.prepareStatement(loansQuery);
            //Assign parameters for query
            stmt.setString(1, dewey);
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

    /**
     * Renews a loan, according to the specified rules.
     * @param borrowerID The id of the borrower who is renewing a loan
     * @param dewey The dewey id of the copy that is being renewed
     * @throws DataNotFoundException If something isn't found in the database
     * @throws SQLException If there's an SQL issue
     * @throws LibraryRulesException If the rules are not kept to
     */
    public void renewLoan(int borrowerID, String dewey) throws DataNotFoundException, SQLException, LibraryRulesException
    {
        //VALIDATING

        //can only renew if the loan has not been recalled

        //and the borrower has not overdue loans
        Borrower borrower = null;
        try
        {
            borrower = getBorrower(borrowerID);
            int size = 0;
            try
            {
                size = borrower.getCopiesOverDue().size();
            }
            catch (Exception e)
            {
                throw new DataNotFoundException(e.getMessage());
            }
            if (size > 0)
            {
                throw new LibraryRulesException("That borrower already has overdue loans!");
            }
            Loan loan = Database.find_loans_by_deweyid(dewey);
            if (loan.recalled)
            {
                throw new LibraryRulesException("This loan has been recalled");
            }
        }
        catch (InvalidArgumentException e)
        {
            throw new SQLException(e.getMessage());
        }
        DateTime dateTime = DateTime.now();
        dateTime = dateTime.plusWeeks(1);
        updateLoan(borrowerID, dewey, new java.util.Date(dateTime.getMillis()), false);
    }

    /**
     * Updates a loan and sets the new dueDate
     * @param borrowerID The borrower's id
     * @param dewey The dewey id of the loaned copy
     * @param newDueDate The new dueDate for the loan
     * @param recall If the loan is to be recalled
     * @throws SQLException If there's a problem connecting to the database
     */
    private void updateLoan(int borrowerID, String dewey, java.util.Date newDueDate, boolean recall) throws SQLException
    {
        String query = "UPDATE loans SET dueDate = ? WHERE borrowerID=? AND deweyID=?";
        if(recall)
        {
            query = "UPDATE loans SET dueDate = ?, recalled = 1 WHERE borrowerID=? AND deweyID=?";
        }
        PreparedStatement stmt = null;
        String errorMessage = null;
        try
        {
            Connection con = DriverManager.getConnection(DATABASE_CONNECTION);
            stmt = con.prepareStatement(query);
            //Assign the parameters for the query
            stmt.setDate(1, new java.sql.Date(newDueDate.getTime()));
            stmt.setInt(2, borrowerID);
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

    /**
     * Finds all the borrowers in the database
     * @param params The various options for searching
     * @return An arraylist of borrowers
     * @throws DataNotFoundException If there is nothing in the database
     * @throws InvalidArgumentException If the arguments given were invalid
     */
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

    /**
     * Gets all the borrowers
     * @return An array list of all the borrowers
     * @throws DataNotFoundException If there is nothing in the database
     * @throws InvalidArgumentException If the arguments given were invalid
     */
    public ArrayList<Borrower> getBorrowers() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getBorrowers(new Hashtable<String,Object>());
    }

    /**
     * Gets a single borrower
     * @param id The id of a borrower
     * @return A single Borrower object
     * @throws DataNotFoundException If that borrower doesn't exist
     * @throws InvalidArgumentException If the arguments given were invalid
     */
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

    /**
     * Gets books from the database
     * @param params The search options
     * @return An array list of the books
     * @throws DataNotFoundException If there are no details in the database
     * @throws InvalidArgumentException If the search options were invalid
     */
    public ArrayList<Item> getBooks(Hashtable<String, Object> params) throws DataNotFoundException, InvalidArgumentException
    {
        ArrayList<Object> objects = this.getObjects(params, "books");
        ArrayList<Item> books = new ArrayList<Item>();

        //Cast it as an item object
        for (Object object : objects)
        {
            books.add((Item) object);
        }

        return books;
    }

    /**
     * Gets all the books from the database
     * @return An array list of books
     * @throws DataNotFoundException If there are no details in the database
     * @throws InvalidArgumentException If the search options were invalid
     */
    public ArrayList<Item> getBooks() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getBooks(new Hashtable<String,Object>());
    }

    /**
     * Gets a certain book
     * @param isbn The isbn of the book to search from
     * @return An item object relating to book.
     * @throws DataNotFoundException If that isbn doesn't exist
     * @throws InvalidArgumentException If the search options are invalid
     */
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

    /**
     * Gets periodicals from the database
     * @param params The search options
     * @return An array list of the periodicals
     * @throws DataNotFoundException If there are no details in the database
     * @throws InvalidArgumentException If the search options were invalid
     */
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

    /**
     * Gets all the periodicals
     * @return An array list of the periodicals
     * @throws DataNotFoundException If there are no details in the database
     * @throws InvalidArgumentException If the search options were invalid
     */
    public ArrayList<Item> getPeriodicals() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getPeriodicals(new Hashtable<String,Object>());
    }

    /**
     * Gets a specific periodical
     * @param issn The issn of the periodical
     * @return An item object relating to that periodical
     * @throws DataNotFoundException If there is no periodical with that name
     * @throws InvalidArgumentException If the search options were invalid
     */
    public Item getPeriodical(int issn) throws DataNotFoundException, InvalidArgumentException
    {
        Hashtable<String,Object> params = new Hashtable<String,Object>();
        params.put("issn", issn);
        Item item;
        try
        {
            item = this.getPeriodicals(params).get(0);
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new DataNotFoundException("No periodical was found with the issn:"+issn);
        }

        return item;
    }

//==============================================================
// COPIES GETTER METHODS
//==============================================================

    /**
     * Gets copies from the database
     * @param params The search options
     * @return An array list of copies
     * @throws DataNotFoundException If no copies are in the database
     * @throws InvalidArgumentException If the search options were invalid
     */
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

    /**
     * Gets all the copies from the database
     * @return An array list of the copies
     * @throws DataNotFoundException If there are no loans in the database
     * @throws InvalidArgumentException If the search options were invalid
     */
    public ArrayList<Copy> getCopies() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getCopies(new Hashtable<String,Object>());
    }

    /**
     * Gets a certain copy from the database
     * @param deweyID The dewey id of the copy
     * @return A single copy object
     * @throws DataNotFoundException If that deweyID doesn't relate to any copies in the database
     * @throws InvalidArgumentException If the serach options were invalid
     */
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

    /**
     * Gets some loans from the database
     * @param params The search options
     * @return An array list of loans
     * @throws DataNotFoundException If there were no loans in the database
     * @throws InvalidArgumentException If the search options were invalid
     */
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

    /**
     * Gets all the loans
     * @return An array list of loans from the database
     * @throws DataNotFoundException If there were no loans in the database
     * @throws InvalidArgumentException If the search options were invalid
     */
    public ArrayList<Loan> getLoans() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getLoans(new Hashtable<String,Object>());
    }

    /**
     * Gets a loan from the database
     * @param id The id of a borrower
     * @return The first loan that the borrower has
     * @throws DataNotFoundException If there were no loans
     * @throws InvalidArgumentException If the search options were invalid
     */
    public Loan getLoan(int id) throws DataNotFoundException, InvalidArgumentException
    {
        Hashtable<String,Object> params = new Hashtable<String,Object>();
        params.put("borrowerID", id);

        Loan loan;
        try
        {
            loan = this.getLoans(params).get(0);
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new DataNotFoundException("No loan was found with the id:"+id);
        }
        return loan;
    }

//==============================================================
// RESERVATIONS GETTER METHODS
//==============================================================

    /**
     * Gets some reservations from the database
     * @param params The search options
     * @return An array list of reservations from the database
     * @throws DataNotFoundException If there were no reservations in the database
     * @throws InvalidArgumentException If the options were invalid
     */
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

    /**
     * Gets all the reservations from the database
     * @return An array list of all the reservations in the database
     * @throws DataNotFoundException If there were no reservations in the database
     * @throws InvalidArgumentException If the options were invalid
     */
    public ArrayList<Reservation> getReservations() throws DataNotFoundException, InvalidArgumentException
    {
        return this.getReservations(new Hashtable<String,Object>());
    }

    /**
     * Returns a reservation from the database for a borrower
     * @param id The id of a borrower
     * @return The first reservation found for that borrower
     * @throws DataNotFoundException If there were no reservations for that borrower
     * @throws InvalidArgumentException If the search options were invalid
     */
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
     *                <li>"borrowerID" -> int</li>
     *                <li>"isbn" -> int</li>
     *                <li>"issn" -> int</li>
     *                <strong>NOTE:</strong> If both isbn and issn are keys, isbn will take priority. Do not pass null
     *                as a value to the "isbn" key.
     */
    public void addReservation(Hashtable<String, Object> details) throws SQLException, LibraryRulesException, DataNotFoundException
    {
        String query = "INSERT INTO reservations(borrowerID, isbn, issn, date) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = null;
        String errorMessage = null; //Used if we have an error
        ArrayList<Copy> copies;
        try
        {
            if (details.containsKey("isbn"))
            {
                copies = Database.find_copies_by_isbn(details.get("isbn").toString());
            }
            else
            {
                copies = Database.find_copies_by_issn(details.get("issn").toString());
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
                        if (!copy.referenceOnly)
                        {
                            canReserve = true;
                        }
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
            recallLoan(earliestLoan.borrower_id, earliestLoan.deweyID, new java.util.Date(newDueDate.getMillis()));
        }
        try
        {
            Connection con = DriverManager.getConnection(DATABASE_CONNECTION);
            stmt = con.prepareStatement(query);
            //Give isbn and issn the right values in the database
            if (details.containsKey("isbn"))
            {
                stmt.setInt(2, Integer.parseInt(details.get("isbn").toString()));
                stmt.setInt(3, 0);
            }
            else
            {
                stmt.setInt(3, Integer.parseInt((String)details.get("issn").toString()));
                stmt.setInt(2, 0);
            }
            stmt.setInt(1,Integer.parseInt(details.get("borrowerID").toString()));
            DateTime date = DateTime.now();
            stmt.setTimestamp(4, new Timestamp(date.getMillis()));
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

    private void recallLoan(int borrowerID, String deweyID, Date date) throws SQLException
    {
        updateLoan(borrowerID, deweyID, date, true);
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
    /**
     * Select rows from a table with optional search parameters and return an arraylist of objects (which are instantiated depending on which table was called i.e. if table `loans` is called then it returns an arraylist of Loan objects.
     * @return ArrayList<Object> the objects that are created from the data from mysql
     * @throws DataNotFoundException if no data was found
     * @throws InvalidArgumentException if a search column specified in the params is not a valid column in the mysql table
     */
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
                    objects.add(new Loan(result.getString("deweyID"), (java.util.Date) result.getDate("issueDate"), (java.util.Date) result.getDate("dueDate"), result.getInt("borrowerID"), result.getBoolean("recalled")));				//Using java.util.Date to avoid conflict with java.sql.Date
                }else if(table_name.equals("borrowers"))
                {
                    objects.add(new Borrower(result.getInt("borrowerID"), result.getString("forename"), result.getString("surname"), result.getString("email")));
                }else if(table_name.equals("copies"))
                {
                    String dewey = result.getString("deweyID");
                    Boolean reference = result.getBoolean("reference");
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
