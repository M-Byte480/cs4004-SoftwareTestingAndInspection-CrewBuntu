import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class LibraryVersion1 {
    private ArrayList<String> books;
    private ArrayList<Book> bookArrayList ;
    private ArrayList<Subscription> subscriptions;
    private ArrayList<Journal> journalSubs;
    private ArrayList<Student> borrowers;
    private ArrayList<Reminder> reminders;
    private ArrayList<Loan> loans;
    private static ArrayList<EBookRequest> requestsForEBooks;

    private ArrayList<Shelf> shelves;

    private HashMap<String, ArrayList<Student>> borrowInstances = new HashMap<String, ArrayList<Student>>();


    private static final ArrayList<String> bibliography;
    static {
        try {
            bibliography = populate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public LibraryVersion1() {
        books = new ArrayList<String>();
        subscriptions = new ArrayList<Subscription>();
        journalSubs = new ArrayList<Journal>();
        borrowers = new ArrayList<Student>();
        shelves = new ArrayList<Shelf>();
    }

    public void getNewBook(String book) {
        books.add(book);
    }

    public void populateBook(String [] books) {
        for (String book : books) {
            this.books.add(book);
        }
    }

    public ArrayList<String> searchBooks(String bookName) {
        ArrayList<String> books = new ArrayList<>();
        for (String book : this.books) {
            if (book.indexOf(bookName.toLowerCase()) != -1) {
                books.add(book);
            }
        }
        return books;
    }


    public ArrayList<Journal> getJournalSubs() {
        return journalSubs;
    }

    public boolean inOtherLibs(String book, LibraryVersion1 l) {
        if (l.getBooks().contains(book)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean inOtherLibsJournal(Journal journal, LibraryVersion1 l) {
        if (l.getJournalSubs().contains(journal)) {
            return true;
        } else {
            return false;
        }
    }


    public ArrayList<String> getBooks() {
        return books;
    }

    public void borrow(String book, String studentName, int daysUntilDue) {
        Student borrower = new Student(book, studentName, daysUntilDue);
        borrowers.add(borrower);
    }

    public String getBorrowers(String book) {
        String returnMe = "";
        for (int i = 0; i < borrowers.size(); i++) {
            if (borrowers.get(i).getBorrowedBook().equals(book)) {
                returnMe += borrowers.get(i).toString();
            }
        }
        return returnMe;
    }

    public boolean linearSearch(String name) {
        for (String type :
                bibliography) {
            if (name.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean binarySearch(String name)  {

        try {
            return -1 < Collections.binarySearch(bibliography, name);
        }catch (Exception ignore){

        }
        return false;
    }

    private static ArrayList<String> populate() throws FileNotFoundException {
        File file = new File("src/main/java/wordlist.180000");
        Scanner writeBibliography = new Scanner(file);
        ArrayList<String> words = new ArrayList<>();
        while (writeBibliography.hasNextLine()) {
            words.add(writeBibliography.nextLine());
        }
        return words;
    }



    public void addLoan(Loan loan) {
        loans.add(new Loan(loan.getLoanee(), loan.getBook(), loan.getStartDate()));
    }

    public static ArrayList<EBookRequest> getRequestsForEBooks() {
        return requestsForEBooks;
    }

    public void isBookDue(Loan loan) {
        if ((loan.getDueDate().compareTo(LocalDate.now()) > 0)) {
            reminders.add(new Reminder(loan.getLoanee(), loan.getBook()));
        }
    }

    public static void sendReminder(Student loanee) {
        // Need to send a Reminder object that will periodically be sent to the student's email

    }

    public static boolean faster(Duration a, Duration b) {
        return a.toMillis() <= b.toMillis();
    }


    public void subscribe(Journal journal) {
        journalSubs.add(journal);
    }


    //----------------------SUBSCRIPTIONS-----------------------------------------


    public void addASubscription(Subscription s) {
        if (subscriptions.contains(s)) {
        } else {
            subscriptions.add(s);
        }

    }

    public ArrayList<Subscription> getSubs() {
        return subscriptions ;
    }
    public boolean inOtherLibsSubscription(Subscription subscription, LibraryVersion1 l) {
        boolean check = false;
        if (l.getSubs().contains(subscription)){
            subscriptions.remove(subscription);
            check = true;
    }
        return check;
}
    //-----------------------------------------------------Book----------------------------------------
    public void lendABookOfInterest(Book s) {
        if (bookArrayList.contains(s)) {
        } else {
            bookArrayList.add(s);
        }

    }

    public ArrayList<String> getBook() {
        return books ;
    }
    public boolean inOtherLibsShelf(Book  s, LibraryVersion1 l) {
        boolean check = false;
        if (l.getSubs().contains(s)){
            subscriptions.remove(s);
            check = true;
        }
        return check;
    }

    //-----------------------------------------------------SHELVES----------------------------------------


    public void newShelf(Shelf s) {
        shelves.add(s);
    }


    public void addNewBooksToSystem(Book b, Shelf s) {
        if (shelves.contains(s)) {
            s.addBookToShelf(b);
            b.setShelfWhereStored(s);
        }
    }

    public boolean checkShelfForBook(Book book) {
        boolean bookAvailable = false;
        if (book.getShelfWhereStored() != null && book.getShelfWhereStored().getBooks().contains(book)) {
            bookAvailable = true;
            return bookAvailable;
        }

        return checkBookInLibrary(book);
    }

    public boolean checkBookInLibrary(Book book) {
        boolean bookAvailable = false;
        for (Shelf s : shelves) {
            for (Book b : s.getBooks()) {
                if (b.equals(book)) {
                    bookAvailable = true;
                }
            }
        }
        return bookAvailable;

    }

    public boolean addBorrower(String bookName, Student student) {
        borrowInstances.putIfAbsent(bookName, new ArrayList<Student>());
        String name = student.getName();
        ArrayList<String> borrowers = getBorrowerHistory(bookName);
        int counter = 0;
        for (String borrower : borrowers) {
            if (borrower.equals(name)) {
                counter++;
            }
        }
        if (counter > 1) {
            return false;
        }
        borrowInstances.get(bookName).add(student);
        return true;
    }

    public ArrayList<String> getBorrowerHistory(String bookName) {
        ArrayList<String> borrowers = new ArrayList<String>();
        ArrayList<Student> students = borrowInstances.get(bookName);
        for (Student student : students) {
            borrowers.add(student.getName());
        }
        return borrowers;
    }

    public ArrayList<Journal> getJournals() {
        return journalSubs;
    }

    public ArrayList<Journal> geteJournals() {
        ArrayList<Journal> toReturn = new ArrayList<>();
        for (Journal j :
                journalSubs) {
            if (j.iseJournal()) {
                toReturn.add(j);
            }
        }
        return toReturn;
    }
}
