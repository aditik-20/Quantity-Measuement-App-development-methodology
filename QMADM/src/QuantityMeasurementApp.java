public class QuantityMeasurementApp {

    // ---------------- UNIT ENUM ----------------
    public enum LengthUnit {

        FEET(1.0),
        INCH(1.0 / 12.0);

        private final double conversionFactorToFeet;

        LengthUnit(double conversionFactorToFeet) {
            this.conversionFactorToFeet = conversionFactorToFeet;
        }

        public double getConversionFactorToFeet() {
            return conversionFactorToFeet;
        }
    }

    // ---------------- GENERIC QUANTITY CLASS ----------------
    public static class QuantityLength {

        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return this.value * unit.getConversionFactorToFeet();
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }
    }

    // ---------------- STATIC METHODS (optional abstraction layer) ----------------
    public static boolean compare(double value1, LengthUnit unit1,
                                  double value2, LengthUnit unit2) {

        QuantityLength q1 = new QuantityLength(value1, unit1);
        QuantityLength q2 = new QuantityLength(value2, unit2);

        return q1.equals(q2);
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {

        System.out.println("1 feet vs 12 inch: " +
                compare(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH)); // true

        System.out.println("1 inch vs 1 inch: " +
                compare(1.0, LengthUnit.INCH, 1.0, LengthUnit.INCH)); // true

        System.out.println("1 feet vs 2 feet: " +
                compare(1.0, LengthUnit.FEET, 2.0, LengthUnit.FEET)); // false
    }
}