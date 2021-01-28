package yummy.one.yummyonevendor.Models.OrderDetails;

public class OrderDetails {

    public String Name;
    public String Price;
    public String Type;
    public String Qty;
    public String Image;
    public String Details;

    public OrderDetails(){}

    public OrderDetails(String Name,String Price,String Type,String Qty,String Image,String Details){
        this.Name=Name;
        this.Price=Price;
        this.Type=Type;
        this.Image=Image;
        this.Qty=Qty;
        this.Details=Details;
    }
}