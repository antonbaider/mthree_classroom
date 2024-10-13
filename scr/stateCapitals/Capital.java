package stateCapitals;

import java.util.Objects;

public class Capital {
    String name;
    int population;
    double mileage;

    @Override
    public String toString() {
        return name + " | Pop: " + population + " | Area: " + mileage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Capital capital = (Capital) o;
        return population == capital.population && Double.compare(mileage, capital.mileage) == 0 && Objects.equals(name, capital.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, population, mileage);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public Capital(String name, int population, double mileage) {
        this.name = name;
        this.population = population;
        this.mileage = mileage;
    }
}
