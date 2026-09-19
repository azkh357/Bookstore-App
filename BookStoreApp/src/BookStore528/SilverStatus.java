package BookStore528;

public class SilverStatus implements CustomerStatus {
    @Override
    public String getStatusName() {
        return "Silver";
    }

    @Override
    public double calcCost(double cost) {
        // Silver members pay the regular price (same as gold)
        return cost;
    }

    @Override
    public void updateStatus(Customer c) {
        // Transition to Gold if points reach the threshold
        if (c.getPoints() >= 1000) {
            c.setStatus(new GoldStatus());
        }
    }
    //Overrides interface methods that are implemented (from CustomerStatus)

}
