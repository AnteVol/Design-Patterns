#include <stdio.h>
#include <stdlib.h>

typedef char const* (*PTRFUN)();

struct Animal {
    PTRFUN *animalFunctions;
    const char* name;
};

struct Animal* createDog(const char* name);
struct Animal* constructDog(struct Animal* newDog, const char* name);
char const* dogGreet(void);
char const* dogMenu(void);

struct Animal* createCat(const char* name);
struct Animal* constructCat(struct Animal* newCat, const char* name);
char const* catGreet(void);
char const* catMenu(void);

char const* animalPrintGreeting(struct Animal* animal);
char const* animalPrintMenu(struct Animal* animal);
void testAnimals(void);

struct Animal* createDog(const char* name) {
    int AnimalSize = sizeof(struct Animal);   
    struct Animal* newDog = (struct Animal*) malloc(1 * AnimalSize);
    return constructDog(newDog, name);
}

struct Animal* constructDog(struct Animal* newDog, const char* name) {
    int PTRFUNsize = sizeof(PTRFUN);
    newDog->name = name;
    newDog->animalFunctions = (PTRFUN*) malloc(2 * PTRFUNsize);
    newDog->animalFunctions[0] = dogGreet;
    newDog->animalFunctions[1] = dogMenu;
    return newDog;
}

char const* dogGreet(void) {
    return "vau!";
}
char const* dogMenu(void) {
    return "kuhanu govedinu";
}

struct Animal* createCat(const char* name) {
    int AnimalSize = sizeof(struct Animal);
    struct Animal* newCat = (struct Animal*) malloc(1 * AnimalSize);
    return constructCat(newCat, name);
}

struct Animal* constructCat(struct Animal* newCat, const char* name) {
    int PTRFUNsize = sizeof(PTRFUN);
    newCat->name = name;
    newCat->animalFunctions = (PTRFUN*) malloc(2 * PTRFUNsize);
    newCat->animalFunctions[0] = catGreet;
    newCat->animalFunctions[1] = catMenu;
    return newCat;
}

char const* catGreet(void) {
    return "mijau!";
}
char const* catMenu(void) {
    return "konzerviranu tunjevinu";
}

char const* animalPrintGreeting(struct Animal* animal) {
    return animal->animalFunctions[0]();
} 
char const* animalPrintMenu(struct Animal* animal) {
    return animal->animalFunctions[1]();
}

struct Animal* createManyDogs(int manyDogs) {
    int AnimalSize = sizeof(struct Animal);
    struct Animal* dogs = (struct Animal*) malloc(manyDogs * AnimalSize);
    
    for (int i = 0; i < manyDogs; i++) {
        const char* dogsName = "Pas";
        struct Animal* newDog = &dogs[i];
        constructDog(newDog, dogsName);
    }
    
    return dogs;
}

struct Animal createDogStack(const char* name){
    struct Animal dog;
    constructDog(&dog, name);
    return dog;
}

struct Animal createCatStack(const char* name){
    struct Animal cat;
    constructDog(&cat, name);
    return cat;
}

void testAnimals(void) {
    struct Animal* p1=createDog("Hamlet");
    struct Animal* p2=createCat("Ofelija");
    struct Animal* p3=createDog("Polonije");

    printf("%s pozdravlja: %s\n", p1->name, animalPrintGreeting(p1));
    printf("%s pozdravlja: %s\n", p2->name, animalPrintGreeting(p2));
    printf("%s pozdravlja: %s\n", p3->name, animalPrintGreeting(p3));

    printf("%s voli %s\n", p1->name, animalPrintMenu(p1));
    printf("%s voli %s\n", p2->name, animalPrintMenu(p2));
    printf("%s voli %s\n", p3->name, animalPrintMenu(p3));

    free(p1); free(p2); free(p3);
}

int main() {
    testAnimals();

    int n = 5; 
    struct Animal* dogs = createManyDogs(n);
    printf("Mnogi psi pozdravljaju:\n");
    for(int i = 0; i< n; i++){
        struct Animal* dog = &dogs[i];
        printf("\t %s %d pozdravlja: %s i voli %s\n", dog->name, i, animalPrintGreeting(dog), animalPrintMenu(dog));
    }
    free(dogs);

    return 0;
}
