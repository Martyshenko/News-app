package savern.newsapp;

public class News {
    private String mDate;
    private String mTitle;
    private String mDescription;
    private String mUrl;


    public News(String date, String title, String description, String url) {
        mDate = date;
        mTitle = title;
        mDescription = description;
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getDescription() {
        return mDescription;
    }

}
