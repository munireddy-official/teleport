package com.teleport.exception.handler;

public class SequenceLimitExceededException extends TrackingNumberGenerationException{
    public SequenceLimitExceededException(){

    }
    public SequenceLimitExceededException(String msg){
        super(msg);
    }
}
