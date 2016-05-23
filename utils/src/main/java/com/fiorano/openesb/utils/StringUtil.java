/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import java.awt.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public final class StringUtil {

    /**
     * the line separator for this OS
     */
    public static final String LINE_SEP = System.getProperty("line.separator");

    private static final String XML_UNICODE_PREPEND_STR = "&#";


    /**
     * HTML-ize String, splitting long lines
     * NOTE: this is copy/paste of org.openide.explorer.propertysheet.PropUtils.createHtmlTooltip(...)
     */
    public static String htmlize(String title, String s, int wrapAfter, String footer) {
        if (BasicHTML.isHTMLString(s))
            return s;

        // break up massive tooltips
        String token;
        if (s.indexOf(" ") != -1) { //NOI18N
            token = " "; //NOI18N
        } else if (s.indexOf(",") != -1) { //NOI18N
            token = ","; //NOI18N
        } else if (s.indexOf(";") != -1) { //NOI18N
            token = ";"; //NOI18N
        } else if (s.indexOf("/") != -1) { //NOI18N
            token = "/"; //NOI18N
        } else if (s.indexOf("\\") != -1) { //NOI18N
            token = "\\"; //NOI18N
        } else {
            //give up
            return s;
        }
        StringTokenizer tk = new StringTokenizer(s, token, true);

        StringBuffer sb = new StringBuffer(s.length() + 20);
        sb.append("<html><body>"); //NOI18N
        if (title != null) {
            sb.append("<b><u>"); //NOI18N
            sb.append(title);
            sb.append("</u></b><br>"); //NOI18N
        }


        int charCount = 0;
        int lineCount = 0;
        while (tk.hasMoreTokens()) {
            String a = tk.nextToken();
            // HTML-ize only non-html values. HTML values should already
            // contain correct HTML strings.
            a = replaceString(a, "&", "&amp;"); //NOI18N
            a = replaceString(a, "<", "&lt;"); //NOI18N
            a = replaceString(a, ">", "&gt;"); //NOI18N
            a = replaceString(a, "\r\n", "<br>"); //NOI18N
            a = replaceString(a, "\n", "<br>"); //NOI18N

            int offset = a.indexOf("<br>");
            if (offset == -1)
                charCount += a.length();
            else
                charCount = a.length() - offset - 1;

            sb.append(a);
            if (tk.hasMoreTokens()) {
                charCount++;
            }
            if (charCount > wrapAfter) {
                if (tk.hasMoreTokens())
                    sb.append("<br>"); //NOI18N
                charCount = 0;
                lineCount++;
                if (lineCount > 10) {
                    //Don't let things like VCS variables create
                    //a tooltip bigger than the screen. 99% of the
                    //time this is not a problem.
                    sb.append("..."); //NOI18N
                    return sb.toString();
                }
            }
        }
        if (footer != null) {
            sb.append("<br><b>"); //NOI18N
            sb.append(footer);
            sb.append("</b>"); //NOI18N
        }
        sb.append("<body></html>"); //NOI18N
        return sb.toString();
    }

    public static void pad(String arr[], int orientation) {
        int maxLength = 0;
        for (int i = 0; i < arr.length; i++)
            maxLength = Math.max(maxLength, arr[i] != null ? arr[i].length() : 0);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null || arr[i].length() == 0) {
                char ch[] = new char[maxLength];
                Arrays.fill(ch, ' ');
                arr[i] = new String(ch).intern();
            }
            int pad = maxLength - arr[i].length();
            switch (orientation) {
                case SwingConstants.LEFT: {
                    char ch[] = new char[pad];
                    Arrays.fill(ch, ' ');
                    arr[i] = (arr[i] + new String(ch)).intern();
                    break;
                }
                case SwingConstants.CENTER: {
                    int left = pad / 2;
                    char leftCh[] = new char[left];
                    Arrays.fill(leftCh, ' ');
                    int right = pad - left;
                    char rightCh[] = new char[right];
                    Arrays.fill(rightCh, ' ');
                    arr[i] = (new String(leftCh) + arr[i] + new String(rightCh)).intern();
                }
                case SwingConstants.RIGHT: {
                    char ch[] = new char[pad];
                    Arrays.fill(ch, ' ');
                    arr[i] = (new String(ch) + arr[i]).intern();
                    break;
                }
            }
        }
    }

    /**
     * Search-and-replace fixed string matches within a string.
     * NOTE: this is copy/paste of org.openide.util.Utilities.replaceString(...)
     *
     * @param original    the original string
     * @param replaceFrom the substring to be find
     * @param replaceTo   the substring to replace it with
     * @return a new string with all occurrences replaced
     */
    public static String replaceString(String original, String replaceFrom, String replaceTo) {
        int index = 0;
        if ("".equals(replaceFrom)) return original; // NOI18N

        StringBuffer buf = new StringBuffer();
        while (true) {
            int pos = original.indexOf(replaceFrom, index);
            if (pos == -1) {
                buf.append(original.substring(index));
                return buf.toString();
            }
            buf.append(original.substring(index, pos));
            buf.append(replaceTo);
            index = pos + replaceFrom.length();
            if (index == original.length())
                return buf.toString();
        }
    }

    /**
     * Eliminates redundant spaces from a string
     */
    public static String normalizeSpace(String str) {
        if (str.length() <= 1)
            return str;

        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(str);
        if (tokenizer.hasMoreTokens())
            buffer.append(tokenizer.nextToken());
        while (tokenizer.hasMoreTokens())
            buffer.append(' ').append(tokenizer.nextToken());

        return buffer.toString();
    }

    /**
     * Returns a string formed by replacing individual characters that
     * appear in the second argument with the characters that appear
     * at the corresponding position in the third argument
     */
    public static String translate(String s0, String s1, String s2) {
        StringBuffer sb = new StringBuffer();
        int s2len = s2.length();
        for (int i = 0; i < s0.length(); i++) {
            char c = s0.charAt(i);
            int j = s1.indexOf(c);
            if (j < s2len) {
                sb.append((j < 0 ? c : s2.charAt(j)));
            }
        }
        return sb.toString();
    }

    /*-------------------------------------------------[ StringLiterals ]---------------------------------------------------*/

    // NOTE: the return value is not surrounded by double-quotes
    public static String toJavaStringLiteral(String str, boolean useRaw) {
        StringBuffer buf = new StringBuffer(str.length() * 6); // x -> \u1234
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (c) {
                case '\b':
                    buf.append("\\b");
                    break; // NOI18N
                case '\t':
                    buf.append("\\t");
                    break; // NOI18N
                case '\n':
                    buf.append("\\n");
                    break; // NOI18N
                case '\f':
                    buf.append("\\f");
                    break; // NOI18N
                case '\r':
                    buf.append("\\r");
                    break; // NOI18N
                case '\"':
                    buf.append("\\\"");
                    break; // NOI18N
                case '\\':
                    buf.append("\\\\");
                    break; // NOI18N
                default:
                    if (c >= 0x0020 && (useRaw || c <= 0x007f))
                        buf.append(c);
                    else {
                        buf.append("\\u"); // NOI18N
                        String hex = Integer.toHexString(c);
                        for (int j = 0; j < 4 - hex.length(); j++)
                            buf.append('0');
                        buf.append(hex);
                    }
            }
        }
        return buf.toString();
    }

    // NOTE: the argument should not be surrounded by double-quotes
    public static String fromJavaStringLiteral(String str) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            switch (c) {
                case '\\':
                    if (i == str.length() - 1) {
                        buf.append('\\');
                        break;
                    }
                    c = str.charAt(++i);
                    switch (c) {
                        case 'n':
                            buf.append('\n');
                            break;
                        case 't':
                            buf.append('\t');
                            break;
                        case 'r':
                            buf.append('\r');
                            break;
                        case 'u':
                            int value = 0;
                            for (int j = 0; j < 4; j++) {
                                c = str.charAt(++i);
                                switch (c) {
                                    case '0':
                                    case '1':
                                    case '2':
                                    case '3':
                                    case '4':
                                    case '5':
                                    case '6':
                                    case '7':
                                    case '8':
                                    case '9':
                                        value = (value << 4) + c - '0';
                                        break;
                                    case 'a':
                                    case 'b':
                                    case 'c':
                                    case 'd':
                                    case 'e':
                                    case 'f':
                                        value = (value << 4) + 10 + c - 'a';
                                        break;
                                    case 'A':
                                    case 'B':
                                    case 'C':
                                    case 'D':
                                    case 'E':
                                    case 'F':
                                        value = (value << 4) + 10 + c - 'A';
                                        break;
                                    default:
                                        throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                                }
                            }
                            buf.append((char) value);
                            break;
                        default:
                            buf.append(c);
                            break;
                    }
                    break;
                default:
                    buf.append(c);
            }
        }
        return buf.toString();
    }

    /*-------------------------------------------------[ Hex-Binary Conversions ]---------------------------------------------------*/

    /**
     * Decode a single hex digit
     *
     * @param c the hex digit
     * @return the numeric value of the hex digit
     * @throws IllegalArgumentException if it isn't a hex digit
     */
    private static int fromHex(char c) {
        int d = "0123456789ABCDEFabcdef".indexOf(c); //NOI18N
        if (d > 15)
            d = d - 6;
        if (d < 0)
            throw new IllegalArgumentException("Invalid hexadecimal digit: " + c);
        return d;
    }

    public static String binary2Hex(byte binaryValue[]) {
        String digits = "0123456789ABCDEF"; //NOI18N
        StringBuffer sb = new StringBuffer(binaryValue.length * 2);
        for (int i = 0; i < binaryValue.length; i++) {
            sb.append(digits.charAt((binaryValue[i] >> 4) & 0xf));
            sb.append(digits.charAt(binaryValue[i] & 0xf));
        }
        return sb.toString();
    }

    public static byte[] hex2Binary(String hex) {
        if ((hex.length() & 1) != 0)
            throw new IllegalArgumentException("A hexBinary value must contain an even number of characters");
        byte binaryValue[] = new byte[hex.length() / 2];
        for (int i = 0; i < binaryValue.length; i++) {
            binaryValue[i] = (byte) ((fromHex(hex.charAt(2 * i)) << 4) +
                    (fromHex(hex.charAt(2 * i + 1))));
        }
        return binaryValue;
    }

    /*-------------------------------------------------[ Color ]---------------------------------------------------*/

    public static String toHex(Color c) {
        String s = Integer.toHexString(c.getRGB() & 0xFFFFFF);
        while (s.length() < 6)
            s = '0' + s;
        return s;
    }

    /*-------------------------------------------------[ WhiteSpaces ]---------------------------------------------------*/

    public static String indentString(String str, int count) {
        StringBuffer buf = new StringBuffer(str.length() * count);
        for (int i = 0; i < count; i++)
            buf.append(str);
        return buf.toString();
    }

    /**
     * Returns the number of leading white space characters in the specified
     * string.
     *
     * @param str The string
     * @return The leadingWhiteSpace value
     */
    public static String getLeadingWhiteSpace(String str) {
        return str.substring(0, getLeadingWhiteSpaceWidth(str));
    }

    /**
     * Returns the number of leading white space characters in the specified
     * string.
     *
     * @param str The string
     * @return The leadingWhiteSpace value
     */
    public static int getLeadingWhiteSpaceWidth(String str) {
        int whitespace = 0;
        loop:
        for (; whitespace < str.length(); ) {
            switch (str.charAt(whitespace)) {
                case ' ':
                case '\t':
                    whitespace++;
                    break;
                default:
                    break loop;
            }
        }
        return whitespace;
    }

    /**
     * Returns the width of the leading white space in the specified string.
     *
     * @param str     The string
     * @param tabSize The tab size
     * @return The leadingWhiteSpaceWidth value
     */
    public static int getLeadingWhiteSpaceWidth(String str, int tabSize) {
        int whitespace = 0;
        loop:
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case ' ':
                    whitespace++;
                    break;
                case '\t':
                    whitespace += (tabSize - whitespace % tabSize);
                    break;
                default:
                    break loop;
            }
        }
        return whitespace;
    }

    /**
     * Create a blank String made of spaces.
     *
     * @param len Amount of spaces contained in the String
     * @return A blank <code>String</code>
     */
    public static String createWhiteSpace(int len) {
        return createWhiteSpace(len, 0);
    }

    /**
     * Create a blank String made of tabs.
     *
     * @param len     Amount of spaces contained in the String
     * @param tabSize Tabulation size
     * @return A blank <code>String</code>
     */
    public static String createWhiteSpace(int len, int tabSize) {
        StringBuffer buf = new StringBuffer();

        if (tabSize == 0) {
            while (len-- > 0)
                buf.append(' ');
        } else {
            int count = len / tabSize;
            while (count-- > 0)
                buf.append('\t');

            count = len % tabSize;
            while (count-- > 0)
                buf.append(' ');
        }
        return buf.toString();
    }

    /**
     * Returns true if the String is either NULL or empty(after trim)
     */
    public static boolean isEmpty(String str) {
        if (str == null)
            return true;
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) != ' ')
                return false;
        }
        return true;
    }

    public static boolean isWhitespace(String str) {
        if (str == null)
            return true;
        for (int i = str.length() - 1; i >= 0; i--) {
            if (!Character.isWhitespace(str.charAt(i)))
                return false;
        }
        return true;
    }

    /*-------------------------------------------------[ JavaIdentifier ]---------------------------------------------------*/

    public static String toString(Object obj) {
        return obj != null ? obj.toString() : null;
    }


    public static String[] getTokens(String str, String delim, boolean trim) {
        if (str == null)
            return new String[0];

        StringTokenizer stok = new StringTokenizer(str, delim);
        String tokens[] = new String[stok.countTokens()];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = trim ? stok.nextToken().trim() : stok.nextToken();
        }
        return tokens;
    }

    public static String toString(String tokens[], String delim, boolean trim) {
        if (tokens == null)
            return null;

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < tokens.length; i++) {

            if (i > 0)
                buf.append(delim);
            if (trim)
                buf.append(tokens[i].trim());
            else
                buf.append(tokens[i]);
        }
        return buf.toString();
    }

    /**
     * Convinience method to test whether the given char is Unicode(16-bit encoded)
     *
     * @param c
     * @return true if the char is Unicode, or 16-bits
     */
    public static boolean isJavaUnicode(char c) {
        return c > 0XFF;
    }

    /**
     * Convinience method to find whether the given string has a pattern
     * similar to <pre>&#XXXX;</pre>, which indicates its a XML representation of Unicode
     *
     * @param xmlToken
     * @return boolean
     */
