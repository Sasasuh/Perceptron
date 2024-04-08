import java.util.HashMap;
import java.util.Map;

public class Perceptron {
    private double[] weights;
    private double learningRate;
    private double threshold;
    private Map<String, Integer> labelsMap = new HashMap<>();

    public Perceptron(int inputCount, double learningRate) {
        this.weights = new double[inputCount];
        this.learningRate = learningRate;
        this.threshold = Math.random() * 10 - 5;
        for (int i = 0; i < inputCount; i++) {
            weights[i] = Math.random() * 10 - 5;
        }
    }

    public void learn(double[] input, int expectedOutput) {
        int output = compute(input);
        for (int j = 0; j < weights.length; j++) {
            weights[j] += learningRate * (expectedOutput - output) * input[j];
        }
        threshold += learningRate * (expectedOutput - output);
    }

    public int compute(double[] input) {
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += weights[i] * input[i];
        }
        return sum >= threshold ? 1 : 0;
    }

    public Map<String, Integer> getLabelsMap() {
        return labelsMap;
    }
}
