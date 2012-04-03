package bench.colls;

public abstract class PerfBase<C> {
	
	String name;

	public PerfBase(String name) {
		this.name = name;
	}

	// Override this Template Method for different tests.
	// Returns actual number of repetitions of test.
	abstract int test(C container, PerfParam tp);

}