package db.dao;

public class ProjectPrice {
    private String clientName;
    private int price;

    public int getPrice() {
        return price;
    }

    public void setProjectPrice(int price) {
        this.price = price;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
