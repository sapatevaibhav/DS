package com.example.calculatorservice;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
public class CalculatorClient {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter number A: ");
            double a = sc.nextDouble();
            System.out.print("Enter number B: ");
            double b = sc.nextDouble();
            System.out.print("Enter operation (add, subtract, multiply, divide): ");
            String op = sc.next();
            String urlString = "http://localhost:8080/calculator/" + op + "?a=" + a + "&b=" + b;
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String result = in.readLine();
            in.close();
            System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
