package problem_1;

public class agent {
	public agent() {
		
	}
	
	private String month_array[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December",};
	//Prints out the string name for the month corresponding to the int
	public String getMonth (int month) {
		return month_array[month-1];
	}
	
	//Prints out a list of randomly generated months, e.g., 4. April, 2. February..., months can repeat
	public void printRandomList(int length) {
		for (int i = 0; i < length ; i ++) {
			int intrandomInt1 = (int)(Math.random()*12)+1;
			System.out.println( intrandomInt1 + "." +  month_array[intrandomInt1-1]) ;}
	}
	
	//prints all the months in order
	public void printMonths() {
		for (int i = 0; i < 12; i ++) {
			System.out.println((i+1) + "." + month_array[i]);
		}
	}
	
	 //make sure it checks start and end  
	public void printRangeOfMonths(int start, int end) {
		for (int i = start; i < end+1;i++ ) {
			System.out.println((i) + "." + month_array[i-1]);
		}
	} 
	
	// Prints the number of the month
	public void printMonthNumber(String monthName) {	
		  int month_number;
		  int len = month_array.length;
	      int i = 0;
	 
	      while (i < len) {
	            if (month_array[i] == monthName) {
	                 month_number = i;
	                 System.out.println(month_number+1);
	                 i++;
	            }
	            else {
	                i = i + 1;
	            }
	            
	        }}
	
	public void print_month_days(int date) {
		int days_array[] = {31,28,31,30,31,30,31,31,30,31,30,31};
		int j = 0;
		for (int i:days_array) {
			date -= i;
			if (date< 0) {
				date += i;
				System.out.println(month_array[j]+","+(date));
				break;
			}else {
				j++;
			}
		}
	}
	
} 

