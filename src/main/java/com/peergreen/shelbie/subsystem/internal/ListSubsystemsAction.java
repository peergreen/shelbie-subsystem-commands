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

package com.peergreen.shelbie.subsystem.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.HandlerDeclaration;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.service.command.CommandSession;
import org.fusesource.jansi.Ansi;
import org.osgi.service.subsystem.Subsystem;

/**
 * List the Subsystems.
 */
@Component
@Command(name = "list-subsystems",
         scope = "subsystem",
         description = "List the Subsystems.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class ListSubsystemsAction implements Action {

    private static Map<Subsystem.State, Ansi.Color> COLORS;

    static {
        COLORS = new HashMap<Subsystem.State, Ansi.Color>();
        COLORS.put(Subsystem.State.ACTIVE, Ansi.Color.GREEN);
        COLORS.put(Subsystem.State.INSTALL_FAILED, Ansi.Color.RED);
        COLORS.put(Subsystem.State.INSTALLED, Ansi.Color.YELLOW);
        COLORS.put(Subsystem.State.INSTALLING, Ansi.Color.YELLOW);
        COLORS.put(Subsystem.State.RESOLVING, Ansi.Color.YELLOW);
        COLORS.put(Subsystem.State.RESOLVED, Ansi.Color.GREEN);
        COLORS.put(Subsystem.State.STARTING, Ansi.Color.YELLOW);
        COLORS.put(Subsystem.State.STOPPING, Ansi.Color.YELLOW);
        COLORS.put(Subsystem.State.UNINSTALLED, Ansi.Color.YELLOW);
        COLORS.put(Subsystem.State.UNINSTALLING, Ansi.Color.YELLOW);
    }

    @Requires(optional = true)
    private Subsystem[] subsystems;

    @Option(name = "-v",
            aliases = "--verbose",
            description = "When activated, display additional Subsystem information",
            required = false)
    private boolean verbose = false;

    @Argument(name = "pattern",
              description = "Glob expression (ex: *root*) to filter Subsystems")
    private Glob glob;

    public Object execute(final CommandSession session) throws Exception {

        List<Subsystem> filtered = filter();

        Ansi buffer = Ansi.ansi();
        if (!filtered.isEmpty()) {
            buffer.a(Ansi.Attribute.INTENSITY_BOLD);
            buffer.a("[        Status]  ID Subsystem\n");
            buffer.a(Ansi.Attribute.INTENSITY_BOLD_OFF);
        }
        boolean first = true;
        for (Subsystem subsystem : filtered) {
            if (first) {
                first = false;
            } else {
                buffer.a("\n");
            }
            printSubsystem(buffer, subsystem);
        }

        // Print subsystems statuses
        System.out.println(buffer.toString());
        return null;
    }

    private List<Subsystem> filter() {
        if (glob == null) {
            return Arrays.asList(subsystems);
        }

        Pattern pattern = glob.getPattern();
        List<Subsystem> filtered = new ArrayList<Subsystem>();
        for (Subsystem subsystem : subsystems) {
            if (pattern.matcher(subsystem.getSymbolicName()).matches()) {
                filtered.add(subsystem);
            }
        }

        return filtered;
    }

    private void printSubsystem(Ansi buffer, Subsystem subsystem) {

        String status = String.format(
                "%14S",
                subsystem.getState().name()
        );

        // Print status in the first column
        buffer.a("[");
        buffer.fg(COLORS.get(subsystem.getState()));
        buffer.a(status);
        buffer.fg(Ansi.Color.DEFAULT);
        buffer.a("] ");

        // Then place the factory name
        buffer.a(String.format(
                "%3d %s (%s)",
                subsystem.getSubsystemId(),
                subsystem.getSymbolicName(),
                subsystem.getVersion()
        ));

    }
}