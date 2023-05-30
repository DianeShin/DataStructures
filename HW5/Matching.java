import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Matching
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input)
	{
		char command = input.charAt(0);
		String filePath = input.split(" ")[1];

		if (command == '<'){
			List<String> lines = new ArrayList<>();

			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
				String line;
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}
			} catch (IOException e) {
				System.err.println("Error reading the file: " + e.getMessage());
			}
		}

		else if (command == '@');
		else if (command == '?');
		else if (command == '/');
		else if (command == '+');
		else return;
	}
}
