package mainpackage;

import symbol.SymbolTable;

import java.util.ArrayList;
import java.util.Stack;

public class Postfix {


    public static ArrayList<String> listOfOperands = new ArrayList<>();
    public static ArrayList<String> expression = new ArrayList<>();

    public static boolean infixToPostfix(String operand) {

        String suboperand = "";
        listOfOperands = new ArrayList<>();
        Stack stack = new Stack();
        String operator;



        for(int i=0; i<operand.length(); i++)
        {
            if(!isOperator(operand.charAt(i)))
            {
                if(i == operand.length()-1)
                {
                    suboperand = suboperand + operand.charAt(i);
                    System.out.println(suboperand);
                    expression.add(suboperand);
                    if(!stack.isEmpty())
                    {
                        operator = stack.pop().toString();
                        System.out.println(operator);
                        expression.add(operator + "");

                    }

                }
                else
                    suboperand = suboperand + operand.charAt(i);
            }
            else
            {
                listOfOperands.add(suboperand);
                System.out.println(suboperand);
                expression.add(suboperand);
                suboperand = "";
                if(!stack.isEmpty())
                {
                    operator = stack.pop().toString();
                    System.out.println(operator);
                    expression.add(operator);
                }
                stack.push(operand.charAt(i));
            }

        }
        System.out.println();
        System.out.println();
        for (String str : expression) {
            System.out.print(str + " ");
        }

        return stack.isEmpty();

    }

    public static int evaluatePostfixExpression() {

        int value=0;
        Stack stack = new Stack();
        int a, b;

        for (String str : expression) {
            if(!isOperator(str.charAt(0)))
            {
                try{
                    value = Integer.parseInt(str);

                }catch(NumberFormatException e){
                    value = SymbolTable.getInstance().getSymbol(str).getValue();
                }
                //get address from symtab and use it here
                stack.push(value);

            }
            else
            {
                a = (int) stack.pop();
                b = (int) stack.pop();

                stack.push(evaluate(a, b, str.charAt(0)));
            }
        }
        int res = (int) stack.pop();
        System.out.println("postfix result is " + res);
        return res;
    }

    private static int evaluate(int a, int b, char operator) {

        switch(operator)
        {
            case '+':
                return a+b;
            case '-':
                return b-a;
            case '*':
                return b*a;
            case '/':
                return b/a;
        }

        return 0;
    }

    private static boolean isOperator(char x) {
        return x=='+' || x=='-' || x=='*' || x=='/' || x=='(' || x==')';
    }

    public static boolean containsOperator(String string) {

        for(int i=0; i<string.length(); i++)
        {
            char c = string.charAt(i);

            if(c=='+' || c=='-' || c=='*' || c=='/' || c=='(' || c==')')
                return true;

        }

        return false;
    }
}
