package Person.BotTypes;
/**
 *
 */
public final class Combinatorics
{
    private Combinatorics() {
    }

    public static int factorial(int n) {
	int result = 1;
	for (int i = 2; i <= n; i++) {
	    result *= i;
	}
	return result;
    }

    public static double choose(int n, int k) {
	if (k < n - k) k = n - k;
	double result;
	double numerator = 1;
	for (int i = n; i > k; i--) {
	    numerator *= i;
	}
	result = numerator / factorial(n - k);
	return result;
    }
}
