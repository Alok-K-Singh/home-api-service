package com.alok.spring.batch.service;

import com.alok.spring.batch.model.Expense;
import com.alok.spring.batch.model.IExpenseCategoryMonthSum;
import com.alok.spring.batch.model.IExpenseMonthSum;
import com.alok.spring.batch.repository.ExpenseRepository;
import com.alok.spring.batch.response.GetExpensesMonthSumResponse;
import com.alok.spring.batch.response.GetExpensesResponse;
import com.alok.spring.batch.response.GetExpensesMonthSumByCategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public GetExpensesResponse getAllExpenses() {

        List<Expense> expenses = expenseRepository.findAll();
        Collections.sort(expenses, (t1, t2) -> t2.getDate().compareTo(t1.getDate()));

        return GetExpensesResponse.builder()
                .expenses(expenses.stream()
                        .map(expense -> GetExpensesResponse.Expense.builder()
                                .id(expense.getId())
                                .date(expense.getDate())
                                .head(expense.getHead())
                                .amount(expense.getAmount())
                                .category(expense.getCategory())
                                .comment(expense.getComment())
                                .build())
                        .collect(Collectors.toList()))
                .count(expenses.size())
                .build();
    }

    public GetExpensesResponse getCurrentMonthExpenses() {

        List<Expense> expenses = expenseRepository.findAllForCurrentMonth();
        Collections.sort(expenses, (t1, t2) -> t2.getDate().compareTo(t1.getDate()));

        return GetExpensesResponse.builder()
                .expenses(expenses.stream()
                        .map(expense -> GetExpensesResponse.Expense.builder()
                                .id(expense.getId())
                                .date(expense.getDate())
                                .head(expense.getHead())
                                .amount(expense.getAmount())
                                .category(expense.getCategory())
                                .comment(expense.getComment())
                                .build())
                        .collect(Collectors.toList()))
                .count(expenses.size())
                .build();
    }

    public GetExpensesMonthSumByCategoryResponse getMonthWiseExpenseCategorySum() {

        List<IExpenseCategoryMonthSum> expenseCategorySums = expenseRepository.findCategorySumGroupByMonth();

        return GetExpensesMonthSumByCategoryResponse.builder()
                .expenseCategorySums(expenseCategorySums)
                .count(expenseCategorySums.size())
                .build();
    }

    public GetExpensesMonthSumResponse getMonthWiseExpenseSum() {

        List<IExpenseMonthSum> expenseSums = expenseRepository.findSumGroupByMonth();

        return GetExpensesMonthSumResponse.builder()
                .expenseCategorySums(expenseSums)
                .count(expenseSums.size())
                .build();
    }
}
