import java.awt.*;
/**
 * Created by DarkEmperor on 2017-07-15.
 */
public class test {

    public static void main(String[] args){
        /*
        Java is pass by value: this means that references are copied. Therefore, when the references that are passed
        into methods are assigned to new objects, the original reference is still pointing to the original object.
         */
        Point point1 = new Point(1,1);
        method(point1);
        System.out.println(point1.toString());
    }

    public static void method(Point point2) {
        // Value of point1 is set.
        point2.setLocation(2,2);
        /* point1 is not modified: point2 is just assigned to a new object, but point1 is still assigned
        to the original. This works because only the value of the reference is passed: if the reference itself
        was passed, point1 would be reassigned to a new object.*/
        point2 = new Point(3,3);
    }




}
