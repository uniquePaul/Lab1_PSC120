package problem_1;

public class environment {
	public static void main(String[] args) {
		agent a = new agent();
		
		System.out.print("\n//*****test part for getMonth*****// \n");
		System.out.println(a.getMonth(3)); //print -->3. March
		
		System.out.print("\n//*****test part for printMonths*****// \n");
		a.printMonths();
		
		System.out.print("\n//*****test part for printRandomList*****// \n");
		a.printRandomList(10);
		
		System.out.print("\n //*****test part for printMonthNummber*****// \n");
		a.printMonthNumber("July");
		
		System.out.print("\n //*****test part for printRangeOfMonths*****// \n");
		a.printRangeOfMonths(2,7);
		
		System.out.print("\n //*****test part for prob2*****// \n");
		a.print_month_days(100);
		
		
	}

}
