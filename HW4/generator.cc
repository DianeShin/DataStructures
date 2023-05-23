#include <fstream>
#include <sstream>

#include <iostream>
#include <vector>
#include <set>
#include <random>
using namespace std;

int main() {
    int num_elem = 50000;
    int rminimum = -100000000;	// 최소값
    int rmaximum = 100000000;	// 최대값    
    int same_num = 500;
    fstream output_file;
    output_file.open("1.in", fstream::in | fstream::out | fstream::trunc);

    output_file << num_elem << endl;
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<int> distribution(std::numeric_limits<int>::min(), std::numeric_limits<int>::max());


    // Print the generated edges
    for (int i = 0; i < num_elem; i++){
            int randomValue = distribution(gen);
            output_file << randomValue << endl;   

    }

    output_file.close();
    return 0;
}