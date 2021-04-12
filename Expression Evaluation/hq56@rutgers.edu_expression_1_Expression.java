package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import structures.Stack;
public class Expression
{
   public static String delims = " \t*+-/()[]";

   private static boolean isNumeric(String string1) { 
       try {  
         Double.parseDouble(string1);  
         return true;
       } catch(NumberFormatException error){  
         return false;  
       }  
 }
   public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
   	StringTokenizer obj=new StringTokenizer(expr, " \t*+-/()]");
       while(obj.hasMoreTokens()){
        String token=obj.nextToken();
        if(isNumeric(token)){
         continue;
        }
        int i=token.indexOf('[');
        if(i==-1){
         Variable varable1=new Variable(token);
         if(vars.indexOf(varable1)==-1){
         vars.add(varable1);
         }
        }
        else{
         String newToken=token.substring(0,i);
         Array array1=new Array(newToken);
         if(arrays.indexOf(array1)==-1){
         arrays.add(array1);}
         String Remain =token.substring(i+1);
         makeVariableLists(Remain,vars,arrays);
         
        }
       }
      }
     
   public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars,
           ArrayList<Array> arrays) throws IOException
   {
       while (sc.hasNextLine())
       {
           StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
           int numTokens = st.countTokens();
           String tok = st.nextToken();
           Variable var = new Variable(tok);
           Array arr = new Array(tok);
           int vari = vars.indexOf(var);
           int arri = arrays.indexOf(arr);
           if (vari == -1&& arri == -1)
           {
               continue;
           }
           int num = Integer.parseInt(st.nextToken());
           if (numTokens == 2)
           { // scalar symbol
               vars.get(vari).value = num;
           }
           else
           { // array symbol
               arr = arrays.get(arri);
               arr.values = new int[num];
               // following are (index,val) pairs
               while (st.hasMoreTokens())
               {
                   tok = st.nextToken();
                   StringTokenizer stt = new StringTokenizer(tok, " (,)");
                   int index = Integer.parseInt(stt.nextToken());
                   int val = Integer.parseInt(stt.nextToken());
                   arr.values[index] = val;
               }
           }
       }
   }

   public static float evaluate(String expr, ArrayList<Variable> vars,ArrayList<Array> arrays){
       expr.trim();
       Stack<Character> Cha = new Stack<>();
       Stack<Float> Flo = new Stack<>();
       Stack<String> Str = new Stack<>();

       StringBuffer strbuffer = new StringBuffer("");
       float counter = 0;
       int i = 0;
       while (i < expr.length()){
           switch (expr.charAt(i)){
           case '(':
               Cha.push(expr.charAt(i));
               break;
           case ')':
               while (!Cha.isEmpty() && !Flo.isEmpty()&& (Cha.peek() != '('))
               {
                findOperationResult(Cha, Flo);
               }
               if (Cha.peek() == '('){
                   Cha.pop();
               }
               break;
           case '[':
               //if the character is open brace '['
               //push the value into the arrStk
               Str.push(strbuffer.toString());
               //set the lenght of the string buffer object to 0
               strbuffer.setLength(0);
               //push the character of the expression into operatorStk
               Cha.push(expr.charAt(i));
               break;
           case ']':
               //if the character is closed brace ']'
               //iterate the operator stack until the open brace found
               while (!Cha.isEmpty() && !Flo.isEmpty()
                       && (Cha.peek() != '['))
               {
                   //call the method findOperationResult
                   findOperationResult(Cha, Flo);
               }
               //if the peek of the operator stack is open brace
               if (Cha.peek() == '[')
               {
                   //then pop the value from the operator stack
                   Cha.pop();
               }
               //get the index value of the operandStk first element
               int idx = Flo.pop().intValue();
               //create an object of the array iterator
               Iterator<Array> itr = arrays.iterator();
               while (itr.hasNext())
               {
                   Array arr = itr.next();
                   if (arr.name.equals(Str.peek()))
                   {
                       Flo.push((float) arr.values[idx]);
                       Str.pop();
                       break;
                   }
               }

               break;
           case ' ':
               break;
           case '+':
           case '-':
           case '*':
           case '/':
               //check for empty, peek value and Precedence(
               while (!Cha.isEmpty() && (Cha.peek() != '(')
                       && (Cha.peek() != '[')
                       && isPrecedenceLow(expr.charAt(i), Cha.peek()))
               {
                   //call the method findOperationResult
                   findOperationResult(Cha, Flo);
               }
               Cha.push(expr.charAt(i));
               break;
           default:
               //if the expression is characters from a to z or A to Z
               if ((expr.charAt(i) >= 'a' && expr.charAt(i) <= 'z')
                       || (expr.charAt(i) >= 'A' && expr.charAt(i) <= 'Z'))
               {
                   //append the characters to string buffer
                   strbuffer.append(expr.charAt(i));
                   if (i + 1 < expr.length())
                   {
                       //check if the character of the expr
                       if (expr.charAt(i + 1) == '+'
                               || expr.charAt(i + 1) == '-'
                               || expr.charAt(i + 1) == '*'
                               || expr.charAt(i + 1) == '/'
                               || expr.charAt(i + 1) == ')'
                               || expr.charAt(i + 1) == ']'
                               || expr.charAt(i + 1) == ' ')
                       {
                           //create an object for the Variable var
                           Variable var = new Variable(strbuffer.toString());
                           int idxVar = vars.indexOf(var);
                           counter = vars.get(idxVar).value;
                           // Add operand to operandStk
                           Flo.push(counter);
                           strbuffer.setLength(0);

                       }

                   }
                   else
                   {
                       // the last variable of the expression
                       // convert to integer value;
                       Variable var = new Variable(strbuffer.toString());
                       int varIndex = vars.indexOf(var);
                       counter = vars.get(varIndex).value;
                       // Add operand to operandStk
                       Flo.push(counter);
                       // System.out.println("variable=" + str_buf.toString()
                       // + " value=" + fValue);
                       strbuffer.setLength(0);
                   }

               }
               else if (expr.charAt(i) >= '0' && expr.charAt(i) <= '9')
               {
                   // collect all the digits of the constant bConst = true;
                   strbuffer.append(expr.charAt(i));
                   if (i + 1 < expr.length())
                   {
                       if (expr.charAt(i + 1) == '+'
                               || expr.charAt(i + 1) == '-'
                               || expr.charAt(i + 1) == '*'
                               || expr.charAt(i + 1) == '/'
                               || expr.charAt(i + 1) == ')'
                               || expr.charAt(i + 1) == ']'
                               || expr.charAt(i + 1) == ' ')
                       {
                           // all the digits are collect convert from string to
                           // integer
                           counter = Integer.parseInt(strbuffer.toString());
                           // Add operand to operandStk
                           Flo.push(counter);
                           // System.out.println("constant=" + fValue);
                           strbuffer.setLength(0);
                       }
                   }
                   else
                   {
                       //convert the last constant of the expression to integer
                       counter = Float.parseFloat(strbuffer.toString());
                       // Add operand to operandStk
                       Flo.push(counter);
                       strbuffer.setLength(0);
                   }
               }
               break;
           }
           i++;
       }
       Float answer = Float.valueOf(0);
       if (i == expr.length())
       {
           while (Cha.size() > 0 && Flo.size() > 1)
           {
               //call the method findOperationResult
               findOperationResult(Cha, Flo);
           }
           if (Flo.size() > 0)
           {
               answer = Flo.pop();
           }
       }
       return answer.floatValue();
   }

   //definition of the method findOperationResult()
   private static void findOperationResult(Stack<Character> operatorStk,
           Stack<Float> operandStk)
   {
       Float answer = Float.valueOf(0);
       if (operatorStk.size() > 0&& operandStk.size() > 1)
       {
           //get the two operands
           Float oper1 = operandStk.pop().floatValue();
           Float oper2 = operandStk.pop().floatValue();
           //perform the operations
           switch (operatorStk.pop())
           {
           case '+':
               answer = oper2 + oper1;
               break;
           case '-':
               answer = oper2 - oper1;
               break;
           case '*':
               answer = oper2 * oper1;
               break;
           case '/':
               answer = oper2 / oper1;
               break;
           }
           operandStk.push(answer);

       }
       else if (operandStk.size() > 0)
       {
           answer = operandStk.pop();
           //push the value intp operand stack
           operandStk.push(operandStk.pop().floatValue());
       }

   }
   //definition of the method isPrecedenceLow()
   private static boolean isPrecedenceLow(char cha1, char cha2)
   {
       if ((cha1 == '*' || cha1 == '/')&& (cha2 == '+' || cha2 == '-'))
       {
           return false;
       }
       return true;
   }
}



