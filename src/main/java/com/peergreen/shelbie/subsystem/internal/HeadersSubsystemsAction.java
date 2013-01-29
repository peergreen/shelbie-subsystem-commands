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

package com.peergreen.shelbie.subsystem.internal;

import java.util.Locale;
import java.util.Map;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.HandlerDeclaration;
import org.apache.felix.service.command.CommandSession;
import org.fusesource.jansi.Ansi;
import org.osgi.service.subsystem.Subsystem;
import org.osgi.service.subsystem.SubsystemConstants;

/**
 * List the Subsystems.
 */
@Component
@Command(name = "headers",
         scope = "subsystem",
         description = "Show Subsytem headers.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class HeadersSubsystemsAction implements Action {

    @Argument(name = "subsystem",
              required = true,
              description = "Subsystem to display")
    private Subsystem subsystem;

    public Object execute(final CommandSession session) throws Exception {

        Ansi buffer = Ansi.ansi();

        Map<String, String> headers = subsystem.getSubsystemHeaders(Locale.getDefault());
        //SubsystemConstants.

        buffer.a(String.format("%s/%s (%s)",
                subsystem.getSymbolicName(),
                subsystem.getVersion(),
                subsystem.getSubsystemId()
        ));
        buffer.newline();
        buffer.a("---------------------------------------------------------");

        printSymbolicName(buffer, headers);
        printVersion(buffer, headers);
        printType(buffer, headers);
        printName(buffer, headers);
        printDescription(buffer, headers);
        printContent(buffer, headers);
        printManifestVersion(buffer, headers);

        printExportService(buffer, headers);
        printImportService(buffer, headers);

        /*
        SubsystemConstants.DEPLOYED_CONTENT;
        SubsystemConstants.DEPLOYMENT_MANIFESTVERSION;
        SubsystemConstants.PREFERRED_PROVIDER;
        SubsystemConstants.PROVISION_RESOURCE;
        */

        // Print subsystems
        System.out.println(buffer.toString());
        return null;
    }

    private void printImportService(Ansi buffer, Map<String, String> headers) {
        printOneLiner(buffer, headers, SubsystemConstants.SUBSYSTEM_IMPORTSERVICE);
    }

    private void printExportService(Ansi buffer, Map<String, String> headers) {
        printOneLiner(buffer, headers, SubsystemConstants.SUBSYSTEM_EXPORTSERVICE);
    }

    private void printManifestVersion(Ansi buffer, Map<String, String> headers) {
        printOneLiner(buffer, headers, SubsystemConstants.SUBSYSTEM_MANIFESTVERSION);
    }

    private void printContent(Ansi buffer, Map<String, String> headers) {
        printOneLiner(buffer, headers, SubsystemConstants.SUBSYSTEM_CONTENT);
    }

    private void printDescription(Ansi buffer, Map<String, String> headers) {
        printOneLiner(buffer, headers, SubsystemConstants.SUBSYSTEM_DESCRIPTION);
    }

    private void printType(Ansi buffer, Map<String, String> headers) {
        printOneLiner(buffer, headers, SubsystemConstants.SUBSYSTEM_TYPE);
    }

    private void printVersion(Ansi buffer, Map<String, String> headers) {
        printOneLiner(buffer, headers, SubsystemConstants.SUBSYSTEM_VERSION);
    }

    private void printName(Ansi buffer, Map<String, String> headers) {
        printOneLiner(buffer, headers, SubsystemConstants.SUBSYSTEM_NAME);
    }

    private void printSymbolicName(Ansi buffer, Map<String, String> headers) {
        printOneLiner(buffer, headers, SubsystemConstants.SUBSYSTEM_SYMBOLICNAME);
    }

    private void printOneLiner(Ansi buffer, Map<String, String> headers, String header) {
        String value = headers.get(header);
        if (value != null) {
            buffer.newline();
            buffer.a(Ansi.Attribute.INTENSITY_BOLD);
            buffer.a(String.format("%-26s", header));
            buffer.a(Ansi.Attribute.INTENSITY_BOLD_OFF);
            buffer.a(value);
        }
    }


}