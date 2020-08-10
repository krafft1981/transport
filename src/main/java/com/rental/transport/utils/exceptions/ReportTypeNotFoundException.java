package com.rental.transport.utils.exceptions;

public class ReportTypeNotFoundException extends RuntimeException {

    public ReportTypeNotFoundException(String type) {

        super(String.format("Report with type %s not found", type));
    }
}
