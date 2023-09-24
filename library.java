import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;
import java.time.LocalTime;
import java.time.Duration;

class Librarian{
    public int booksCount=0;
    public void regMember(String name, int age, String phno){
        if (this.findMember(phno)!=-1){
            System.out.println("Phone Number already exists.");
        }
        else{
            Member mem1=new Member(name, age, phno);
            Member.lstMembers.add(mem1);
            System.out.println("Member Successfully Registered with "+phno+"!");
        }
    }
    public void removeMember(String memId){
        if (Member.lstMembers.size()==0){
            System.out.println("No member registered yet.");
        }
        else{
            Iterator<Member> i = Member.lstMembers.iterator();
            while (i.hasNext()){
                Member memRemove=i.next();
                if (memRemove.phno_.equals(memId)){
                    i.remove();
                    System.out.println("Member successfully removed.");
                    break;
                }
                else if (!(i.hasNext())){
                    System.out.println("Invalid Member Id.");
                }
            }
        }
    }

    public void addBook(String title, String author, int copies){
        Book book1;
        for (int i=0; i<copies; i++){
            book1=new Book(title, author);
            book1.copies_=1;
            Book.lstBooks.add(book1);
            booksCount++;
            book1.bookId_=booksCount;
        }
        System.out.println("Book Added Successfully!");
    }
    public void removeBook(int bookId){
        if (Book.lstBooks.size()==0){
            System.out.println("No book added yet.");
        }
        else{
            Iterator<Book> i = Book.lstBooks.iterator();
            while (i.hasNext()){
                Book bookRemove=i.next();
                if (bookRemove.bookId_==bookId){
                    i.remove();
                    System.out.println("Book successfully removed.");
                    break;
                }
                else if (!(i.hasNext())){
                    System.out.println("Invalid Book Id.");
                }
            }
        }
    }
    
    public void viewMembers(){
        if (Member.lstMembers.size()==0){
            System.out.println("No member registered yet.");
        }
        else{
            Iterator<Member> i = Member.lstMembers.iterator();
            Member current;
            while (i.hasNext()){
                current=i.next();
                System.out.println("Member Id: "+current.phno_);
                System.out.println("Name: "+current.name_);
                System.out.println("Age: "+current.age_);
                System.out.println("Phone Number: "+current.phno_);
                System.out.println("Books issued: ");
                current.listMyBooks();
                System.out.println("Penalty Amount: Rs."+current.penalty_+"\n");
            }
        }
    }
    public void viewBooks(){
        if (Book.lstBooks.size()==0){
            System.out.println("No book added yet.");
        }
        else{
            Iterator<Book> i = Book.lstBooks.iterator();
            Book current;
            while (i.hasNext()){
                current=i.next();
                if(current.copies_>0){
                    System.out.println("Book Id: "+current.bookId_);
                    System.out.println("Title: "+current.title_);
                    System.out.println("Author: "+current.author_+"\n");
                }
            }
        }   
    }

    public int findMember(String memId){
        int index=-1;
        int count=-1;
        Iterator<Member> i = Member.lstMembers.iterator();
        Member current;
        while (i.hasNext()){
            count++;
            current=i.next();
            if (current.phno_.equals(memId)){
                index=count;
                break;
            }
        }
        return index;
    }
}

class Member{
    String name_;
    int age_;
    String phno_;
    String memId_;
    int penalty_;
    List<Book> memBooks;
    int numBooks_;
    LocalTime issue1, issue2;
    public static List<Member> lstMembers = new ArrayList<>();
    Member(String name, int age, String phno){
        this.name_=name;
        this.age_=age;
        this.phno_=phno;
        this.memId_=phno;
        this.penalty_=0;
        this.memBooks = new ArrayList<>();
        this.numBooks_=0;
    }

