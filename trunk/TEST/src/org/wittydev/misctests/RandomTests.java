package org.wittydev.misctests;
import java.util.Random;
import java.util.UUID;
import java.security.SecureRandom;

public class RandomTests {
	public static void main(String[] args) {
		long t1, t2;
		long n;
		
		t1=System.currentTimeMillis();
		Random rnd1=new Random(t1);
		t2=System.currentTimeMillis();
		System.out.println("Random 1 created in ms. "+(t2-t1));

		t1=System.currentTimeMillis();
		Random rnd2=new Random(t1);
		t2=System.currentTimeMillis();
		System.out.println("Random 2 created in ms. "+(t2-t1));

		t1=System.currentTimeMillis();
		SecureRandom srnd1=new SecureRandom((t1+"").getBytes());
		t2=System.currentTimeMillis();
		System.out.println("SecureRandom 1 created in ms. "+(t2-t1));
		
		t1=System.currentTimeMillis();
		SecureRandom srnd2=new SecureRandom((t1+"").getBytes());
		t2=System.currentTimeMillis();
		System.out.println("SecureRandom 2 created in ms. "+(t2-t1));

		
		t1=System.currentTimeMillis();
		n=rnd1.nextLong();
		t2=System.currentTimeMillis();
		System.out.println("Random number 1 ["+n+"] generated in ms. "+(t2-t1));

		t1=System.currentTimeMillis();
		n=rnd2.nextLong();
		t2=System.currentTimeMillis();
		System.out.println("Random number 2 ["+n+"] generated in ms. "+(t2-t1));
		
		
		t1=System.currentTimeMillis();
		n=srnd1.nextLong();
		t2=System.currentTimeMillis();
		System.out.println("SecureRandom number 1 ["+n+"] generated in ms. "+(t2-t1));

		t1=System.currentTimeMillis();
		n=srnd2.nextLong();
		t2=System.currentTimeMillis();
		System.out.println("SecureRandom number 2 ["+n+"] generated in ms. "+(t2-t1));
		
		
		t1=System.currentTimeMillis();
		UUID idOne = UUID.randomUUID();
		t2=System.currentTimeMillis();
		System.out.println("UUID 1 ["+idOne+"] generated in ms. "+(t2-t1));

		t1=System.currentTimeMillis();
		UUID idTwo = UUID.randomUUID();
		t2=System.currentTimeMillis();
		System.out.println("UUID 2 ["+idTwo+"] generated in ms. "+(t2-t1));
		System.out.println(idTwo.variant());
		System.out.println(idTwo.getMostSignificantBits());

		
		
		
		
	}
}
