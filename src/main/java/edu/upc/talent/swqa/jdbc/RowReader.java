package edu.upc.talent.swqa.jdbc;

import edu.upc.talent.swqa.functions.Function1;

@FunctionalInterface
public interface RowReader<A> extends Function1<ResultSetView, A> {
}
