package yummy.one.yummyonevendor.Models.Orders;

public class OrdersData {

    public String OrderNo;
    public String ItemsDetails;
    public String PushId;
    public String OrderDateTime;
    public String OrderDate;
    public String DeliveryDate;
    public String SubTotal;
    public String VendorCommision;
    public String Status;
    public String Vendor;
    public String Total;
    public String CustomerName;
    public String Qty;

    public  OrdersData(){}

    public OrdersData(String OrderNo, String ItemDetails, String PushId, String OrderDateTime, String SubTotal, String Status, String VendorCommision, String Vendor, String OrderDate, String DeliveryDate, String Total,String CustomerName,String Qty){
        this.OrderDateTime=OrderDateTime;
        this.ItemsDetails=ItemDetails;
        this.PushId=PushId;
        this.OrderNo=OrderNo;
        this.SubTotal=SubTotal;
        this.VendorCommision=VendorCommision;
        this.Status=Status;
        this.Vendor=Vendor;
        this.OrderDate=OrderDate;
        this.DeliveryDate=DeliveryDate;
        this.Total=Total;
        this.CustomerName=CustomerName;
        this.Qty=Qty;
    }
}
