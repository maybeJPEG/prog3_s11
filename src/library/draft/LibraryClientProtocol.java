package library.draft;

public interface LibraryClientProtocol {
    void init(int bookId,int lineNumber);
    String nextLine();
}
