  #include <iostream>
  #include <assert.h>
  #include <stdlib.h>

  struct Point{
    int x; int y;
  };
  struct Shape{
    enum EType {circle, square, romb};
    EType type_;
  };
  struct Circle{
     Shape::EType type_;
     double radius_;
     Point center_;
  };
  struct Square{
     Shape::EType type_;
     double side_;
     Point center_;
  };
  struct Romb{
     Shape::EType type_;
     double side;
     Point center_;
  };
  void drawSquare(struct Square*){
    std::cerr <<"in drawSquare\n";
  }
  void drawCircle(struct Circle*){
    std::cerr <<"in drawCircle\n";
  }
  void drawRomb(struct Romb*){
    std::cerr <<"in drawRomb\n";
  }
  void drawShapes(Shape** shapes, int n){
    for (int i=0; i<n; ++i){
      struct Shape* s = shapes[i];
      switch (s->type_){
      case Shape::square:
        drawSquare((struct Square*)s);
        break;
      case Shape::circle:
        drawCircle((struct Circle*)s);
        break;
      case Shape::romb:
        drawRomb((struct Romb*)s);
        break;
      default:
        assert(0); 
        exit(0);
      }
    }
  }

  void moveSquare(struct Square* s, int x, int y){
    std::cerr <<"in moveSquare\n";
    s->center_.x += x;
    s->center_.y += y;
  }
  void moveCircle(struct Circle* c, int x, int y){
    std::cerr <<"in moveCircle\n";
    c->center_.x += x;
    c->center_.y += y;
  }
  void moveRomb(struct Romb* r, int x, int y){
    std::cerr <<"in moveCircle\n";
    r->center_.x += x;
    r->center_.y += y;
  }

  void moveShapes(Shape* shape, int x, int y){
      switch (shape->type_){
      case Shape::circle:
        moveCircle((struct Circle*)shape, x, y);
        break;
      case Shape::square:
        moveSquare((struct Square*)shape, x, y);
        break;
      case Shape::romb:
        moveRomb((struct Romb*)shape, x, y);
        break;
      default:
        break;
      }
  }

  int main(){
    Shape* shapes[5];
    shapes[0]=(Shape*)new Circle;
    shapes[0]->type_=Shape::circle;
    shapes[1]=(Shape*)new Square;
    shapes[1]->type_=Shape::square;
    shapes[2]=(Shape*)new Square;
    shapes[2]->type_=Shape::square;
    shapes[3]=(Shape*)new Circle;
    shapes[3]->type_=Shape::circle;
    shapes[4]=(Shape*) new Romb;
    shapes[4]->type_ = Shape::romb;

    drawShapes(shapes, 5);
  }