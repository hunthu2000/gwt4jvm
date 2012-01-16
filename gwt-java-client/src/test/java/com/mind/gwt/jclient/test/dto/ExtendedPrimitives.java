package com.mind.gwt.jclient.test.dto;

public class ExtendedPrimitives extends Primitives
{
    private static final long serialVersionUID = 1L;

    public static ExtendedPrimitives createClientToServerObject()
    {
        return new ExtendedPrimitives(Primitives.createMaxValue());
    }

    public static ExtendedPrimitives createServerToClientObject()
    {
        return new ExtendedPrimitives(Primitives.createMinValue());
    }

    public ExtendedPrimitives() {} // Don't change it to private, otherwise GWT would generate different code and we would get different test case! 

    public ExtendedPrimitives(Primitives primitives)
    {
        super(primitives);
    }

}