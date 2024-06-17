package com.loan.loan;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/loan")
public class Controller {

    @GetMapping("/calculateApprox")
    public double calculateApprox(@RequestParam double loanAmount, @RequestParam double interestRate,
                                  @RequestParam int term, @RequestParam double rv) {
        return ((loanAmount + rv) / 2 * interestRate / 12 * term + (loanAmount - rv)) / term;
    }

    @PostMapping("/saveQuote")
    public double saveQuote(@RequestParam double loanAmount, @RequestParam double interestRate,
                            @RequestParam int term, @RequestParam double rv, HttpSession session) {
        double monthlyInterestRate = interestRate / 12 / 100;
        double exactPayment = (loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -term));
        session.setAttribute("savedQuote", new LoanQuote(loanAmount, interestRate, term, rv, exactPayment));
        return exactPayment;
    }

    @GetMapping("/getQuote")
    public LoanQuote getQuote(HttpSession session) {
        return (LoanQuote) session.getAttribute("savedQuote");
    }

    static class LoanQuote {
        private double loanAmount;
        private double interestRate;
        private int term;
        private double rv;
        private double paymentAmount;

        public LoanQuote(double loanAmount, double interestRate, int term, double rv, double paymentAmount) {
            this.loanAmount = loanAmount;
            this.interestRate = interestRate;
            this.term = term;
            this.rv = rv;
            this.paymentAmount = paymentAmount;
        }

        // Getters and setters
    }
}