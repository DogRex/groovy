class CategoryTest extends GroovyTestCase {

  void testCategories() {
    use (StringCategory.class) {
      assert "Sam".lower() == "sam";
      use (IntegerCategory.class) {
        assert "Sam".lower() == "sam";
        assert 1.inc() == 2;
      }
        shouldFail(MissingMethodException, { 1.inc() });
    }
    shouldFail(MissingMethodException, { "Sam".lower() });
  }
  
  void testCategoryDefinedProperties() {
  
    use(CategoryTestPropertyCategory) { 
      assert getSomething() == "hello"
      assert something == "hello"
      something = "nihao"
      assert something == "nihao"
    }
    
    // test the new value again in a new block
    use(CategoryTestPropertyCategory) { 
      assert something == "nihao"
    }

  }
}

class StringCategory {
  static String lower(String string) {
    return string.toLowerCase();
  }
}

class IntegerCategory {
  static Integer inc(Integer i) {
    return i + 1;
  }
}


class CategoryTestPropertyCategory {
     private static aVal = "hello"
     static getSomething(Object self) { return aVal }
     static void setSomething(Object self, newValue) { aVal = newValue }
}

