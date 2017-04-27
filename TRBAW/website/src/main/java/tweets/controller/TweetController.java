package tweets.controller;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
//import tweets.MLAlgorithmTest;
import tweets.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tweets.repository.TrainingTweetRepository;
import tweets.repository.TweetRepository;
import tweets.utils.DailyTweets;
import tweets.utils.Sector;
import tweets.utils.TypeOfTweets;
import tweets.utils.UsersVennDiagram;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.*;
import java.util.*;

/**
     This class's purpose is to control the Tweet model and the TrianingTweet model (deleting, updating) so that it can be used later on in the Client side.
@author     Dalia Mostafa

 **/

@Controller
public class TweetController {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TrainingTweetRepository trainingTweetRepository;

    /**
    Fetches all the tweets contained in the Database, and writes them to a CSV file.
     @return tweets An array list of all the tweets in the database.
     @throws IOException an IOException is thrown if the writing process is interrupted or failed
    */
    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/tweets")
    @ResponseBody
    public ArrayList<Tweet> getTweetsCSVFile() throws IOException {
        ArrayList<Tweet> tweets = Lists.newArrayList(tweetRepository.findAll());
        writeCSVFileTweets("allTweets", tweets);
        return tweets;
    }

    /**
        Fetches all the tweets contained in the Database.
        @return ArrayList &lt;Tweet&gt; an arraylist of all the tweets in the database
     */
    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/")
    @ResponseBody
    public ArrayList<Tweet> getTweets() {
        return Lists.newArrayList(tweetRepository.findAll());
    }

    /**
        Fetches a list of all Training Tweets and writes them to a CSV file. It also makes sure that all training tweets are represented in N,B, and R symbols instead of the numbers assigned to the training tweets previously.

        @return trainingTweets List of TrainingTweets
        @throws IOException an IOException is thrown if the writing process is interrupted or failed
     */
    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/tt")
    @ResponseBody
    public List<TrainingTweet> getTrainingTweets() throws IOException {
        ArrayList<TrainingTweet> trainingTweets = Lists.newArrayList(trainingTweetRepository.findAll());
        for(int i =0;i<trainingTweets.size();i++){
            String type = trainingTweets.get(i).getType();
            if(type.equals("1")){
                type = "R";
                trainingTweets.get(i).setType(type);
                trainingTweetRepository.save(trainingTweets.get(i));
            }
            else if(type.equals("2")){
                type = "B";
                trainingTweets.get(i).setType(type);
                trainingTweetRepository.save(trainingTweets.get(i));

            }
            else if(type.equals("3")){
                type = "N";
                trainingTweets.get(i).setType(type);
                trainingTweetRepository.save(trainingTweets.get(i));
            }
            }
        writeCSVFile("trainingtweets", trainingTweets);
        return trainingTweets;
    }

    /**
    Fetches a subset of Tweets of type Risk.
    @return risks List of risks Tweets
     */
    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/risks")
    @ResponseBody
    public List<Tweet> getriskSubset() {
//        ArrayList<Tweet> risks = Lists.newArrayList(tweetRepository.findByType("3"));
        ArrayList<Tweet> risks = Lists.newArrayList(tweetRepository.findByType("R"));
        return risks.subList(0, Math.min(1000, risks.size()));
    }

    /**
    Fetches a subset of Tweets of type Benefits.
    @return benefits List of benefit tweets
     */
    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/benefits")
    @ResponseBody
    public List<Tweet> getbenefitSubset() {
//        ArrayList<Tweet> benefits = Lists.newArrayList(tweetRepository.findByType("2"));
        ArrayList<Tweet> benefits = Lists.newArrayList(tweetRepository.findByType("B"));
        return benefits.subList(0, Math.min(1000, benefits.size()));
    }

