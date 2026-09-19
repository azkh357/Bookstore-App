# BookStoreApp

BookStoreApp is a small Java Swing desktop application for managing a bookstore.
It was built as an object-oriented programming project.

## What it can do

- Log in as the store owner or as a customer
- Add and remove books from the inventory
- Add and remove customer accounts
- Buy books from the inventory
- Redeem customer points during a purchase
- Track customer points and Silver/Gold membership status
- Save books and customer data in local text files

## Technologies

- Java
- Java Swing
- Apache Ant / NetBeans project
- Local text-file storage

## Running the application

You need a compatible Java JDK and Apache Ant installed.

From the project folder, run:

```
ant clean jar
ant run
```

You can also run the packaged application:

```
java -jar dist\BookStoreApp.jar
```

Run the application from the project folder so it can find `books.txt` and
`customers.txt`.

## Demo accounts

Owner:

```
Username: admin
Password: admin
```

Customer accounts are stored in `customers.txt`. The sample file includes:

```
Username: Shaheer
Password: K

Username: Joe
Password: Mama
```

This project uses local text files for demonstration purposes, so the sample
credentials are not intended for real-world use.
