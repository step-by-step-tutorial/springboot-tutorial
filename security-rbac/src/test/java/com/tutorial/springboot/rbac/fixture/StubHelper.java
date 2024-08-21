package com.tutorial.springboot.rbac.fixture;

import java.util.List;

public class StubHelper<ELEMENT> {

    private final List<ELEMENT> elements;

    public StubHelper(List<ELEMENT> elements) {
        this.elements = elements;
    }

    public ELEMENT asOne() {
        return elements.getFirst();
    }

    public List<ELEMENT> asList() {
        return elements;
    }

}