package nyc.c4q.helenchan.makinghistory.models;

import java.util.List;

/**
 * Created by leighdouglas on 3/4/17.
 */

public class MapPoint {
    Coordinate coordinate;
    List<Content> contentList;

    public MapPoint() {

    }

    public MapPoint(Coordinate coordinate, List<Content> contentList) {
        this.coordinate = coordinate;
        this.contentList = contentList;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }
}
