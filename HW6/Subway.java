import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Subway {
    private class Station{
        public String id;
        public String name;
        public String line;
    }

    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("No file name");
            return;
        }

        String fileName = args[0];
        ArrayList<String> lines = readFile(fileName);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				calculate(input);
			}
			catch (IOException e)
			{
				System.out.println("ERR : " + e.toString());
			}
		}
    }

    private static ArrayList<String> readFile(String fileName) {
        ArrayList<String> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(" ");
                // process
            }
        } catch (IOException e) {
            System.out.println("ERR: " + e.toString());
        }

        return result;
    }

    private static void calculate(String input) {

    }
}
