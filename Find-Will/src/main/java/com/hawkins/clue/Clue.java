package com.hawkins.clue;

import java.util.List;

public interface Clue {
  String prompt();

  List<String> options();

  record Result(String line, boolean complete) {}

  Result interact(int option);
}
