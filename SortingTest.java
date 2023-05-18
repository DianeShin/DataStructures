import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.
                char algo = ' ';

				if (args.length == 4) {
                    return;
                }

				String command = args.length > 0 ? args[0] : br.readLine();

				if (args.length > 0) {
                    args = new String[4];
                }
				
				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'S':	// Search
						algo = DoSearch(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    if (command.charAt(0) != 'S') {
                        for (int i = 0; i < newvalue.length; i++) {
                            System.out.println(newvalue[i]);
                        }
                    } else {
                        System.out.println(algo);
                    }
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}
// value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
		// 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
		// 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
		// 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.
		
	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoBubbleSort(int[] value) {
        int n = value.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (value[j] > value[j + 1]) {
                    // Swap elements
                    int temp = value[j];
                    value[j] = value[j + 1];
                    value[j + 1] = temp;
                }
            }
        }
        return value;
    }
    

	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoInsertionSort(int[] value) {
        int n = value.length;
        for (int i = 1; i < n; i++) {
            int key = value[i];
            int j = i - 1;
            while (j >= 0 && value[j] > key) {
                value[j + 1] = value[j];
                j--;
            }
            value[j + 1] = key;
        }
        return value;
    }
    

	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoHeapSort(int[] value) {
        int n = value.length;
    
        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(value, n, i);
    
        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            int temp = value[0];
            value[0] = value[i];
            value[i] = temp;
    
            // call max heapify on the reduced heap
            heapify(value, i, 0);
        }
        return value;
    }
    
    private static void heapify(int[] value, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
    
        if (left < n && value[left] > value[largest])
            largest = left;
    
        if (right < n && value[right] > value[largest])
        largest = right;

        if (largest != i) {
            int swap = value[i];
            value[i] = value[largest];
            value[largest] = swap;

            heapify(value, n, largest);
        }
    }    

	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoMergeSort(int[] value) {
        if (value.length <= 1)
            return value;
    
        int mid = value.length / 2;
        int[] left = Arrays.copyOfRange(value, 0, mid);
        int[] right = Arrays.copyOfRange(value, mid, value.length);
    
        left = DoMergeSort(left);
        right = DoMergeSort(right);
    
        return merge(left, right);
    }
    
    private static int[] merge(int[] left, int[] right) {
        int[] merged = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
    
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j])
                merged[k++] = left[i++];
            else
                merged[k++] = right[j++];
        }
    
        while (i < left.length)
            merged[k++] = left[i++];
    
        while (j < right.length)
            merged[k++] = right[j++];
    
        return merged;
    }    

	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoQuickSort(int[] value) {
        quickSort(value, 0, value.length - 1);
        return value;
    }
    
    private static void quickSort(int[] value, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(value, low, high);
            quickSort(value, low, pivotIndex - 1);
            quickSort(value, pivotIndex + 1, high);
        }
    }
    
    private static int partition(int[] value, int low, int high) {
        int pivot = value[high];
        int i = low - 1;
    
        for (int j = low; j < high; j++) {
            if (value[j] < pivot) {
                i++;
                int temp = value[i];
                value[i] = value[j];
                value[j] = temp;
            }
        }
    
        int temp = value[i + 1];
        value[i + 1] = value[high];
        value[high] = temp;
    
        return i + 1;
    }
    

	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoRadixSort(int[] value) {
        // Separate positive and negative numbers
        int[] positiveNumbers = Arrays.stream(value)
                .filter(num -> num >= 0)
                .toArray();
        int[] negativeNumbers = Arrays.stream(value)
                .filter(num -> num < 0)
                .map(num -> -num) // Convert negative numbers to positive for sorting
                .toArray();
    
        // Sort the positive numbers and negative numbers separately
        positiveNumbers = radixSort(positiveNumbers);
        negativeNumbers = radixSort(negativeNumbers);
    
        // Merge the sorted positive and negative numbers
        int[] sortedArray = new int[value.length];
        int positiveIndex = 0;
        int negativeIndex = negativeNumbers.length - 1;
    
        // Copy the negative numbers in descending order
        for (int i = 0; i < value.length; i++) {
            if (value[i] < 0) {
                sortedArray[i] = -negativeNumbers[negativeIndex];
                negativeIndex--;
            }
        }
    
        // Copy the positive numbers in ascending order
        for (int i = 0; i < value.length; i++) {
            if (value[i] >= 0) {
                sortedArray[i] = positiveNumbers[positiveIndex];
                positiveIndex++;
            }
        }
    
        return sortedArray;
    }
    
    private static int[] radixSort(int[] value) {
        // Find the maximum number to determine the number of digits
        int max = getMax(value);
    
        // Perform counting sort for every digit from least significant to most significant
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(value, exp);
        }
    
        return value;
    }
    
    private static int getMax(int[] value) {
        int max = Math.abs(value[0]);
        for (int i = 1; i < value.length; i++) {
            if (Math.abs(value[i]) > max) {
                max = Math.abs(value[i]);
            }
        }
        return max;
    }
    
    private static void countingSort(int[] value, int exp) {
        int n = value.length;
        int[] output = new int[n];
        int[] count = new int[19]; // Range from -9 to 9 (total 19 elements)
    
        Arrays.fill(count, 0);
    
        // Store count of occurrences in count[]
        for (int i = 0; i < n; i++) {
            int digit = (value[i] / exp) % 10;
            count[digit + 9]++; // Shift the index by 9 to handle negative numbers
        }
    
        // Change count[i] so that count[i] contains the actual
        // position of this digit in output[]
        for (int i = 1; i < 19; i++) { // Range from -9 to 9 (total 19 elements)
            count[i] += count[i - 1];
        }
    
        // Build the output array
        for (int i = n - 1; i >= 0; i--) {
            int digit = (value[i] / exp) % 10;
            output[count[digit + 9] - 1] = value[i];
            count[digit + 9]--;
        }
    
        // Copy the output array to arr[], so that arr[] contains
        // sorted numbers according to the current digit
        System.arraycopy(output, 0, value, 0, n);
    }
    
	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static char DoSearch(int[] value)
	{
		// TODO : Search 를 구현하라.
		return (' '); // 여러 조건을 설정하고, 각 조건에 따라 B, I, H, M, Q, R 중 하나를 리턴하라.
	}
}
