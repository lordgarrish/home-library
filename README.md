## Home library database
You can create your home library database by running library.sql in MySQL, PostgreSQL or another RDBMS.
With this app you can view, add, rename and delete books from the library.
Tables consist of author's name, book title and genre and sorted by author's id. 
Two tables are referencing by one-to-many relationship, with author's id being the foreign key in books table.  
This project I've made during learning the JDBC API of Java.

### Usage:
You will need Java 11 or later.<br><br>
Inside `src` folder run the following command in your terminal:<br>
```
java Main.java
```
Follow instructions on screen.
