public class Album {
  private String artist;
  private String title;
  private String year;

  // Constructors
  public Album() {
    // Default constructor
  }

  public Album(String artist, String title, String year) {
    this.artist = artist;
    this.title = title;
    this.year = year;
  }

  // Getter and Setter methods
  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  // toString method for easy debugging
  @Override
  public String toString() {
    return "Album{" +
        "artist='" + artist + '\'' +
        ", title='" + title + '\'' +
        ", year='" + year + '\'' +
        '}';
  }
}

