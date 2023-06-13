import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Subway {
    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("No file name");
            return;
        }

        String fileName = args[0];

        SubwayMap subwayMap = new SubwayMap();
        subwayMap.constructMap(fileName);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
                String input = br.readLine();

				if (input.compareTo("QUIT") == 0)
					break;
                else {
                    // split input
                    String[] words = input.split(" ");

                    subwayMap.calculate(words[0]);
                    subwayMap.printResult(words[0], words[1]);
                }
			}
			catch (IOException e)
			{
				System.out.println("ERR : " + e.toString());
			}
		}
    }
}
