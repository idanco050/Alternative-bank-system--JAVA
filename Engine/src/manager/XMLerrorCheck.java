package manager;

import classes.Loan;
import classes.Customer;

import java.util.ArrayList;
import java.util.List;


public class XMLerrorCheck {


     private List<Loan> loanList = new ArrayList<>();
     private List<Customer> customerList = new ArrayList<>();
     private List<String> categoryList = new ArrayList<>();
     private String trueString = "True";


    public XMLerrorCheck(List<Loan> loanList, List<String> categoryList){
        this.loanList = loanList;
        this.categoryList = categoryList;
    }

    public String check(){

        String categoryCheckStr;
        String rateCheckStr;

        for (Loan l : loanList)
        {
//            customerCheckStr = customerCheck(l);
//            if(!(customerCheck(l)==trueString))
//                return customerCheckStr;
            categoryCheckStr = categoryCheck(l);
            if(!(categoryCheck(l)==trueString))
                return categoryCheckStr;
            rateCheckStr = rateCheck(l);
            if(!(rateCheck(l)==trueString))
                return rateCheckStr;
        }
        return (doubleCheck());
    }


//    private String customerCheck(Loan loan){
//        for(Customer c : customerList)
//        {
//            String name = c.getName();
//            if (loan.getOwnerName().equals(name))
//            {
//                return trueString;
//            }
//        }
//       return ("There is not such customer as : " + loan.getOwnerName());
//    }

    private String categoryCheck(Loan loan){
        for (String str : categoryList)
        {
            if (loan.getCategory().equals(str))
            {
                return trueString;
            }
        }
        return "There is not such a category in the system : " + loan.getCategory();
    }

    private String rateCheck(Loan loan) {
        if(loan.getTotalMonths()%loan.getMonthsPerPayment() == 0)
            return trueString;
        else
            return ("The rate time does not fit: " + loan.getTotalMonths() + " is not divisible by " + loan.getMonthsPerPayment());
    }

    private String doubleCheck(){
        for (int i =0; i < customerList.size(); i++)
        {
            for (int j = i+1; j < customerList.size(); j++)
            {
                if(customerList.get(i).getName().equals(customerList.get(j).getName()))
                {
                    return ("There are two customers with the name : " + customerList.get(i).getName() );
                }
            }
        }
        return trueString;
    }
}
