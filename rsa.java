/*
Author: Parker Buckley
Date Modified: 4/21/2020
Purpose:
	The objective of this project is to implement components of the RSA cryptosystem. 
	Key setup: 
		This module will compute and output the keys: public and private. The keys will be output to
		two separate files named public key and private key.
	Encryption: 
		This module will take the public key and a message to be encrypted as the inputs. They
		will be read from the files public key and message, respectively.
		The module will output the ciphertext (encrypted message).
		The ciphertext will be stored in a file named ciphertext.
	Decryption: 
		This module will take the public key, the private key and the ciphertext to be decrypted as
		the inputs. They will be read from the files public key, private key and ciphertext.
*/

//preprocessor directive
import java.io.*;
import java.util.*;
import java.math.BigInteger;
import java.lang.*;

public class rsa {
	//global variables

	//calculates x^a (mod n) where a is a standard int. complexity: O(log_{2} a + n) 
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

	//calculates result = x^a (mod n) Complexity: O(log_{2}(a))
	public static BigInteger modexp(BigInteger x, BigInteger a, BigInteger n)
	{
		//=============INITIALIZE=========================
		BigInteger result = BigInteger.ONE; 	//result storage
		int upperBound = a.bitLength();		//calculates the 2's compliment length of a (AKA BINARY)
		BigInteger power = x; 			//used to calculate individual powers of x^(i) for final computation
		BigInteger exponent = BigInteger.ZERO; 	//Used to test the exponent value
		BigInteger two = new BigInteger("2"); 	//CONSTANT
		BigInteger[] arr = new BigInteger[upperBound];	//Stores binary powers of x^(i)
		for(int i = 0; i < upperBound; i++)	{ arr[i] = BigInteger.ONE; } //INITIALIZE
		//=================================================

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
			//multiply all of the binary powers to get x^(a) (mod n)
		}
		result = result.mod(n);