    /**
        Fetches a subset of Tweets of type Neutral.
        @return neutral List of neutral tweets
    */
    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/neutral")
    @ResponseBody
    public List<Tweet> getneutralSubset() {
//        ArrayList<Tweet> neutrals = Lists.newArrayList(tweetRepository.findByType("1"));
        ArrayList<Tweet> neutrals = Lists.newArrayList(tweetRepository.findByType("N"));
        return neutrals.subList(0, Math.min(1000, neutrals.size()));
    }

    /**
     * Saves tweets to MySQL database.
     * @param tweets An array list of tweets that need to be saved.
     */
    public void saveTweets(ArrayList<Tweet> tweets) {
        tweetRepository.save(tweets);
    }

    /**
     * Saves a tweet to the MySQL database.
     * @param tweet
     */
    public void save(Tweet tweet) {
        tweetRepository.save(tweet);
    }

    /**
    This method aims to send JSON objects to the Frontend side for the line graph. The objects returned represent how many risk/benefit/neutral tweets were posted on a certain date.
     <br>
     In this method we use a hashmap that has a date as its key and an TypeOfTweets object as a value.
     <br>
     First, the method gets tweets from getTweets() method, then checks what type they are (risk/benefit/neutral) and the date they were posted on, and based on the date of each tweet brings the suitable TypeOfTweet object from the hashmap. It then increments the number of that type of tweet in the TypeOfTweets object.
    <br>
     After that, it loops over all the tweets then creates a DailyTweets object for each date, adding the
    number of benefit, neutral and risk tweets for each DailyTweets pbject. Each DailyTweets object is added to a list called dailyTweets, which is sorted according to date, then returned.
    @return List &lt;DailyTweets&gt; dailyTweets
     */

    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/Dates")
    @ResponseBody
    public List<DailyTweets> getDailyTweetsRates() {
        ArrayList<Tweet> tweets = getTweets();
        ArrayList<DailyTweets> dailyTweets = new ArrayList();
        HashMap<String, TypeOfTweets> dailyTweetsHashmap = new HashMap<String, TypeOfTweets>();
        ArrayList<Date> dates = new ArrayList<Date>();
        String createdAt;
        String type;
        Calendar cal = Calendar.getInstance();
        TypeOfTweets typeOfTweets = new TypeOfTweets(0, 0, 0);
        for (Tweet tweet : tweets) {
            type = tweet.getType();
            cal.setTime(tweet.getCreatedAt());
            createdAt = cal.get(Calendar.YEAR) + "-" + addZero(cal.get(Calendar.MONTH)+1) + "-" + addZero(cal.get(Calendar.DATE));
//            if (type.equals("1")) {
                if (type.equals("B")) {
                if (dailyTweetsHashmap.containsKey(createdAt)) {
                    typeOfTweets = dailyTweetsHashmap.get(createdAt);
                    typeOfTweets.setBenefits(dailyTweetsHashmap.get(createdAt).getBenefits() + 1);
                } else {
                    typeOfTweets = new TypeOfTweets(1, 0, 0);
                    dates.add(tweet.getCreatedAt());
                }
//            } else if (type.equals("2")) {
                } else if (type.equals("N")) {
                if (dailyTweetsHashmap.containsKey(createdAt)) {
                    typeOfTweets = dailyTweetsHashmap.get(createdAt);
                    typeOfTweets.setNeutral(dailyTweetsHashmap.get(createdAt).getNeutral() + 1);
                } else {
                    dates.add(tweet.getCreatedAt());
                    typeOfTweets = new TypeOfTweets(0, 0, 1);
                }
            //}                else if (type.equals("3")) {
        } else if (type.equals("R")) {
            if (dailyTweetsHashmap.containsKey(createdAt)) {
                    typeOfTweets = dailyTweetsHashmap.get(createdAt);
                    typeOfTweets.setRisks(dailyTweetsHashmap.get(createdAt).getRisks() + 1);
                } else {
                    dates.add(tweet.getCreatedAt());
                    typeOfTweets = new TypeOfTweets(0, 1, 0);
                }
            }
            dailyTweetsHashmap.put(createdAt, typeOfTweets);

        }

        Collections.sort(dates);
        for (int i = 0; i < dates.size(); i++) {
            cal.setTime(dates.get(i));
            String date = cal.get(Calendar.YEAR) + "-" + addZero(cal.get(Calendar.MONTH)+1) + "-" + addZero(cal.get(Calendar.DATE));

    //            String date = dates.get(i).getYear()+"-"+dates.get(i).getMonth()+"-"+dates.get(i).getDate();
            typeOfTweets = dailyTweetsHashmap.get(date);
            dailyTweets.add(new DailyTweets(date, typeOfTweets.getBenefits(), typeOfTweets.getRisks(),typeOfTweets.getNeutral()));
        }

        return dailyTweets;
    }

