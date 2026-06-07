package com.javastudy.stringdatetime;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Demonstrates random number generation in Java.
 * <p>
 * Key points:
 * <ul>
 *   <li>java.util.Random - pseudorandom, not thread-safe, uses CAS internally</li>
 *   <li>ThreadLocalRandom - per-thread instance, no contention, preferred in multithreaded code</li>
 *   <li>SecureRandom - cryptographically secure, slower, use for security-sensitive values</li>
 *   <li>All three support nextInt(), nextDouble(), nextBoolean(), etc.</li>
 *   <li>ThreadLocalRandom.current() - get the thread-local instance</li>
 *   <li>Random is reproducible with a seed; SecureRandom is not</li>
 * </ul>
 */
public class RandomDemo {

    /**
     * Basic Random usage: create and generate integers.
     */
    public static int randomInt(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }

    /**
     * Random with a fixed seed for reproducibility.
     */
    public static int seededRandomInt(long seed, int bound) {
        Random random = new Random(seed);
        return random.nextInt(bound);
    }

    /**
     * Random: generate a double between 0.0 (inclusive) and 1.0 (exclusive).
     */
    public static double randomDouble() {
        Random random = new Random();
        return random.nextDouble();
    }

    /**
     * Random: generate a boolean.
     */
    public static boolean randomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    /**
     * ThreadLocalRandom: the preferred way in concurrent code.
     * No contention between threads, faster than shared Random.
     */
    public static int threadLocalRandomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    /**
     * ThreadLocalRandom: generate int in a range [origin, bound).
     */
    public static int threadLocalRandomIntRange(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    /**
     * ThreadLocalRandom: generate double in a range.
     */
    public static double threadLocalRandomDoubleRange(double origin, double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    /**
     * SecureRandom: cryptographically secure random numbers.
     * Slower but suitable for tokens, keys, passwords.
     */
    public static int secureRandomInt(int bound) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(bound);
    }

    /**
     * SecureRandom: generate random bytes (e.g., for a salt or token).
     */
    public static byte[] secureRandomBytes(int numBytes) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[numBytes];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    /**
     * SecureRandom with a specific algorithm.
     */
    public static int secureRandomWithAlgorithm(String algorithm, int bound) throws Exception {
        SecureRandom secureRandom = SecureRandom.getInstance(algorithm);
        return secureRandom.nextInt(bound);
    }

    /**
     * Random with nextGaussian() for normally distributed values.
     */
    public static double gaussianRandom() {
        Random random = new Random();
        return random.nextGaussian();
    }

    /**
     * Generate N random ints and return them as an array.
     */
    public static int[] generateArray(int count, int bound) {
        return ThreadLocalRandom.current().ints(count, 0, bound).toArray();
    }
}
