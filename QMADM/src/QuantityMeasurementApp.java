public class QuantityMeasurementApp {

    // ---------------- LENGTH UNIT ENUM ----------------
    public enum LengthUnit {

        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CENTIMETER(0.01 / 0.3048); // cm → feet

        private final double factorToFeet;

        LengthUnit(double factorToFeet) {
            this.factorToFeet = factorToFeet;
        }

        public double getFactorToFeet() {
            return factorToFeet;
        }
    }

    // ---------------- GENERIC VALUE OBJECT ----------------
    public static class QuantityLength {

        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid numeric value");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }

            this.value = value;
            this.unit = unit;
        }

        // Normalize to base unit (FEET)
        private double toFeet() {
            return value * unit.getFactorToFeet();
        }

        // ---------------- UC3/UC4 EQUALITY ----------------
        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;
            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // ---------------- UC5 CORE API (CONVERSION) ----------------
    public static double convert(double value,
                                 LengthUnit source,
                                 LengthUnit target) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid numeric value");
        }
        if (source == null || target == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        // Convert source → feet
        double valueInFeet = value * source.getFactorToFeet();

        // Convert feet → target
        return valueInFeet / target.getFactorToFeet();
    }

    // ---------------- OVERLOADED API (OPTIONAL UX LAYER) ----------------
    public static double convert(QuantityLength quantity,
                                 LengthUnit targetUnit) {

        return convert(quantity.value, quantity.unit, targetUnit);
    }

    // ---------------- DEMO ----------------
    public static void main(String[] args) {

        System.out.println("1 feet → inches: " +
                convert(1.0, LengthUnit.FEET, LengthUnit.INCH)); // 12

        System.out.println("3 yards → feet: " +
                convert(3.0, LengthUnit.YARD, LengthUnit.FEET)); // 9

        System.out.println("36 inches → yards: " +
                convert(36.0, LengthUnit.INCH, LengthUnit.YARD)); // 1

        System.out.println("1 cm → inch: " +
                convert(1.0, LengthUnit.CENTIMETER, LengthUnit.INCH)); // ~0.3937
    }
}