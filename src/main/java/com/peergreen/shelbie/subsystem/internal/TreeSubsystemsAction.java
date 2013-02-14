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

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.HandlerDeclaration;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.service.command.CommandSession;
import org.fusesource.jansi.Ansi;
import org.osgi.service.subsystem.Subsystem;

import com.peergreen.tree.Node;
import com.peergreen.tree.NodeAdapter;
import com.peergreen.tree.NodeVisitor;
import com.peergreen.tree.node.LazyNode;

/**
 * List the Subsystems.
 */
@Component
@Command(name = "tree-subsystem",
         scope = "subsystem",
         description = "Display Subsystem as a tree.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class TreeSubsystemsAction implements Action {

    @Requires(filter = "(subsystem.id=0)")
    private Subsystem rootSubsystem;

    @Argument(name = "subsystem",
              description = "Subsystem to be used as root (optional), root Subsystem is used by default")
    private Subsystem subsystem;

    @Override
    public Object execute(final CommandSession session) throws Exception {

        Subsystem base = selectBaseSubsystem();
        Node<Subsystem> root = new LazyNode<Subsystem>(new SubsystemNodeAdapter(), base);

        Ansi buffer = Ansi.ansi();
        root.walk(new SubsystemVisitor(buffer, root));

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

    private class SubsystemNodeAdapter implements NodeAdapter<Subsystem> {
        @Override
        public Iterable<Subsystem> getChildren(Subsystem parent) {
            return parent.getChildren();
        }
    }

    private class SubsystemVisitor implements NodeVisitor<Subsystem> {
        private final Ansi buffer;
        private final Node<Subsystem> root;

        public SubsystemVisitor(Ansi buffer, Node<Subsystem> root) {
            this.buffer = buffer;
            this.root = root;
        }

        @Override
        public void visit(Node<Subsystem> node) {
            if (!node.equals(root)) {
                buffer.newline();
            }

            Subsystem data = node.getData();
            int depth = depth(node);
            for (int i = 0; i < depth; i++) {
                buffer.a("  ");
            }
            buffer.a(Ansi.Attribute.INTENSITY_BOLD);
            buffer.a(data.getSubsystemId());
            buffer.a(Ansi.Attribute.INTENSITY_BOLD_OFF);
            buffer.a(String.format(
                    " - %s/%s [%s]",
                    data.getSymbolicName(),
                    data.getVersion(),
                    data.getType()
            ));
        }

        private int depth(Node<Subsystem> node) {
            int depth = 0;
            while (node.getParent() != null) {
                depth++;
                node = node.getParent();
            }
            return depth;
        }
    }
}