    /**
     * This method is used to add zeroes to a number so that it could be read in the correct date format. (DD/MM/YYYY)
     * @param cal the number that we will add zeroes to
     * @return the string containing the number with the added zeroes.
     */
    public String addZero(int cal){
        String s = cal+"";
        if(s.length()==1)
            return "0" + s;
        return s;
    }
    /**
     This method aims to send JSON objects to the Frontend side for the pie graph. The objects returned represent how many risk/benefit/neutral tweets were posted each year.
     <br>
    First, the method gets all tweets from getTweets() method,  loops over all the tweets, and gets year of each tweet and increments the number of that type of tweet in the TypeTweet model. A hashmap with the date as a key and a TypeOfTweets object as its value is used. It then puts it inside the hashmap with the year it was posted on as a key. Afterwards, we iterate through all of the TypeOfTweets model inside the hashmap, creating a model Sector for each year, then a hashmap of sectors is created, with the year as
    a key once again.
     <br>
     This is done so that the structure of JSONs could be easily used when received from the client side.

    @return Hashmap &lt;String, ArrayList&lt;Sector&gt;%gt; hashMap
     */
    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/Year")
    @ResponseBody
    public HashMap<String, ArrayList<Sector>> getTweetsRatesByYear() {
        ArrayList<Tweet> tweets = getTweets();
        HashMap<String, TypeOfTweets> typeOfTweetsHashmap = new HashMap<String, TypeOfTweets>(); //year created is the key of this hashmap
        ArrayList<String> years = new ArrayList<String>();
        String createdAt;
        String type;
        Calendar cal = Calendar.getInstance();
        TypeOfTweets typeOfTweets = new TypeOfTweets(0, 0, 0);
        for (Tweet tweet : tweets) {
            type = tweet.getType();
            cal.setTime(tweet.getCreatedAt());
            createdAt = cal.get(Calendar.YEAR) + "";
//            if (type.equals("1")) {
            if (type.equals("B")) {
                if (typeOfTweetsHashmap.containsKey(createdAt)) {
                    typeOfTweets = typeOfTweetsHashmap.get(createdAt);
                    typeOfTweets.setBenefits(typeOfTweetsHashmap.get(createdAt).getBenefits() + 1);
                } else {
                    typeOfTweets = new TypeOfTweets(1, 0, 0);
                    years.add(createdAt);
                }
            }
//            if (type.equals("2")) {
            if (type.equals("N")) {
                if (typeOfTweetsHashmap.containsKey(createdAt)) {
                    typeOfTweets = typeOfTweetsHashmap.get(createdAt);
                    typeOfTweets.setNeutral(typeOfTweetsHashmap.get(createdAt).getNeutral() + 1);
                } else {
                    years.add(createdAt);
                    typeOfTweets = new TypeOfTweets(0, 0, 1);
                }
            }
//            if (type.equals("3")) {
                if (type.equals("R")) {
                if (typeOfTweetsHashmap.containsKey(createdAt)) {
                    typeOfTweets = typeOfTweetsHashmap.get(createdAt);
                    typeOfTweets.setRisks(typeOfTweetsHashmap.get(createdAt).getRisks() + 1);
                } else {
                    years.add(createdAt);
                    typeOfTweets = new TypeOfTweets(0, 1, 0);
                }
            }
            typeOfTweetsHashmap.put(createdAt, typeOfTweets);
        }

        HashMap<String, ArrayList<Sector>> hashMap = new HashMap<String, ArrayList<Sector>>();
        ArrayList<Sector> sectors;
        for (int i = 0; i < typeOfTweetsHashmap.size(); i++) {
            String date = years.get(i);
            sectors = new ArrayList<Sector>();
            sectors.add(new Sector("benefits", typeOfTweetsHashmap.get(date).getBenefits()));
            sectors.add(new Sector("risks", typeOfTweetsHashmap.get(date).getRisks()));
            sectors.add(new Sector("neutral", typeOfTweetsHashmap.get(date).getNeutral()));

            hashMap.put(date, sectors);
        }
        return hashMap;
    }

