package org.wittydev.math;

import org.wittydev.util.DataTools;

public class CombinatoricsUtil {

	/***
	 * Same as method  "Object[][] combinationsArrays (Object[] s, int k )".
	 * The only difference is that the first argument ("s") is automatically set to :
	 * new int {0,1,2,3, ..., n}  
	 * 
	 * @param n
	 * @param k
	 * @return
	 */
	public static final int[][] combinationsArrays (int n, int k ) {
		if ( k > n ) throw new IllegalArgumentException("combinations subset [k] should be less then the set [n]");

		byte[] posArr = new byte[n];
		for (int i=0; i<k; i++){
			posArr[i]=1;
		}		
		int[][] arr = new int[(int)combinationsNumber(n, k)][];
		for ( int i=0; i<arr.length; i++ ){
			arr[i] = new int[k];
			int cnt=0; 
			for (int j=0; j<posArr.length; j++){
				if (posArr[j]==1){
					arr[i][cnt++]=j;
				}
			}
			posArr=nextCombinationsArray(posArr);
		}
		return arr;
	}
	
	/**
	 * Given S, the set of all possible unique elements, this method returns all the possible combinations
	 * of k elements.
	 * A combination is an un-ordered collection of unique sizes
	 * rif: http://en.wikipedia.org/wiki/Combination
	 *   
	 * For example,  having a set of 4 elements {A, B, C, D}. Setting k to 3   would return:
	 * {{A,B,C},{A,B,D},{A,C,D},{B,C,D}}
	 *     
	 * @param s	Is the set of all possible and unique elements
	 * @param k Is the size of the subsets  
	 * @return returns an array of all the possible combinations.
	 */

	public static final Object[][] combinationsArrays (Object[] s, int k ) {
		int n = s.length;
		if ( k > n ) throw new IllegalArgumentException("combinations subset [k] should be less then the set [n]");
		
		byte[] posArr = new byte[n];
		for (int i=0; i<k; i++){
			posArr[i]=1;
		}		
		Object[][] arr = new Object[(int)combinationsNumber(n, k)][];
		for ( int i=0; i<arr.length; i++ ){
			arr[i] = new Object[k];
			int cnt=0; 
			for (int j=0; j<posArr.length; j++){
				if (posArr[j]==1){
					arr[i][cnt++]=s[j];
				}
			}
			posArr=nextCombinationsArray(posArr);
		}
		return arr;
	}
	
	
	public static byte[] nextCombinationsArray(byte[] posArr){
		for ( int i=posArr.length-1; i>=0; i--){
			if (posArr[i]==1){
				if (i<posArr.length-1 && posArr[i+1]==0){
					posArr[i]=0;
					posArr[i+1]=1;
					//System.out.println("===============>"+DataTools.arrayToString(posArr));
					return posArr;
				}else{
					
					for (int j=i-1; j>=0; j--){
						//System.out.println("OP===============>"+DataTools.arrayToString(posArr)+"==>"+(i)+"==>"+(j));
						if (posArr[j]==1 && posArr[j+1]==0){
							posArr[j]=0;
							posArr[j+1]=1;
							//System.out.println("==>"+DataTools.arrayToString(posArr));
							int cnt=0;
							for (int k=j+2; k<posArr.length;k++){
								if (posArr[k]==1){
									posArr[k]=0;
									posArr[j+2+cnt++]=1;
								}
							}
							return posArr;
						}
					}
					
					
				}
					
			}
		}
		return posArr;
	}
	
	/***
	 * Given S a set of n unique elements. This method calculates the number of possible combinations of fixed size k.
	 * The method simply implement the following formula
	 * 		<combinations[n,k]> = n! / (k!*(n-k)!)
	 * 
	 * rif. http://en.wikipedia.org/wiki/Combination
	 * 
	 * @param n	is the number of unique elements in a given set 
	 * @param k is the fixed size of the subsets we are considering   
	 * @return the number of possible subsets
	 */	
	public static  long combinationsNumber (int n, int k ) {
		return MiscMathUtil.factorial( n ) / (MiscMathUtil.factorial (k)*MiscMathUtil.factorial (n-k));
	}
	
	

	public static void main(String[] args) {
		System.out.println(combinationsNumber(10, 3));
		//System.out.println( "==>"+DataTools.arrayToString(combinationsArrays(4, 3)) );
		
		String[] objs={"A", "B", "C", "D"};
		System.out.println( "==>"+DataTools.arrayToString(combinationsArrays(objs, 3)) );
		
		
		
		byte[] arr={1,1,1,1,0,0,0,0,0};
		System.out.println(combinationsNumber(arr.length, 4));
		System.out.println(DataTools.arrayToString(arr));
		System.out.println(DataTools.arrayToString(nextCombinationsArray(arr)));
		
		for (int i =0; i<combinationsNumber(arr.length, 4); i++){
			arr=nextCombinationsArray(arr);
			System.out.println(DataTools.arrayToString(arr));
		}
		
		
		
	}
	
}
