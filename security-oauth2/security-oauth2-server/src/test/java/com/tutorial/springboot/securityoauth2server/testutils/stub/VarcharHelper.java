package com.tutorial.springboot.securityoauth2server.testutils.stub;

import java.util.List;
import java.util.stream.Stream;

public class VarcharHelper<ELEMENT> {

    private final ELEMENT[] elements;

    @SafeVarargs
    public VarcharHelper(ELEMENT... elements) {
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