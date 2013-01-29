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

import java.util.List;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.HandlerDeclaration;
import org.apache.felix.service.command.CommandSession;
import org.fusesource.jansi.Ansi;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
import org.osgi.framework.Version;
import org.osgi.resource.Capability;
import org.osgi.resource.Resource;
import org.osgi.service.subsystem.Subsystem;

/**
 * List the Subsystems.
 */
@Component
@Command(name = "info",
         scope = "subsystem",
         description = "Display complete information about a Subsystem.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class InfoSubsystemAction implements Action {

    @Argument(name = "subsystem",
              required = true,
              description = "Subsystem to display")
    private Subsystem subsystem;

    public Object execute(final CommandSession session) throws Exception {

        Ansi buffer = Ansi.ansi();

        buffer.a(String.format(
                "Subsystem %d - %s/%s [%s]",
                subsystem.getSubsystemId(),
                subsystem.getSymbolicName(),
                subsystem.getVersion(),
                subsystem.getType()
        ));
        buffer.newline();
        buffer.a(dashline(120));
        buffer.newline();

        buffer.a(Ansi.Attribute.INTENSITY_BOLD);
        buffer.a(String.format("%-14s", "Location"));
        buffer.a(Ansi.Attribute.INTENSITY_BOLD_OFF);
        buffer.a(subsystem.getLocation());
        buffer.newline();

        buffer.a(Ansi.Attribute.INTENSITY_BOLD);
        buffer.a(String.format("%-14s", "State"));
        buffer.a(Ansi.Attribute.INTENSITY_BOLD_OFF);
        buffer.a(subsystem.getState().name());
        buffer.newline();

        if (!subsystem.getConstituents().isEmpty()) {
            buffer.a(Ansi.Attribute.INTENSITY_BOLD);
            buffer.a(String.format("%-14s", "Constituent(s)"));
            buffer.a(Ansi.Attribute.INTENSITY_BOLD_OFF);
            buffer.newline();

            for (Resource resource : subsystem.getConstituents()) {
                buffer.a("  ");
                buffer.a(resource(resource));
                extra(buffer, resource);
                buffer.newline();
            }
        }

        if (!subsystem.getParents().isEmpty()) {
            buffer.a(Ansi.Attribute.INTENSITY_BOLD);
            buffer.a(String.format("%-14s", "Parent(s)"));
            buffer.a(Ansi.Attribute.INTENSITY_BOLD_OFF);
            buffer.newline();

            for (Subsystem parent : subsystem.getParents()) {
                buffer.a("  ");
                buffer.a(String.format(
                        "%d - %s/%s [%s]",
                        parent.getSubsystemId(),
                        parent.getSymbolicName(),
                        parent.getVersion(),
                        parent.getType()
                ));
                buffer.newline();
            }
        }

        // Print subsystems
        System.out.println(buffer.toString());
        return null;
    }

    private static String dashline(int width) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) {
            sb.append("-");
        }
        return sb.toString();
    }

    private void extra(Ansi buffer, Resource resource) {
        if (resource instanceof BundleReference) {
            Bundle bundle = ((BundleReference) resource).getBundle();
            buffer.a(" ");
            buffer.a(bundleState(bundle));
            buffer.a(" [");
            buffer.a(bundle.getBundleId());
            buffer.a("]");
        }
    }

    private String bundleState(Bundle bundle) {
        switch (bundle.getState()) {
            case Bundle.ACTIVE:
                return "ACTIVE";
            case Bundle.INSTALLED:
                return "INSTALLED";
            case Bundle.RESOLVED:
                return "RESOLVED";
            case Bundle.STARTING:
                return "STARTING";
            case Bundle.STOPPING:
                return "STOPPING";
            case Bundle.UNINSTALLED:
                return "UNINSTALLED";
        }

        return null;
    }

    private String resource(Resource resource) {
        List<Capability> capabilities = resource.getCapabilities("osgi.identity");
        if (!capabilities.isEmpty()) {
            Capability identity = capabilities.get(0);
            String id = (String) identity.getAttributes().get("osgi.identity");
            String type = (String) identity.getAttributes().get("type");
            Version version = (Version) identity.getAttributes().get("version");
            return String.format("%s/%s [%s]",
                    id, version, type);
        }
        return null;
    }

}