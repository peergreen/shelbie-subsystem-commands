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

import com.peergreen.shelbie.subsystem.internal.Glob;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.service.command.Converter;

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
public class GlobConverter implements Converter {

    @ServiceProperty(name = Converter.CONVERTER_CLASSES,
            value = "com.peergreen.shelbie.subsystem.internal.Glob")
    private String supportedClasses;

    public Object convert(Class<?> desiredType, Object in) throws Exception {
        if (Glob.class.equals(desiredType)) {
            if (in instanceof String) {
                return new Glob((String) in);
            }
        }
        return null;
    }

    public CharSequence format(Object target, int level, Converter escape) throws Exception {
        if (target instanceof Glob) {
            Glob glob = (Glob) target;
            return glob.getOrigin();
        }
        return null;
    }
}
