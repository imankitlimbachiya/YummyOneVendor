package yummy.one.yummyonevendor.Quantity;

public class Quantity {

    public String Qty;
    public String Mrp;
    public String Price;

    public Quantity(String qty, String mrp, String price) {
        Qty = qty;
        Mrp = mrp;
        Price = price;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getMrp() {
        return Mrp;
    }

    public void setMrp(String mrp) {
        Mrp = mrp;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