    public void listBooks(){
        if (Book.lstBooks.size()==0){
            System.out.println("No books available.");
        }
        else{
            Iterator<Book> i = Book.lstBooks.iterator();
            while (i.hasNext()){
                Book current=i.next();
                if (current.copies_!=0){
                    System.out.println("Book Id: "+current.bookId_);
                    System.out.println("Title: "+current.title_);
                    System.out.println("Author: "+current.author_+"\n");
                }
            }
        }
    }
    public void listMyBooks(){
        if (this.numBooks_==0){
            System.out.println("No book issued yet.");
        }
        else{
            Iterator<Book> i = this.memBooks.iterator();
            while (i.hasNext()){
                Book current=i.next();
                System.out.println("Book Id: "+current.bookId_);
                System.out.println("Title: "+current.title_);
                System.out.println("Author: "+current.author_+"\n");
            }
        }
    }
    
    public void issueBook(int bookId){
        if (this.numBooks_==1){
            int fine=this.calcFine();
            this.penalty_=fine;
        }

        if(this.penalty_!=0){
            System.out.println("Please clear penalty before issuing new book");
        }
        else if (this.numBooks_==2){
            System.out.println("You cannot hold more than 2 books at a time.");
        }
        else{
            Iterator<Book> i = Book.lstBooks.iterator();
            while (i.hasNext()){
                Book bookIssue=i.next();
                if (bookIssue.bookId_==bookId){
                    if(bookIssue.copies_==0){
                        System.out.println("This book is currently unavailable");
                        break;
                    }
                    else{
                        this.memBooks.add(bookIssue);
                        bookIssue.copies_--;
                        this.numBooks_++;
                        LocalTime now=LocalTime.now();
                        if (this.numBooks_==1){
                            issue1=now;
                        }
                        else if (this.numBooks_==2){
                            issue2=now;
                        }
                        System.out.println("Book Issued Successfully!");
                        break;
                    }
                }
                else if (!(i.hasNext())){
                    System.out.println("Invalid Book Id");
                }
            }
        }
    }
    public void returnBook(int bookId){
        LocalTime returnTime=LocalTime.now();
        LocalTime issueTime;
        if ((this.numBooks_==0) || (this.numBooks_==1 && this.memBooks.get(0).bookId_!=bookId) || (this.numBooks_==2 && this.memBooks.get(0).bookId_!=bookId && this.memBooks.get(1).bookId_!=bookId)){
            System.out.println("This book is not issued by you.");
        }
        else{
            if (this.memBooks.get(0).bookId_==bookId){
                issueTime=issue1;
                this.memBooks.get(0).copies_++;
                this.memBooks.remove(0);

            }
            else{
                issueTime=issue2;
                this.memBooks.get(1).copies_++;
                this.memBooks.remove(1);
            }
            numBooks_--;
            Duration dur=Duration.between(issueTime,returnTime);
            int durSec = (int)dur.getSeconds();
            int fine=0, delay=0;
            if (durSec>10){
                delay=durSec-10;
                fine=delay*3;
            }
            this.penalty_=fine;
            System.out.println("Book Id: "+bookId+" successfully returned. Rs "+this.penalty_+" has been charged for a delay of "+delay+" days.");
        }
    }

    public int calcFine(){
        LocalTime start = this.issue1;
        LocalTime end = LocalTime.now();
        Duration dur=Duration.between(start,end);
            int durSec = (int)dur.getSeconds();
            int fine=0, delay=0;
            if (durSec>10){
                delay=durSec-10;
                fine=delay*3;
            }
        return fine;
    }
    public void payFine(){
        int fine=this.penalty_;
        this.penalty_=0;
        System.out.println("You had a total fine of Rs "+fine+". It has been paid successfully!");
    }
}

class Book{
    String title_;
    String author_;
    int copies_;
    int bookId_;
    public static List<Book> lstBooks = new ArrayList<>();
    Book(String title, String author){
        this.title_=title;
        this.author_=author;
    }
}

