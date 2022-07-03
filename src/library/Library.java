package library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Library {
    private HashMap<Integer, List<String>> books;
    public Library(){
        this.books=new HashMap<>();
        this.books.put(1,new ArrayList<>());
        this.books.get(1).add("A");
        this.books.get(1).add("B");
        this.books.get(1).add("C");
        this.books.get(1).add("D");
        this.books.put(2,new ArrayList<>());
        this.books.get(2).add("1");
        this.books.get(2).add("2");
        this.books.get(2).add("3");
        this.books.get(2).add("4");
    }
    public boolean bookExists(int bookId){
        return this.books.containsKey(bookId);
    }
    public boolean lineExists(int bookId,int lineNumber){
        return this.books.get(bookId).size()>lineNumber;
    }
    public String getLine(int bookId,int lineNumber){
        if(!this.bookExists(bookId)) throw new IllegalArgumentException();
        if(!this.lineExists(bookId,lineNumber)) throw new IllegalArgumentException();
        return this.books.get(bookId).get(lineNumber);
    }
}
