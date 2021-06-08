package com.gavin.myapp.web.rest.errors;

import java.net.URI;
import org.zalando.problem.AbstractThrowableProblem;

public class CommonException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public CommonException(String code, String msg) {
        super(URI.create(ErrorConstants.PROBLEM_BASE_URL + "/" + code), msg);
    }
}
