public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1000.0, WeightUnit.GRAM);

        // Equality
        System.out.println(w1.equals(w2)); // true

        // Conversion
        System.out.println(w1.convertTo(WeightUnit.GRAM)); // 1000 g

        // Addition (default unit = first operand)
        System.out.println(w1.add(w2)); // 2 kg

        // Addition (explicit target unit)
        System.out.println(w1.add(w2, WeightUnit.GRAM)); // 2000 g

        // Pound conversion
        QuantityWeight w3 = new QuantityWeight(2.20462, WeightUnit.POUND);
        System.out.println(w3.convertTo(WeightUnit.KILOGRAM)); // ~1 kg
    }
}

// -------------------- Standalone Enum --------------------

enum WeightUnit {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double factorToKg;

    WeightUnit(double factorToKg) {
        this.factorToKg = factorToKg;
    }

    public double getConversionFactor() {
        return factorToKg;
    }

    // Convert current unit → base (kg)
    public double convertToBaseUnit(double value) {
        return value * factorToKg;
    }

    // Convert base (kg) → current unit
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factorToKg;
    }
}

// -------------------- QuantityWeight Class --------------------

class QuantityWeight {

    private final double value;
    private final WeightUnit unit;
    private static final double EPSILON = 1e-6;

    public QuantityWeight(double value, WeightUnit unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (Double.isNaN(value) || Double.isInfinite(value))
            throw new IllegalArgumentException("Invalid value");

        this.value = value;
        this.unit = unit;
    }

    // Convert to another unit
    public QuantityWeight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);

        return new QuantityWeight(converted, targetUnit);
    }

    // Add (default: result in this.unit)
    public QuantityWeight add(QuantityWeight other) {
        return add(other, this.unit);
    }

    // Add with explicit target unit
    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        double sum = base1 + base2;
        double result = targetUnit.convertFromBaseUnit(sum);

        return new QuantityWeight(result, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        QuantityWeight other = (QuantityWeight) obj;

        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public int hashCode() {
        double base = unit.convertToBaseUnit(value);
        return Double.hashCode(base);
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}