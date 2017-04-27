package tweets.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Dalia Mostafa
 */
@Entity
@Table(name = "tweets2_filtered_classified")
public class Tweet {

    @Id
    @GeneratedValue
    @Column(name = "tweet_id")
    private Long id;

    @Column(name = "tweet_text", unique = false)
    private String content;

    @Column(name = "created_at")
    private Date createdAt;

    private String type;

    @Column(name = "screen_name")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
//            content.replace("RT","").trim();
//            content = content.toLowerCase();
//            return content = content.replaceAll("https?://\\S+\\s?", "").replaceAll("@[a-zA-Z0-9_]*", "").
//            replaceAll("#[a-zA-Z0-9_]*","").replace(",","").replace("\t", " ").replace("\"","").
//                    replace("\'", "").replace("\n", " ").replace("\r", " ").replace("\f", " ").replace("\b", " ").
//                    replace("via", "").replace(".","").replace("-", "").replace(">", "").replace("<","").replace("}","")
//                    .replace("(", "").replace(")", "").replaceAll("\\d+","").replace(":", "").replace("http","").replace("&","")
//                    .replace(".","").replace("\\","").replace("/","").replace("_","").replace("'","").replace(",","").replace("!","")
//                    .replace("?","").replace("*","").replace("$","");
        return content;
    }

    public String getRawContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Tweet(String content, Date createdAt, String type, String username) {
        this.content = content;
        this.createdAt = createdAt;
        this.type = type;
        this.username = username;
    }

    public Tweet() {
    }
}