package com.tutorial.springboot.securityoauth2server.util;

import java.util.List;

public record TripleCollection<T>(List<T> newItems, List<T> deletionItems, List<T> commonItem) {

}