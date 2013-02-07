/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.bench;

public class CircularReference {

	static int i = 0;

	static abstract class A {

		abstract B getB();

		int k = i++;

		public String toString() {
			return "" + k;
		}

	}

	static abstract class B {

		abstract A getA();

		int k = i++;

		public String toString() {
			return "" + k;
		}

	}

	public static void doStuff(A foo, B bar) {
		System.out.println("foo = " + foo);
		System.out.println("foo.b = " + foo.getB());
		System.out.println("bar = " + bar);
		System.out.println("bar.a = " + bar.getA());
	}

	public static void main(String[] args) {

		new Object() {

			final A foo = new A() {
				B getB() {
					return bar;
				}
			};

			final B bar = new B() {
				A getA() {
					return foo;
				}
			};

			{
				doStuff(foo, bar);
			}

		};

	}
}
