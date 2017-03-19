package nyc.c4q.helenchan.makinghistory;

/**
 * Created by leighdouglas on 3/13/17.
 */

public class Constants {
    static final int REQUEST_CODE_LOCATION = 140;
    static final int REQUEST_CODE_CAMERAANDLOCATION = 312;
    static final int REQUEST_CODE_PROFILEPIC = 232;
    static final int REQUEST_IMAGE_CAPTURE = 94;
    static final int REQUEST_PICK_IMAGE = 999;

    //    private void addContentToDatabase() {

    //first line adds a coordinate, second location adds content to list at that location
//        mFirebaseDatabase.child("MapPoint").child("Location3").setValue(new Coordinate(40.720398, -74.025452));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));


//        these two lines of code are super important. this is how you push new contents into the list of data at a point
//        mFirebaseDatabase.child("MapPoint").child("Location2").child("Content").push().setValue(new Content("Washington Sq", "Historical", "This was the washsq a long time ago", "wash sq", "http://oldnyc-assets.nypl.org/600px/707997f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location2").child("Content").push().setValue(new Content("Washington Sq", "Historical", "This was the washsq a long time ago", "wash sq22", "http://oldnyc-assets.nypl.org/600px/707997f-a.jpg", "1920"));

//    }

//    private void addImagetomyapartment() {
//        mFirebaseDatabase.child("MapPoint")
//                .child("Location101")
//                .child("ContentList")
//                .push()
//                .setValue(new Content("Brooklyn: Broadway between Cornelia and Jefferson Street", "Historical", "Building behind the above ground train", "Broadway between Cornelia and Jefferson Street", "https://3rdeyesolation.files.wordpress.com/2012/02/tumblr_lp3lwpq4ay1qe3h33o1_500.jpg?w=500", "1990"));
//
//    }

//    private void saveToFirebase(Location lastLocation) {
//        Coordinate currLocation = new Coordinate(lastLocation.getLatitude(), lastLocation.getLongitude());
//        mFirebaseDatabase.child("locations").push().setValue(currLocation);
//    }
}
