import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Trainer {
    private Perceptron perceptron;

    public Trainer(Perceptron perceptron) {
        this.perceptron = perceptron;
    }

    public void train(String filename) throws IOException {
        List<double[]> inputs = new ArrayList<>();
        List<Integer> expectedOutputs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String parts[] = line.split(";");
                double[] input = new double[parts.length - 1];

                for (int i = 0; i < parts.length - 1; i++) {
                    input[i] = Double.parseDouble(parts[i]);
                }
                inputs.add(input);
                String label = parts[parts.length - 1];
                if (!perceptron.getLabelsMap().containsKey(label)) {
                    perceptron.getLabelsMap().put(label, perceptron.getLabelsMap().size());
                }
                expectedOutputs.add(perceptron.getLabelsMap().get(label));
            }
        }
        //Collections.shuffle(inputs, new Random());
        for (int i = 0; i < inputs.size(); i++) {
            double[] input = inputs.get(i);
            int expectedOutput = expectedOutputs.get(i);
            perceptron.learn(input, expectedOutput);
        }

    }
}