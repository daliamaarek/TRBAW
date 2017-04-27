package tweets.utils;

/**
 * Created by daliamaarek on 28/05/16.
 */
public class TypeOfTweets {
    private int benefits; //number of benefit tweets
    private int risks;//number of risk tweets
    private int neutral; //number of neutral tweets

    public TypeOfTweets(int benefits, int risks, int neutral) {
        this.benefits = benefits;
        this.risks = risks;
        this.neutral = neutral;
    }

    public int getBenefits() {
        return benefits;
    }

    public void setBenefits(int benefits) {
        this.benefits = benefits;
    }

    public int getRisks() {
        return risks;
    }

    public void setRisks(int risks) {
        this.risks = risks;
    }

    public int getNeutral() {
        return neutral;
    }

    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }
}
