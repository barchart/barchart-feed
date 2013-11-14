/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class ASCII {

	public static final Charset ASCII_CHARSET = Charset.forName("US-ASCII");

	public static final CharsetEncoder ASCII_ENCODER = ASCII_CHARSET
			.newEncoder();

	public static final byte NUL = 0x00;

	public static final byte SOH = 0x01;
	public static final byte STX = 0x02;
	public static final byte ETX = 0x03;

	public static final byte LF = 0x0A;
	public static final byte CR = 0x0D;

	public static final byte DC1 = 0x11;
	public static final byte DC2 = 0x12;
	public static final byte DC3 = 0x13;
	public static final byte DC4 = 0x14;

	public static final byte US = 0x1F;

	public static final byte SPACE = 0x20;
	public static final byte AT = '@';
	public static final byte DASH = '-';
	public static final byte UNDER = '_';
	public static final byte DOT = '.';
	public static final byte COMMA = ',';
	public static final byte EXCL = '!';
	public static final byte POUND = '#';
	public static final byte QUEST = '?';
	public static final byte BAR = '|';
	public static final byte PLUS = '+';
	public static final byte MINUS = '-';
	public static final byte DOLLAR = '$';
	public static final byte STAR = '*';
	public static final byte LESS = '<';
	public static final byte MORE = '>';
	public static final byte EQUAL = '=';
	public static final byte COLON = ':';
	public static final byte PERCENT = '%';

	public static final String STRING_DOT = ".";
	public static final String STRING_EMPTY = "";
	public static final String STRING_DASH = "-";
	public static final String STRING_COMMA = ",";
	public static final String STRING_COLON = ":";
	public static final String STRING_UNDER = "_";
	public static final String STRING_BAR = "|";

	public static final String REGEX_DOT = "\\.";

	//

	public static final byte _0_ = '0'; // 48[0x30]
	public static final byte _1_ = '1';
	public static final byte _2_ = '2';
	public static final byte _3_ = '3';
	public static final byte _4_ = '4';
	public static final byte _5_ = '5';
	public static final byte _6_ = '6';
	public static final byte _7_ = '7';
	public static final byte _8_ = '8';
	public static final byte _9_ = '9';

	//

	public static final byte _A_ = 'A'; // 65 [0x41
	public static final byte _B_ = 'B';
	public static final byte _C_ = 'C';
	public static final byte _D_ = 'D';
	public static final byte _E_ = 'E';
	public static final byte _F_ = 'F';
	public static final byte _G_ = 'G';
	public static final byte _H_ = 'H';
	public static final byte _I_ = 'I';
	public static final byte _J_ = 'J';
	public static final byte _K_ = 'K';
	public static final byte _L_ = 'L';
	public static final byte _M_ = 'M';
	public static final byte _N_ = 'N';
	public static final byte _O_ = 'O';
	public static final byte _P_ = 'P';
	public static final byte _Q_ = 'Q';
	public static final byte _R_ = 'R';
	public static final byte _S_ = 'S';
	public static final byte _T_ = 'T';
	public static final byte _U_ = 'U';
	public static final byte _V_ = 'V';
	public static final byte _W_ = 'W';
	public static final byte _X_ = 'X';
	public static final byte _Y_ = 'Y';
	public static final byte _Z_ = 'Z';

	//

	public static final byte _a_ = 'a'; // 97 [0x61]
	public static final byte _b_ = 'b';
	public static final byte _c_ = 'c';
	public static final byte _d_ = 'd';
	public static final byte _e_ = 'e';
	public static final byte _f_ = 'f';
	public static final byte _g_ = 'g';
	public static final byte _h_ = 'h';
	public static final byte _i_ = 'i';
	public static final byte _j_ = 'j';
	public static final byte _k_ = 'k';
	public static final byte _l_ = 'l';
	public static final byte _m_ = 'm';
	public static final byte _n_ = 'n';
	public static final byte _o_ = 'o';
	public static final byte _p_ = 'p';
	public static final byte _q_ = 'q';
	public static final byte _r_ = 'r';
	public static final byte _s_ = 's';
	public static final byte _t_ = 't';
	public static final byte _u_ = 'u';
	public static final byte _v_ = 'v';
	public static final byte _w_ = 'w';
	public static final byte _x_ = 'x';
	public static final byte _y_ = 'y';
	public static final byte _z_ = 'z';

	public static enum Hex {
		_0_('0'), //
		_1_('1'), //
		_2_('2'), //
		_3_('3'), //
		_4_('4'), //
		_5_('5'), //
		_6_('6'), //
		_7_('7'), //
		_8_('8'), //
		_9_('9'), //
		_A_('A'), //
		_B_('B'), //
		_C_('C'), //
		_D_('D'), //
		_E_('E'), //
		_F_('F'), //
		;
		public final byte code;

		Hex(char code) {
			this.code = (byte) code;
		}
	}

	final static Hex[] hexValues = Hex.values();

	public static final String toHexString(byte[] array) {
		final int size = array.length;
		final byte[] hexArray = new byte[size * 2];
		int i = 0;
		for (int k = 0; k < size; k++) {
			byte value = array[k];
			byte hex0 = (byte) ((value & 0xF0) >>> 4);
			byte hex1 = (byte) ((value & 0x0F));
			hexArray[i++] = hexValues[hex0].code;
			hexArray[i++] = hexValues[hex1].code;
		}
		return new String(hexArray, ASCII_CHARSET);
	}

	public static final void toUpperCase(final byte[] array) {
		if (array == null) {
			return;
		}
		final int size = array.length;
		for (int k = 0; k < size; k++) {
			final byte ascii = array[k];
			if (_a_ <= ascii && ascii <= _z_) {
				array[k] = (byte) (ascii - 32);
			}
		}
	}

	public static final void toLowerCase(final byte[] array) {
		if (array == null) {
			return;
		}
		final int size = array.length;
		for (int k = 0; k < size; k++) {
			final byte ascii = array[k];
			if (_A_ <= ascii && ascii <= _Z_) {
				array[k] = (byte) (ascii + 32);
			}
		}
	}

	//

	public static final boolean isDigit(final byte digit) {
		return _0_ <= digit && digit <= _9_;
	}

	public static final boolean isDigit(final char digit) {
		return _0_ <= digit && digit <= _9_;
	}

	public static final boolean isDigit(final int digit) {
		return _0_ <= digit && digit <= _9_;
	}

	//

	public static final boolean isLetterUpper(final byte letter) {
		return _A_ <= letter && letter <= _Z_;
	}

	public static final boolean isLetterLower(final byte letter) {
		return _a_ <= letter && letter <= _z_;
	}

	public static final boolean isLetterUpper(final char letter) {
		return _A_ <= letter && letter <= _Z_;
	}

	public static final boolean isLetterLower(final char letter) {
		return _a_ <= letter && letter <= _z_;
	}

	public static final boolean isLetterUpper(final int letter) {
		return _A_ <= letter && letter <= _Z_;
	}

	public static final boolean isLetterLower(final int letter) {
		return _a_ <= letter && letter <= _z_;
	}

	//

	public static final boolean isLetter(final byte letter) {
		return isLetterUpper(letter) || isLetterLower(letter);
	}

	public static final boolean isLetter(final char letter) {
		return isLetterUpper(letter) || isLetterLower(letter);
	}

	public static final boolean isLetter(final int letter) {
		return isLetterUpper(letter) || isLetterLower(letter);
	}

	//

	public static final boolean containsDigit(final byte[] array) {
		if (array == null) {
			return false;
		}
		final int size = array.length;
		for (int k = 0; k < size; k++) {
			if (isDigit(array[k])) {
				return true;
			}
		}
		return false;
	}

	public static final boolean containsDigit(final char[] array) {
		if (array == null) {
			return false;
		}
		final int size = array.length;
		for (int k = 0; k < size; k++) {
			if (isDigit(array[k])) {
				return true;
			}
		}
		return false;
	}

	public static final boolean containsDigit(final int[] array) {
		if (array == null) {
			return false;
		}
		final int size = array.length;
		for (int k = 0; k < size; k++) {
			if (isDigit(array[k])) {
				return true;
			}
		}
		return false;
	}

	public static final boolean containsDigit(final CharSequence sequence) {
		if (sequence == null) {
			return false;
		}
		final int size = sequence.length();
		for (int k = 0; k < size; k++) {
			if (isDigit(sequence.charAt(k))) {
				return true;
			}
		}
		return false;
	}

	//

}
