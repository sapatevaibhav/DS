import CalculatorApp.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;

class CalculatorImpl extends CalculatorPOA {
    public float add(float a, float b) {
        return a + b;
    }

    public float subtract(float a, float b) {
        return a - b;
    }

    public float multiply(float a, float b) {
        return a * b;
    }

    public float divide(float a, float b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }
}

public class CalculatorServer {
    public static void main(String[] args) {
        try {
            // Create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // Create servant and register it with the ORB
            CalculatorImpl calculatorImpl = new CalculatorImpl();
            orb.connect(calculatorImpl);

            // Get the root naming context
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Bind the object reference in naming
            String name = "Calculator";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, calculatorImpl);

            System.out.println("CalculatorServer ready and waiting...");

            // Wait for invocations from clients
            orb.run();
        } catch (Exception e) {
            System.err.println("Error: " + e);
            e.printStackTrace(System.out);
        }
    }
}
