package com.tutorial.springboot.security_rbac_jwt.util;

import java.util.List;

public record TripleCollection<T>(List<T> newItems, List<T> deletionItems, List<T> commonItem) {

}