		if (a.testBit(0))	//checks if the LSB of the exponent is set.
		{
			//If LSB is set, multiply one more time!
			result = result.multiply(x).mod(n);
			exponent = exponent.add(BigInteger.ONE);
		}	
		return result;
	}	
	
	public static void encrypt()
	{	
		//initialize variables	
		BigInteger public_key = BigInteger.ONE;
		BigInteger message = BigInteger.ZERO;
		BigInteger ciphertext = BigInteger.ZERO;
		BigInteger e = new BigInteger("65537");
		
		//COMPUTATION OF MOD EXPONENTIATION
		String messageString = readUsingScanner("message");
		String publicString = readUsingScanner("public_key");
		public_key = new BigInteger(publicString);
		message = new BigInteger(messageString);

		ciphertext = modexp(message,e,public_key);
		writeUsingFileWriter(ciphertext.toString(),"ciphertext");   
	}		

	public static void decrypt()
	{	
		//INIT IO OBJECTS
		BigInteger public_key = BigInteger.ONE;
		BigInteger private_key = BigInteger.ONE;
		BigInteger message = BigInteger.ZERO;
		BigInteger ciphertext = BigInteger.ZERO;

		//FILE IO
		String cipherString = readUsingScanner("ciphertext");
		String publicString = readUsingScanner("public_key");
		String privateString = readUsingScanner("private_key");
		//TYPE CASTING
		ciphertext = new BigInteger(cipherString);
		public_key = new BigInteger(publicString);
		private_key = new BigInteger(privateString);

		message = modexp(ciphertext,private_key,public_key);

		writeUsingFileWriter(message.toString(),"decrypted_message");	
	}

	public static void keySetup()
	{
		BigInteger d,n,p,q,diff = BigInteger.TEN.pow(95);
		BigInteger e = new BigInteger("65537");
		System.out.println("Getting prime...");
		p = generatePrime();
		System.out.println("Getting prime...");
		q = generatePrime();
			
		while(p.subtract(q).abs().compareTo(diff) < 0)
		{
			q = generatePrime();
			System.out.println("Ensuring primes are 10^95 apart");
		}		
		n = p.multiply(q);	// n = pq	PUBLIC KEY

		BigInteger inversionMod;	//(p-1)(q-1)
		inversionMod = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		System.out.println("Calculating e inverse (private key)");
		d = euclid(e,inversionMod);
		System.out.println("Writing Results to files");
		
		writeUsingFileWriter(n.toString(),"public_key");
		writeUsingFileWriter(d.toString(),"private_key");
		
		System.out.println("Done...");
	}

	//by default generates a prime on the order of 10^100 - 10^150
	public static BigInteger generatePrime()
	{
		/*
		On average we should expect to select less than 300 random nums before finding
		a suitable prime in the range of 10^100 and 10^150
		test primality
		*/
		BigInteger randomInt = BigInteger.ZERO;
		boolean found = false;
		System.out.println("Finding Prime:");
		int iterations = 0;
		while(!found)
		{
			iterations = iterations+1;
			//calculate a random integer to test for primality.
			randomInt = getRandom();
			/*calculate a random integer to mod. By Fermat's theorem,
				For ANY a in {1,2,...,a-2},
				a^(n-1) = 1 (mod n)

				The n is a prime!!
			*/
			BigInteger a = getRandom(BigInteger.ZERO, randomInt.subtract(BigInteger.ONE));
			BigInteger randMinus = randomInt.subtract(BigInteger.ONE);
			BigInteger result = modexp(a,randMinus,randomInt);
			if (result.compareTo(BigInteger.ONE) == 0) 
			{
				found = true;
				//System.out.println("Found in: " + iterations + " iterations.");
			}
		}//end while
		return randomInt;
	}//end generatePrime()

	private static void writeUsingFileWriter(String data, String fileName) 
	{
        	File file = new File(fileName);
        	FileWriter fr = null;
        	try 
		{
            		fr = new FileWriter(file);
            		fr.write(data);
        	}
	 	catch (IOException e) 
		{
            		e.printStackTrace();
        	}
		finally
		{
            		//close resources
            		try {
                		fr.close();
            		} catch (IOException e) {
                		e.printStackTrace();
            		}
        	}
    	}

	private static String readUsingScanner(String fileName) 
	{
		String result = "";
        	File file = new File(fileName);
        	try 
		{	
        	Scanner scan = new Scanner(file);
			if (scan.hasNextLine())
			{
            			result = scan.nextLine();
			}
			else System.out.println("Error reading from " + fileName );
        	}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	return result;
    	}		

	// returns a random BigInteger on the order of 10^100 - 10^150
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
	}//end getRandom()

	// returns a random BigInteger in a given range
	public static BigInteger getRandom(BigInteger lowerRange, BigInteger upperRange) 
	{
		BigInteger difference = upperRange.subtract(lowerRange);
		int numBits = upperRange.bitLength(); //2's compliment length of upper range

		Random randomNumber = new Random(); //generate random bits.
		//store the random bits in a BigInteger of 2's compliment length numBits.
		BigInteger RANDOM = new BigInteger(numBits, randomNumber);

		/*
		if random is generate above or below the range specified:
		Add or subtract the lower range to ensure bounds are satisfied.
		*/
		if(RANDOM.compareTo(lowerRange) < 0)
			RANDOM = RANDOM.add(lowerRange);
		if(RANDOM.compareTo(upperRange) >= 0)
			RANDOM = RANDOM.mod(difference).add(lowerRange);
		
		return RANDOM;
	}//end getRandom()
	
	//Extended euclidean algorithm calculating the GCD(a,b) and returning a^{-1} (mod b)
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
			//shift all of the indicies for the next iteration
			x1 = x2;
			x2 = x3;
			y1 = y2;
			y2 = y3;
			r1 = r2;
			r2 = r3;
			if(a.compareTo(b) > 0)
			{ 
				result = x1; // chooses the correct inverse
				if (result.compareTo(BigInteger.ZERO) < 0)//if negative
					result = result.add(a); //ensures positivity mod a
			}
			else
			{
				result = y1; // chooses the correct inverse
				if (result.compareTo(BigInteger.ZERO) < 0)//if negative
					result = result.add(b); //ensures positivity mod b
			}
		}//end while
		return result;
	}//end euclid

	public static void main(String args[]) throws IOException 
	{	
		Scanner in = new Scanner(System.in);
		boolean quit = false;
		keySetup();
			
		while(!quit)
		{
			// Key Setup
			System.out.println("Setting up keys... Please wait.");
			//process user input...
			System.out.println("Enter a command: d to decrypt, e to encrypt, q to quit");
			String userInput = in.nextLine();

			while(
			userInput.compareTo("e") == 0 &&
			userInput.compareTo("q") == 0 &&
			userInput.compareTo("d") == 0 )
			{
				System.out.println("Enter a valid command: d,e,q : ");
				userInput = in.nextLine();
			}
			// Done processing user input...
			
			if(userInput.compareTo("d") == 0)//decrypt
			{
				decrypt();
			}
			else if(userInput.compareTo("e") == 0)//encrypt
			{
				encrypt();
			}
			else if(userInput.compareTo("q") == 0)
				quit = true;
			else
			{
				System.out.println("no input");
			}
		}				
	}//End main
}//End JS class
