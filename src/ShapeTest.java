//Requirements
//Create a set of classes to represent a square, rectangle, triangle, and circle. 
// Have these classes inherit from an abstract base class called Shape.
//
//Each class will implement at least two read-only fields area and perimeter — represented by their getters: 
// getArea(), which will return the area of the shape; and getPerimeter(), which will return the perimeter of the shape.
//
//Tips
//The abstract base class — Shape — will have a property called color and the two methods getArea() and getPerimeter(), 
// but they will be empty. They are designed to be overridden by inherited shapes, 
// so make sure that any shape that inherits from the base class implements their own versions of
// getArea() and getPerimeter() based on the type of shape it is.
//
//We recommend that you start with a square because this should be the easiest to implement. 
// Create a Shape base class, inherit a square from it, and override the two methods. 
// If you have done this correctly, it should give you the idea for the others.
//

abstract class Shape {
    String color;

    public Shape(String color) {
        this.color = color;
    }

    abstract double getArea();

    abstract double getPerimeter();

    public String getColor() {
        return color;
    }

    public String getFullDetails() {
        return "Shape: " + getClass().getName() + ", Color: " + color + ", Area: " + getArea() + ", Perimeter: " + getPerimeter();
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
}

class Rectangle extends Shape {
    private double length;
    private double width;

    public Rectangle(String color, double length, double width) {
        super(color);
        this.length = length;
        this.width = width;
    }

    @Override
    public double getArea() {
        return length * width;
    }

    @Override
    public double getPerimeter() {
        return 2 * (length + width);
    }
}

class Square extends Shape {
    private double side;

    public Square(String color, double side) {
        super(color);
        this.side = side;
    }

    @Override
    public double getArea() {
        return side * side;
    }

    @Override
    public double getPerimeter() {
        return 4 * side;
    }
}

class Triangle extends Shape {
    private double base;
    private double height;
    private double sideA, sideB, sideC;

    public Triangle(String color, double base, double height, double sideA, double sideB, double sideC) {
        super(color);
        this.base = base;
        this.height = height;
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    @Override
    public double getArea() {
        return 0.5 * base * height;
    }

    @Override
    public double getPerimeter() {
        return sideA + sideB + sideC;
    }
}

public class ShapeTest {
    public static void main(String[] args) {
        shapingShapes();
    }

    private static void shapingShapes() {
        Shape[] shapes = {new Circle("Red", 2.2), new Circle("Blue", 2.2), new Circle("Green", 2.2), new Circle("Yellow", 2.2), new Rectangle("White", 2, 3), new Rectangle("Orange", 2, 3), new Rectangle("Pink", 2, 3), new Square("Green", 2.2), new Square("Yellow", 2.2), new Square("Orange", 2.2), new Triangle("Black", 2.3, 3.4, 5, 6, 7), new Triangle("Pink", 2, 33.2, 3, 54, 2)};

        for (Shape shape : shapes) {
            System.out.println(shape.getFullDetails());
        }
    }
}