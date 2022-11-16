import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class University {
    private ArrayList<Library> libraries;
    private ArrayList<University> partners;

    public University() {
        libraries = new ArrayList<Library>();
        partners = new ArrayList<University>();
    }

    //adding a new library to partners
    public void newLibrary(Library l) {
        libraries.add(l);
    }

    public void newPartner(University u) {
        partners.add(u);
    }

    public ArrayList<University> getPartners() {
        return partners;
    }

    public ArrayList<Library> getLibraries() {
        return libraries;
    }

    public ArrayList<String> findBook(String bookName) throws Exception {
        ArrayList<String> books = new ArrayList<String>();
        ArrayList<Library> tempLibraries = new ArrayList<Library>();
        ArrayList<University> tempPartners = getPartners();
        tempLibraries.addAll(libraries);
        for (University u : tempPartners) {
            tempLibraries.addAll(u.getLibraries());
        }
        for (Library l : tempLibraries) {
            books.addAll(l.searchBooks(bookName));
        }
        if (books.size() == 0) {
            throw new Exception("There are no matching search results.");
        }
        // Change books to a set and back to remove duplicate entries.
        Set<String> set = new HashSet<String>(books);
        ArrayList<String> list = new ArrayList<String>(set);
        return list;
    }


    //go through every library
    //if library contains book don't add it
    //if it doesn't add it to specified library
    public boolean getBookForLib(Library l, String book) {
        boolean addIt = true;
        for (int i = 0; i < libraries.size(); i++) {
            if (l.inOtherLibs(book, libraries.get(i))) {
                addIt = false;
                break;
            }
        }
        if (addIt == true) {
            l.getNewBook(book);
        }
        return addIt;
    }

    //go through every library
    //if library contains subscription don't add it
    //if it doesn't add it to specified library
    public boolean subscribe(Library l, Journal journal) {
        boolean addIt = true;
        for (int i = 0; i < libraries.size(); i++) {
            if (l.inOtherLibsJournal(journal, libraries.get(i))) {
                addIt = false;
                break;
            }
        }
        if (addIt) {
            l.subscribe(journal);
        }
        return addIt;
    }

    // test run
    //adding all university libraries
    //add all librarys to university
    //add university to partners
    //add this university to partners
    public void joinUni(University u) {
        libraries.addAll(u.getLibraries());
        u.getLibraries().addAll(libraries);
        partners.add(u);
        u.getPartners().add(this);
    }

    //----------------------SUBSCRIPTIONS-----------------------------------------
    public boolean getSubscriptionForLib(Library l, Subscription s) {
        boolean bookAvailable = false;
        for (int i = 0; i < libraries.size(); i++) {
            if (l.inOtherLibsSubscription(s, libraries.get(i)) == true) {
                l.addASubscription(s);
                bookAvailable = true;
            } else {
                bookAvailable = false;
            }
        }

        return bookAvailable;
    }

    //-----------------------------------------------------Book----------------------------------------
    public boolean getBookForLib(Library l, Book s) {
        boolean bookAvailable = false;
        for (int i = 0; i < libraries.size(); i++) {
            if (l.inOtherLibsShelf(s, libraries.get(i)) == true) {
                l.lendABookOfInterest(s);
                bookAvailable = true;
            } else {
                bookAvailable = false;
            }
        }

        return bookAvailable;
    }
    //-----------------------------------------------------SHELVES----------------------------------------
    public boolean checkUniversityForBook(Book book) {
        boolean bookAvailable = true;
        for (Library l : libraries) {
            if (l.checkShelfForBook(book)) {
                bookAvailable = true;
            } else {
                bookAvailable = false;
            }

        }
        return bookAvailable;

    }
}