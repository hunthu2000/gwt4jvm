package com.mind.gwt.jclient.xhr;

public class ResponseTextSnapshot
{
    private final StringBuffer stringBuffer;
    private final int length;

    public ResponseTextSnapshot()
    {
        this(new StringBuffer());
    }

    public ResponseTextSnapshot(StringBuffer stringBuffer)
    {
        this.stringBuffer = stringBuffer;
        this.length = stringBuffer.length();
    }

    @Override
    public String toString()
    {
        return stringBuffer.substring(0, length);
    }

}