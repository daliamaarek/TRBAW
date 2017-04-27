package tweets.classification;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tweets.controller.TweetController;
import tweets.model.TrainingTweet;
import tweets.model.Tweet;
import tweets.repository.TweetRepository;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stopwords.Rainbow;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.Randomize;

import java.util.*;

/**
 classification is the class responsible for Tweet Classification.
 <p>
 Tweets are classified into risks, benefits, and neutral tweets (R, B, N) and then saved into the MySQL database. A J48 classifier is used for classification. Filters are applied to the data so that the data is suitable for classification. Training data is used to train the classifier before classifying all of the tweets.
 </p>
 @author     Dalia Mostafa
 */
@Controller
public class classification {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    TweetController tweetController;

    static int class1 = 0, class2 = 0, class3 = 0, max, numOfDataSets;
    static ArrayList<Tweet> classifiedTweets;

    /**
     *        This is the method which classifies tweets into the three categories (risk, benefit, and neutral).
     *        <br>
     *        First, it gets all the tweets from the database, and sends them to getTweetsandPreprocess() so that the tweets and training tweets are suitable for building the classifier and classification.
     *        The training tweets are saved to an ARFF file, and an WEKA instance is obtained from this file. Suitable filters are applied to the training tweet instance using the applyFiltersToTrainingSet method and the smote method. A J48 classifier is created and built using the training tweets' instance.
     * @throws Exception
     */
    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/class")
    @ResponseBody
    public void classify() throws Exception {
//        System.out.println("inside!");

        classifiedTweets = tweetController.getTweets();

        getTweetsAndPreprocess();

        classifiedTweets = tweetController.getTweets();

        tweetRepository.save(classifiedTweets.get(0));

        tweetController.getTrainingTweets();

        ConverterUtils.DataSource sourceTT = new ConverterUtils.DataSource("trainingtweets.arff");
        Instances data = sourceTT.getDataSet();
        data.setClassIndex(1);

        data = applyFiltersToTrainingSet(data, data);
        data = smote(data);

        J48 j48 = new J48();
        j48.setUnpruned(false);
        j48.setConfidenceFactor(0.25f);
        j48.setMinNumObj(2);
        j48.buildClassifier(data);

        Tweet tweet;
        ArrayList<Tweet> tweetsToSave;
        String type;
        ConverterUtils.DataSource tweetSource;
        Instances labeled;
        int start = 0;

//        System.out.println("classifying + saving..");

        classifiedTweets = Lists.newArrayList(classifiedTweets.subList(start, classifiedTweets.size()));

        for (int j = 0; j < numOfDataSets; j++) {

            tweetSource = new ConverterUtils.DataSource("T" + j + ".arff");
            labeled = tweetSource.getDataSet();
            labeled.setClassIndex(labeled.numAttributes() - 1);
            labeled = applyFiltersToTrainingSet(labeled, data);
            tweetsToSave = new ArrayList<Tweet>();

            while(!labeled.isEmpty()){

                tweet = classifiedTweets.remove(0);
                double clsLabel = j48.classifyInstance(labeled.instance(0));
                labeled.instance(0).setClassValue(clsLabel);
                type = labeled.instance(0).stringValue(1);
                tweet.setType(type);
                tweetsToSave.add(tweet);
                labeled.remove(0);
            }

//            System.out.println(j);

            tweetRepository.save(tweetsToSave);

//            System.out.println("okay!");

        }

    }

