#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef double (PTRFUN)(struct Unary_Function*, double);

double value_at(struct Unary_Function *uf, double x);
double value_at_square(struct Unary_Function *uf, double x);
double value_at_linear(struct Unary_Function *uf, double x);
double negative_value_at(struct Unary_Function *uf, double x);
double negative_value_at_linear(struct Unary_Function *uf, double x);

struct Unary_Function {
    PTRFUN *virtualFunctionTable[2]; // tablica virtualnih funkcija
    int lower_bound;
    int upper_bound;
    //void (*tabulate)(struct Unary_Function *self);//članska metoda, prima referencu na sebe
};

struct Square {
    PTRFUN *virtualFunctionTable[2];
    int lower_bound;
    int upper_bound;
};

struct Linear {
    PTRFUN *virtualFunctionTable[2];
    int lower_bound;
    int upper_bound;
    double a;
    double b;
};

double value_at(struct Unary_Function *uf, double x) {  // daje implementaciju gornjoj čistoj virtualnoj funkciji
      return x*x;
};

double value_at_square(struct Unary_Function *uf, double x) {  // daje implementaciju gornjoj čistoj virtualnoj funkciji
      return x*x;
};

double value_at_linear(struct Unary_Function *uf, double x) {  // daje implementaciju gornjoj čistoj virtualnoj funkciji
      struct Linear *help = (struct Linear*)uf;     
      return help->a*x + help->b;
};

double negative_value_at(struct Unary_Function *uf, double x) { // virtualna funkcija koja svaka druga klasa koristi na svoj nacin
      return -value_at(uf, x);
}

double negative_value_at_linear(struct Unary_Function *uf, double x) { // virtualna funkcija koja svaka druga klasa koristi na svoj nacin
      return -value_at_linear(uf, x);
}

void constructUnaryFunction(struct Unary_Function *thisuf, int lb, int ub){
    thisuf->virtualFunctionTable[0] = NULL;
    thisuf->virtualFunctionTable[1] = negative_value_at;
    thisuf->lower_bound = lb;
    thisuf->upper_bound = ub;
}

struct Unary_Function* createUnaryFunction(int lb, int ub){
    struct Unary_Function *newUnaryFunction = (struct Unary_Function*) malloc(sizeof(struct Unary_Function));
    constructUnaryFunction(newUnaryFunction, lb, ub);
    return newUnaryFunction;
}

void constructSquare(struct Square* thisq, int lb, int ub){
    constructUnaryFunction((struct Unary_Function*)thisq, lb, ub);
    thisq->virtualFunctionTable[0] = value_at;
    thisq->virtualFunctionTable[1] = negative_value_at;
}

struct Square* createSquare(int lb, int ub){
    struct Square* newSquare = (struct Square*)malloc(sizeof(struct Square));
    constructSquare(newSquare, lb, ub);
    return newSquare;
}

void constructLinear(struct Linear* thislinear, int lb, int ub, double const_a, double const_b){
    constructUnaryFunction((struct Unary_Function*)thislinear, lb, ub);
    thislinear->virtualFunctionTable[0] = value_at_linear;
    thislinear->virtualFunctionTable[1] = negative_value_at_linear;
    thislinear->a = const_a;
    thislinear->b = const_b;
}

struct Linear* createLinear(int lb, int ub, double const_a, double const_b){
    struct Linear* newLinear = (struct Linear*)malloc(sizeof(struct Linear));
    constructLinear(newLinear, lb, ub, const_a, const_b);
    return newLinear;
}

void tabulate(struct Unary_Function* thisuf) {// ovdje uf predstavlja this
      for(int x = thisuf->lower_bound; x <= thisuf->upper_bound; x++) {
        printf("f(%d)=%lf\n", x, thisuf->virtualFunctionTable[0](thisuf, x));
      }
};

static bool same_functions_for_ints(struct Unary_Function *f1, struct Unary_Function *f2, double tolerance) {// statička funkcija
      if(f1->lower_bound != f2->lower_bound) return false;//prima u arg samo ono sto pise, brz this i nema pointer
      if(f1->upper_bound != f2->upper_bound) return false;
      for(int x = f1->lower_bound; x <= f1->upper_bound; x++) {
        double delta = f1->virtualFunctionTable[0](f1, x) - f2->virtualFunctionTable[0](f2, x);
        if(delta < 0) delta = -delta;
        if(delta > tolerance) return false;
      }
      return true;
};


int main(){
    struct Unary_Function *f1 = (struct Unary_Function*) createSquare(-2, 2);
    tabulate(f1);
    struct Unary_Function *f2 = (struct Unary_Function*) createLinear(-2, 2, 5, -2);
    tabulate(f2);
    printf("f1==f2: %s\n", same_functions_for_ints(f1, f2, 1E-6) ? "DA" : "NE");
    printf("neg_val f2(1) = %lf\n", f2->virtualFunctionTable[1](f2, 1.0));
    free(f1);
    free(f2);
    return 0;
}