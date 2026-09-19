package BookStore528;

public class GoldStatus implements CustomerStatus {
    @Override
    public String getStatusName() {
        return "Gold";
    }

    @Override
    public double calcCost(double cost) {
        // Gold members pay the regular price (same as silver)
        // Easy to implement discounts for customers based on status (future-proofing)
        return cost;
    }

    @Override
    public void updateStatus(Customer c) {
        // Demote to Silver if points drop below the threshold
        if (c.getPoints() < 1000) {
            c.setStatus(new SilverStatus());
        }
    }
    //Overrides interface methods that are implemented (from CustomerStatus)
}
