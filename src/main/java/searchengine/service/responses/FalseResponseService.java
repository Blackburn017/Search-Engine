package searchengine.service.responses;

public class FalseResponseService implements ResponseService {
    private final String error;

    public FalseResponseService(String error) {
        this.error = error;
    }

    @Override
    public boolean getResult() {
        return false;
    }

    public String getError() {
        return error;
    }
}
