package zaslontelecom.esk.backend.api.Controller.Requests;

public class PagedQuery {
    private int pageNumber;
    private int resultPerPage;
    private String query = "";

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getResultPerPage() {
        return resultPerPage;
    }

    public void setResultPerPage(int resultPerPage) {
        this.resultPerPage = resultPerPage;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