public class library {
    public static void main(String[] args){

        System.out.println("Library Portal Initialized....\n"+"---------------------------------");
        int role, action;
        Scanner scan = new Scanner(System.in);
        Librarian lib = new Librarian();
        while (true){
            System.out.println("1. Enter as a librarian\n2. Enter as a member\n3. Exit");
            System.out.println("---------------------------------");
            role=scan.nextInt();
            scan.nextLine();
            
            if (role==1){
                //LIBRARIAN
                while (true){
                    System.out.println("---------------------------------");
                    System.out.println("1. Register a member\n2. Remove a member\n3. Add a book\n4. Remove a book");
                    System.out.println("5. View all members along with their books and fines to be paid");
                    System.out.println("6. View all books\n7. Back\n---------------------------------");

                    action=scan.nextInt();
                    scan.nextLine();
                    if (action==1){
                        System.out.print("Name: ");
                        String name=scan.nextLine();
                        System.out.print("Age: ");
                        int age=scan.nextInt();
                        scan.nextLine();
                        System.out.print("Phone Number: ");
                        String phno=scan.nextLine();
                        lib.regMember(name, age, phno);
                        continue;
                    }
                    else if (action==2){
                        System.out.print("Enter member ID: ");
                        String memId=scan.nextLine();
                        lib.removeMember(memId);
                        continue;
                    }
                    else if (action==3){
                        System.out.print("Title: ");
                        String title=scan.nextLine();
                        System.out.print("Author: ");
                        String author=scan.nextLine();
                        System.out.print("Copies: ");
                        int copies=scan.nextInt();
                        scan.nextLine();
                        lib.addBook(title, author, copies);
                        continue;
                    }
                    else if (action==4){
                        lib.viewBooks();
                        System.out.print("Enter book ID: ");
                        int bookId=scan.nextInt();
                        scan.nextLine();
                        lib.removeBook(bookId);
                        continue;
                    }
                    else if (action==5){
                        lib.viewMembers();
                        continue;
                    }
                    else if (action==6){
                        lib.viewBooks();
                        continue;
                    }
                    else if (action==7){
                        System.out.println("---------------------------------");
                        break;
                    }
                    else{
                        System.out.println("Invalid entry: Select an action (1-7) from the menu");
                        continue;
                    }
                }   
            }
            else if (role==2){
                //MEMBER
                System.out.print("Phone Number: ");
                String phno=scan.nextLine();

                int i=lib.findMember(phno);

                if (i==-1){
                    System.out.println("Member not registered.");
                    continue;
                }
                else{
                    System.out.println("Welcome "+Member.lstMembers.get(i).name_+". Member Id: "+Member.lstMembers.get(i).phno_);

                    while (true){
                        System.out.println("1. List Available Books\n2. List My Books\n3. Issue book\n4. Return book\n5. Pay Fine\n6. Back");
                        System.out.println("---------------------------------");

                        action=scan.nextInt();
                        scan.nextLine();

                        if (action==1){
                            Member.lstMembers.get(i).listBooks();
                            continue;
                        }
                        else if (action==2){
                            Member.lstMembers.get(i).listMyBooks();
                            continue;
                        }
                        else if (action==3){
                            Member.lstMembers.get(i).listBooks();
                            System.out.print("Enter Book Id: ");
                            int bookId = scan.nextInt();
                            scan.nextLine();
                            Member.lstMembers.get(i).issueBook(bookId);
                            continue;
                        }
                        else if (action==4){
                            System.out.print("Book Id: ");
                            int bookId = scan.nextInt();
                            scan.nextLine();
                            Member.lstMembers.get(i).returnBook(bookId);
                            continue;
                        }
                        else if (action==5){
                            Member.lstMembers.get(i).payFine();
                            continue;
                        }
                        else if (action==6){
                            System.out.println("---------------------------------");
                            break; 
                        }
                        else{
                            System.out.println("Invalid entry: Select an action (1-6) from the menu");
                            continue;
                        }
                    }
                }
            }
            else if(role==3){
                System.out.println("Thanks for visiting!");
                break;
            }
            else{
                System.out.println("Invalid entry: Select an action (1-3) from the menu");
                continue;
            }
        }
        scan.close();
    }
}
