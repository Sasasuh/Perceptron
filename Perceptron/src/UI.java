import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UI {
    private Perceptron perceptron;
    private Trainer trainer;

    public UI(Perceptron perceptron, Trainer trainer) {
        this.perceptron = perceptron;
        this.trainer = trainer;
    }

    public void start(String trainFile) throws IOException {
        trainer.train(trainFile);

    }

    private void test(String filename) throws IOException {
        int totalCorrectCount = 0;
        int totalTestCount = 0;
        Map<String, Integer> correctCounts = new HashMap<>();
        Map<String, Integer> totalCounts = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                double[] input = new double[parts.length - 1];
                for (int i = 0; i < parts.length - 1; i++) {
                    input[i] = Double.parseDouble(parts[i]);
                }
                Integer expectedOutputObj = perceptron.getLabelsMap().get(parts[parts.length - 1]);
                if (expectedOutputObj == null) {
                    System.out.println("Unknown class label: " + parts[parts.length - 1]);
                    continue;
                }
                String label = parts[parts.length - 1];
                int expectedOutput = expectedOutputObj.intValue();
                int output = perceptron.compute(input);

                if (!correctCounts.containsKey(label)) {
                    correctCounts.put(label, 0);
                    totalCounts.put(label, 0);
                }

                if (output == expectedOutput) {
                    correctCounts.put(label, correctCounts.get(label) + 1);
                    totalCorrectCount++;
                }
                totalCounts.put(label, totalCounts.get(label) + 1);
                totalTestCount++;

                System.out.println(Arrays.toString(input) + " - " + getLabelFromOutput(output));
            }
        }

        for (Map.Entry<String, Integer> entry : correctCounts.entrySet()) {
            String label = entry.getKey();
            int correctCount = entry.getValue();
            int totalCount = totalCounts.get(label);
            double accuracy = (double) correctCount / totalCount;
            System.out.println("Accuracy for label " + label + ": " + accuracy);
        }

        double overallAccuracy = (double) totalCorrectCount / totalTestCount;
        System.out.println("Overall accuracy: " + overallAccuracy);
    }


    private String getLabelFromOutput(int output) {
        for (Map.Entry<String, Integer> entry : perceptron.getLabelsMap().entrySet()) {
            if (entry.getValue() == output) {
                return entry.getKey();
            }
        }
        return "Unknown";
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the learning rate: ");
        double learningRate = scanner.nextDouble();
        String trainFile = "data/trainset.csv";
        String testFile = "data/testset.csv";

        Perceptron perceptron = new Perceptron(4, learningRate);
        Trainer trainer = new Trainer(perceptron);

        UI ui = new UI(perceptron, trainer);

        ui.start(trainFile);
        ui.test(testFile);

        while (true) {
            System.out.print("Enter 1 to provide new iteration, 2 to test with a custom vector, 0 to exit: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                ui.start(trainFile);
                ui.test(testFile);
            } else if (choice == 2) {
                System.out.print("Enter a vector separated by semicolons (e.g., 1.2;3.2;2.3;4.2): ");
                scanner.nextLine(); // Consume newline left-over
                String vectorInput = scanner.nextLine();
                String[] parts = vectorInput.split(";");
                double[] customInput = new double[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    customInput[i] = Double.parseDouble(parts[i]);
                }
                int output = perceptron.compute(customInput);
                System.out.println("Custom input: " + Arrays.toString(customInput) + " - " + ui.getLabelFromOutput(output));
            } else if (choice == 0) {
                break;
            }
        }
    }
}