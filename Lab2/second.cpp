#include <iostream>
#include <set>
#include <list>
#include <vector>

template <typename Iterator, typename Predicate>
Iterator mymax(
    Iterator first, Iterator last, Predicate pred) {
    Iterator max = first;
    while (first != last) {
        if (pred(*max, *first) < 0) {
            max = first;
        }
        ++first;
    }
    return max;
}

int gt_int(int a, int b) {
    return (a > b) ? 1 : ((a < b) ? -1 : 0);
}

int gt_str(const std::string& a, const std::string& b) {
    return a.compare(b);
}

int arr_int[] = {1, 3, 5, 7, 4, 6, 9, 2, 0};
std::set<int> numbers_set = {39, 6, 1, 8, 2, 999, 4, 5, 7};
std::list<int> numbers_list = {3, 60, 1, 887, 2, 98, 4, 5, 7};
std::vector<int> numbers_vector = {3, 60, 1, 887, 2, 98, 4087, 5, 7};
std::string str_arr[] = {"apple", "banana", "orange", "grape", "kiwi"};

int main() {
    const int* maxint = mymax(
        std::begin(arr_int), std::end(arr_int), gt_int);
    std::cout << "Result: " << *maxint << "\n";

    const auto maxset = mymax(
        numbers_set.begin(), numbers_set.end(), gt_int);
    std::cout << "Result: " << *maxset << "\n";

    const auto maxList = mymax(
        numbers_list.begin(), numbers_list.end(), gt_int);
    std::cout << "Result: " << *maxList << "\n";

    const auto maxVecor = mymax(
        numbers_vector.begin(), numbers_vector.end(), gt_int);
    std::cout << "Result: " << *maxVecor << "\n";

    const std::string* maxString = mymax(
        std::begin(str_arr), std::end(str_arr), gt_str);
    std::cout << "Result: " << *maxString << "\n";

    return 0;
}
