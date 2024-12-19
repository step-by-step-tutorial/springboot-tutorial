package com.tutorial.springboot.security_rbac_jwt.testutils.stub;

import com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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
        return Stream.of(elements).collect(toList());
    }

    public List<ELEMENT> asUniqList(Function<ELEMENT, ?> comparator) {
        return CollectionUtils.removeDuplication(Stream.of(elements).collect(toList()), comparator).collect(toList());
    }

}