package com.github.cashpath.exception;

public class OpportunityCardNotFoundException extends RuntimeException{
    public OpportunityCardNotFoundException(Long id){
        super("OpportunityCard not found with id " + id);
    }
}
