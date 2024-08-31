package com.tutorial.springboot.security_rbac_jwt.test_utils.stub;

import java.util.List;
import java.util.stream.Stream;

public class StubHelper<ELEMENT> {

    private final ELEMENT[] elements;

    @SafeVarargs
    public StubHelper(ELEMENT... elements) {
        this.elements = elements;
    }

    public ELEMENT asOne() {
        if (elements == null || elements.length == 0) {
            return null;
        }
        return elements[0];
    }

    public List<ELEMENT> asList() {
        return Stream.of(elements).toList();
    }

}