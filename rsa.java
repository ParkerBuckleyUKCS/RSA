/*
Author: Parker Buckley
Date Modified: 2/3/2020
Purpose: A file based Join and select program that reads salary and location data
	and aggregates into a single output.
Class: CS405G
File Name: prog0_BUCKLEY.java
*/

//preprocessor directive
import java.io.*;
import java.util.*;
import java.math.BigInteger;
import java.lang.*;

public class rsa {
	//global variables
	public static BigInteger modexp(BigInteger x, int a, BigInteger n)
	{
		BigInteger result = x;

		double doubleFloor = Math.floor(Math.log(a)/Math.log(2));
		int upperBound = (int) doubleFloor;
		//calculates floor(log base 2 of a)
		for(int i = 0; i < upperBound; i++ )
		{
			result = result.multiply(result); //square it!
			result = result.mod(n);		
		}
		double doubleExp = Math.pow(2.0,doubleFloor);
		int exp = (int) doubleExp;
		for(int i = 0; i < (a - exp); i++)
		{
			result = result.multiply(x);
			result = result.mod(n);	
		}

		return result;
	}

	public static BigInteger modexp(BigInteger x, BigInteger a, BigInteger n)
	{
		BigInteger result = BigInteger.ONE;
		int upperBound = a.bitLength();
		BigInteger power = x;
		BigInteger exponent = BigInteger.ZERO;
		BigInteger two = new BigInteger("2");
		BigInteger[] arr = new BigInteger[upperBound];
		for(int i = 0; i < upperBound; i++)	{ arr[i] = BigInteger.ONE; } 

		//check the bit set at each binary decimal
		for(int i = 1; i < upperBound; i++)
		{
			power = power.pow(2).mod(n); //calculate x := x^2
			if(a.testBit(i))
			{
			//if the bit is set in the exponent, a, save the value of x^index (mod n)
				arr[i] = power.mod(n);
				exponent = exponent.add(two.pow(i)); 
			}
		}
	
		for(int i = 1; i < upperBound; i++)
		{
			result = result.multiply(arr[i]);
			//multiply the x^index (mod n) to get x^(a) (mod n)
		}
		result = result.mod(n);

		if (a.testBit(0))
		{
			result = result.multiply(x).mod(n);
			exponent = exponent.add(BigInteger.ONE);
		}	
		//System.out.println("Bit Length of exponent: " + upperBound);
		//System.out.println("Exponent value: " + exponent);
		return result;
	}	
	
	public static void encrypt()
	{

	}		

	public static void decrypt()
	{
		
	}

	public static void keySetup()
	{
		
	}

	public static BigInteger generatePrime()
	{
		//on average we should expect to select less than 300 random nums before finding
		// a suitable prime in the range of 10^100 and 10^150
		//test primality
		BigInteger randomInt = BigInteger.ZERO;
		boolean found = false;
		System.out.println("Finding Prime:");
		int iterations = 0;
		while(!found)
		{
			iterations = iterations+1;
			randomInt = getRandom();
			BigInteger a = getRandom(BigInteger.ZERO, randomInt.subtract(BigInteger.ONE));
			BigInteger randMinus = randomInt.subtract(BigInteger.ONE);
			BigInteger result = modexp(a,randMinus,randomInt);
			if (result.compareTo(BigInteger.ONE) == 0) 
			{
				found = true;
				System.out.println("Found in: " + iterations + " iterations.");
			}
		}
		return randomInt;
	}

	public static BigInteger getRandom() 
	{
		BigInteger ten = new BigInteger("10");
		BigInteger upperRange = ten.pow(150);
		BigInteger lowerRange = ten.pow(100);
		BigInteger difference = upperRange.subtract(lowerRange);
		int numBits = upperRange.bitLength(); 

		Random randomNumber = new Random();
		BigInteger RANDOM = new BigInteger(numBits, randomNumber);

		if(RANDOM.compareTo(lowerRange) < 0)
			RANDOM = RANDOM.add(lowerRange);
		if(RANDOM.compareTo(upperRange) >= 0)
			RANDOM = RANDOM.mod(difference).add(lowerRange);
		
		return RANDOM;
	}

	public static BigInteger getRandom(BigInteger lowerRange, BigInteger upperRange) 
	{
		BigInteger difference = upperRange.subtract(lowerRange);
		int numBits = upperRange.bitLength(); 

		Random randomNumber = new Random();
		BigInteger RANDOM = new BigInteger(numBits, randomNumber);

		if(RANDOM.compareTo(lowerRange) < 0)
			RANDOM = RANDOM.add(lowerRange);
		if(RANDOM.compareTo(upperRange) >= 0)
			RANDOM = RANDOM.mod(difference).add(lowerRange);
		
		return RANDOM;
	}
	
	public static BigInteger euclid(BigInteger a, BigInteger b)
	{
		BigInteger zero = BigInteger.ZERO;
		BigInteger one = BigInteger.ONE;
		BigInteger result = zero;
		//constants set up

		BigInteger r1 = a, r2 = b, r3 = one;
		BigInteger q = zero;
		BigInteger x1 = one , x2 = zero, x3 = zero;
		BigInteger y1 = zero, y2 = one, y3 = zero;

		if (r1.compareTo(r2) == 0)
		{
			return r1; //they are equal
		}
		else if(r1.compareTo(r2) < 0)
		{
			r1 = b;	//swap, b is bigger initially.
			r2 = a;
		}

		BigInteger temp = one;	//used as a storage var for calculations
		while(r3.compareTo(BigInteger.ZERO) != 0)
		{
			r3 = r1.subtract(q.multiply(r2));
			x3 = x1.subtract(q.multiply(x2));
			y3 = y1.subtract(q.multiply(y2));
			//calculate next iteration. Shift index down.
			if(r3.compareTo(BigInteger.ZERO) > 0)
			{
				BigInteger[] answers = r2.divideAndRemainder(r3);
				q = answers[0];	//quotient a.k.a floor of the division in double mode
			}
			x1 = x2;
			x2 = x3;
			y1 = y2;
			y2 = y3;
			r1 = r2;
			r2 = r3;
			if(a.compareTo(b) > 0)
			{ 
				result = x1;
				if (result.compareTo(BigInteger.ZERO) < 0)
					result = result.add(a);
			}
			else
			{
				result = y1;
				if (result.compareTo(BigInteger.ZERO) < 0)
					result = result.add(b);
			}
		}
		return result;
	}

	public static void main(String args[]) throws IOException 
	{	/*
		BigInteger a = new BigInteger(args[0]);
		BigInteger b = new BigInteger(args[1]);
		BigInteger c = new BigInteger(args[2]);
		BigInteger officialresult = a.modPow(b,c);
		BigInteger myResult = modexp(a,b,c);
		System.out.println("My function: " + myResult);
		System.out.println("modPow: " + officialresult);
		
		System.out.println("Random BigInteger of 100-150 digits: ");
		System.out.println( getRandom() );
		
		BigInteger largePrime = generatePrime();
		System.out.println("Large Prime: " + largePrime);
		
		BigInteger a = new BigInteger(args[0]);
		BigInteger b = new BigInteger(args[1]);
		BigInteger test = euclid(a,b);

		System.out.println(a +"^-1 (mod " + b + " ) = " + test);
		*/

		

 
	}//End main
}//End JS class
