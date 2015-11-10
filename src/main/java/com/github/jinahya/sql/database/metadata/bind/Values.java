/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.jinahya.sql.database.metadata.bind;


import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class Values {


    private static final Logger logger = getLogger(Metadata.class.getName());


    static Object adapt(final Class<?> type, final Object value,
                        final Object target) {

        if (type != null && type.isInstance(value)) {
            return value;
        }

        if (type != null && !type.isPrimitive() && value == null) {
            return value;
        }

        final Class<?> valueType = value == null ? null : value.getClass();

        if (Boolean.TYPE.equals(type)) {
            if (value != null && Number.class.isInstance(value)) {
                return ((Number) value).intValue() != 0;
            }
            if (value == null || !Boolean.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) to {2}",
                           new Object[]{value, valueType, target});
                return false;
            }
            return value;
        }

        if (Boolean.class.equals(type)) {
            if (value != null && Number.class.isInstance(value)) {
                return ((Number) value).intValue() != 0;
            }
            if (value != null && !Boolean.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) to {2}",
                           new Object[]{value, valueType, target});
                return Boolean.FALSE;
            }
            return value;
        }

        if (Short.TYPE.equals(type)) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) to {2}",
                           new Object[]{value, valueType, target});
                return (short) 0;
            }
            if (Short.class.isInstance(value)) {
                return value;
            }
            return ((Number) value).shortValue();
        }

        if (Short.class.equals(type)) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) to {2}",
                           new Object[]{value, valueType, target});
                return null;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).shortValue();
        }

        if (Integer.TYPE.equals(type)) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) to {2}",
                           new Object[]{value, valueType, target});
                return 0;
            }
            if (Integer.class.isInstance(value)) {
                return value;
            }
            return ((Number) value).intValue();
        }

        if (Integer.class.equals(type)) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) to {2}",
                           new Object[]{value, valueType, target});
                return null;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).intValue();
        }

        if (Long.TYPE.equals(type)) {
            if (value == null || !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) to {2}",
                           new Object[]{value, valueType, target});
                return 0L;
            }
            if (Long.class.isInstance(value)) {
                return value;
            }
            return ((Number) value).longValue();
        }

        if (Long.class.equals(type)) {
            if (value != null && !Number.class.isInstance(value)) {
                logger.log(Level.WARNING, "cannot adapt {0}({1}) to {2}",
                           new Object[]{value, valueType, target});
                return null;
            }
            if (value == null) {
                return value;
            }
            return ((Number) value).longValue();
        }

        logger.log(Level.WARNING, "unhandled {0}({1}) to {2}",
                   new Object[]{value, valueType, target});

        return value;
    }


    private Values() {

        super();
    }


}