/*    private static boolean hasXMLUnicode(String xmlToken) {
        return MATCHER.contains(xmlToken, XML_UNICODE_REGEX);
    }*/

    /**
     * Converts each Unicode character of a string to its XMLised equivalent: <pre>&#XXXX;</pre>
     * where XXXX is the value of the char in decimal.
     *
     * @param unicodeStr String containing unicode characters
     * @return xml string
     */
    public static String getUnicodeInXMLFormat(String unicodeStr) {

        if (unicodeStr == null) {
            return null;
        }

        char[] unicodeArr = unicodeStr.toCharArray();
        int len = unicodeStr.length();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < len; i++) {
            if (!isJavaUnicode(unicodeArr[i])) {
                sb.append(unicodeArr[i]);
                continue;
            }

            sb.append(XML_UNICODE_PREPEND_STR).append((int) unicodeArr[i]).append(';');
        }

        return sb.toString();

    }

    public static String toJavaIdentifier(String name) {
        if (name.length() == 0)
            return "_"; //NOI18N
        StringBuffer buf = new StringBuffer(name.length());
        for (int i = 0; i < name.length(); i++) {
            if (i == 0 && !Character.isJavaIdentifierStart(name.charAt(i)))
                buf.append('_');
            if (!Character.isJavaIdentifierPart(name.charAt(i)))
                buf.append('_');
            else
                buf.append(name.charAt(i));
        }

        name = buf.toString();
        return JavaUtil.KEY_WORDS.contains(name)
                ? '_' + name
                : name;
    }

    public static String firstLetterToUpperCase(String s, String pref) {
        switch (s.length()) {
            case 0:
                return pref;
            case 1:
                return pref + Character.toUpperCase(s.charAt(0));
            default:
                return pref + Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }
    }

}

