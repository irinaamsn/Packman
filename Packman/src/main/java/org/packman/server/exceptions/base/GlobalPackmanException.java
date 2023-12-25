package org.packman.server.exceptions.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class GlobalPackmanException extends RuntimeException{
    private final int status;
    private final String errorMessage;
    private final long timestamp;
}
