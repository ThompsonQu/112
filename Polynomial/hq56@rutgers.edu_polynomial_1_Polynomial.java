package poly;
import java.io.IOException;
import java.util.Scanner;
/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
 
 /**
  * Reads a polynomial from an input stream (file or keyboard). The storage format
  * of the polynomial is:
  * <pre>
  *     <coeff> <degree>
  *     <coeff> <degree>
  *     ...
  *     <coeff> <degree>
  * </pre>
  * with the guarantee that degrees will be in descending order. For example:
  * <pre>
  *      4 5
  *     -2 3
  *      2 1
  *      3 0
  * </pre>
  * which represents the polynomial:
  * <pre>
  *      4*x^5 - 2*x^3 + 2*x + 3 
  * </pre>
  * 
  * @param sc Scanner from which a polynomial is to be read
  * @throws IOException If there is any input error in reading the polynomial
  * @return The polynomial linked list (front node) constructed from coefficients and
  *         degrees read from scanner
  */
 public static Node read(Scanner sc) 
 throws IOException {
  Node poly = null;
  while (sc.hasNextLine()) {
   Scanner scLine = new Scanner(sc.nextLine());
   poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
   scLine.close();
  }
  return poly;
 }
 
 /**
  * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
  * The returned polynomial MUST have all new nodes. In other words, none of the nodes
  * of the input polynomials can be in the result.
  * 
  * @param poly1 First input polynomial (front of polynomial linked list)
  * @param poly2 Second input polynomial (front of polynomial linked list
  * @return A new polynomial which is the sum of the input polynomials - the returned node
  *         is the front of the result polynomial
  */
 
	 public static Node add(Node poly1, Node poly2) {
	       Node ptr= null;
	       Node answer = null;
	       Node curptr = null;
	       if(poly1 == null && poly2 == null) {
	           return answer;
	       }
	       while(poly1 != null && poly2 != null) {
	           if(poly1.term.degree < poly2.term.degree) {
	               curptr = new Node(poly1.term.coeff, poly1.term.degree, null);
	               if(ptr != null){
	                   ptr.next = curptr;
	               }else{
	                   answer = curptr;
	               }
	               ptr = curptr;
	               poly1 = poly1.next;
	           }else if(poly2.term.degree < poly1.term.degree) {
	               curptr = new Node(poly2.term.coeff, poly2.term.degree, null);
	               if(ptr != null){
	                   ptr.next = curptr;
	               }else{
	                   answer = curptr;
	               }
	               ptr = curptr;
	               poly2 = poly2.next;
	           }else if(poly1.term.degree == poly2.term.degree){
	               curptr = new Node(poly1.term.coeff + poly2.term.coeff, poly1.term.degree,null);
	               if(ptr != null){
	                   ptr.next = curptr;
	               }else{
	                   answer = curptr;
	               }
	               ptr = curptr;
	               poly1 = poly1.next;
	               poly2 = poly2.next;
	           }
	       }
	       if(poly1 != null) {
	           while(poly1 != null) {
	               curptr = new Node(poly1.term.coeff, poly1.term.degree, null);
	               if(ptr != null){
	                   ptr.next = curptr;
	               }else{
	                   answer = curptr;
	                   }
	                   ptr = curptr;
	                   poly1 = poly1.next;
	               }
	       }else if(poly2 != null){
	           while(poly2 != null) {
	               curptr = new Node(poly2.term.coeff, poly2.term.degree, null);
	               if(ptr != null){
	                   ptr.next = curptr;
	               }else{
	                   answer = curptr;
	                   }
	                   ptr = curptr;
	                   poly2 = poly2.next;
	               }
	      }
	       Node prev = null;
	       curptr = answer;
	       while(curptr != null) {
	           if(curptr.term.coeff == 0) {
	               if(curptr == answer) {
	                   answer = curptr.next;
	               }else{
	                   prev.next = curptr.next;
	               }
	           }else{
	               prev = curptr;
	           }
	           curptr = curptr.next;
	       }
	      
	       return answer;
	   }
     
	 public static Node multiply(Node poly1, Node poly2) {
	       /** COMPLETE THIS METHOD **/
	       // FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
	       // CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
	       Node ptr = null;
	       Node end = null;
	       Node answer = null;
	       if(poly1 == null || poly2 == null) {
	           return ptr;
	       }
	       for(Node ptr2 = poly2; ptr2 != null; ptr2 = ptr2.next) {
	           for(Node ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next) {
	               Node curptr = new Node(ptr1.term.coeff * ptr2.term.coeff, ptr1.term.degree + ptr2.term.degree, null);
	           if(end != null){
	               end.next = curptr;
	           }else{
	               ptr = curptr;
	           }
	           end = curptr;
	           }
	           answer = add(answer, ptr);
	           ptr = null;
	           end = null;
	       }
	       return answer;
	   }
	  
	 public static float evaluate(Node poly, float x) {
	       /** COMPLETE THIS METHOD **/
	       // FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
	       // CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
	       float answer = 0;
	       if(poly != null) {
	           for(Node ptr = poly; ptr != null; ptr = ptr.next) {
	               answer += ptr.term.coeff * Math.pow(x,ptr.term.degree);
	           }
	       }
	       return answer;
	   }
	  
 public static String toString(Node poly) {
  if (poly == null) {
   return "0";
  } 
  
  String retval = poly.term.toString();
  for (Node current = poly.next ; current != null ;
  current = current.next) {
   retval = current.term.toString() + " + " + retval;
  }
  return retval;
 } 
}