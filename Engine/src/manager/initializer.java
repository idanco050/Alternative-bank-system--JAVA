package manager;


import classes.*;
import resources.SchemaJAXB;
import resources.generated.generated.AbsDescriptor;
import resources.generated.generated.AbsLoan;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class initializer {

    static public List<Customer> customerList = new ArrayList<Customer>();
    static public List<Loan> loanList = new ArrayList<Loan>();
    static public Categories categories = new Categories();
    static public List<Loan> activeLoans = new ArrayList<>();
    static public List<Loan> loansForSale = new ArrayList<>();

    static private String adminName;
    static private boolean bAdminSet = false;



    public static void addCustomer(String name) {
        Customer cus = new Customer(name);
        customerList.add(cus);
    }


    public static void removeUser(Customer cus) {
        customerList.remove(cus);
    }

    public static List<Customer> getUsers() {
        return customerList;
    }         /////////////                         //////////////

    public static boolean isCustomer(String username) {
        return customerList.contains(nameToCustomer(username));
    }

    public static Customer nameToCustomer(String name){
        for (Customer c : customerList)
        {
            if (name.equals(c.getName()))
            {
                return c;
            }
        }
        return null;
    }

//    public static boolean isCustomer(String name)    {
//        for (Customer c : customerList)
//        {
//            if (name.equals(c.getName()))
//            {
//                return true;
//            }
//        }
//        return false;
//    }

    public static String loadXML(AbsDescriptor descriptor, String username)
    {
        Customer cus = nameToCustomer(username);
        List<AbsLoan> AbsLoanList = descriptor.getAbsLoans().getAbsLoan();
        List<String> categoryList = descriptor.getAbsCategories().getAbsCategory();
        List<Loan> tempList = new ArrayList<>();
        boolean unique;

        for (AbsLoan l : AbsLoanList)
        {
            Loan tempLoan = new Loan(l, username);
            tempList.add(tempLoan);
        }
        XMLerrorCheck checker = new XMLerrorCheck(tempList, categoryList);
        String errorText = checker.check();

        if (errorText.equals("True"))
        {
            for (Loan l : tempList)
            {
                unique = true;
                for (Loan engineLoan : initializer.loanList)
                {
                    if (l.getId().equals(engineLoan.getId())) {
                       unique = false;
                    }
                }
                if (unique)
                {
                    cus.getOwnedLoans().add(l);
                    loanList.add(l);
                    categories.getCategories().add(l.getCategory());
                }
            }
        }
        return errorText;
    }


    public static String loadXMLfromInputStream(InputStream inputStream, String username) throws JAXBException, IOException {
        String errorText = "False";
        if (inputStream != null) {
            AbsDescriptor descriptor = SchemaJAXB.deserializeFrom(inputStream);
            errorText = loadXML(descriptor, username);
        }
        return errorText;
    }
    public static String buyLoan(Loan loan, String username)
    {
        String errorText = "False";
        double remainingCapital = loan.getCapitalRemaining();
        Customer theCustomer = nameToCustomer(username);
        Customer seller = nameToCustomer(loan.getOwnerName());
        if (remainingCapital > theCustomer.getBalance())
            errorText = "Not enough money to buy this loan.";
        else
        {
            theCustomer.setBalance(theCustomer.getBalance() - remainingCapital);
            loan.setOwnerName(username);
            theCustomer.getOwnedLoans().add(loan);

            seller.getOwnedLoans().remove(loan);
            seller.setBalance(seller.getBalance() + remainingCapital);
        }
        return errorText;
    }

    public static String getAdminName() {
        return adminName;
    }

    public static boolean setAdmin(String name){
        if(!bAdminSet)
        {
            adminName = name;
            bAdminSet = true;
        }
        return bAdminSet;
    }
}
