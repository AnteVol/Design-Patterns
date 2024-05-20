
#include<stdio.h>
#include <typeinfo>


class B{
     public:
     virtual int prva()=0;
     virtual int druga(int)=0;
};
class D: public B{
    public:
    virtual int prva(){
        return 42;
    } 
    virtual int druga(int x){
        return prva()+x;
    }
}; 

typedef int (*pfun1)(D*); 
typedef int (*pfun2)(D*, int);

void print(B* derived){
    pfun1 **virtualTable = *(pfun1***)derived;
    pfun1 f1 = (pfun1)virtualTable[0];
    pfun2 f2 = (pfun2)virtualTable[1];
    int first = f1((D*)derived);
    int second = f2((D*)derived, 77);
    printf("%d\n", first);
    printf("%d\n", second); 
} 

int main() {
    B* derived = new D();
    print(derived);
    return 0; 
}