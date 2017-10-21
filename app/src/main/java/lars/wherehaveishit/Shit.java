package lars.wherehaveishit;

import android.widget.EditText;
import android.widget.RatingBar;

public class Shit
{

    long _ID;
    String ShitName;
    String ShitDate;
    String ShitLongitude;
    String ShitLatitude;
    double ShitRatingCleanness;
    double ShitRatingPrivacy;
    double ShitRatingOverall;
    String ShitNote;

    public Shit( String shitName, String shitDate, String currentLocationLonFin, String currentLocationLatFin, double shitRatingCleanness, double shitRatingPrivary, double shitRatingOverall, String shitNote )
    {

        this.ShitName = shitName;
        this.ShitDate = shitDate;
        this.ShitLongitude = currentLocationLonFin;
        this.ShitLatitude = currentLocationLatFin;
        this.ShitRatingCleanness = shitRatingCleanness;
        this.ShitRatingPrivacy = shitRatingPrivary;
        this.ShitRatingOverall = shitRatingOverall;
        this.ShitNote = shitNote;
    }

    public Shit( )
    {


    }

    public long get_ID( )
    {

        return _ID;
    }

    public void set_ID( long _ID )
    {

        this._ID = _ID;
    }

    public String getShitName( )
    {

        return ShitName;
    }

    public void setShitName( String ShitName )
    {

        this.ShitName = ShitName;
    }

    public String getShitDate( )
    {

        return ShitDate;
    }

    public void setShitDate( String ShitDate )
    {

        this.ShitDate = ShitDate;
    }

    public String getShitLongitude( )
    {

        return ShitLongitude;
    }

    public void setShitLongitude( String ShitLongitude )
    {

        this.ShitLongitude = ShitLongitude;
    }

    public String getShitLatitude( )
    {

        return ShitLatitude;
    }

    public void setShitLatitude( String ShitLatitude )
    {

        this.ShitLatitude = ShitLatitude;
    }

    public double getShitRatingCleanness( )
    {

        return ShitRatingCleanness;
    }

    public void setShitRatingCleanness( double ShitRatingCleanness )
    {

        this.ShitRatingCleanness = ShitRatingCleanness;
    }

    public double getShitRatingPrivacy( )
    {

        return ShitRatingPrivacy;
    }

    public void setShitRatingPrivacy( double ShitRatingPrivacy )
    {

        this.ShitRatingPrivacy = ShitRatingPrivacy;
    }


    public double getShitRatingOverall( )
    {

        return getShitRatingOverall();
    }

    public void setShitRatingOverall( double ShitRatingOverall )
    {

        this.ShitRatingOverall = ShitRatingOverall;
    }

    public String getShitNote( )
    {

        return getShitNote();
    }

    public void setShitNote( String ShitNote )
    {

        this.ShitNote = ShitNote;
    }


}
