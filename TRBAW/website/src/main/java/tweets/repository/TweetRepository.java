package tweets.repository;

import tweets.model.Tweet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/** This is the respository interface responsible for Tweets.
 * It contains an extra method <i>FindByType()</i>, which returns a list of all the tweets of a certain type.
 * @author Dalia Mostafa
 */
public interface TweetRepository extends CrudRepository<Tweet, Long> {
    List<Tweet> findByType(String type);
}