    /**
     *This method is responsible for preprocessing the tweets and training tweets so that they could be used efficiently.
     * <br>
     * All punctuation is removed from the data, as well as mentions and surrounding words upon retweeting. (RT @ ...). The number of each class in training tweets is calculated so that the SMOTE algorithm could be later applied to avoid class imbalance issues.
     * <br>
     *     The Tweets are written to CSV and ARFF files to be later used by the WEKA classifier. A subset of 1000 tweets is written into each file to ensure that WEKA can handle the sizes of the files.
     * @throws Exception
     */
    public void getTweetsAndPreprocess() throws Exception {
        ArrayList<Tweet> tweetsTemp = classifiedTweets;
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        List<TrainingTweet> trainingTweets = tweetController.getTrainingTweets();

        String content;
        Tweet tweet;
        while(!tweetsTemp.isEmpty()){
            tweet = new Tweet();
            tweet.setType(tweetsTemp.get(0).getType());
            tweet.setContent(tweetsTemp.get(0).getContent());
            tweet.setId(tweetsTemp.get(0).getId());
            tweet.setCreatedAt(tweetsTemp.get(0).getCreatedAt());
            tweet.setUsername(tweetsTemp.get(0).getUsername());
            content = tweet.getContent();
            content = content.toLowerCase();
            content.replace("RT", "").trim();
            content = content
                    .replaceAll("https?://\\S+\\s?", "")
                    .replaceAll("@[a-zA-Z0-9_]*", "")
                    .replaceAll("#[a-zA-Z0-9_]*", "")
                    .replace(",", "").replace("\t", " ").replace("\"", "")
                    .replace("\'", "").replace("\n", " ").replace("\r", " ").replace("\f", " ").replace("\b", " ").
                            replace("via", "").replace(".", "").replace("-", "").replace(">", "").replace("<", "").replace("}", "")
                    .replace("(", "").replace(")", "").replaceAll("\\d+", "").replace(":", "").replace("http", "").replace("&", "")
                    .replace(".", "").replace("\\", "").replace("/", "").replace("_", "").replace("'", "").replace(",", "").replace("!", "")
                    .replace("?", "").replace("*", "").replace("$", "");
            tweet.setContent(content);
            tweets.add(tweet);
            tweetsTemp.remove(0);
        }
        for (TrainingTweet trainingTweet : trainingTweets) {
            content = trainingTweet.getContent();
            content.replace("RT", "").trim();
            content = content.toLowerCase();
            content = content.replaceAll("https?://\\S+\\s?", "").replaceAll("@[a-zA-Z0-9_]*", "").
                    replaceAll("#[a-zA-Z0-9_]*", "").replace(",", "").replace("\t", " ").replace("\"", "").
                    replace("\'", "").replace("\n", " ").replace("\r", " ").replace("\f", " ").replace("\b", " ").
                    replace("via", "").replace(".", "").replace("-", "").replace(">", "").replace("<", "").replace("}", "")
                    .replace("(", "").replace(")", "").replaceAll("\\d+", "").replace(":", "").replace("http", "").replace("&", "")
                    .replace(".", "").replace("\\", "").replace("/", "").replace("_", "").replace("'", "").replace(",", "").replace("!", "")
                    .replace("?", "").replace("*", "").replace("$", "");
            trainingTweet.setContent(content);

        }
        class1 = 0;
        class2 = 0;
        class3 = 0;
        for (TrainingTweet trainingTweet : trainingTweets) {
            if (trainingTweet.getType().equals("R"))
                class1++;
            else if (trainingTweet.getType().equals("B"))
                class2++;
            else class3++;
        }
        int i = 0;
        numOfDataSets = 0;
        String s;
        while (i < tweets.size()) {
            s = "T" + numOfDataSets;
            tweetController.writeCSVFileTweets(s, tweets.subList(i, Math.min(i + 10000, tweets.size())));
            i += 10000;
            numOfDataSets++;
        }
//        System.out.println("done preprocessing");
    }

    /**
     * This method applies specific filters to Instances.
     * @param filter The filter that is to be applied
     * @param data The instance that will be processed
     * @param training This attribute is only significant in  case of the StringToWordVector method when processing the tweets so that the word vectors of the tweets could be matched to the training tweets.
     * @return
     * @throws Exception
     */
    public static Instances applyFilter(Filter filter, Instances data, Instances training) throws Exception {
        filter.setInputFormat(training);
//        System.out.println(filter.getClass());
        Instances newData = Filter.useFilter(data, filter);
        //        System.out.println("****" + filter.getOptions());
        return newData;
    }

