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

package com.peergreen.shelbie.subsystem.internal.converter;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.service.command.Converter;
import org.osgi.service.subsystem.Subsystem;

/**
 * Created with IntelliJ IDEA.
 * User: guillaume
 * Date: 20/01/13
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
@Component
@Provides
@Instantiate
public class SubsystemConverter implements Converter {

    @ServiceProperty(name = Converter.CONVERTER_CLASSES,
            value = "org.osgi.service.subsystem.Subsystem")
    private String supportedClasses;

    @Requires(optional = true)
    private Subsystem[] subsystems;

    public Object convert(Class<?> desiredType, Object in) throws Exception {
        if (Subsystem.class.equals(desiredType)) {
            if (in instanceof String) {
                String name = (String) in;
                for (Subsystem subsystem : subsystems) {
                    if (subsystem.getSymbolicName().contains(name)) {
                        return subsystem;
                    }
                }
                for (Subsystem subsystem : subsystems) {
                    String identifier = subsystem.getSymbolicName() + "/" + subsystem.getVersion();
                    if (identifier.equals(name)) {
                        return subsystem;
                    }
                }
            }

            if (in instanceof Number) {
                Number number = (Number) in;
                for (Subsystem subsystem : subsystems) {
                    Long id = subsystem.getSubsystemId();
                    if (id.equals(number)) {
                        return subsystem;
                    }
                }
            }
        }
        return null;
    }

    public CharSequence format(Object target, int level, Converter escape) throws Exception {
        if (target instanceof Subsystem) {
            Subsystem subsystem = (Subsystem) target;
            switch (level) {
                case Converter.PART:
                    return String.valueOf(subsystem.getSubsystemId());
                case Converter.LINE:
                    return String.format(
                            " %2d | %25s (%10s) | %s",
                            subsystem.getSubsystemId(),
                            subsystem.getSymbolicName(),
                            subsystem.getVersion(),
                            subsystem.getType()
                    );
                case Converter.INSPECT:
                    return subsystem.getSymbolicName();
            }
        }
        return null;
    }
}
