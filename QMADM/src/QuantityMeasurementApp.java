public class QuantityMeasurementApp {

    // Step 3: Inner Class
    public static class Feet {
        private final double value;

        // Step 4: Constructor
        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        // Step 5: equals() implementation
        @Override
        public boolean equals(Object obj) {

            // Same reference check
            if (this == obj) {
                return true;
            }

            // Null check
            if (obj == null) {
                return false;
            }

            // Type check
            if (this.getClass() != obj.getClass()) {
                return false;
            }

            // Safe cast
            Feet other = (Feet) obj;

            // Double comparison
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // Step 6: Main method
    public static void main(String[] args) {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);
        Feet feet3 = new Feet(2.0);

        System.out.println("1.0 ft vs 1.0 ft: " + feet1.equals(feet2)); // true
        System.out.println("1.0 ft vs 2.0 ft: " + feet1.equals(feet3)); // false
    }
}