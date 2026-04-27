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

        private double toFeet() {
            return value * unit.getFactorToFeet();
        }

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

    // ---------------- CORE NORMALIZATION ADD LOGIC ----------------
    private static double addInFeet(QuantityLength q1, QuantityLength q2) {
        return q1.toFeet() + q2.toFeet();
    }

    // ---------------- UC6 (default unit = first operand) ----------------
    public static QuantityLength add(QuantityLength q1, QuantityLength q2) {

        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Null input not allowed");
        }

        double sumInFeet = addInFeet(q1, q2);
        double resultValue = sumInFeet / q1.unit.getFactorToFeet();

        return new QuantityLength(resultValue, q1.unit);
    }

    // ---------------- UC7 (explicit target unit) ----------------
    public static QuantityLength add(QuantityLength q1,
                                     QuantityLength q2,
                                     LengthUnit targetUnit) {

        if (q1 == null || q2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Null input not allowed");
        }

        double sumInFeet = addInFeet(q1, q2);
        double resultValue = sumInFeet / targetUnit.getFactorToFeet();

        return new QuantityLength(resultValue, targetUnit);
    }

    // ---------------- RAW VALUE OVERLOAD ----------------
    public static QuantityLength add(double v1, LengthUnit u1,
                                     double v2, LengthUnit u2,
                                     LengthUnit targetUnit) {

        return add(new QuantityLength(v1, u1),
                new QuantityLength(v2, u2),
                targetUnit);
    }

    // ---------------- DEMO ----------------
    public static void main(String[] args) {

        System.out.println("1 ft + 12 in (FEET) = " +
                add(new QuantityLength(1.0, LengthUnit.FEET),
                        new QuantityLength(12.0, LengthUnit.INCH),
                        LengthUnit.FEET));

        System.out.println("1 ft + 12 in (INCH) = " +
                add(new QuantityLength(1.0, LengthUnit.FEET),
                        new QuantityLength(12.0, LengthUnit.INCH),
                        LengthUnit.INCH));

        System.out.println("1 ft + 12 in (YARD) = " +
                add(new QuantityLength(1.0, LengthUnit.FEET),
                        new QuantityLength(12.0, LengthUnit.INCH),
                        LengthUnit.YARD));
    }
}