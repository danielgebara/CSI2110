package SEGq2;


public class Reward {
    private final String rewardId;
    private String name;
    private int costInPoints;

    public Reward(String rewardId, String name, int costInPoints) {
        this.rewardId = rewardId;
        this.name = name;
        this.costInPoints = Math.max(0, costInPoints);
    }

    public String getRewardId() { return rewardId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCostInPoints() { return costInPoints; }
    public void setCostInPoints(int costInPoints) { this.costInPoints = Math.max(0, costInPoints); }

    public boolean isEligible(int points) {
        return points >= costInPoints;
    }
}

