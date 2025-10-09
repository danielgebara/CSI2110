package SEGq2;


public class LoyaltyProfile {
    private Client owner; // 1
    private int pointsBalance;
    private String tier;

    public LoyaltyProfile(Client owner, int startingPoints, String tier) {
        this.owner = owner;
        this.pointsBalance = Math.max(0, startingPoints);
        this.tier = tier;
        if (owner != null && owner.getLoyaltyProfile() != this) {
            owner.setLoyaltyProfile(this);
        }
    }

    public Client getOwner() { return owner; }
    /* package-private */ void setOwner(Client owner) { this.owner = owner; }

    public int getPointsBalance() { return pointsBalance; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }

    public void earnPoints(int p) {
        if (p <= 0) throw new IllegalArgumentException("points must be positive");
        pointsBalance += p;
    }

    /** Returns true if redemption succeeded */
    public boolean redeem(Reward r) {
        if (r == null) return false;
        int cost = r.getCostInPoints();
        if (cost <= pointsBalance) {
            pointsBalance -= cost;
            return true;
        }
        return false;
    }

    public int getBalance() { return pointsBalance; }
}

