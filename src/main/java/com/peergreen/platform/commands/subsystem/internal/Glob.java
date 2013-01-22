/*
 * Copyright 2013 Peergreen
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.platform.commands.subsystem.internal;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: guillaume
 * Date: 20/01/13
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */
public class Glob {
    private String origin;
    private Pattern pattern;

    public Glob(String origin) {
        this.origin = origin;
        createRegexPattern();
    }

    private void createRegexPattern() {
        String regex = origin;
        if (regex.contains(".")) {
            // protect existing '.' char
            regex = regex.replaceAll("\\.", "\\\\.");
        }
        if (regex.contains("*")) {
            // convert '*' pseudo regular expression to valid pattern
            // '*' -> '.*'
            regex = regex.replaceAll("\\*", "\\.\\*");
        }
        pattern = Pattern.compile(regex);
    }

    public String getOrigin() {
        return origin;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
