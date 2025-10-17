        MarqBuryComics Ledger Application

Welcome to the MarqBuryComics Ledger Application, a Java-based ledger system designed specifically for managing financial records for fictional MarqburyComics store. This application enables users to securely log in, manage transactions, and search or filter financial entries using various criteria


Features:

User Login System - Prompts the user to log in with user and password + allows 3 login attempts before terminating the application

Transaction Management - 
Add Deposits + Purchases, Stores transactions in csv file

Ledger Viewing Options - View full ledger, Filter by time periods( Month to Date, Previous Month, Year to Date, Previous Year)

Custom Search - Filter based on Start date, End date, Description, Vendor, and/or Amount


Interesting piece of code :
<pre> ```String.format("%-12s| %-11s| %-60s| %-22s|%10.2f\n"``` </pre>


This line is formatting a string using format specifiers basically placeholders that define how values (like strings, numbers, etc.) should be displayed.

"%-12s" == String that is 12 character wide placement to the left

"%10.2f" == float that is 10 character wide placement to the right, containing 2 decimal points 