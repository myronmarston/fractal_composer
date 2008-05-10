/*
  File: Fraction.java
  Downloaded from: http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/misc/Fraction.java
  This has been moved to the com.myronmarston.util package because JRuby seems
  to have issues importing from an edu package.

  Originally written by Doug Lea and released into the public domain.
  This may be used for any purposes whatsoever without acknowledgment.
  Thanks for the assistance and support of Sun Microsystems Labs,
  and everyone contributing, testing, and using this code.

  History:
  Date       Who                What
  7Jul1998  dl               Create public version
  11Oct1999 dl               add hashCode
*/

package com.myronmarston.util;

import org.simpleframework.xml.*;
import java.util.regex.*;


/**
 * An immutable class representing fractions as pairs of longs.
 * Fractions are always maintained in reduced form.
 **/
@Root
public class Fraction implements Cloneable, Comparable, java.io.Serializable {
  @Attribute
  protected final long numerator_;
  
  @Attribute
  protected final long denominator_;
  
  /** Regular expression string to parse a fraction string. */
  public final static String REGEX_STRING = "^(0|(?:[1-9](?:\\d)*))(?:\\/([1-9](?:\\d)*))?$";
  
  /** Regular expression pattern to parse a fraction string. */
  private final static Pattern REGEX_PATTERN = Pattern.compile(REGEX_STRING);
  

  /** Return the numerator **/  
  public final long numerator() { return numerator_; }

  /** Return the denominator **/
  public final long denominator() { return denominator_; }

  /** Exists to allow xml deserialization using the simple framework **/   
  private Fraction() {
      this(0, 1);
  };
  
  /**
   * Constructs a fraction by parsing a fraction string.
   * 
   * @param fractionStr a string in fraction ("1/4") or integer ("23") form
   * @throws java.lang.IllegalArgumentException if the string does not match
   *         the expected pattern or if the denominator is zero
   */
  public Fraction(String fractionStr) throws IllegalArgumentException {
    Matcher match = REGEX_PATTERN.matcher(fractionStr);
    if (!match.matches()) throw new IllegalArgumentException("The fraction string '" + fractionStr + "' is not in a recognized form.");
    String numStr = match.group(1);
    String denStr = match.group(2);
    Long num = Long.parseLong(numStr);
    Long den = (denStr == null || denStr.isEmpty() ? 1L : Long.parseLong(denStr));    
    
    // the code below is copied from the other constructor.
    // it would be better to make a helper method with this logic,
    // but numerator_ and denominator_ are final, so we can only set them
    // in a constructor.  Besides, this code has been well tested, so there is
    // no reason to expect this to ever change.
    
    // normalize while constructing    
    boolean numNonnegative = (num >= 0);
    boolean denNonnegative = (den >= 0);
    long a = numNonnegative? num : -num;
    long b = denNonnegative? den : -den;
    long g = gcd(a, b);
    numerator_ = (numNonnegative == denNonnegative)? (a / g) : (-a / g);
    denominator_ = b / g;
  }
  
  /** Create a Fraction equal in value to num / den **/
  public Fraction(long num, long den) throws IllegalArgumentException {
    if (den == 0L) throw new IllegalArgumentException("The fraction denominator cannot be zero."); 
      
    // normalize while constructing
    boolean numNonnegative = (num >= 0);
    boolean denNonnegative = (den >= 0);
    long a = numNonnegative? num : -num;
    long b = denNonnegative? den : -den;
    long g = gcd(a, b);
    numerator_ = (numNonnegative == denNonnegative)? (a / g) : (-a / g);
    denominator_ = b / g;
  }

  /** Create a fraction with the same value as Fraction f **/
  public Fraction(Fraction f) {
    numerator_ = f.numerator();
    denominator_ = f.denominator();
  }  

  public String toString() { 
    if (denominator() == 1) 
      return "" + numerator();
    else
      return numerator() + "/" + denominator(); 
  }

  public Object clone() { return new Fraction(this); }

  /** Return the value of the Fraction as a double **/
  public double asDouble() { 
    return ((double)(numerator())) / ((double)(denominator()));
  }

