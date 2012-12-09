import java.util.*;
import java.sql.*;

/*
 * Run on command line using:
javac *.java && java -cp connector.jar:. Mysql
 */

public class Mysql
{
    private String databaseConnection = "jdbc:mysql://localhost/team007?user=root&password=pass";

//==============================================================
// BORROWERS GETTER METHODS
//==============================================================
    public ArrayList<Borrower> getBorrowers(Hashtable<String,Object> params) throws DataNotFoundException, InvalidArgumentException
	{
		ArrayList<Object> objects = this.getObjects(params, "borrowers");
		ArrayList<Borrower> borrowers = new ArrayList<Borrower>();

		//Cast it as a Loan object
		for(int i=0; i<objects.size(); i++)
		{
			borrowers.add((Borrower)objects.get(i));
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
		params.put("id", id);
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
		for(int i=0; i<objects.size(); i++)
		{
			books.add((Item)objects.get(i));
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
		for(int i=0; i<objects.size(); i++)
		{
			periodicals.add((Item)objects.get(i));
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
		for(int i=0; i<objects.size(); i++)
		{
			copies.add((Copy)objects.get(i));
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
		for(int i=0; i<objects.size(); i++)
		{
			loans.add((Loan)objects.get(i));
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
		params.put("id", id);

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
		for(int i=0; i<objects.size(); i++)
		{
			reservations.add((Reservation)objects.get(i));
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
		params.put("id", id);

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

    public void updateLoan(Hashtable<String, Object> details, java.util.Date newDate)
    {
        String query = "UPDATE loans SET date=" + newDate + " WHERE id=" +details.get("id") + " AND deweyID=" + details.get("deweyID");
        runQuery(query);
    }

    public void createReservation(Hashtable<String, Object> details)
    {
        String query = "INSERT INTO reservations(id, isbn, issn, date) VALUES ("+ details.get("id") + ", ";
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
        String query = "DELETE FROM reservations WHERE id="+details.get("id")+"AND ";
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
            Connection con = DriverManager.getConnection(databaseConnection);
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
			}else{
				query = "SELECT * FROM `"+table_name+"`";
			}
			//4. Execute the query and instantiate the relevant objects from the results data
            stmt = con.createStatement();
            result = stmt.executeQuery(query);
			while(result.next())
			{
				//Instantiate the objects depending on which table has been selected
				if(table_name == "loans")
				{
					objects.add(new Loan(result.getString("deweyID"), (java.util.Date) result.getDate("issueDate"), result.getInt("id")));				//Using java.util.Date to avoid conflict with java.sql.Date
				}else if(table_name == "borrowers")
				{
					objects.add(new Borrower(result.getInt("id"), result.getString("forename"), result.getString("surname"), result.getString("email")));	
				}else if(table_name == "copies")
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

					//TODO
			        objects.add(new Copy(dewey, reference, item));				
				}else if(table_name == "books")
				{
		            objects.add(new Item(""+result.getInt("isbn"), result.getString("title"), result.getString("author"), result.getString("publisher"), (java.util.Date) result.getDate("date")));					
				}else if(table_name == "periodicals")
				{
               		objects.add(new Item(Integer.toString(result.getInt("issn")), result.getString("title"), result.getInt("volume"), result.getInt("number"), result.getString("publisher"), (java.util.Date) result.getDate("date")));
				}else if(table_name == "reservations")
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

                	objects.add(new Reservation((java.util.Date) result.getDate("date"), result.getInt("id"), item));					
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


    private ResultSet runQuery (String query)
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
	
}