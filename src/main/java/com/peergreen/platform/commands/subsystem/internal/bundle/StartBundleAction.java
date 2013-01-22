package com.peergreen.platform.commands.subsystem.internal.bundle;

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
@Command(name = "start-bundle",
        scope = "constituent",
        description = "Start a Bundle constituent of the current Subsystem")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class StartBundleAction implements Action {

    @Requires(filter = "(subsystem.id=0)")
    private Subsystem rootSubsystem;

    @Argument(name = "bundle",
            required = true,
            description = "Bundle identifier")
    private Long bundleId;

    public Object execute(final CommandSession session) throws Exception {

        BundleContext bundleContext = (BundleContext) session.get("subsystem.context");
        if (bundleContext == null) {
            bundleContext = rootSubsystem.getBundleContext();
            //session.put("subsystem.context", bundleContext);
        }

        Bundle bundle = bundleContext.getBundle(bundleId);
        if (bundle == null) {
            throw new Exception(String.format(
                    "Bundle %d is not visible from region %s",
                    bundleId,
                    region(bundleContext)
            ));
        }

        // TODO add lazy
        bundle.start();

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

    private String region(BundleContext bundleContext) {
        String name = bundleContext.getBundle().getSymbolicName();
        if (name.startsWith("org.osgi.service.subsystem.region.context.")) {
            return name.substring("org.osgi.service.subsystem.region.context.".length());
        }
        return name;
    }

}