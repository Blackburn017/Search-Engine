package searchengine.service.responses;

import searchengine.dto.SearchData;

public class SearchResponseService implements ResponseService {
    private boolean result;
    private int count;
    private SearchData[] data;

    public SearchResponseService() {
    }

    public SearchResponseService(boolean result) {
        this.result = result;
    }

    public SearchResponseService(boolean result, int count, SearchData[] data) {
        this.result = result;
        this.count = count;
        this.data = data;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public SearchData[] getData() {
        return data;
    }

    public void setData(SearchData[] data) {
        this.data = data;
    }
}
