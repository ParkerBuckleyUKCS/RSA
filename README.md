# RSA
 A Practice in Implementing the RSA algoritm in Java 8
 
 
## Alice wants to send an encrypted message to Bob
![Alice wants to send Encrypted text to Bob](https://github.com/ParkerBuckleyUKCS/RSA/blob/master/rsaExplanation.JPG)

### Security of the algorithm
 The security of RSA lies in the computational difficulty of factorization of primes. In the example above, we are dealing
 with primes of 100 decimal digits or larger. For this reason, given primes p and q, calculating the factorization of (p-1)(q-1)
 is very difficult to brute force. By the Fundamental theorem of arithmetic, we know that each integer greater than 1 has a unqique prime factorization. When these prime factors are not small, certain factorization methods like Fermat's and Pollard's become inaccurate and increasingly tasking.
 
 If you cannot factor n into p and q, you cannot invert the public key, e and therefore cannot decrypt.
