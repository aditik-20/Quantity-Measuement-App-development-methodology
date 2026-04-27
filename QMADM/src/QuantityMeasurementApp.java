public class QuantityMeasurementApp {

    // ---------------- FEET CLASS ----------------
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;

            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // ---------------- INCHES CLASS ----------------
    public static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;

            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // ---------------- STATIC METHODS (as per UC2 requirement) ----------------

    public static boolean compareFeet(double value1, double value2) {
        Feet f1 = new Feet(value1);
        Feet f2 = new Feet(value2);
        return f1.equals(f2);
    }

    public static boolean compareInches(double value1, double value2) {
        Inches i1 = new Inches(value1);
        Inches i2 = new Inches(value2);
        return i1.equals(i2);
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {

        System.out.println("1.0 ft vs 1.0 ft: " +
                compareFeet(1.0, 1.0)); // true

        System.out.println("1.0 inch vs 1.0 inch: " +
                compareInches(1.0, 1.0)); // true

        System.out.println("1.0 ft vs 2.0 ft: " +
                compareFeet(1.0, 2.0)); // false
    }
}