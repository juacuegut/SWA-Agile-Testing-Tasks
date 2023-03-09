package edu.upc.talent.swqa.test.utils;

import java.util.ArrayList;
import java.util.List;

public class ConsoleUtils {

  static void printAlignedTable(List<List<String>> table) {
    var columnWidths = new ArrayList<Integer>();
    for (var row : table) {
      for (int i = 0; i < row.size(); i++) {
        var cell = row.get(i);
        if (columnWidths.size() <= i) {
          columnWidths.add(cell.length());
        } else {
          columnWidths.set(i, Math.max(columnWidths.get(i), cell.length()));
        }
      }
    }
    for (var row : table) {
      for (int i = 0; i < row.size(); i++) {
        var cell = row.get(i);
        System.out.print(cell);
        for (int j = 0; j < columnWidths.get(i) - cell.length(); j++) {
          System.out.print(" ");
        }
        System.out.print(" ");
      }
      System.out.println();
    }
  }
}
