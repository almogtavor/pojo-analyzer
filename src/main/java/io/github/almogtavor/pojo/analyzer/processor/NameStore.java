package io.github.almogtavor.pojo.analyzer.processor;

public final class NameStore {

    public static final String UTILITY_CLASS = "Utility class";
    private NameStore() {
        // not to be instantiated in public
    }

    public static class Package {

        private Package() { throw new IllegalStateException(UTILITY_CLASS); }
        public static final String INDENTATION = "    ";
    }
    public static class Class {

        private Class() { throw new IllegalStateException(UTILITY_CLASS); }
        public static String getGeneratedClassName(String clsName) {
            return "Detailed" + clsName;
        }
    }
    public static class Method {
        private Method() { throw new IllegalStateException(UTILITY_CLASS); }
    }
    public static class Variable {

        private Variable() { throw new IllegalStateException(UTILITY_CLASS); }
        public static final String FIELD_DETAILS_LIST_NAME = "fieldDetailsList";
        public static final String FIELD_DETAILS_MAP_NAME = "fieldDetailsMap";
    }
}
