import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The VarianceMeanCalculator class is used for calculating mean and variance of
 * data.
 * 
 * Additionally, it includes a comment explaining the formula x = µ + σ *nextGaussian():
 * The formula x = µ + σ * nextGaussian() is used to transform a 
 * normal distribution value (mean = 0, std dev = 1) to a value that follows a normal distribution 
 * with a specified mean (µ) and standard deviation (σ).
 * nextGaussian() generates a value from a standard normal distribution.
 * Multiplying this value by σ adjusts the distribution to have the desired standard deviation.
 * Adding µ to the result shifts the distribution to have the desired mean.
 * 
 * @author Özcan Karaca
 */
public class VarianceMeanCalculator {
    public static void main(String[] args) {
        // Define the path to the CSV file
        String homeDirectory = System.getProperty("user.home");
        String basePath = homeDirectory + "/Desktop/master-thesis-ozcankaraca";
        Path path = Paths.get(basePath + "/data-for-testbed/data-for-realnetwork/reduced-sample.csv");

        try (BufferedReader br = Files.newBufferedReader(path)) {
            List<Double> maxUploads = new ArrayList<>();
            List<Double> maxDownloads = new ArrayList<>();

            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                maxUploads.add(Double.parseDouble(values[1]));
                maxDownloads.add(Double.parseDouble(values[2]));
            }

            double meanUpload = calculateMean(maxUploads);
            double meanDownload = calculateMean(maxDownloads);
            double varianceUpload = calculateVariance(maxUploads, meanUpload);
            double varianceDownload = calculateVariance(maxDownloads, meanDownload);

            System.out.println("Average upload speed: " + meanUpload);
            System.out.println("Average download speed: " + meanDownload);
            System.out.println("Variance of upload speed: " + varianceUpload);
            System.out.println("Variance of download speed: " + varianceDownload);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Calculate the mean of a list of values
    private static double calculateMean(List<Double> data) {
        if (data.isEmpty())
            return 0.0;

        double sum = 0.0;
        for (Double value : data) {
            sum += value;
        }
        return sum / data.size();
    }

    // Calculate the mean of a list of values
    private static double calculateVariance(List<Double> data, double mean) {
        if (data.isEmpty())
            return 0.0;

        double sumOfSquaredDifferences = 0.0;
        for (Double value : data) {
            sumOfSquaredDifferences += (value - mean) * (value - mean);
        }
        return sumOfSquaredDifferences / data.size();
    }
}