    /**
     *
     *This method simply gets the total number of the neutral, risk and benefit tweets.
     @return TypeOfTweets
     */

    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/Numbers")
    @ResponseBody
    public TypeOfTweets getTotalNumber(){
        int risks = tweetRepository.findByType("R").size();
        int benefits = tweetRepository.findByType("B").size();
        int neutral = tweetRepository.findByType("N").size();
        TypeOfTweets typeOfTweets = new TypeOfTweets(benefits, risks, neutral);
        return typeOfTweets;
    }
    /**
        A hashmap stores the user as its key and a list of integers as its value, each indictaing the no of different types
        of tweets a user has tweeted.
     <br>
     First, the method gets all tweets using getTweets() method, and loops over all the tweets, check the user and type and and increments the type of tweet this user has tweeted in the hashmap.
        Then we iterate through the hashmap to put these values inside UsersVennDiagram objects, finally adding them to a list that is later returned.

       @return ArrayList &lt;UsersVennDiagram&gt; users
     */
    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/Users")
    @ResponseBody
    public ArrayList<UsersVennDiagram> getUsers() {
        ArrayList<UsersVennDiagram> users = new ArrayList<UsersVennDiagram>();
        HashMap<String, ArrayList<Integer>> usersHashmap = new HashMap<String, ArrayList<Integer>>();
        ArrayList<Tweet> tweets = getTweets();
        ArrayList<String> userStrings = new ArrayList<String>();
        Tweet tweet;
        ArrayList<Integer> empty = new ArrayList();
        ArrayList<Integer> change;
        empty.add(0);
        empty.add(0);
        empty.add(0);
        int b, r, n;
        for (int i = 0; i < tweets.size(); i++) {
            tweet = tweets.get(i);
            if (!usersHashmap.containsKey(tweet.getUsername())) {
                usersHashmap.put(tweet.getUsername(), empty);
                userStrings.add(tweet.getUsername());
            }
//            if (tweet.getType().equals("1")) {
            if (tweet.getType().equals("B")) {
                b = usersHashmap.get(tweet.getUsername()).get(0) + 1;
                n = usersHashmap.get(tweet.getUsername()).get(1);
                r = usersHashmap.get(tweet.getUsername()).get(2);

                change = new ArrayList<Integer>();
                change.add(b);
                change.add(n);
                change.add(r);
                usersHashmap.put(tweet.getUsername(), change);
//            } else if (tweet.getType().equals("2")) {
            } else if (tweet.getType().equals("N")) {
                b = usersHashmap.get(tweet.getUsername()).get(0);
                n = usersHashmap.get(tweet.getUsername()).get(1) + 1;
                r = usersHashmap.get(tweet.getUsername()).get(2);

                change = new ArrayList<Integer>();
                change.add(b);
                change.add(n);
                change.add(r);
                usersHashmap.put(tweet.getUsername(), change);
//            } else if (tweet.getType().equals("3")) {
            } else if (tweet.getType().equals("R")) {
                b = usersHashmap.get(tweet.getUsername()).get(0);
                n = usersHashmap.get(tweet.getUsername()).get(1);
                r = usersHashmap.get(tweet.getUsername()).get(2) + 1;

                change = new ArrayList<Integer>();
                change.add(b);
                change.add(n);
                change.add(r);
                usersHashmap.put(tweet.getUsername(), change);
            }
        }
        int benefits = 0, bn = 0, neutral = 0, risks = 0, rn = 0, rb = 0;
        for (int i = 0; i < userStrings.size(); i++) {
            if (usersHashmap.get(userStrings.get(i)).get(0) > 0) {
                benefits++;
                if (usersHashmap.get(userStrings.get(i)).get(1) > 0) {
                    bn++;
                }
                if (usersHashmap.get(userStrings.get(i)).get(2) > 0) {
                    rb++;
                }
            }
            if (usersHashmap.get(userStrings.get(i)).get(1) > 0) {
                neutral++;
                if (usersHashmap.get(userStrings.get(i)).get(2) > 0) {
                    rn++;
                }
            }
            if (usersHashmap.get(userStrings.get(i)).get(2) > 0) {
                risks++;
            }
        }
        users.add(new UsersVennDiagram(neutral, rn, "Neutral"));
        users.add(new UsersVennDiagram(risks, rb, "Risks"));
        users.add(new UsersVennDiagram(benefits, bn, "Benefits"));

        return users;
    }


