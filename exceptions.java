class LibraryRulesException extends Exception {

	public LibraryRulesException(String message) 
	{
		super(message);
	}
	
}

//Invalidargumentss exception deals with exceptions where a method is not given compatible arguments.
class InvalidArgumentException extends Exception {

	public InvalidArgumentException(String message) 
	{
		super(message);
	}
	
}

//Loan exception is applied to methods dealing with loans that may fail for a variety of reasons.
class LoanException extends Exception {

	public LoanException(String message) 
	{
		super(message);
	}
	
}

//Reservation exception is applied to methods dealing with loans that may fail for a variety of reasons.
class ReservationException extends Exception {

	public ReservationException(String message) 
	{
		super(message);
	}
	
}

//DataNotFoundException is thrown by methods where a search has been carried out and the data searched for has not been found.
class DataNotFoundException extends Exception {

	public DataNotFoundException(String message) 
	{
		super(message);
	}
	
}
