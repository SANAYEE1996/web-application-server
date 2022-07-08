package unitTest;


import java.util.UUID;

import org.junit.Test;

public class UUIDGenerateTest {

	private UUID u;
	
	
	@Test
	public void test() {
		u = UUID.randomUUID();
		System.out.println("first generate : " +u);
		u = UUID.randomUUID();
		System.out.println("second generate : " +u);
		u = UUID.randomUUID();
		System.out.println("third generate : " +u);
	}

}
