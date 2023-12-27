package org.packman.server.exceptions;

import org.packman.server.exceptions.base.GlobalPackmanException;

public class MapperCovertException extends GlobalPackmanException {
    public MapperCovertException(int status, String errorMessage, long timestamp) {
        super(status, errorMessage, timestamp);
    }
}
