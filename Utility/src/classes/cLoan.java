package classes;


public class cLoan {


    public enum loanStatus {
        NEWLOAN {
            @Override
            public String toString() {
                return "New loan";
            }
        },
        PENDING {
            @Override
            public String toString() {
                return "Pending";
            }
        },
        ACTIVE {
            @Override
            public String toString() {
                return "Active";
            }
        },
        FINISHED {
            @Override
            public String toString() {
                return "Finished";
            }
        },
        RISK {
            @Override
            public String toString() {
                return "In risk";
            }
        },
    }

    private String id;
    private String category;
    private String ownerName;
    private int capital;
    private int totalMonths;
    private int monthsPerPayment;
    private double interestRate;

    private int moneyInvestedSoFar;
    private int moneyNeeded;
    private int totalPayments;
    private double totalInterest;
    private double monthlyCapital;
    private double monthlyInterest;
    private double payment;
    private int dateOfActivation = -1;
    private int dateFinished = -1;
    private double debt;
    private double interestPaid;
    private double capitalPaid;
    private double capitalRemaining = 0;
    private double interestRemaining = 0;
    private int nextPaymentOn = -1;

    private loanStatus status;

    public cLoan(Loan loan)
    {
        ownerName = loan.getOwnerName();
        this.id = loan.getId();
        this.category = loan.getCategory();
        this.capital = loan.getCapital();
        totalMonths = loan.getTotalMonths();
        monthsPerPayment = loan.getMonthsPerPayment();
        this.interestRate = loan.getInterestRate();
        capitalRemaining = loan.getCapitalRemaining();
        capitalPaid = loan.getCapitalPaid();
        switchStatus(loan.getStatusInt());

        moneyNeeded = capital;
        totalPayments = totalMonths / monthsPerPayment;
        totalInterest = capital * (interestRate / 100);
        monthlyCapital = capital / totalPayments;
        monthlyInterest = totalInterest / totalPayments;
        payment = monthlyInterest + monthlyCapital;
        debt = totalInterest + capital;
    }

    public double getCapitalRemaining() {
        return capitalRemaining;
    }

    public void switchStatus(int s){
        switch(s)
        {
            case 0:
            {
                status = loanStatus.NEWLOAN;
                break;
            }
            case 1:
            {
                status = loanStatus.PENDING;
                break;
            }
            case 2:
            {
                status = loanStatus.ACTIVE;
                break;
            }
            case 3:
            {
                status = loanStatus.FINISHED;
                break;
            }
            case 4:
            {
                status = loanStatus.RISK;
                break;
            }
            default:
            {
                status = loanStatus.NEWLOAN;
                break;
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCategory() {
        return category;
    }

    public int getCapital() {
        return capital;
    }

    public int getTotalMonths() {
        return totalMonths;
    }

    public int getMonthsPerPayment() {
        return monthsPerPayment;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getMoneyNeeded() {
        return moneyNeeded;
    }

    public int getTotalPayments() {
        return totalPayments;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public int getMoneyInvestedSoFar() {
        return moneyInvestedSoFar;
    }

    public double getMonthlyCapital() {
        return monthlyCapital;
    }

    public double getMonthlyInterest() {
        return monthlyInterest;
    }

    public double getPayment() {
        return payment;
    }

    public int getStatusInt() {
        switch(this.status)
        {
            case NEWLOAN:
            {
                return 0;
            }
            case PENDING:
            {
                return 1;
            }
            case ACTIVE:
            {
                return 2;
            }
            case FINISHED:
            {
                return 3;
            }
            default:
            {
                return -1;
            }
        }
    }
    public loanStatus getStatus(){
        return this.status;
    }


    public void setDateOfActivation(int date) {
        this.dateOfActivation = date;
    }

    public int getDateOfActivation()
    {
        return dateOfActivation;
    }

//    public int getNextPayment(int currDate)
//    {
//        return (currDate - (currDate%monthsPerPayment) + monthsPerPayment + dateOfActivation%monthsPerPayment);
//    }

    public int getDateFinished() {
        return dateFinished;
    }

    public int getNextPaymentOn() {
        return nextPaymentOn;
    }

    public void setStatus(loanStatus status) {
        this.status = status;
    }

    public double getDebt() {
        return debt;
    }
}
