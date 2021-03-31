package yummy.one.yummyonevendor.Models;

import com.google.firebase.firestore.GeoPoint;

public class Users {

    public String AccountNumber,Address,BranchAddress,BranchName,Category,City,CityPushId,CloseTime,Commission,Cuisines,DiscountType,Email;
    public String FSSAIAddress,FSSAIExpiryDate,FSSAIImage,FSSAINumber,GSTImage,GSTNumber,IFSCCode,ItemCategory,LICImage,MobileNumber,Name,OfferAmount,POC;
    GeoPoint Location;
    public String PackingCharges,PancardImage,PassbookImage,Password,PreperationTime,Status,UserId,UserName,VendorImage,Zone,ZonePushId,RestaurantName;

    public Users(String AccountNumber, String Address, String BranchAddress, String BranchName, String Category, String City, String CityPushId, String CloseTime, String Commission, String Cuisines, String DiscountType, String Email, String FSSAIAddress, String FSSAIExpiryDate, String FSSAIImage, String FSSAINumber, String GSTImage, String GSTNumber, String IFSCCode, String ItemCategory, String LICImage, String MobileNumber, String Name, String OfferAmount, String POC, GeoPoint Location, String PackingCharges, String PancardImage, String PassbookImage, String Password, String PreperationTime, String Status, String UserId, String UserName, String VendorImage, String Zone, String ZonePushId,String RestaurantName) {
        this.AccountNumber = AccountNumber;
        this.Address = Address;
        this.BranchAddress = BranchAddress;
        this.BranchName = BranchName;
        this.Category = Category;
        this.City = City;
        this.CityPushId = CityPushId;
        this.CloseTime = CloseTime;
        this.Commission = Commission;
        this.Cuisines = Cuisines;
        this.DiscountType = DiscountType;
        this.Email = Email;
        this.FSSAIAddress = FSSAIAddress;
        this.FSSAIExpiryDate = FSSAIExpiryDate;
        this.FSSAIImage = FSSAIImage;
        this.FSSAINumber = FSSAINumber;
        this.GSTImage = GSTImage;
        this.GSTNumber = GSTNumber;
        this.IFSCCode = IFSCCode;
        this.ItemCategory = ItemCategory;
        this.LICImage = LICImage;
        this.MobileNumber = MobileNumber;
        this.Name = Name;
        this.OfferAmount = OfferAmount;
        this.POC = POC;
        this.Location = Location;
        this.PackingCharges = PackingCharges;
        this.PancardImage = PancardImage;
        this.PassbookImage = PassbookImage;
        this.Password = Password;
        this.PreperationTime = PreperationTime;
        this.Status = Status;
        this.UserId = UserId;
        this.UserName = UserName;
        this.VendorImage = VendorImage;
        this.Zone = Zone;
        this.ZonePushId = ZonePushId;
        this.RestaurantName = RestaurantName;
    }

    public Users(){}

}