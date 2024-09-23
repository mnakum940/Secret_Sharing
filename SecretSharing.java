import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SecretSharing {

    // Function to decode a number from a given base
    public static long decodeValue(int base, String value) {
        return Long.parseLong(value, base);
    }

    // Lagrange interpolation to find the constant term c
    public static double lagrangeInterpolation(List<double[]> points) {
        double c = 0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            double xi = points.get(i)[0];
            double yi = points.get(i)[1];
            double li = 1;

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    li *= (0 - points.get(j)[0]) / (xi - points.get(j)[0]);
                }
            }
            c += li * yi;
        }
        return c;
    }

    // Main function to read JSON and calculate c
    public static void main(String[] args) {
        try {
            // Read JSON input file
            String inputData = new String(Files.readAllBytes(Paths.get("input2.json")));
            JSONObject json = new JSONObject(inputData);

            int n = json.getJSONObject("keys").getInt("n");
            int k = json.getJSONObject("keys").getInt("k");

            List<double[]> points = new ArrayList<>();

            // Decode each point using the correct key format
            for (int i = 1; i <= n; i++) {
                String key = String.valueOf(i);
                if (json.has(key)) {
                    JSONObject point = json.getJSONObject(key);
                    int base = point.getInt("base");
                    String value = point.getString("value");
                    long decodedValue = decodeValue(base, value);
                    points.add(new double[]{i, decodedValue}); // Push (x, y) pairs
                } else {
                    System.err.println("Key " + key + " not found in input data");
                }
            }

            // Calculate constant term c using Lagrange interpolation
            double constantTermC = lagrangeInterpolation(points.subList(0, k)); // Use first k points

            System.out.printf("The constant term c is: %.15f%n", constantTermC);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
