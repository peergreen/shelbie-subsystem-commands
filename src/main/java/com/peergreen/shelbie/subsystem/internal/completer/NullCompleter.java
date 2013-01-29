/*
 * Copyright 2013 Peergreen S.A.S.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.shelbie.subsystem.internal.completer;

import java.util.List;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;

import jline.console.completer.Completer;

/**
 * Created by IntelliJ IDEA.
 * User: Guillaume
 * Date: 28 janv. 2010
 * Time: 21:05:49
 * To change this template use File | Settings | File Templates.
 */
@Component
@Provides
public class NullCompleter implements Completer {

    public int complete(String buffer, int cursor, List<CharSequence> clist) {
        return -1;
    }

}
