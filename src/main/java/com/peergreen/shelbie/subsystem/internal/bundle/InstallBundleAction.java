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

package com.peergreen.shelbie.subsystem.internal.bundle;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.HandlerDeclaration;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.service.command.CommandSession;
import org.fusesource.jansi.Ansi;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.subsystem.Subsystem;

/**
 * Created with IntelliJ IDEA.
 * User: guillaume
 * Date: 21/01/13
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
@Component
@Command(name = "install-bundle",
        scope = "constituent",
        description = "Install a Bundle as a Subsystem constituent.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class InstallBundleAction implements Action {

    @Requires(filter = "(subsystem.id=0)")
    private Subsystem rootSubsystem;

    @Argument(name = "to-be-installed",
            required = true,
            description = "URL of the Subsystem to be installed")
    private String location;

    public Object execute(final CommandSession session) throws Exception {

        BundleContext bundleContext = (BundleContext) session.get("subsystem.context");
        if (bundleContext == null) {
            bundleContext = rootSubsystem.getBundleContext();
            //session.put("subsystem.context", bundleContext);
        }

        System.out.print(String.format(
                "Bundle agent: %s%n",
                bundleContext.getBundle().getSymbolicName()
        ));

        Bundle bundle = bundleContext.installBundle(location);
        String name = String.format("%d - %s/%s [osgi.bundle]",
                bundle.getBundleId(),
                bundle.getSymbolicName(),
                bundle.getVersion());

        Ansi buffer = Ansi.ansi();

        buffer.a("Installed Bundle: ");
        buffer.a(Ansi.Attribute.INTENSITY_BOLD);
        buffer.a(name);
        buffer.a(Ansi.Attribute.INTENSITY_BOLD_OFF);
        // Print subsystems
        System.out.println(buffer.toString());
        return null;
    }

}