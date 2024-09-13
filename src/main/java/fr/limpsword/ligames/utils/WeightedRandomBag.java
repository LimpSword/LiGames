package fr.limpsword.ligames.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public final class WeightedRandomBag<T> {

    private final Map<T, Double> map = new HashMap<>();
    private final Random rand = new Random();

    private double accumulatedWeight;

    /**
     * Add an entry to the bag.
     *
     * @param object the object to add
     * @param weight its weight which must be positive
     */
    public void addEntry(T object, double weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("weight must be >= 0");
        }

        accumulatedWeight += weight;

        map.put(object, accumulatedWeight);
    }

    /**
     * Get a random element from the bag.
     * It is *not* required that the sum of the weights match 100
     *
     * @return the randomly found element
     */
    public T getRandom() {
        double r = rand.nextDouble() * accumulatedWeight;

        for (Map.Entry<T, Double> entry : map.entrySet()) {
            if (entry.getValue() >= r) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Get several random elements
     *
     * @param amount the *positive* amount of random element to found
     * @return a list of random items from the bag
     */
    public List<T> getRandoms(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount of items to get must be >= 0");
        }

        if (map.isEmpty() || amount == 0) return new ArrayList<>();
        assert getRandom() != null;
        return IntStream.range(0, amount).mapToObj(i -> getRandom()).toList();
    }

    /**
     * Get the accumulated weight of the bag
     *
     * @return the accumulated weight
     */
    public double getAccumulatedWeight() {
        return accumulatedWeight;
    }

    @Override
    public String toString() {
        return "WeightedRandomBag{" +
                "map=" + map +
                ", accumulatedWeight=" + accumulatedWeight +
                '}';
    }
}