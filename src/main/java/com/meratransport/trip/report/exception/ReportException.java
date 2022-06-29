package com.meratransport.trip.report.exception;

import lombok.Data;


public class ReportException extends RuntimeException{
    public ReportException(String message){
        super(message);
    }
}
