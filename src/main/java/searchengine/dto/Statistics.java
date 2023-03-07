package searchengine.dto;

public class Statistics {
    Total total;
    Detailed[] detailed;

    public Statistics(Total total, Detailed[] detailed) {
        this.total = total;
        this.detailed = detailed;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public Detailed[] getDetailed() {
        return detailed;
    }

    public void setDetailed(Detailed[] detailed) {
        this.detailed = detailed;
    }
}
