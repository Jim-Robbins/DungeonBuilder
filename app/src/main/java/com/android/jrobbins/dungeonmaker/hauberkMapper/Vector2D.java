package com.android.jrobbins.dungeonmaker.hauberkMapper;

import android.util.Log;

/******************************************************************************
 *  Compilation:  javac Vector2D.java
 *  Execution:    java Vector2D
 *
 *  Implementation of a vector of real numbers.
 *
 *  This class is implemented to be immutable: once the client program
 *  initialize a Vector2D, it cannot change any of its fields
 *  (N or data[i]) either directly or indirectly. Immutability is a
 *  very desirable feature of a data type.
 *
 *
 *  % java Vector2D
 *  x        =  (1.0, 2.0, 3.0, 4.0)
 *  y        =  (5.0, 2.0, 4.0, 1.0)
 *  x + y    =  (6.0, 4.0, 7.0, 5.0)
 *  10x      =  (10.0, 20.0, 30.0, 40.0)
 *  |x|      =  5.477225575051661
 *  <x, y>   =  25.0
 *  |x - y|  =  5.0990195135927845
 *
 *  Note that java.util.Vector2D is an unrelated Java library class.
 *
 *  Copyright © 2000–2011, Robert Sedgewick and Kevin Wayne.
 *  http://introcs.cs.princeton.edu/java/33design/Vector.java.html
 ******************************************************************************/

public class Vector2D {

    private final int n;         // length of the vector
    private double[] data;       // array of vector's components

    // create the zero vector of length n
    public Vector2D(int n) {
        this.n = n;
        this.data = new double[n];

    }

    // create the vector from x,y coords
    public Vector2D(int x, int y) {
        this.data = new double[]{x,y};
        this.n = data.length;

    }

    // create a vector from an array
    public Vector2D(double[] data) {
        n = data.length;

        // defensive copy so that client can't alter our copy of data[]
        this.data = new double[n];
        for (int i = 0; i < n; i++)
            this.data[i] = data[i];
    }

    public int x()
    {
        return (int) this.data[0];
    }

    public int y()
    {
        return (int) this.data[1];
    }

    public int z()
    {
        return (int) this.data[3];
    }

    // return the length of the vector
    public int length() {
        return n;
    }

    // return the inner product of this Vector2D a and b
    public double dot(Vector2D that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("Dimensions disagree");
        double sum = 0.0;
        for (int i = 0; i < n; i++)
            sum = sum + (this.data[i] * that.data[i]);
        return sum;
    }

    // return the Euclidean norm of this Vector2D
    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    // return the Euclidean distance between this and that
    public double distanceTo(Vector2D that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("Dimensions disagree");
        return this.minus(that).magnitude();
    }

    // return this + that
    public Vector2D plus(Vector2D that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("Dimensions disagree");
        Vector2D c = new Vector2D(n);
        for (int i = 0; i < n; i++)
            c.data[i] = this.data[i] + that.data[i];
        return c;
    }

    // return this - that
    public Vector2D minus(Vector2D that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("Dimensions disagree");
        Vector2D c = new Vector2D(n);
        for (int i = 0; i < n; i++)
            c.data[i] = this.data[i] - that.data[i];
        return c;
    }

    // return the corresponding coordinate
    public double cartesian(int i) {
        return data[i];
    }

    // create and return a new object whose value is (this * factor)
    public Vector2D scale(double factor) {
        Vector2D c = new Vector2D(n);
        for (int i = 0; i < n; i++)
            c.data[i] = factor * data[i];
        return c;
    }


    // return the corresponding unit vector
    public Vector2D direction() {
        if (this.magnitude() == 0.0)
            throw new RuntimeException("Zero-vector has no direction");
        return this.scale(1.0 / this.magnitude());
    }

    // return a string representation of the vector
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("(");
        for (int i = 0; i < n; i++) {
            s.append(data[i]);
            if (i < n-1) s.append(", ");
        }
        s.append(")");
        return s.toString();
    }

    // test client
    public static void main(String[] args) {
        double[] xdata = { 1.0, 2.0, 3.0, 4.0 };
        double[] ydata = { 5.0, 2.0, 4.0, 1.0 };

        Vector2D x = new Vector2D(xdata);
        Vector2D y = new Vector2D(ydata);

        Log.d("Vector2D", "x        =  " + x);
        Log.d("Vector2D","y        =  " + y);
        Log.d("Vector2D","x + y    =  " + x.plus(y));
        Log.d("Vector2D","10x      =  " + x.scale(10.0));
        Log.d("Vector2D","|x|      =  " + x.magnitude());
        Log.d("Vector2D","<x, y>   =  " + x.dot(y));
        Log.d("Vector2D","|x - y|  =  " + x.minus(y).magnitude());
    }
}

