/**
 * Copyright 2013 OW2 Shelbie
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

package com.peergreen.platform.commands.subsystem.internal.completer;

import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.service.subsystem.Subsystem;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: Guillaume
 * Date: 28 janv. 2010
 * Time: 21:05:49
 * To change this template use File | Settings | File Templates.
 */
@Component(propagation = true)
@Provides(specifications = Completer.class)
public class SubsystemCompleter extends StringsCompleter {

    @Requires(optional = true)
    private Subsystem[] subsystems;

    /**
     * Create a new StringsCompleter with a single possible completion
     * values.
     */
    public SubsystemCompleter() {
        super("");
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> clist) {
        // Update candidates list
        getStrings().clear();
        getStrings().addAll(getCandidates());
        return super.complete(buffer, cursor, clist);
    }

    private SortedSet<String> getCandidates() {
        SortedSet<String> candidates = new TreeSet<String>();
        for (Subsystem subsystem : subsystems) {
            candidates.add(subsystem.getSymbolicName());
        }
        return candidates;
    }

}
