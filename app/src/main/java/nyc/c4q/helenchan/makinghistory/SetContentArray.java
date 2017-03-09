package nyc.c4q.helenchan.makinghistory;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.models.Content;

/**
 * Created by shannonalexander-navarro on 3/8/17.
 */

public class SetContentArray {

    List<Content> listOfContent = new ArrayList<>();

    private void addContent() {
        listOfContent.add(new Content(
                "name null",
                "type null",
                "2866 West 6th Street, west side, between Neptune Ave., and Sheepshead Bay Road, showing The Harvest, formerly the Coney Island House, later the Oceanic Hotel. It formerly stood, prior to 1921, at Ne. 622 Neptune Ave.\nDecember 1923.\nEugene L. Armbruster Collection.\nThe same, also showing overhead the Culver (B.M.T) Line.\nDecember 1923.\nEugene L. Armbruster Collection.\nThe same, another view. Once a railroad ran from this Hotel to the beach and the Cars left every few minutes to accomodate the guests.\nDecember 1923.\nEugene L. Armbruster Collection.\n",
                "folder null",
                "http://oldnyc-assets.nypl.org/600px/702449f-a.jpg",
                "1923"));
        listOfContent.add(new Content(
                "name null",
                "type null",
                "43 Third Avenue, adjoining the S.E. corner of 10th Street.\nMay 18, 1934.\nP. L. Sperr.\n",
                "folder null",
                "http://oldnyc-assets.nypl.org/600px/707997f-a.jpg",
                "1934"
        ));

        listOfContent.add(new Content(
                "name null",
                "type null",
                "East 36th Street, north side, between First and Second avenues, showing workmen clearing the former\nsite of St. Gabriel's Church and school.\nNote the corner stone of same in the foreground. To the left is the Second Avenue \"El\" and to the right, 37th Street tenements. View No. 3 shows the last mentioned\nfeature only.\nJune 28, 1939\nViews: 1-2-3\nP. L. SpeIr\n",
                "folder null",
                "http://oldnyc-assets.nypl.org/600px/712287f-b.jpg",
                "1939"
        ));
        listOfContent.add(new Content(
                "name null",
                "type null",
                "Ninth Avenue, north from W. 50th Street, as photographed from the downtown \"El\" platform. Shown are details of track and station at this point prior to demolition. Note the raised express portion. This was inclined to permit the shunting of 6th Avenue trains eastward on W. 53rd Street.\nJune 6, 1940\nViews 1,2,3\nP. L. Sperr\n",
                "folder null",
                "http://www.oldnyc.org/rotated-assets/600px/710415f-c.90.jpg",
                "1940"
        ));
        listOfContent.add(new Content(
                "name null",
                "type null",
                "152 Broadway, adjoining the N. E. corner of Liberty Street, showing Frank Hegger's Photographic Depot, the best known and most popular establishment of its kind in its day. The Williamsburg City Building is the corner structure. On the left is a portion of No. 154-8 Broadway. The three of these structures were replaced by the present 21-story Westinghouse Building.\n1883\nGift of New York Historical Society\nThru Mr. Vigilanti\n",
                "folder null",
                "http://oldnyc-assets.nypl.org/600px/717072f.jpg",
                "1883"
        ));

    }
}
