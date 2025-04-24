import CalculatorApp.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;

public class CalculatorClient {
    public static void main(String[] args) {
        try {
            // Create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // Get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Resolve the object reference in naming
            String name = "Calculator";
            Calculator calculator = CalculatorHelper.narrow(ncRef.resolve_str(name));

            // Perform operations
            float a = 10, b = 5;
            System.out.println("Addition: " + a + " + " + b + " = " + calculator.add(a, b));
            System.out.println("Subtraction: " + a + " - " + b + " = " + calculator.subtract(a, b));
            System.out.println("Multiplication: " + a + " * " + b + " = " + calculator.multiply(a, b));
            System.out.println("Division: " + a + " / " + b + " = " + calculator.divide(a, b));
        } catch (Exception e) {
            System.err.println("Error: " + e);
            e.printStackTrace(System.out);
        }
    }
}
