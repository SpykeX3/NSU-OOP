package ru.nsu.fit.chernikov;

import com.google.gson.annotations.Expose;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Class to store notes. Contains title, text and date. */
class Record implements Comparable<Record> {
  @Expose(serialize = false, deserialize = true)
  private Date creationTime;

  @Expose(serialize = false, deserialize = true)
  private String title;

  @Expose(serialize = false, deserialize = true)
  private String body;

  Record(String _title, String _body, Date _time) {
    title = _title;
    body = _body;
    creationTime = _time;
  }

  /**
   * Compare records by date then by title and then by body (text).
   *
   * @param record to compare with
   * @return positive if is more then record, negative if less, zero otherwise
   */
  @Override
  public int compareTo(Record record) {
    int dateCmp = creationTime.compareTo(record.creationTime);
    if (dateCmp != 0) {
      return dateCmp;
    }
    int titleCmp = title.compareTo(record.title);
    if (titleCmp != 0) {
      return titleCmp;
    }
    return body.compareTo(record.body);
  }

  /**
   * Check if record was created before given time.
   *
   * @param time to compare with
   * @return true if was created before
   */
  boolean before(Date time) {
    return creationTime.compareTo(time) <= 0;
  }

  /**
   * Check if record was created after given time.
   *
   * @param time to compare with
   * @return true if was created after
   */
  boolean after(Date time) {
    return creationTime.compareTo(time) >= 0;
  }

  /**
   * Check if record contains substring in title.
   *
   * @param substr to look for
   * @return true if contains
   */
  boolean inTitle(String substr) {
    return title.contains(substr);
  }

  /** @return Record's title. */
  public String getTitle() {
    return title;
  }

  /** @return Record's body (text). */
  public String getBody() {
    return body;
  }

  /** @return Printable representation of record. */
  String getPrintable() {
    DateFormat dateFmt = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss aa");
    return "Title: "
        + title
        + "\tDate:"
        + dateFmt.format(creationTime)
        + "\nText: "
        + body
        + "\n\n";
  }
}
