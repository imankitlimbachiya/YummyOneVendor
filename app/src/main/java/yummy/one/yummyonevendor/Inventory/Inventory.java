package yummy.one.yummyonevendor.Inventory;

public class Inventory {
    public String ItemName;
    public String PushId;
    public String Status;
    public String ApprovalStatus;
    public String FoodType;
    public String SellingPrice;
    public String Category;

    public Inventory(){}

    public Inventory(String ItemName, String PushId, String Status, String FoodType, String ApprovalStatus, String SellingPrice,String Category){
        this.ItemName=ItemName;
        this.PushId=PushId;
        this.Status=Status;
        this.FoodType=FoodType;
        this.ApprovalStatus=ApprovalStatus;
        this.SellingPrice=SellingPrice;
        this.Category=Category;
    }
}
