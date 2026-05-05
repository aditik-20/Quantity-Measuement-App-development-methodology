import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

public class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    /* =========================
       PUBLIC API (UNCHANGED)
       ========================= */

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double result = performBaseArithmetic(other, ArithmeticOperation.ADD);
        return new Quantity<>(targetUnit.convertFromBaseUnit(result), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        double result = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        return new Quantity<>(targetUnit.convertFromBaseUnit(result), targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false);
        double thisBase = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        if (otherBase == 0) {
            throw new ArithmeticException("Division by zero not allowed");
        }

        return thisBase / otherBase;
    }

    public Quantity<U> convertTo(U targetUnit) {
        double base = unit.convertToBaseUnit(value);
        return new Quantity<>(targetUnit.convertFromBaseUnit(base), targetUnit);
    }

    /* =========================
       CENTRALIZED VALIDATION
       ========================= */

    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetRequired) {

        if (other == null) {
            throw new IllegalArgumentException("Operand cannot be null");
        }

        if (!this.unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Cross-category operations not allowed");
        }

        if (Double.isNaN(this.value) || Double.isNaN(other.value)) {
            throw new IllegalArgumentException("NaN values not allowed");
        }

        if (Double.isInfinite(this.value) || Double.isInfinite(other.value)) {
            throw new IllegalArgumentException("Infinite values not allowed");
        }

        if (targetRequired && targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
    }

    /* =========================
       CORE DRY HELPER METHOD
       ========================= */

    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {

        validateArithmeticOperands(other, this.unit, false);

        double thisBase = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return operation.compute(thisBase, otherBase);
    }

    /* =========================
       ARITHMETIC ENUM (DRY CORE)
       ========================= */

    private enum ArithmeticOperation {

        ADD((a, b) -> a + b),

        SUBTRACT((a, b) -> a - b),

        DIVIDE((a, b) -> {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        });

        private final DoubleBinaryOperator operator;

        ArithmeticOperation(DoubleBinaryOperator operator) {
            this.operator = operator;
        }

        public double compute(double a, double b) {
            return operator.applyAsDouble(a, b);
        }
    }

    /* =========================
       EQUALITY (UNCHANGED LOGIC)
       ========================= */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity)) return false;

        Quantity<?> other = (Quantity<?>) obj;

        if (!this.unit.getClass().equals(other.unit.getClass())) return false;

        double thisBase = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return Math.abs(thisBase - otherBase) < 0.0001;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.getClass(), unit.convertToBaseUnit(value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}