    /**
     * This method applies all of the suitable filters to an instance of training tweets.
     * <br>
     *     The filters applied include StringToWordVector to the content of the tweets, NominalToString
     * @param data The training data that is to be processed
     * @param training The training data that is to be processed
     * @return instance after applying different filters.
     * @throws Exception
     */
    public static Instances applyFiltersToTrainingSet(Instances data, Instances training) throws Exception {

        NominalToString nominalToString = new NominalToString();
        Rainbow rainbow = new Rainbow();
        WordTokenizer wordTokenizer = new WordTokenizer();
        LovinsStemmer lovinsStemmer = new LovinsStemmer();
        StringToWordVector stringToWordVector = new StringToWordVector();
        stringToWordVector.setMinTermFreq(4);
        stringToWordVector.setStopwordsHandler(rainbow);
        stringToWordVector.setStemmer(lovinsStemmer);
        stringToWordVector.setTokenizer(wordTokenizer);
        stringToWordVector.setAttributeNamePrefix("tweet_");
        stringToWordVector.setTokenizer(wordTokenizer);
        stringToWordVector.setWordsToKeep(10000);
        stringToWordVector.setDoNotOperateOnPerClassBasis(true);
        stringToWordVector.setLowerCaseTokens(true);
        stringToWordVector.setAttributeNamePrefix("tweet_");


//        System.out.println(data.classAttribute().index() + "");
        //        numericToNominal.setAttributeIndices("last");
        nominalToString.setAttributeIndexes("first");
        //        stringToWordVector.setAttributeIndices("1");

        max = Math.max(class1, Math.max(class2, class3));

//        System.out.println(class1 + " " + class2 + " " + class3 + " " + max);
        //        data = applyFilter(numericToNominal, data);
//        System.out.println("numeric check");
        data = applyFilter(nominalToString, data, data);
//        System.out.println("nominal check");


        data = applyFilter(stringToWordVector, data, training);
//        System.out.println("string check");
        return data;
    }

    /**
     * This method applies the SMOTE algorithm to the data. The SMOTE algorithm is a smart solution for the class imbalance problem in mahcine learning.
     * <br>
     *     The algorithm increases the two classes with the least amount of tweets.
     * @param data The data that the smote algorithm will be applied to
     * @return data after the SMOTE algorithm is applied
     * @throws Exception
     */
    public static Instances smote(Instances data) throws Exception {
        if (max != class1) {
            SMOTE smote = new SMOTE();
            smote.setNearestNeighbors(5);
            smote.setRandomSeed(1);
            smote.setPercentage((int) max / class1 * 100);
            smote.setClassValue("0");
//            System.out.println("R");
            data = applyFilter(smote, data, data);
//            System.out.println(data.size());
        }
        if (max != class2) {
            SMOTE smote = new SMOTE();
            smote.setNearestNeighbors(5);
            smote.setRandomSeed(1);
            smote.setPercentage((int) (max / class2) * 100);
//            System.out.println(max / class2 * 100);
            smote.setClassValue("1");
//            System.out.println("B");

            data = applyFilter(smote, data, data);
//            System.out.println(data.size());
        }
        if (max != class3) {
            SMOTE smote = new SMOTE();
            smote.setNearestNeighbors(5);
            smote.setRandomSeed(1);
            smote.setPercentage((int) max / class3 * 100);
            smote.setClassValue("2");
//            System.out.println("N");
            data = applyFilter(smote, data, data);
        }

//        System.out.println("smote check");
        Randomize randomize = new Randomize();
        data = applyFilter(randomize, data, data);
//        System.out.println("randomize check");
        return data;
    }
}
