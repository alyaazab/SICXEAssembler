package mainpackage;

import java.util.ArrayList;
import java.util.Stack;

public class Postfix {

    public static boolean infixToPostfix(String operand) {

        String suboperand = "";
        ArrayList<String> listOfOperands = new ArrayList<>();
        Stack stack = new Stack();
        char operator;


        for(int i=0; i<operand.length(); i++)
        {
            if(!isOperator(operand.charAt(i)))
            {
                if(i == operand.length()-1)
                {
                    System.out.println(suboperand);
                    if(!stack.isEmpty())
                    {
                        operator = (char) stack.pop();
                        System.out.println(operator);

                    }

                }
                else
                    suboperand = suboperand + operand.charAt(i);
            }
            else
            {
                listOfOperands.add(suboperand);
                System.out.println(suboperand);
                suboperand = "";
                if(!stack.isEmpty())
                {
                    operator = (char) stack.pop();
                    System.out.println(operator);
                }
                stack.push(operand.charAt(i));
            }

        }

        return stack.isEmpty();

    }


    private static boolean isOperator(char x) {
        return x=='+' || x=='-' || x=='*' || x=='/' || x=='(' || x==')';
    }
}
