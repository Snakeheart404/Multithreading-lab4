import java.util.HashMap;

public class RandomVariableCharacteristics {

    private final HashMap<Integer, Long> data;
    HashMap<Integer, Double> distributionLaw = new HashMap<>();
    public RandomVariableCharacteristics (HashMap<Integer, Long> data)
    {
        this.data = data;
    }

    public HashMap<Integer, Double> getDistributionLaw() {
        double sum = 0;
        for (var entry : data.entrySet()) {
            sum += entry.getValue();
        }

        for (var entry : data.entrySet()) {
            double probability = entry.getValue() / sum;
            distributionLaw.put(entry.getKey(), probability);
        }
        return distributionLaw;
    }

    public double getMean() {
        double mean = 0;
        for (var entry : distributionLaw.entrySet()) {
            mean += entry.getKey() * entry.getValue();
        }
        return mean;
    }

    public double getVariance() {
        double variance = 0;
        for (var entry : distributionLaw.entrySet()) {
            variance += Math.pow(entry.getKey(), 2) * entry.getValue();
        }
        variance -= Math.pow(this.getMean(), 2);
        return variance;
    }

    public double getStandardDeviation() {
        return Math.sqrt(this.getVariance());
    }
}
