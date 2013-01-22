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
import org.apache.felix.service.command.CommandSession;
import org.fusesource.jansi.Ansi;
import org.osgi.service.subsystem.Subsystem;

/**
 * List the Subsystems.
 */
@Component
@Command(name = "start-subsystem",
         scope = "subsystem",
         description = "Start a given Subsystem.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class StartSubsystemAction implements Action {

    @Argument(name = "subsystem",
              required = true,
              description = "Subsystem to be started")
    private Subsystem subsystem;

    public Object execute(final CommandSession session) throws Exception {

        String name = String.format("%d - %s/%s [%s]",
                subsystem.getSubsystemId(),
                subsystem.getSymbolicName(),
                subsystem.getVersion(),
                subsystem.getType());
        subsystem.start();

        Ansi buffer = Ansi.ansi();

        buffer.a("Started Subsystem: ");
        buffer.a(Ansi.Attribute.INTENSITY_BOLD);
        buffer.a(name);
        buffer.a(Ansi.Attribute.INTENSITY_BOLD_OFF);
        System.out.println(buffer.toString());

        return null;
    }

}