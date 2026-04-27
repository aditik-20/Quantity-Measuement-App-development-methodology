public class QuantityMeasurementApp {

    // ---------------- UNIT ENUM ----------------
    public enum LengthUnit {

        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CENTIMETER(0.01 / 0.3048);

        private final double factorToFeet;

        LengthUnit(double factorToFeet) {
            this.factorToFeet = factorToFeet;
        }

        public double getFactorToFeet() {
            return factorToFeet;
        }
    }

    // ---------------- VALUE OBJECT ----------------
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

        // Convert to base unit (FEET)
        private double toFeet() {
            return value * unit.getFactorToFeet();
        }

        // ---------------- UC3/UC4/UC5 EQUALITY ----------------
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

        // expose internal values for UC6 operations safely
        private double getValue() {
            return value;
        }

        private LengthUnit getUnit() {
            return unit;
        }
    }

    // ---------------- UC5 CONVERSION (reused concept) ----------------
    public static double convert(double value,
                                 LengthUnit source,
                                 LengthUnit target) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid numeric value");
        }
        if (source == null || target == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        double valueInFeet = value * source.getFactorToFeet();
        return valueInFeet / target.getFactorToFeet();
    }

    // ---------------- UC6 ADDITION CORE API ----------------
    public static QuantityLength add(QuantityLength q1,
                                     QuantityLength q2,
                                     LengthUnit resultUnit) {

        if (q1 == null || q2 == null || resultUnit == null) {
            throw new IllegalArgumentException("Null input not allowed");
        }

        // normalize both to FEET
        double q1Feet = q1.toFeet();
        double q2Feet = q2.toFeet();

        double sumInFeet = q1Feet + q2Feet;

        // convert result into target unit (unit of first operand)
        double resultValue = sumInFeet / resultUnit.getFactorToFeet();

        return new QuantityLength(resultValue, resultUnit);
    }

    // ---------------- OVERLOAD USING RAW VALUES ----------------
    public static QuantityLength add(double v1, LengthUnit u1,
                                     double v2, LengthUnit u2,
                                     LengthUnit resultUnit) {

        return add(new QuantityLength(v1, u1),
                new QuantityLength(v2, u2),
                resultUnit);
    }

    // ---------------- DEMO ----------------
    public static void main(String[] args) {

        System.out.println("1 ft + 2 ft = " +
                add(1.0, LengthUnit.FEET, 2.0, LengthUnit.FEET, LengthUnit.FEET));

        System.out.println("1 ft + 12 in = " +
                add(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.FEET));

        System.out.println("12 in + 1 ft = " +
                add(12.0, LengthUnit.INCH, 1.0, LengthUnit.FEET, LengthUnit.INCH));

        System.out.println("1 yard + 3 ft = " +
                add(1.0, LengthUnit.YARD, 3.0, LengthUnit.FEET, LengthUnit.YARD));

        System.out.println("36 in + 1 yard = " +
                add(36.0, LengthUnit.INCH, 1.0, LengthUnit.YARD, LengthUnit.INCH));
    }
}