package library.draft;

public interface LibraryServerProtocol {
    int getBookId();
    int getLineNumber();
    void setLine(String line);
}
