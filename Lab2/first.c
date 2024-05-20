#include <stdio.h>
#include <string.h>

//(*compar)(const void *, const void *) pokazivac na funkciju prema kojoj bi se trebalo za pojedini tip provesti usporedba

const void* mymax(const void *base, size_t nmemb, size_t size, int (*compar)(const void *, const void *));
int gt_int(const void *, const void *);
int gt_char(const void *, const void *);
int gt_str(const void *, const void *);

const void* myMax(const void *base, size_t nmemb, size_t size, int (*compar)(const void *, const void *)){
    const void* max = base;
    for(int i = 1; i < nmemb; i++){
        const void* toCompare = base + i * size;
        if(!compar(max, toCompare)){
            max = toCompare;
        }
    }
    return max;
}

int gt_int(const void *a, const void *b){
    int first = *((int*) a); // uzmem sadržaj od void pointera kojeg sam castao na integer pointer
    int second = *((int*) b);
    if(first > second){
        return 1;
    }
    return 0;
} 
int gt_char(const void *a, const void *b){
    int first = *((char*) a); 
    int second = *((char*) b);
    if((first - second) > 0){
        return 1;
    }
    return 0;
}
int gt_str(const void *a, const void *b){
    char* first = *((char**) a); 
    char* second = *((char**) b);
    if(strcmp(first, second) < 0){
        return 1;
    }
    return 0;
}


int main() {
    int arr_int[] = { 1, 3, 5, 7, 4, 6, 9, 2, 0 };
    char arr_char[]="Suncana strana ulice";
    const char* arr_str[] = {
        "Gle", "malu", "vocku", "poslije", "kise",
        "Puna", "je", "kapi", "pa", "ih", "njise"
    };

    int resultInt = *((int*)myMax(arr_int, sizeof(arr_int)/sizeof(arr_int[0]), sizeof(arr_int[0]), gt_int));
    printf("Biggest int in array: %d\n", resultInt);
    char resultChar = *((char*)myMax(arr_char, sizeof(arr_char)/sizeof(arr_char[0]), sizeof(arr_char[0]), gt_char));
    printf("Biggest char in array: %c\n", resultChar);
    char* resultString = *((char**)myMax(arr_str, sizeof(arr_str)/sizeof(arr_str[0]), sizeof(arr_str[0]), gt_str));
    printf("Biggest string in array of: %s\n", resultString);
    return 0;
}