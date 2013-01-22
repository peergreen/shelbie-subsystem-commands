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

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
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
@Command(name = "install-subsystem",
         scope = "subsystem",
         description = "Display Subsystem as a tree.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class InstallSubsystemAction implements Action {

    @Requires(filter = "(subsystem.id=0)")
    private Subsystem rootSubsystem;

    @Argument(name = "to-be-installed",
              index = 0,
              required = true,
              description = "URL of the Subsystem to be installed")
    private String url;

    @Argument(name = "parent-subsystem",
              index = 1,
              description = "Subsystem to be used as parent (optional), root Subsystem is used by default")
    private Subsystem subsystem;

    public Object execute(final CommandSession session) throws Exception {

        Subsystem base = selectBaseSubsystem();

        Subsystem child = base.install(url);

        String name = String.format("%d - %s/%s [%s]",
                child.getSubsystemId(),
                child.getSymbolicName(),
                child.getVersion(),
                child.getType());

        Ansi buffer = Ansi.ansi();

        buffer.a("Subsystem ID: ");
        buffer.a(Ansi.Attribute.INTENSITY_BOLD);
        buffer.a(name);
        buffer.a(Ansi.Attribute.INTENSITY_BOLD_OFF);
        // Print subsystems
        System.out.println(buffer.toString());
        return null;
    }

    private Subsystem selectBaseSubsystem() {
        if (subsystem == null) {
            return rootSubsystem;
        }
        return subsystem;
    }

}