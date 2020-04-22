# RSA
 A Practice in Implementing the RSA algoritm in Java 8
 
 
## Alice wants to send an encrypted message to Bob
![Alice wants to send Encrypted text to Bob](https://github.com/ParkerBuckleyUKCS/RSA/blob/master/rsaExplanation.JPG)

### Security of the algorithm
 The security of RSA lies in the computational difficulty of factorization of primes. In the example above, we are dealing
 with primes of 100 decimal digits or larger. For this reason, given primes p and q, calculating the factorization of (p-1)(q-1)
is very difficult to brute force. By the Fundamental theorem of arithmetic, we know that each integer greater than 1 has a unique prime factorization. When these prime factors are not small, certain factorization methods like Fermat's and Pollard's become inaccurate and increasingly tasking.
 
 If you cannot factor n into p and q, you cannot invert the public key, e and therefore cannot decrypt.
 
 ## Purpose and Usage
 
 Purpose:
	The objective of this project is to implement components of the RSA cryptosystem. 
	keySetup(): 
		This module will compute and output the keys: public and private. The keys will be output to
		two separate files named public key and private key.
	encryp(): 
		This module will take the public key and a message to be encrypted as the inputs. They
		will be read from the files public key and message, respectively.
		The module will output the ciphertext (encrypted message).
		The ciphertext will be stored in a file named ciphertext.
	decrypt(): 
		This module will take the public key, the private key and the ciphertext to be decrypted as
		the inputs. They will be read from the files public key, private key and ciphertext.
 generatePrime():
  This module will take generate a random prime on the order of 10^150 by using RNG and Fermat's primality theorem.
