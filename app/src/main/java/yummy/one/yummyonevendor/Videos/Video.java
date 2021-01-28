package yummy.one.yummyonevendor.Videos;

public class Video {
    public String Url,Name,StoreName,Userid,Length;

    public Video(){}

    public Video(String Url,String Name,String StoreName,String Userid,String Length){
        this.Name=Name;
        this.Url=Url;
        this.StoreName=StoreName;
        this.Userid=Userid;
        this.Length=Length;
    }
}
