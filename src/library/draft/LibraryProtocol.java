package library.draft;

public class LibraryProtocol implements LibraryClientProtocol,LibraryServerProtocol {
    @Override
    public void init(int bookId, int lineNumber) {

    }

    @Override
    public String nextLine() {
        return null;
    }

    @Override
    public int getBookId() {
        return 0;
    }

    @Override
    public int getLineNumber() {
        return 0;
    }

    @Override
    public void setLine(String line) {

    }
}
