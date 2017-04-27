package tweets.model;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 @author Dalia Mostafa
 */
@Entity
@Table(name = "training_tweets3")
public class TrainingTweet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(name = "text")
    private String content; //text of tweet

    @Column(name = "class")
    private String type; //type of tweet

    @Column(name = "created_at")
    private Date createdAt; //date tweet was written in

    public TrainingTweet(String content, String type, Date createdAt) {
        this.content = content;
        this.type = type;
        this.createdAt = createdAt;
    }

    public TrainingTweet() {
    }

    public String getId() {
        return id + "_";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
