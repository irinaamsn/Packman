package org.packman.server.exceptions;

import org.packman.server.exceptions.base.GlobalPackmanException;

public class NotFoundException extends GlobalPackmanException {
    public NotFoundException(int status, String errorMessage, long timestamp) {
        super(status, errorMessage, timestamp);
    }
}
