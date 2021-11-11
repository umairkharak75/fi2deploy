package zaslontelecom.esk.backend.api.Controller.Response;

public class PagedQueryResult<T> {

    private Iterable<T> result;
    private long resultLength;

    public Iterable<T> getResult() {
        return result;
    }

    public void setResult(Iterable<T> res) {
        this.result = res;
    }

    public long getResultLength() {
        return resultLength;
    }

    public void setResultLength(long len) {
        this.resultLength = len;
    }
}
