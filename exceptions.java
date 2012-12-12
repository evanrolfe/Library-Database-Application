/**
 * LibraryRulesException is thrown when a method tries to bend the rules of the system. For example,
 * trying to issue a 7th loan to a borrower.
 */
class LibraryRulesException extends Exception 
{

	/**
	 * LibraryRulesException constructor
	 * @param message The message which will be displayed when the exception is thrown
	 */
	public LibraryRulesException(String message) 
	{
		super(message);
	}
}

/**
 * InvalidArgumentsException is thrown when a method is not given compatible arguments.
 */
class InvalidArgumentException extends Exception 
{

	/**
	 * InvalidArugementException constructor
	 * @param message The message which will be displayed when the exception is thrown
	 */
	public InvalidArgumentException(String message) 
	{
		super(message);
	}
}

/**
 * LoanException is thrown when a Loan may not be created or updated.
 */
class LoanException extends Exception 
{
	
	/**
	 * LoanException constructor
	 * @param message The message which will be displayed when the exception is thrown
	 */
	public LoanException(String message) 
	{
		super(message);
	}	
}

/**
 * ReservationException is thrown when a Reservation may not be created.
 */
class ReservationException extends Exception 
{

	/**
	 * ReservationException constructor
	 * @param message The message which will be displayed when the exception is thrown
	 */
	public ReservationException(String message) 
	{
		super(message);
	}	
}

/**
 * DataNotFoundException is thrown by methods where a search has been carried out and the data searched for has not been found.
 */
class DataNotFoundException extends Exception 
{

	/**
	 * DataNotFoundException constructor
	 * @param message The message which will be displayed when the exception is thrown
	 */
	public DataNotFoundException(String message) 
	{
		super(message);
	}
}
