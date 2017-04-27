package tweets.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tweets.model.TrainingTweet;

import java.util.Date;
import java.util.List;

/**
 * @author Dalia Mostafa
 */

public interface TrainingTweetRepository extends CrudRepository<TrainingTweet, String> {
}