  /** 
   * Compute the nonnegative greatest common divisor of a and b.
   * (This is needed for normalizing Fractions, but can be
   * useful on its own.)
   **/
  public static long gcd(long a, long b) { 
    long x;
    long y;

    if (a < 0) a = -a;
    if (b < 0) b = -b;

    if (a >= b) { x = a; y = b; }
    else        { x = b; y = a; }

    while (y != 0) {
      long t = x % y;
      x = y;
      y = t;
    }
    return x;
  }

  /** return a Fraction representing the negated value of this Fraction **/
  public Fraction negative() {
    long an = numerator();
    long ad = denominator();
    return new Fraction(-an, ad);
  }

  /** return a Fraction representing 1 / this Fraction **/
  public Fraction inverse() {
    long an = numerator();
    long ad = denominator();
    return new Fraction(ad, an);
  }


  /** return a Fraction representing this Fraction plus b **/
  public Fraction plus(Fraction b) {
    long an = numerator();
    long ad = denominator();
    long bn = b.numerator();
    long bd = b.denominator();
    return new Fraction(an*bd+bn*ad, ad*bd);
  }

  /** return a Fraction representing this Fraction plus n **/
  public Fraction plus(long n) {
    long an = numerator();
    long ad = denominator();
    long bn = n;
    long bd = 1;
    return new Fraction(an*bd+bn*ad, ad*bd);
  }

  /** return a Fraction representing this Fraction minus b **/
  public Fraction minus(Fraction b) {
    long an = numerator();
    long ad = denominator();
    long bn = b.numerator();
    long bd = b.denominator();
    return new Fraction(an*bd-bn*ad, ad*bd);
  }

  /** return a Fraction representing this Fraction minus n **/
  public Fraction minus(long n) {
    long an = numerator();
    long ad = denominator();
    long bn = n;
    long bd = 1;
    return new Fraction(an*bd-bn*ad, ad*bd);
  }


  /** return a Fraction representing this Fraction times b **/
  public Fraction times(Fraction b) {
    long an = numerator();
    long ad = denominator();
    long bn = b.numerator();
    long bd = b.denominator();
    return new Fraction(an*bn, ad*bd);
  }

  /** return a Fraction representing this Fraction times n **/
  public Fraction times(long n) {
    long an = numerator();
    long ad = denominator();
    long bn = n;
    long bd = 1;
    return new Fraction(an*bn, ad*bd);
  }

  /** return a Fraction representing this Fraction divided by b **/
  public Fraction dividedBy(Fraction b) {
    long an = numerator();
    long ad = denominator();
    long bn = b.numerator();
    long bd = b.denominator();
    return new Fraction(an*bd, ad*bn);
  }

  /** return a Fraction representing this Fraction divided by n **/
  public Fraction dividedBy(long n) {
    long an = numerator();
    long ad = denominator();
    long bn = n;
    long bd = 1;
    return new Fraction(an*bd, ad*bn);
  }

  /** return a number less, equal, or greater than zero
   * reflecting whether this Fraction is less, equal or greater than 
   * the value of Fraction other.
   **/
  public int compareTo(Object other) {
    Fraction b = (Fraction)(other);
    long an = numerator();
    long ad = denominator();
    long bn = b.numerator();
    long bd = b.denominator();
    long l = an*bd;
    long r = bn*ad;
    return (l < r)? -1 : ((l == r)? 0: 1);
  }

  /** return a number less, equal, or greater than zero
   * reflecting whether this Fraction is less, equal or greater than n.
   **/

  public int compareTo(long n) {
    long an = numerator();
    long ad = denominator();
    long bn = n;
    long bd = 1;
    long l = an*bd;
    long r = bn*ad;
    return (l < r)? -1 : ((l == r)? 0: 1);
  }

  public boolean equals(Object other) {
    return compareTo((Fraction)other) == 0;
  }

  public boolean equals(long n) {
    return compareTo(n) == 0;
  }

  public int hashCode() {
    return (int) (numerator_ ^ denominator_);
  }
}
