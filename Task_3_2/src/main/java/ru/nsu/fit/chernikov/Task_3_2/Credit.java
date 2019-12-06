package ru.nsu.fit.chernikov.Task_3_2;

class Credit {
  private boolean isMark;
  private boolean isFinal;
  private int mark;

  Credit(boolean cred, boolean _final) {
    isMark = false;
    if (cred) {
      mark = 1;
    } else {
      mark = 0;
    }
    isFinal = _final;
  }

  Credit(int _mark, boolean _final) {
    isMark = true;
    mark = _mark;
    isFinal = _final;
  }

  int getMark() {
    return mark;
  }

  boolean isMark() {
    return isMark;
  }

  boolean isFinal() {
    return isFinal;
  }
}
