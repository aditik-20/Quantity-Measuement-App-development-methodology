import java.util.Objects;

/**
 * Interface defining measurable unit behavior
 */
interface IMeasurable {
    double getConversionFactor();

    default double convertToBaseUnit(double value) {
        return value * getConversionFactor();
    }

    default double convertFromBaseUnit(double baseValue) {
        return baseValue / getConversionFactor();
    }

    String getUnitName();
}

/**
 * Length Units
 */
enum LengthUnit implements IMeasurable {
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double factor;

    LengthUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public String getUnitName() {
        return name();
    }
}

/**
 * Weight Units
 */
enum WeightUnit implements IMeasurable {
    KILOGRAM(1.0),
    GRAM(0.001);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public String getUnitName() {
        return name();
    }
}

/**
 * Volume Units
 */
enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double factor;

    VolumeUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public String getUnitName() {
        return name();
    }
}

/**
 * Generic Quantity Class
 */
class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    private static final double EPSILON = 1e-6;

    public Quantity(double value, U unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be finite");

        this.value = value;
        this.unit = unit;
    }

    private void validateOperand(Quantity<U> other) {
        if (other == null)
            throw new IllegalArgumentException("Other quantity cannot be null");

        if (this.unit.getClass() != other.unit.getClass())
            throw new IllegalArgumentException("Incompatible measurement categories");

        if (!Double.isFinite(other.value))
            throw new IllegalArgumentException("Invalid numeric value");
    }

    private double toBaseValue() {
        return unit.convertToBaseUnit(value);
    }

    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    /**
     * Equality check (cross-unit supported)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity<?> other)) return false;

        if (this.unit.getClass() != other.unit.getClass())
            return false;

        double base1 = this.toBaseValue();
        double base2 = ((Quantity<?>) other).unit.convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(round(toBaseValue()), unit.getClass());
    }

    /**
     * Convert to target unit
     */
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double base = toBaseValue();
        double converted = targetUnit.convertFromBaseUnit(base);

        return new Quantity<>(round(converted), targetUnit);
    }

    /**
     * Addition
     */
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateOperand(other);
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double resultBase = this.toBaseValue() + other.toBaseValue();
        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), targetUnit);
    }

    /**
     * Subtraction
     */
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateOperand(other);
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double resultBase = this.toBaseValue() - other.toBaseValue();
        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), targetUnit);
    }

    /**
     * Division (returns scalar)
     */
    public double divide(Quantity<U> other) {
        validateOperand(other);

        double divisor = other.toBaseValue();
        if (Math.abs(divisor) < EPSILON)
            throw new ArithmeticException("Division by zero");

        return this.toBaseValue() / divisor;
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}

/**
 * Demo Application
 */
public class QuantityMeasurementApp {

    public static void main(String[] args) {

        // LENGTH
        Quantity<LengthUnit> l1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(6, LengthUnit.INCHES);

        System.out.println(l1.subtract(l2)); // 9.5 FEET
        System.out.println(l1.add(l2));      // 10.5 FEET
        System.out.println(l1.divide(new Quantity<>(2, LengthUnit.FEET))); // 5

        // WEIGHT
        Quantity<WeightUnit> w1 = new Quantity<>(10, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(5000, WeightUnit.GRAM);

        System.out.println(w1.subtract(w2)); // 5 KG
        System.out.println(w1.divide(w2));   // 2

        // VOLUME
        Quantity<VolumeUnit> v1 = new Quantity<>(5, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(500, VolumeUnit.MILLILITRE);

        System.out.println(v1.subtract(v2)); // 4.5 L
        System.out.println(v1.add(v2));      // 5.5 L
        System.out.println(v1.divide(new Quantity<>(10, VolumeUnit.LITRE))); // 0.5

        // EQUALITY
        System.out.println(
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE))
        ); // true
    }
}