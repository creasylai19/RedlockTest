package com.creasy.dao;

import com.creasy.pojo.Loan;

/**
 * @author laicreasy
 */
public interface ILoanDao {

    Loan queryLoanById(int id);

    int updateLoan(Loan loan);

    Loan queryLoanWithoutDeal();

}
