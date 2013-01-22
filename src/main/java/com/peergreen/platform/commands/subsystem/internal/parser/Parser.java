package com.peergreen.platform.commands.subsystem.internal.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: guillaume
 * Date: 21/01/13
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
public class Parser {
    public static final void main(String... args) throws Exception {

        String text = "a,b;version=1;uses:=\"a,b\",c;version=\"[1.2.0,)\"";
        // 1 2 "333 4" 55 6    "77" 8 999

        String regex = "=\"([^\"]*)\"|([^,;=]+)";

        Matcher m = Pattern.compile(regex).matcher(text);
        while (m.find()) {
            if (m.group(1) != null) {
                System.out.println("Quoted [" + m.group(1) + "]");
            } else {
                System.out.println("Plain [" + m.group(2) + "]");
            }
        }
    }
}