    /**
     * This method writes TrainingTweets to both a CSV file and an ARFF file.
     * <br>
     * In this method only the content and type attributes are written down, as they are the most important features used in classification.
     * @param csvFileName The name of the CSV file that will contain the TrainingTweets.
     * @param trainingTweets A list of TrainingTweets that will be saved to the CSV file.
     * @throws IOException Thrown if the writing process is interrupted or fails.
     */
    public static void writeCSVFile(String csvFileName, List<TrainingTweet> trainingTweets) throws IOException{
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors = new CellProcessor[]{
                new NotNull(), // text
                new NotNull(), // class
        };

        try {
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName),
                    CsvPreference.STANDARD_PREFERENCE);
            String[] header = {"content", "type"};
            beanWriter.writeHeader(header);

            for (TrainingTweet trainingTweet : trainingTweets) {
                beanWriter.write(trainingTweet, header, processors);
            }

        } catch (IOException ex) {
            System.err.println("Error writing the CSV file: " + ex);
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error closing the writer: " + ex);
                }
            }
        }
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csvFileName));
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(csvFileName + ".arff"));
        saver.setDestination(new File(csvFileName + ".arff"));
        saver.writeBatch();
    }

    /**
     * This method writes Tweets to both a CSV file and an ARFF file.
     * <br>
     * In this method only the content and type attributes are written down, as they are the most important features used in classification.
     * @param csvFileName The name of the CSV file that will contain the TrainingTweets.
     * @param tweets A list of Tweets that will be saved to the CSV file.
     * @throws IOException Thrown if the writing process is interrupted or fails.
     */
    public static void writeCSVFileTweets(String csvFileName, List<Tweet> tweets) throws IOException {
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors = new CellProcessor[]{
                new NotNull(), // text
                new NotNull(), // class
        };

        try {
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName),
                    CsvPreference.STANDARD_PREFERENCE);
            String[] header = {"content", "type"};
            beanWriter.writeHeader(header);

            for (Tweet tweet : tweets) {
                beanWriter.write(tweet, header, processors);
            }

        } catch (IOException ex) {
            System.err.println("Error writing the CSV file: " + ex);
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error closing the writer: " + ex);
                }
            }
        }
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csvFileName));
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(csvFileName + ".arff"));
        saver.setDestination(new File(csvFileName + ".arff"));
        saver.writeBatch();
    }


}