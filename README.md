For Evan: sort out Loan.getFine()

Things to Validate

- COMPLETED: Mysql.addLoan() (borrower cannot have more than 6 loans, and no laons overdue)

- EVAN: Mysql.addLoan() - copy must not be reference only
- EVAN: Mysql.addLoan() - no outstanding reservations unless they themselves have reserved it
-

- PADDY: Mysql.addReservation() - if no free copies then recall
 
	-check that its a valid borrower ID

- EVAN: Renewing a loan: Mysql.updateLoan()

	-can only renew if the loan has not been recalled
	-and the borrower has not overdue loans

- PADDY: Discharging a loan: Mysql.deleteLoan()

	-copy must be on loan
	-if Loan.getFine() > 0 then display popup with fines, then still delete the loan
